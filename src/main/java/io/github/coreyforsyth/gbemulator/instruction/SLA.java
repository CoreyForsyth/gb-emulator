package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SLA extends Instruction<Byte, Void>
{

    public SLA(Accessor<Byte> primary)
    {
        super(primary, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        int value = primary.apply(cpu);
        boolean carry = (value & 0x80) == 0x80;
        value = (value << 1) & 0xFF;
        cpu.clearFlags();
        cpu.setZero(value == 0);
        cpu.setCarry(carry);
        primary.accept(cpu, (byte) value);
    }
}