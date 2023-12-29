package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class CALL extends Instruction<Boolean, Character>
{
    private final boolean negative;

    public CALL(Accessor<Boolean> primary, Accessor<Character> secondary, boolean negative)
    {
        super(primary, secondary);
        this.negative = negative;
    }

    @Override
    public void accept(CPU cpu)
    {
        char routineAddress = secondary.apply(cpu);
        if (primary.apply(cpu) ^ negative)
        {
            byte pcLower = cpu.getPCLower();
            byte pcHigher = cpu.getPCHigher();
            char sp = cpu.getSP();
            cpu.cpuWriteByte(--sp, pcHigher);
            cpu.cpuWriteByte(--sp, pcLower);
            cpu.setSP(sp);
            cpu.setPC(routineAddress);
            cpu.cycle();
        }
    }
}
