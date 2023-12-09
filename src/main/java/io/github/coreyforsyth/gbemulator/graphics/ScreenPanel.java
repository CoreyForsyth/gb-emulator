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
import java.util.function.Function;
import javax.swing.JPanel;

public class ScreenPanel extends JPanel
{
	private CPU cpu;
	private boolean update;

	public ScreenPanel(CPU cpu) {
        super();
		this.cpu = cpu;
		setMinimumSize(new Dimension(480, 432));
        setPreferredSize(new Dimension(480, 432));
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


	public static void drawImage(CPU cpu, BufferedImage image)
	{
		WritableRaster raster = image.getRaster();
		for (int ty = 0; ty < 18; ty++)
		{
			for (int tx = 0; tx < 20; tx++)
			{
				int startIndex = 0x8000 + tx * 16 + 320 * ty;
				for (int py = 0; py < 8; py++)
				{
					for (int px = 0; px < 8; px++)
					{
						int address1 = (startIndex + py * 2);
						byte byte1 = cpu.readByte((char) address1);
						char address2 = (char) (startIndex + py * 2 + 1);
						byte byte2 = cpu.readByte(address2);
						int sample = ((byte1 >> (7 - px)) & 1) | ((byte2 >> (6 - px)) & 2);
						raster.setSample(px + tx * 8, py + ty * 8, 0, sample);
					}
				}
			}
		}




	}

	public static void main(String[] args)
	{

		byte b1 = (byte) 227;
		String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
		System.out.println(s1); // 11100011

	}

	public void drawScreen(Graphics g) {
		Color[] colors = new Color[4];
		colors[0] = new Color(0xFFFFFF);
		colors[1] = new Color(0xAAAAAA);
		colors[2] = new Color(0x555555);
		colors[3] = new Color(0x000000);
		int[] cmap = Arrays.stream(colors).map(Color::getRGB)
			.mapToInt(Integer::intValue).toArray();

		IndexColorModel indexColorModel = new IndexColorModel(2, 4, cmap, 0, false, -1, DataBuffer.TYPE_BYTE);
		BufferedImage image = new BufferedImage(160, 144, BufferedImage.TYPE_BYTE_BINARY, indexColorModel);
		drawImage(cpu, image);
		g.drawImage(image.getScaledInstance(480, 432, Image.SCALE_DEFAULT), 0, 0, null);
	}

	public void update() {
		update = true;
		repaint();
	}
}
