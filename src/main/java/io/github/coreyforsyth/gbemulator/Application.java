package io.github.coreyforsyth.gbemulator;

import com.formdev.flatlaf.FlatDarkLaf;
import java.io.File;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application
{

    public static void main(String[] args) {
//        byte a = (byte) 0b10000010;
//        char c = (char) a;
//
//        char b = (char) (0x00ff);
//        System.out.println(Integer.toBinaryString((b + a) & 0xffff));
//        System.out.println((int)((char) (b + c)));
//        if (true)
//            return;

        FlatDarkLaf.setup();
        CPU cpu = RomLoader.initCpu(new File(System.getProperty("user.dir") + "/src/main/resources/blue.gb"));
        Debugger debugger = new Debugger(cpu);


    }

}
