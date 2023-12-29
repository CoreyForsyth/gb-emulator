package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class SRA extends Instruction<Byte, Void>
{

    public SRA(Accessor<Byte> primary)
    {
        super(primary, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        int value = primary.apply(cpu) & 0xFF;
        int bit7 = value & 0x80;
        boolean carry = (value & 0x01) == 1;
        value = (value >> 1) | bit7;
        cpu.clearFlags();
        cpu.setZero(value == 0);
        cpu.setCarry(carry);
        primary.accept(cpu, (byte) value);
    }
}
