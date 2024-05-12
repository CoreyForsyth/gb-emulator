package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;

public class EI extends Instruction<Void, Void>
{

    public EI()
    {
        super(null, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        cpu.setInterruptEnabled(true);
    }
}