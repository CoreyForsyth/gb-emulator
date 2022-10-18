package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.Function;

public class PUSH implements Instruction
{

    private final Function<CPU, Character> getter;

    public PUSH(Function<CPU, Character> getter)
    {
        this.getter = getter;
    }

    @Override
    public void accept(CPU cpu)
    {
        char sp = cpu.getSP();
        Character character = getter.apply(cpu);
        byte higher = (byte) (byte) ((character & 0xff00) >> 8);
        byte lower = (byte) (character & 0xff);
        cpu.writeByte(--sp, higher);
        cpu.writeByte(--sp, lower);
        cpu.setSP(sp);
    }
}
