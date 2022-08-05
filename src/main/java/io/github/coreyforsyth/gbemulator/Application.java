package io.github.coreyforsyth.gbemulator;

import com.formdev.flatlaf.FlatDarkLaf;
import java.io.File;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application
{

    public static void main(String[] args) {

        byte mask = (byte) 0xFF;


        System.out.printf("%02X%n", mask);
        byte test = (byte) 0x41;
        System.out.println((test & mask) != test);
        if (true)
            return;

        FlatDarkLaf.setup();
        CPU cpu = RomLoader.initCpu(new File(System.getProperty("user.dir") + "/src/main/resources/blue.gb"));
        Debugger debugger = new Debugger(cpu);


    }

}
