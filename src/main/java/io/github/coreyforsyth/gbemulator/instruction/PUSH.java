package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.Function;

public class PUSH extends Instruction<Character, Void>
{


    public PUSH(Accessor<Character> primary)
    {
        super(primary, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        cpu.cycle();
        char sp = cpu.getSP();
        Character character = primary.apply(cpu);
        byte higher = (byte) (byte) ((character & 0xff00) >> 8);
        byte lower = (byte) (character & 0xff);
        cpu.cpuWriteByte(--sp, higher);
        cpu.cpuWriteByte(--sp, lower);
        cpu.setSP(sp);
    }
}
