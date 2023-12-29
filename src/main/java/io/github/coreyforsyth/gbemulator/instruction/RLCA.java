package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

class RLCA extends RLC
{
    public RLCA()
    {
        super(Accessor.A);
    }

    @Override
    public void accept(CPU cpu)
    {
        super.accept(cpu);
        cpu.setZero(false);
    }
}
