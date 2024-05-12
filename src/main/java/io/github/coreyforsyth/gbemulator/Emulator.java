package io.github.coreyforsyth.gbemulator;

import io.github.coreyforsyth.gbemulator.instruction.Instructions;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Emulator extends JPanel
{
    private CPU cpu;
    private final BufferedImage image;
    private long nextCycleTime;

    public Emulator(CPU cpu)
    {
        this.cpu = cpu;
        Color[] colors = new Color[4];
        colors[0] = new Color(0xFFFFFF);
        colors[1] = new Color(0xADADAD);
        colors[2] = new Color(0x525252);
        colors[3] = new Color(0x000000);
        int[] cmap = Arrays.stream(colors).map(Color::getRGB)
            .mapToInt(Integer::intValue).toArray();
        IndexColorModel indexColorModel = new IndexColorModel(2, 4, cmap, 0, false, -1, DataBuffer.TYPE_BYTE);
        image = new BufferedImage(160, 144, BufferedImage.TYPE_BYTE_BINARY, indexColorModel);
        Dimension size = new Dimension(160, 144);
        setPreferredSize(size);
        setMinimumSize(size);
        Thread gameThread = new Thread(() -> {
            nextCycleTime = System.nanoTime();
            while (true) {
                long currentTime = System.nanoTime();
                if (currentTime > nextCycleTime) {
                    int cycles = Instructions.next(cpu);
                    if (cpu.getDisplay().isImageReady()) {
                        copyGameImage(cpu);
                        cpu.getDisplay().setImageReady(false);
                    }
                    nextCycleTime = nextCycleTime + cycles * 3000L;
                } else {
                    Thread.onSpinWait();
                }
            }
        });
        gameThread.start();
    }

    private void copyGameImage(CPU cpu)
    {
        try
        {
            SwingUtilities.invokeAndWait(() -> {
                BufferedImage displayImage = cpu.getDisplay().getImage();
                displayImage.copyData(image.getRaster());
                this.repaint();
            });
        }
        catch (InterruptedException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 160, 144, null);
    }
}
