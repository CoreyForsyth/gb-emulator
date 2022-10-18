package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ADDChar extends CharCarryInstruction
{

    public ADDChar(Function<CPU, Character> a, Function<CPU, Character> b, BiConsumer<CPU, Character> setter)
    {
        super(a, b, setter);
    }

    @Override
    public int applyOperation(CPU cpu, Character a, Character b)
    {
        return a + b;
    }

    @Override
    public void setZ(CPU cpu, Character result)
    {

    }
}