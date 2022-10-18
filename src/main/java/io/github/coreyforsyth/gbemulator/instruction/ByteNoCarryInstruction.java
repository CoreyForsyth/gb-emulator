package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class ByteNoCarryInstruction extends FlagInstruction<Byte>
{
    public ByteNoCarryInstruction(Function<CPU, Byte> a, Function<CPU, Byte> b, BiConsumer<CPU, Byte> setter)
    {
        super(a, b, setter);
    }

    @Override
    public Byte castResult(int result)
    {
        return (byte) result;
    }
}
