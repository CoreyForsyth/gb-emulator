package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;

public class CB extends Instruction<Void, Void>
{
    public CB()
    {
        super(null, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        CBInstructions.next(cpu);
    }
}
