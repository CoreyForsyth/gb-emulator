package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ADD extends ByteCarryInstruction
{

    public ADD(Function<CPU, Byte> a, Function<CPU, Byte> b, BiConsumer<CPU, Byte> setter)
    {
        super(a, b, setter);
    }

    @Override
    public int applyOperation(CPU cpu, Byte a, Byte b)
    {
        return a + b;
    }
}
