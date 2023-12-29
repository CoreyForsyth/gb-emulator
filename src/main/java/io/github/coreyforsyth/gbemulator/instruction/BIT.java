package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class BIT extends Instruction<Byte, Void>
{

    private final byte testMask;

    public BIT(Accessor<Byte> primary, int index)
    {
        super(primary, null);
        this.testMask = (byte) (1 << index);
    }

    @Override
    public void accept(CPU cpu)
    {
        Byte value = primary.apply(cpu);
        cpu.setZero((value & testMask) == 0);
        cpu.setHalfCarry(true);
        cpu.setSubtraction(false);
    }
}
