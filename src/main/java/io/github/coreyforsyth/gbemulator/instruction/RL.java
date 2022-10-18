package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RL implements Instruction
{

    private final Function<CPU, Byte> getter;
    private final BiConsumer<CPU, Byte> setter;

    public RL(Function<CPU, Byte> getter, BiConsumer<CPU, Byte> setter)
    {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void accept(CPU cpu)
    {
        int value = getter.apply(cpu);
        boolean carry = (value & 0x80) == 0x80;
        value = (value << 1) | (cpu.isCarry() ? 1 : 0);
        cpu.clearFlags();
        cpu.setZero(value == 0);
        cpu.setCarry(carry);
        setter.accept(cpu, (byte) value);
    }
}
