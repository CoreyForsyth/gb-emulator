package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;

public abstract class ByteNoCarryInstruction extends FlagInstruction<Byte>
{
    public ByteNoCarryInstruction(Accessor<Byte> primary, Accessor<Byte> secondary)
    {
        super(primary, secondary);
    }

    @Override
    public Byte castResult(int result)
    {
        return (byte) result;
    }
}
