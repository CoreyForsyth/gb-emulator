package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class INCC extends Instruction<Character, Void>
{

    public INCC(Accessor<Character> primary)
    {
        super(primary, null);
    }

	@Override
	public void accept(CPU cpu)
	{
        cpu.cycle();
        char value = primary.apply(cpu);
        primary.accept(cpu, (char) (value + 1));
	}
}
