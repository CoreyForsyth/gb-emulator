package io.github.coreyforsyth.gbemulator.graphics;

import io.github.coreyforsyth.gbemulator.CPU;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import javax.swing.JPanel;

public class ScreenPanel extends JPanel
{
	private CPU cpu;
	private boolean update;

	public ScreenPanel(CPU cpu) {
        super();
		this.cpu = cpu;
		setMinimumSize(new Dimension(512, 512));
        setPreferredSize(new Dimension(512, 512));
        setBackground(Color.BLACK);
    }

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (update) {
			update = false;
			drawScreen(g);
		}
	}


//	public static void drawImage(CPU cpu, BufferedImage image)
//	{
//		WritableRaster raster = image.getRaster();
//
//
//		char windowTileMap = 0x9C00;
//		char backgroundTileMap = 0x9800;
//		char tileData = 0x9000;
//
//
//		for (int tx = 0; tx < 32; tx++)
//		{
//			for (int ty = 0; ty < 32; ty++)
//			{
//				char tileIndexAddress = (char) (ty * 32 + tx + backgroundTileMap);
//				byte tileIndex = cpu.readByte(tileIndexAddress);
//				char startAddress = (char)(tileData + (tileIndex * 16));
//				int tileStartX = tx * 8;
//				int tileStartY = ty * 8;
//				drawTile(cpu, raster, tileStartX, tileStartY, startAddress, false, false, false, 0b11100100);
//			}
//		}
//
//		for (int objectIndex = 39; objectIndex >=0; objectIndex--)
//		{
//			int objectAddress = 0xFE00 + 4 * objectIndex;
//			byte yPos = cpu.readByte((char) objectAddress);
//			byte xPos = cpu.readByte((char) (objectAddress + 1));
//			byte tileIndex = cpu.readByte((char) (objectAddress + 2));
//			byte attributes = cpu.readByte((char) (objectAddress + 3));
//			char paletteAddress = (char) (0xFF48 + ((attributes >> 4) & 1));
//			boolean flipX = ((attributes >> 5) & 1) == 1;
//			boolean flipY = ((attributes >> 6) & 1) == 1;
//			// obj is always on top of BG without including priority flag
//			boolean priority = ((attributes >> 7) & 1) == 1;
//			int palette = cpu.readByte(paletteAddress) & 0xFF;
//			drawTile(cpu, raster, (xPos & 0xFF) - 8, (yPos & 0xFF) - 16, (char) (0x8000 + ((0xFF & tileIndex) * 16)), flipX, flipY, true, palette);
//		}
//
//
//
//	}


//	private static void drawTile(CPU cpu, WritableRaster raster, int tileStartX, int tileStartY, char startAddress, boolean flipX, boolean flipY, boolean transparent, int pallette)
//	{
//		for (int py = 0; py < 8; py++)
//		{
//			int byteAddressOffset = getByteAddressOffset(py, flipY);
//			char address1 = (char) (startAddress + byteAddressOffset);
//			char address2 = (char) (startAddress + byteAddressOffset + 1);
//			byte byte1 = cpu.readByte(address1);
//			byte byte2 = cpu.readByte(address2);
//			for (int px = 0; px < 8; px++)
//			{
//				int byteIndex = getByteIndex(px, flipX);
//				int colorShift = ((byte1 >> byteIndex) & 1) | ((byte2 >> byteIndex) & 1) << 1;
//				if (colorShift != 0 || !transparent) {
//					int sample = (pallette >> (colorShift << 1)) & 3;
//					raster.setSample(px + tileStartX, py + tileStartY, 0, sample);
//				}
//			}
//		}
//	}
//
//	private static int getByteAddressOffset(int py, boolean flip)
//	{
//		int i = py * 2;
//		return flip ? 14 - i : i;
//	}
//
//	private static int getByteIndex(int px, boolean flip)
//	{
//		return flip ? px : 7 - px;
//	}
//
//	public static void main(String[] args)
//	{
//
//		byte b1 = (byte) 227;
//		String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
//		System.out.println(s1); // 11100011
//
//	}

	public void drawScreen(Graphics g) {
//		Color[] colors = new Color[4];
//		colors[0] = new Color(0xFFFFFF);
//		colors[1] = new Color(0xADADAD);
//		colors[2] = new Color(0x525252);
//		colors[3] = new Color(0x000000);
//		int[] cmap = Arrays.stream(colors).map(Color::getRGB)
//			.mapToInt(Integer::intValue).toArray();
//
//		IndexColorModel indexColorModel = new IndexColorModel(2, 4, cmap, 0, false, -1, DataBuffer.TYPE_BYTE);
//		BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_BINARY, indexColorModel);
//		drawImage(cpu, image);
		BufferedImage image = cpu.getDisplay().getImage();
		g.drawImage(image.getScaledInstance(512, 512, Image.SCALE_DEFAULT), 0, 0, null);
	}

	public void update() {
		update = true;
		repaint();
	}
}
