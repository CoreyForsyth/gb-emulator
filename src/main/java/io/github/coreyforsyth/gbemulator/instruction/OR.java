package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class OR extends ByteNoCarryInstruction
{
    public OR(Accessor<Byte> secondary)
    {
        super(Accessor.A, secondary);
    }

    @Override
    public int applyOperation(CPU cpu, Byte a, Byte b)
    {
        return a | b;
    }
}
