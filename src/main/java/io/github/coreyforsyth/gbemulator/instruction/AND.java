package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class AND extends ByteNoCarryInstruction
{
    public AND(Accessor<Byte> secondary)
    {
        super(Accessor.A, secondary);
    }

    @Override
    public int applyOperation(CPU cpu, Byte a, Byte b)
    {
        return a & b;
    }

    @Override
    public void setHC(CPU cpu, Byte result, Byte a, Byte b)
    {
        cpu.setHalfCarry(true);
    }
}
