package io.github.coreyforsyth.gbemulator.graphics;

import io.github.coreyforsyth.gbemulator.memory.IO;
import io.github.coreyforsyth.gbemulator.memory.Oam;
import io.github.coreyforsyth.gbemulator.memory.VRam;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;

public class Display
{
	private static final char LY = 0xFF44;
	private static final char LYC = 0xFF45;
	private static final char STAT = 0xFF41;
	private static final char BGP = 0xFF47;

	private final IO io;
	private final VRam vRam;
	private final Oam oam;
	private final IndexColorModel indexColorModel;

	private BufferedImage image;
	private int dotIndex;
	private int mode;

	public static void main(String[] args)
	{
		Display display = new Display(new IO(), new VRam(), new Oam());
		for (int i = 0; i < 140000; i++)
		{
			display.cycle(4);
		}
	}

	public Display(IO io, VRam vRam, Oam oam)
	{
		this.io = io;
		this.vRam = vRam;
		this.oam = oam;
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

	}

	public void cycle(int dotsElapsed) {
		dotIndex += dotsElapsed;
		setLy();
		int dotX = dotIndex % 456;
		if (mode == 2 && dotX >= 80)
		{
			setMode(3);
		} else if (mode == 3 && dotX >= 252) {
			setMode(0);
		} else if (mode == 0 && dotX < 252) {
			if (dotIndex / 456 > 143) {
				byte IF = io.read((char) 0xFF0F);
				IF = (byte) (IF | 1);
				io.write((char) 0xFF0F, IF);
				setMode(1);
				writeImage();
			} else {
				setMode(2);
			}
		} else if (mode == 1 && dotIndex >= 70224) {
			dotIndex = dotX;
			setMode(2);
		}
	}

	public void writeImage()
	{
		System.out.println("WRITING IMAGE");
		image = new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_BINARY, indexColorModel);
		WritableRaster raster = image.getRaster();


		char windowTileMap = 0x9C00;
		char backgroundTileMap = 0x9800;
		char tileData = 0x9000;

		int bgPalette = io.read(BGP);

		for (int tx = 0; tx < 32; tx++)
		{
			for (int ty = 0; ty < 32; ty++)
			{
				char tileIndexAddress = (char) (ty * 32 + tx + backgroundTileMap);
				byte tileIndex = vRam.read(tileIndexAddress);
				char startAddress = (char)(tileData + (tileIndex * 16));
				int tileStartX = tx * 8;
				int tileStartY = ty * 8;
				drawTile(vRam, raster, tileStartX, tileStartY, startAddress, false, false, false, bgPalette);
			}
		}

		for (int objectIndex = 39; objectIndex >=0; objectIndex--)
		{
			int objectAddress = 0xFE00 + 4 * objectIndex;
			byte yPos = oam.read((char) objectAddress);
			byte xPos = oam.read((char) (objectAddress + 1));
			byte tileIndex = oam.read((char) (objectAddress + 2));
			byte attributes = oam.read((char) (objectAddress + 3));
			char paletteAddress = (char) (0xFF48 + ((attributes >> 4) & 1));
			boolean flipX = ((attributes >> 5) & 1) == 1;
			boolean flipY = ((attributes >> 6) & 1) == 1;
			// obj is always on top of BG without including priority flag
			boolean priority = ((attributes >> 7) & 1) == 1;
			int palette = io.read(paletteAddress) & 0xFF;
			drawTile(vRam, raster, (xPos & 0xFF) - 8, (yPos & 0xFF) - 16, (char) (0x8000 + ((0xFF & tileIndex) * 16)), flipX, flipY, true, palette);
		}



	}

	private static void drawTile(VRam vRam, WritableRaster raster, int tileStartX, int tileStartY, char startAddress, boolean flipX, boolean flipY, boolean transparent, int palette)
	{
		for (int py = 0; py < 8; py++)
		{
			int byteAddressOffset = getByteAddressOffset(py, flipY);
			char address1 = (char) (startAddress + byteAddressOffset);
			char address2 = (char) (startAddress + byteAddressOffset + 1);
			byte byte1 = vRam.read(address1);
			byte byte2 = vRam.read(address2);
			for (int px = 0; px < 8; px++)
			{
				int byteIndex = getByteIndex(px, flipX);
				int colorShift = ((byte1 >> byteIndex) & 1) | ((byte2 >> byteIndex) & 1) << 1;
				if (colorShift != 0 || !transparent) {
					int sample = (palette >> (colorShift << 1)) & 3;
					raster.setSample(px + tileStartX, py + tileStartY, 0, sample);
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

	private void setLy()
	{
		io.write(LY, (byte) (dotIndex / 456));
	}

	private void setMode(int mode) {
		this.mode = mode;
		byte newValue = (byte) ((io.read(STAT) & 0xfc) | mode);
		io.write(STAT, newValue);
	}

	public BufferedImage getImage()
	{
		if (image == null) {
			writeImage();
		}
		return image;
	}
}
