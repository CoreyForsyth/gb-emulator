package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class XOR extends ByteNoCarryInstruction
{
    public XOR(Accessor<Byte> secondary)
    {
        super(Accessor.A, secondary);
    }

    @Override
    public int applyOperation(CPU cpu, Byte a, Byte b)
    {
        return a ^ b;
    }

}
