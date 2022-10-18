package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SBC extends ByteCarryInstruction
{
    public SBC(Function<CPU, Byte> a, Function<CPU, Byte> b, BiConsumer<CPU, Byte> setter)
    {
        super(a, b, setter);
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