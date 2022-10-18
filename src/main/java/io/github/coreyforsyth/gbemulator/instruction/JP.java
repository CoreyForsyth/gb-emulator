package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.Function;
import java.util.function.Predicate;

public class JP implements Instruction
{

    private final Function<CPU, Character> getter;
    private final Predicate<CPU> shouldJump;

    public JP(Function<CPU, Character> getter, Predicate<CPU> shouldJump)
    {
        this.getter = getter;
        this.shouldJump = shouldJump;
    }

    @Override
    public void accept(CPU cpu)
    {
        Character jump = getter.apply(cpu);
        if (shouldJump.test(cpu)) {
            cpu.setPC(jump);
        }
    }
}
