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
		setMinimumSize(new Dimension(160, 144));
        setPreferredSize(new Dimension(160, 144));
        setBackground(Color.BLACK);
    }

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
        BufferedImage image = cpu.getDisplay().getImage();
        g.drawImage(image, 0, 0, null);
	}

	public void drawScreen(Graphics g) {
	}

	public void update() {
		update = true;
		repaint();
	}

    public void setCpu(CPU cpu)
    {
        this.cpu = cpu;
    }
}
