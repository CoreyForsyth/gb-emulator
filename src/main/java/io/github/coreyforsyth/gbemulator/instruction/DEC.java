package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DEC extends SUB
{

    public DEC(Function<CPU, Byte> a, BiConsumer<CPU, Byte> setter)
    {
        super(a, (cpu) -> (byte) 1, setter);
    }

    @Override
    public void setCy(CPU cpu, int result, Byte a, Byte b) {}
}
