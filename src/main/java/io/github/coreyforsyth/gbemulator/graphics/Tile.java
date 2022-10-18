package io.github.coreyforsyth.gbemulator.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.function.Function;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Tile
{

    public static void main(String[] args)
    {
        System.out.println(0x9BFF - 0x9800);
        byte[] bytes = new byte[]{0x3C, 0x7E, 0x42, 0x42, 0x42, 0x42, 0x42, 0x42, 0x7E, 0x5E, 0x7E, 0x0A, 0x7C, 0x56, 0x38, 0x7C};
        Color[] colors = new Color[4];
        colors[0] = new Color(0x081820);
        colors[1] = new Color(0x346856);
        colors[2] = new Color(0x88c070);
        colors[3] = new Color(0xe0f8d0);
        BufferedImage tile = createTile(bytes, colors);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon(tile.getScaledInstance(320, 320, Image.SCALE_DEFAULT));
        JLabel jLabel = new JLabel(img);
        frame.add(jLabel);
        frame.pack();
        frame.setVisible(true);

    }

    public static BufferedImage createTile(byte[] bytes, Color[] colors)
    {
        if (bytes.length != 16)
        {
            throw new RuntimeException("Wrong amount of data for tile");
        }
        BufferedImage image = new BufferedImage(8, 8, BufferedImage.TYPE_BYTE_BINARY, new IndexColorModel(2, 4, getColors(colors, Color::getRed), getColors(colors, Color::getGreen), getColors(colors, Color::getBlue)));
        WritableRaster raster = image.getRaster();
        for (int y = 0; y < 8; y++)
        {
            for (int x = 0; x < 8; x++)
            {
                byte byte1 = bytes[y * 2];
                byte byte2 = bytes[y * 2 + 1];
                int sample = ((byte1 >> (7 - x)) & 1) | ((byte2 >> (6 - x)) & 2);
                raster.setSample(x, y, 0, sample);
            }
        }
        return image;
    }

    public static byte[] getColors(Color[] colors, Function<Color, Integer> getter)
    {
        byte[] bytes = new byte[4];
        for (int i = 0; i < colors.length; i++)
        {
            int color = getter.apply(colors[i]);
            bytes[i] = (byte) color;
        }
        return bytes;
    }

}
