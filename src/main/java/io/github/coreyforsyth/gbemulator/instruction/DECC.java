package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class DECC extends Instruction<Character, Void>
{

    public DECC(Accessor<Character> setter)
    {
        super(setter, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        char value = primary.apply(cpu);
        primary.accept(cpu, (char) (value - 1));
    }
}
