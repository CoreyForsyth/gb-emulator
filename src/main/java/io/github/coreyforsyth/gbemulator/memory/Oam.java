package io.github.coreyforsyth.gbemulator.memory;

public class Oam implements ReadWrite
{
	final byte[] ram;

	public Oam()
	{
		this.ram = new byte[0xA0];
	}

	public int getOffset(int sp) {
		if (sp < 0xFE00 || sp > 0xFE9F) {
			throw new RuntimeException("WorkRam access issue");
		}
		return sp - 0xFE00;
	}

	@Override
	public byte read(char address)
	{
		return ram[getOffset(address)];
	}

	@Override
	public void write(char address, byte value)
	{
		ram[getOffset(address)] = value;
	}
}
