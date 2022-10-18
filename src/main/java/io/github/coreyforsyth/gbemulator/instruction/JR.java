package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.Predicate;

public class JR extends JP
{
    public JR(Predicate<CPU> shouldJump)
    {
        super(cpu -> (char) (cpu.nextByte() + cpu.getPC()), shouldJump);
    }
}
