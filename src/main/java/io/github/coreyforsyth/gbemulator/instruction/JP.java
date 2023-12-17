package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class JP extends Instruction<Boolean, Character>
{
    private final boolean negative;

    public JP(Accessor<Boolean> primary, Accessor<Character> secondary, boolean negative)
    {
        super(primary, secondary);
        this.negative = negative;
    }

    @Override
    public void accept(CPU cpu)
    {
        Character jump = secondary.apply(cpu);
        if (primary.apply(cpu) ^ negative) {
            cpu.setPC(jump);
        }
    }
}
