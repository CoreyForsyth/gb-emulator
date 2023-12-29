package io.github.coreyforsyth.gbemulator;

import com.formdev.flatlaf.FlatDarkLaf;
import java.io.File;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application
{

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        CPU cpu = RomLoader.initCpu(new File(System.getProperty("user.dir") + "/src/main/resources/blue.gb"));
        Debugger debugger = new Debugger(cpu);
    }

}
