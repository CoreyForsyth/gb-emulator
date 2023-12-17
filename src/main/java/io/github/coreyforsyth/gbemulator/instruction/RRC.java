package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RRC extends Instruction<Byte, Void>
{

    public RRC(Accessor<Byte> primary)
    {
        super(primary, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        int value = primary.apply(cpu);
        boolean carry = (value & 0x01) == 0x01;
        value = (value >> 1) | (carry ? 0x80 : 0);
        cpu.clearFlags();
        cpu.setCarry(carry);
        cpu.setZero(value == 0);
        primary.accept(cpu, (byte) value);
    }
}
