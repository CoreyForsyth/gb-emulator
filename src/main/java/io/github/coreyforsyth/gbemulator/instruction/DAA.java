package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;

public class DAA extends Instruction<Void, Void>
{
    public DAA()
    {
        super(null, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        if (cpu.isSubtraction())
        {
            if (cpu.isCarry())
            {
                cpu.setA((byte) (cpu.getA() - 0x60));
            }
            if (cpu.isHalfCarry())
            {
                cpu.setA((byte) (cpu.getA() - 0x06));
            }
        }
        else
        {
            if (cpu.isCarry() || (Byte.toUnsignedInt(cpu.getA())) > 0x99)
            {
                cpu.setA((byte) (cpu.getA() + 0x60));
                cpu.setCarry(true);
            }
            if (cpu.isHalfCarry() || (cpu.getA() & 0x0f) > 0x09)
            {
                cpu.setA((byte) (cpu.getA() + 0x06));
            }
        }
        cpu.setZero(cpu.getA() == 0);
        cpu.setHalfCarry(false);
    }
}
