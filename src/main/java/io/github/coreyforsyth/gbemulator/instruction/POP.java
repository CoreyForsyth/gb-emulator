package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;

public class POP implements Instruction
{

    private final BiConsumer<CPU, Character> setter;

    public POP(BiConsumer<CPU, Character> setter)
    {
        this.setter = setter;
    }

    @Override
    public void accept(CPU cpu)
    {
        char sp = cpu.getSP();
        byte lower = cpu.cpuReadByte(sp++);
        byte higher = cpu.cpuReadByte(sp++);
        setter.accept(cpu, (char) ((higher & 0xFF) << 8 | (lower & 0xFF)));
        cpu.setSP(sp);
    }
}
