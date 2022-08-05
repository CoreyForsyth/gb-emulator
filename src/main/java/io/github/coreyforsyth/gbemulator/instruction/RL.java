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
        boolean carry = cpu.isCarry();
        value = (value << 1) | (carry ? 1 : 0);
        cpu.setZero(value == 0);
        setter.accept(cpu, (byte) value);
        cpu.clearFlags();
        cpu.setCarry(carry);
    }
}
