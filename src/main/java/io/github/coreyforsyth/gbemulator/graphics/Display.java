package io.github.coreyforsyth.gbemulator.graphics;

import io.github.coreyforsyth.gbemulator.Bus;
import io.github.coreyforsyth.gbemulator.memory.Oam;
import io.github.coreyforsyth.gbemulator.memory.ObjectAttribute;
import io.github.coreyforsyth.gbemulator.memory.ReadWrite;
import io.github.coreyforsyth.gbemulator.memory.VRam;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Display implements ReadWrite
{

	private final Bus bus;
    private final Oam oam;
    private final VRam vRam;
	private final IndexColorModel indexColorModel;

	private BufferedImage image;
    private BufferedImage displayOff;

    private volatile boolean imageReady;
	private int dotIndex;
	private int mode;

    private byte lcdc;
    private byte stat;
    private byte scy;
    private byte scx;
    private byte ly;
    private byte lyc;
    private byte dma;
    private byte bgp;
    private byte obp0;
    private byte obp1;
    private byte wy;
    private byte wx;

    private boolean statLatch;

	public Display(Bus bus, Oam oam, VRam vRam)
	{
		this.bus = bus;
        this.oam = oam;
        this.vRam = vRam;
        Color[] colors = new Color[4];
		colors[0] = new Color(0xFFFFFF);
		colors[1] = new Color(0xADADAD);
		colors[2] = new Color(0x525252);
		colors[3] = new Color(0x000000);
		int[] cmap = Arrays.stream(colors).map(Color::getRGB)
			.mapToInt(Integer::intValue).toArray();
		indexColorModel = new IndexColorModel(2, 4, cmap, 0, false, -1, DataBuffer.TYPE_BYTE);
		dotIndex = 0;
		mode = 2;
        image = new BufferedImage(160, 144, BufferedImage.TYPE_BYTE_BINARY, indexColorModel);
        displayOff = new BufferedImage(160, 144, BufferedImage.TYPE_BYTE_BINARY, indexColorModel);

	}

	public void cycle() {
		dotIndex += 16;
        dotIndex %= 70224;
        int dotX = dotIndex % 456;
        ly = (byte) (dotIndex / 456);

        boolean vBlankInterrupt = false;
        if ((ly & 0xFF) > 0x8f) {
            if (mode == 0) {
                vBlankInterrupt = true;
            }
            mode = 1;
        } else if (dotX <= 80) {
            mode = 2;
        } else if (dotX <= 252) {
            if (mode == 2) {
                writeLine();
            }
            mode = 3;
        } else {
            mode = 0;
        }

        boolean lycSelected = lyc == ly;
        refreshStat(lycSelected);

        if (vBlankInterrupt) {
            imageReady = true;
            bus.requestInterrupt(0x01);
        }

        boolean statHigh = (mode == 0 && (stat & 0x08) == 0x08) ||
            (mode == 1 && (stat & 0x10) == 0x10) ||
            (mode == 2 && (stat & 0x20) == 0x20) ||
            (lycSelected && (stat & 0x40) == 0x40);

        if (statHigh)
        {
            if (!statLatch) {
                statLatch = true;
                bus.requestInterrupt(0x02);
            }
        } else {
            statLatch = false;
        }

	}


    public void writeLine()
	{
		WritableRaster raster = image.getRaster();

        char windowTileMapArea = (char) ((lcdc & 0x40) == 0x40 ? 0x9C00: 0x9800 );
        boolean windowEnable = (lcdc & 0x20) == 0x20;
        boolean backgroundAndWindowTileDataArea = (lcdc & 0x10) == 0x10;
        boolean bgWindowEnabled = (lcdc & 0x01) == 0x01;
        char backgroundTileMapArea = (char) ((lcdc & 0x08) == 0x08 ? 0x9C00: 0x9800 );
        boolean objSize = (lcdc & 0x04) == 0x04;
        boolean objEnable = (lcdc & 0x02) == 0x02;
        int lineNumber = ly & 0xFF;
        int scrollX = scx & 0xFF;
        int scrollY = scy & 0xFF;
        int windowX = (wx & 0xFF) - 7;
        int windowY = wy & 0xFF;

        ObjectAttribute[] objectAttributes = oam.getObjectAttributes();
        List<ObjectAttribute> objects = Arrays.stream(objectAttributes)
            .filter(objectAttribute -> (objectAttribute.getY() & 0xFF) - 16 + (objSize ? 16 : 8) > lineNumber & (objectAttribute.getY() & 0xFF) - 16 <= lineNumber)
            .limit(100)
            .sorted(Comparator.comparingInt(o -> (o.getX() & 0xFF)))
            .toList();

        for (int fetcherX = 0; fetcherX < 160; fetcherX++)
        {
            int pixel;
            if (bgWindowEnabled) {
                char tileMapAddress;
                int tileMapX;
                int tileMapY;
                if (windowEnable && fetcherX >= windowX && lineNumber >= windowY) {
                    tileMapX = fetcherX - windowX;
                    tileMapY = lineNumber - windowY;
                    tileMapAddress = windowTileMapArea;
                } else {
                    tileMapX = (fetcherX + scrollX) & 0xFF;
                    tileMapY = (lineNumber + scrollY) & 0xFF;
                    tileMapAddress = backgroundTileMapArea;
                }
                int tilePixelX = tileMapX % 8;
                int tilePixelY = tileMapY % 8;
                int tileIndex = (tileMapX / 8) + 32 * (tileMapY / 8);
                char tileDataIndexAddress = (char) (tileMapAddress + tileIndex);
                byte tileDataIndex = vRam.read(tileDataIndexAddress);
                char startAddress = getStartAddress(tileDataIndex, backgroundAndWindowTileDataArea);
                pixel = getColor(getPixelColorIndex(startAddress, tilePixelX, tilePixelY), bgp & 0xFF);
            } else {
                pixel = 0;
            }
            for (ObjectAttribute object : objects)
            {
                int x = object.getX() & 0xFF;
                int y = object.getY() & 0xFF;
                byte objectTileIndex = object.getTileIndex();

                if (x - 8 > fetcherX || x <= fetcherX) {
                    continue;
                }

                if (objSize) {
                    if (y - 8 > lineNumber & y - 16 <= lineNumber) {
                        objectTileIndex = (byte) (objectTileIndex & 0xFE);
                    } else {
                        objectTileIndex = (byte) (objectTileIndex | 0x01);
                        y += 8;
                    }
                }
                int objTilePixelX = fetcherX - x + 8;
                int objTilePixelY = lineNumber - y + 16;
                char startAddress = getStartAddress(objectTileIndex, true);
                int objPixelColorIndex = getPixelColorIndex(startAddress, objTilePixelX, objTilePixelY);
                if (objPixelColorIndex != 0) {
                    if (!object.isPriority() || pixel == 0) {
                        pixel = getColor(objPixelColorIndex, object.isDmgPalette() ? obp1 : obp0);
                    }
                    break;
                }
            }
            
            raster.setSample(fetcherX, lineNumber, 0, pixel);
            
            
//            System.out.println(pixel);



        }
//
//		for (int tx = 0; tx < 32; tx++)
//		{
//			for (int ty = 0; ty < 32; ty++)
//			{
//				char tileIndexAddress = (char) (ty * 32 + tx + backgroundTileMapArea);
//				byte tileIndex = bus.read(tileIndexAddress);
//                char startAddress = getStartAddress(tileIndex, backgroundAndWindowTileDataArea);
//                int tileStartX = tx * 8;
//				int tileStartY = ty * 8;
//				drawTile(bus, raster, tileStartX, tileStartY, startAddress, false, false, false, bgp);
//			}
//		}
//
//        if (objEnable)
//        {
//            for (int objectIndex = 39; objectIndex >=0; objectIndex--)
//            {
//                int objectAddress = 0xFE00 + 4 * objectIndex;
//                byte yPos = bus.read((char) objectAddress);
//                byte xPos = bus.read((char) (objectAddress + 1));
//                byte tileIndex = bus.read((char) (objectAddress + 2));
//                byte attributes = bus.read((char) (objectAddress + 3));
//                boolean flipX = ((attributes >> 5) & 1) == 1;
//                boolean flipY = ((attributes >> 6) & 1) == 1;
//                // obj is always on top of BG without including priority flag
//                boolean priority = ((attributes >> 7) & 1) == 1;
//                int palette = (attributes & 0x10) == 0x10 ? obp1 : obp0;
//                drawTile(bus, raster, (xPos & 0xFF) - 8, (yPos & 0xFF) - 16, (char) (0x8000 + ((0xFF & tileIndex) * 16)), flipX, flipY, true, palette);
//            }
//        }
//        int i = 0b11001011;
//
//        // window
//        if (windowEnable) {
//            for (int tx = 0; tx < 32; tx++)
//            {
//                for (int ty = 0; ty < 32; ty++)
//                {
//                    char tileIndexAddress = (char) (ty * 32 + tx + windowTileMapArea);
//                    byte tileIndex = bus.read(tileIndexAddress);
//                    char startAddress = getStartAddress(tileIndex, backgroundAndWindowTileDataArea);
//                    int tileStartX = tx * 8;
//                    int tileStartY = ty * 8;
//                    drawTile(bus, raster, tileStartX, tileStartY, startAddress, false, false, false, bgp);
//                }
//            }
//        }

	}

    private int getPixelColorIndex(char startAddress, int tilePixelX, int tilePixelY)
    {

        int byteAddressOffset = getByteAddressOffset(tilePixelY, false);
        char address1 = (char) (startAddress + byteAddressOffset);
        char address2 = (char) (startAddress + byteAddressOffset + 1);
        byte byte1 = bus.read(address1);
        byte byte2 = bus.read(address2);
        int byteIndex = getByteIndex(tilePixelX, false);
        return  ((byte1 >> byteIndex) & 1) | ((byte2 >> byteIndex) & 1) << 1;
    }

    private int getColor(int colorIndex, int palette) {
        return (palette >> (colorIndex << 1)) & 3;
    }


    private static char getStartAddress(byte tileDataIndex, boolean bgWindowTiles)
    {
        if (bgWindowTiles) {
            return (char)(0x8000 + ((tileDataIndex & 0xFF) * 16));
        } else {
            return (char)(0x9000 + (tileDataIndex * 16));
        }
    }

    private static void drawTile(Bus bus, WritableRaster raster, int tileStartX, int tileStartY, char startAddress, boolean flipX, boolean flipY, boolean transparent, int palette)
	{
		for (int py = 0; py < 8; py++)
		{
			int byteAddressOffset = getByteAddressOffset(py, flipY);
			char address1 = (char) (startAddress + byteAddressOffset);
			char address2 = (char) (startAddress + byteAddressOffset + 1);
			byte byte1 = bus.read(address1);
			byte byte2 = bus.read(address2);
			for (int px = 0; px < 8; px++)
			{
				int byteIndex = getByteIndex(px, flipX);
				int colorShift = ((byte1 >> byteIndex) & 1) | ((byte2 >> byteIndex) & 1) << 1;
				if (colorShift != 0 || !transparent) {
					int sample = (palette >> (colorShift << 1)) & 3;
                    int x = px + tileStartX;
                    int y = py + tileStartY;
                    if (x >= 0 && x <= 256 && y >= 0 && y <= 256) {
                        raster.setSample(x, y, 0, sample);
                    }
				}
			}
		}
	}

	private static int getByteAddressOffset(int py, boolean flip)
	{
		int i = py * 2;
		return flip ? 14 - i : i;
	}

	private static int getByteIndex(int px, boolean flip)
	{
		return flip ? px : 7 - px;
	}

	private void refreshStat(boolean lycSelected)
	{
        stat = (byte) ((stat & 0b11111000) | mode | (lycSelected ? 0x04 : 0));
	}

	public BufferedImage getImage()
	{
        boolean displayEnabled = (lcdc & 0x80) == 0x80;
        if (displayEnabled) {
            return image;
        } else {
            return displayOff;
        }
	}

    private void executeDma()
    {
        char dmaStart = (char) ((dma & 0xFF) * 0x0100);
        for (int i = 0; i < 0xa0; i++)
        {
            char sourceAddress = (char) (dmaStart + i);
            char destinationAddress = (char) (0xFE00 + i);
            bus.write(destinationAddress, bus.read(sourceAddress));
        }
    }

    @Override
    public byte read(char address)
    {
        return switch (address) {
            case Bus.LCDC -> lcdc;
            case Bus.STAT -> stat;
            case Bus.SCY -> scy;
            case Bus.SCX -> scx;
            case Bus.LY -> ly;
            case Bus.LYC -> lyc;
            case Bus.DMA -> dma;
            case Bus.BGP -> bgp;
            case Bus.OBP0 -> obp0;
            case Bus.OBP1 -> obp1;
            case Bus.WY -> wy;
            case Bus.WX -> wx;
            default -> throw new IllegalStateException("Unexpected value: " + address);
        };
    }

    @Override
    public void write(char address, byte value)
    {
        switch (address) {
            case Bus.LCDC -> {
                lcdc = value;
            }
            case Bus.STAT -> stat = value;
            case Bus.SCY -> scy = value;
            case Bus.SCX -> scx = value;
            case Bus.LY -> {}
            case Bus.LYC -> lyc = value;
            case Bus.DMA ->
            {
                dma = value;
                executeDma();
            }
            case Bus.BGP -> bgp = value;
            case Bus.OBP0 -> obp0 = value;
            case Bus.OBP1 -> obp1 = value;
            case Bus.WY -> wy = value;
            case Bus.WX -> wx = value;
            default -> throw new IllegalStateException("Unexpected value: " + address);
        };
    }

    public boolean isImageReady()
    {
        return imageReady;
    }

    public void setImageReady(boolean imageReady)
    {
        this.imageReady = imageReady;
    }

    public byte getLy()
    {
        return ly;
    }
}
