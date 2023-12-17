package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class LD extends Instruction<Byte, Byte>
{

	public LD(Accessor<Byte> primary, Accessor<Byte> secondary)
	{
		super(primary, secondary);
	}

	@Override
	public void accept(CPU cpu)
	{
		primary.accept(cpu, secondary.apply(cpu));
	}
}
