package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;

public class POP extends Instruction<Character, Void>
{

    public POP(Accessor<Character> primary)
    {
        super(primary, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        char sp = cpu.getSP();
        byte lower = cpu.cpuReadByte(sp++);
        byte higher = cpu.cpuReadByte(sp++);
        primary.accept(cpu, (char) ((higher & 0xFF) << 8 | (lower & 0xFF)));
        cpu.setSP(sp);
    }
}
