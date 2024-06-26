package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class CB extends Instruction<Byte, Void>
{
    public CB()
    {
        super(Accessor.IM8, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        CBInstructions.next(cpu);
    }
}
