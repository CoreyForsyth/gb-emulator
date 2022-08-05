package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.Function;

public class TestBit implements Instruction
{

    private final byte testMask;
    private final Function<CPU, Byte> getter;

    public TestBit(int count, Function<CPU, Byte> getter)
    {
        this.testMask = (byte) (1 << count);
        this.getter = getter;
    }

    @Override
    public void accept(CPU cpu)
    {
        Byte value = getter.apply(cpu);
        cpu.setZero((value & testMask) != value);
        cpu.setHalfCarry(true);
        cpu.setCarry(false);
    }
}
