package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class LDC extends Instruction<Character, Character>
{

	private final Function<CPU, Character> getter;
	private final BiConsumer<CPU, Character> setter;

	public LDC(Accessor<Character> setter, Accessor<Character> getter)
	{
        super(setter, getter);
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void accept(CPU cpu)
	{
		setter.accept(cpu, getter.apply(cpu));
	}
}
