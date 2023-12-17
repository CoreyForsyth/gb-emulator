package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class CCF extends Instruction<Void, Void>
{
    public CCF()
    {
        super(null, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        cpu.setSubtraction(false);
        cpu.setHalfCarry(false);
        cpu.setCarry(!cpu.isCarry());
    }
}
