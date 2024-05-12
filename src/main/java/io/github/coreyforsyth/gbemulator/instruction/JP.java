package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class JP extends Instruction<Boolean, Character>
{
    private final boolean negative;
    private final boolean jumpCycle;

    public JP(Accessor<Boolean> primary, Accessor<Character> secondary, boolean negative)
    {
        this(primary, secondary, negative, true);
    }

    public JP(Accessor<Boolean> primary, Accessor<Character> secondary, boolean negative, boolean jumpCycle)
    {
        super(primary, secondary);
        this.negative = negative;
        this.jumpCycle = jumpCycle;
    }

    @Override
    public void accept(CPU cpu)
    {
        Character jump = secondary.apply(cpu);
        if (primary.apply(cpu) ^ negative) {
            if (jumpCycle) {
                cpu.cycle();
            }
            cpu.setPC(jump);
        }
    }
}
