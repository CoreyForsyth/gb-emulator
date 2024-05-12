package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class RET extends Instruction<Boolean, Void>
{
    private final boolean negative;

    public RET(Accessor<Boolean> primary, boolean negative)
    {
        super(primary, null);
        this.negative = negative;
    }

    @Override
    public void accept(CPU cpu)
    {
        if (primary.apply(cpu) ^ negative)
        {
            cpu.cycle();
            char sp = cpu.getSP();
            byte pcLower = cpu.cpuReadByte(sp++);
            byte pcHigher = cpu.cpuReadByte(sp++);
            cpu.setSP(sp);
            cpu.setPC((char) (pcHigher << 8 | (pcLower & 0xFF)));
        }
    }
}
