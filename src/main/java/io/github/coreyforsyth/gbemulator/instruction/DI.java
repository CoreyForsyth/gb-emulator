package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;

public class DI extends Instruction<Void, Void>
{

    public DI()
    {
        super(null, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        cpu.setInterruptEnabled(false);
    }
}
