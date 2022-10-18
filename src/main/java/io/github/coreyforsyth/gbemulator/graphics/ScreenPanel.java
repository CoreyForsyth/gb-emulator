package io.github.coreyforsyth.gbemulator.graphics;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

public class ScreenPanel extends JPanel
{
    public ScreenPanel() {
        super();
        setMinimumSize(new Dimension(512, 512));
        setPreferredSize(new Dimension(512, 512));
        setBackground(Color.BLACK);
    }
}
