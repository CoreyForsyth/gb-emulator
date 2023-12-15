package io.github.coreyforsyth.gbemulator;

import io.github.coreyforsyth.gbemulator.graphics.Display;
import io.github.coreyforsyth.gbemulator.memory.Cartridge;
import io.github.coreyforsyth.gbemulator.memory.HRam;
import io.github.coreyforsyth.gbemulator.memory.IO;
import io.github.coreyforsyth.gbemulator.memory.Oam;
import io.github.coreyforsyth.gbemulator.memory.VRam;
import io.github.coreyforsyth.gbemulator.memory.WorkRam;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
		VRam vRam = new VRam();
		IO io = new IO();
		Oam oam = new Oam();
		CPU cpu = new CPU(cartridge, new WorkRam(), new HRam(), vRam, oam, io, new Display(io, vRam, oam));
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
		addDefaultScreen(cpu);
        return cpu;
    }

	public static void addDefaultScreen(CPU cpu) {
		try
		{
			byte[] bytes = Files.readAllBytes(Path.of("src/main/resources/intro_frame_export_vram"));
			System.out.println(bytes.length);
			if (bytes.length >= 0x2000) {
				for (int i = 0x8000; i < 0xA000; i++)
				{
					int j = i - 0x8000;
					int value = bytes[j];
					cpu.writeByte((char) i, (byte) value);
				}
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		try
		{
			byte[] bytes = Files.readAllBytes(Path.of("src/main/resources/intro_frame_export_oam"));
			System.out.println(bytes.length);
			if (bytes.length >= 0xA0) {
				for (int i = 0xFE00; i < 0xFEA0; i++)
				{
					int j = i - 0xFE00;
					int value = bytes[j];
					cpu.writeByte((char) i, (byte) value);
				}
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		cpu.writeByte((char) 0xFF47, (byte) 0xE4);
		cpu.writeByte((char) 0xFF48, (byte) 0xE4);
		cpu.writeByte((char) 0xFF49, (byte) 0xE4);
	}
}
