package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class ADD extends ByteCarryInstruction
{

    public ADD(Accessor<Byte> primary, Accessor<Byte> secondary)
    {
        super(primary, secondary);
    }

    @Override
    public int applyOperation(CPU cpu, Byte a, Byte b)
    {
        return a + b;
    }
}
