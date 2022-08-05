package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RLC implements Instruction
{

    private final Function<CPU, Byte> getter;
    private final BiConsumer<CPU, Byte> setter;

    public RLC(Function<CPU, Byte> getter, BiConsumer<CPU, Byte> setter)
    {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void accept(CPU cpu)
    {
        int value = getter.apply(cpu);
        cpu.clearFlags();
        boolean carry = (value & 0x80) == value;
        cpu.setCarry(carry);
        value = (value << 1) | (carry ? 1 : 0);
        cpu.setZero(value == 0);
        setter.accept(cpu, (byte) value);
    }
}
