package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class CPL extends Instruction<Byte, Void>
{
    public CPL()
    {
        super(Accessor.A, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        primary.accept(cpu, (byte) ~primary.apply(cpu));
        cpu.setSubtraction(true);
        cpu.setHalfCarry(true);
    }
}
