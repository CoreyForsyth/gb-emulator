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
	private final CPU cpu;
	private boolean update;

	public ScreenPanel(CPU cpu) {
        super();
		this.cpu = cpu;
		setMinimumSize(new Dimension(256, 256));
        setPreferredSize(new Dimension(256, 256));
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

	public void drawScreen(Graphics g) {
		BufferedImage image = cpu.getDisplay().getImage();
		g.drawImage(image.getScaledInstance(256, 256, Image.SCALE_DEFAULT), 0, 0, null);
	}

	public void update() {
		update = true;
		repaint();
	}
}
