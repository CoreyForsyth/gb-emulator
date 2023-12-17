package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class SBC extends ByteCarryInstruction
{
    public SBC(Accessor<Byte> primary, Accessor<Byte> secondary)
    {
        super(primary, secondary);
    }

    @Override
    public int applyOperation(CPU cpu, Byte a, Byte b)
    {
        return a - b - (cpu.isCarry() ? 1 : 0);
    }

    @Override
    public void setS(CPU cpu)
    {
        cpu.setSubtraction(true);
    }
}
