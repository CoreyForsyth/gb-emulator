package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;

public class NOP extends Instruction<Void, Void>
{
    public NOP()
    {
        super(null, null);
    }

    @Override
    public void accept(CPU cpu)
    {

    }
}
