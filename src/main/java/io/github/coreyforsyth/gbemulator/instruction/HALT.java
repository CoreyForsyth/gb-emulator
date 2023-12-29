package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;

public class HALT extends Instruction<Void, Void>
{
    public HALT()
    {
        super(null, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        cpu.setHalt(true);
        System.out.println("HALTED");
    }
}
