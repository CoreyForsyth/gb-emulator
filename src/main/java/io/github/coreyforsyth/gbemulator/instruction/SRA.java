package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SRA implements Instruction
{

    private final Function<CPU, Byte> getter;
    private final BiConsumer<CPU, Byte> setter;

    public SRA(Function<CPU, Byte> getter, BiConsumer<CPU, Byte> setter)
    {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void accept(CPU cpu)
    {
        int value = getter.apply(cpu);
        int bit8 = value & 0x80;
        boolean carry = (value & 0x01) == 1;
        value = (value >> 1) | bit8;
        cpu.clearFlags();
        cpu.setZero(value == 0);
        cpu.setCarry(carry);
        setter.accept(cpu, (byte) value);
    }
}
