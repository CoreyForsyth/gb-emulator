package io.github.coreyforsyth.gbemulator.memory;

import java.io.PrintStream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class IO implements ReadWrite
{

	final byte[] ram;

	public IO()
	{
		this.ram = new byte[0x007F];
	}

	public int getOffset(int sp) {
		if (sp < 0xFF00 || sp > 0xFF7F) {
			throw new RuntimeException("WorkRam access issue");
		}
		return sp - 0xFF00;
	}
	
	@Override
	public byte read(char address)
	{
		byte value = ram[getOffset(address)];
		switch (address)
		{
//			case 0xFF41 -> printWriteLog(address, value, "STAT");
			case 0xFF0F ->
				printWriteLog(address, value, "IF", "Reading");
		}
//		if (address == 0xFF44) {
//			System.out.printf("reading LY, value = %02X\n", b);
//		}
		return value;
	}

	@Override
	public void write(char address, byte value)
	{
		switch (address)
		{
//			case 0xFF41 -> printWriteLog(address, value, "STAT", "Writing");
			case 0xFF0F -> printWriteLog(address, value, "VBLANK IF", "Writing");
			case 0xFF47, 0xFF48, 0xFF49 ->
				printWriteLog(address, value, "palette", "Writing");
			case 0xFF46 -> printWriteLog(address, value, "DMA", "Writing");
		}
		ram[getOffset(address)] = value;
	}

	private static PrintStream printWriteLog(int address, byte value, String type, String action)
	{
		return System.out.printf("%s %02X into %s address %04X\n", action, value, type, address);
	}
}
