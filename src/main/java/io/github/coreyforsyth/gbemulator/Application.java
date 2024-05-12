package io.github.coreyforsyth.gbemulator;

import com.formdev.flatlaf.FlatDarkLaf;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.UIManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application
{

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        UIManager.put("@background", "#fff");
        CPU cpu = RomLoader.initCpu(new File(args[0]));
        Emulator emulator = new Emulator(cpu);
        JFrame jFrame = new JFrame();
        jFrame.setContentPane(emulator);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.pack();
    }

}
