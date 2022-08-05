package io.github.coreyforsyth.gbemulator;

import io.github.coreyforsyth.gbemulator.memory.ExternalRam;
import io.github.coreyforsyth.gbemulator.memory.HRam;
import io.github.coreyforsyth.gbemulator.memory.Cartridge;
import io.github.coreyforsyth.gbemulator.memory.WorkRam;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RomLoader
{
    public static CPU initCpu(File file)
    {
        byte[] data;
        try
        {
            data = Files.readAllBytes(file.toPath());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        Cartridge cartridge = new Cartridge(data);
        CPU cpu = new CPU(cartridge, new WorkRam(), new HRam());
        cpu.setA((byte) 0x01);
//        cpu.setF((byte) 0x80);
        cpu.setZero(true);
        cpu.setSubtraction(false);
        cpu.setHalfCarry(true);
        cpu.setCarry(true);
        cpu.setB((byte) 0x00);
        cpu.setC((byte) 0x13);
        cpu.setD((byte) 0x00);
        cpu.setE((byte) 0xD8);
        cpu.setH((byte) 0x01);
        cpu.setL((byte) 0x4D);
        cpu.setPC((char) 0x0100);
        cpu.setSP((char) 0xFFFE);
//        for (int i = 0; i < 50; i++)
//        {
//            for (int j = 0; j < 16; j++)
//            {
////                System.out.format("%02X ", cpu.nextByte());
//
////                byte high = (byte) (rom.read((char) (i * 16 + j)) & 0xFF);
////                byte low = (byte) (rom.read((char) (i * 16 + j + 1)) & 0xFF);
////                int i1 = high << 8 & low & 0xFF;
//////                System.out.format("%02X %02X ", high, low);
////                System.out.format("%04X ", ((high << 8) & 0xFFFF) | (low & 0xFFFF));
//////                byte high = rom.read((char) (i * 16 + j));
//////                System.out.print((char) high);
//            }
//            System.out.println();
//        }
        return cpu;
    }
}
