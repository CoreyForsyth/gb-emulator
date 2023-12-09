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
		return ram[getOffset(address)];
	}

	@Override
	public void write(char address, byte value)
	{
		ram[getOffset(address)] = value;
	}
}
