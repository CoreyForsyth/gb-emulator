package io.github.coreyforsyth.gbemulator.memory;

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
		this.ram = new byte[0x0080];
	}

	public int getOffset(int sp) {
		if (sp < 0xFF00 || sp > 0xFF7F) {
			throw new RuntimeException("IO access issue");
		}
		return sp & 0xFF;
	}
	
	@Override
	public byte read(char address)
	{
        return ram[getOffset(address)];
	}

	@Override
	public void write(char address, byte value)
	{
//		switch (address)
//		{
//			case 0xFF0F -> printWriteLog(address, value, "IF", "Writing");
//		}
        try
        {
            ram[getOffset(address)] = value;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	private static void printWriteLog(int address, byte value, String type, String action)
	{
		System.out.printf("%s %02X into %s address %04X\n", action, value, type, address);
	}
}
