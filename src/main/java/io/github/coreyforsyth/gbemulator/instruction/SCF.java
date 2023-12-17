package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class SCF extends Instruction<Void, Void>
{
    public SCF()
    {
        super(null, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        cpu.setCarry(true);
        cpu.setHalfCarry(false);
        cpu.setSubtraction(false);
    }
}
