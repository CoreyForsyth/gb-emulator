package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class SET extends Instruction<Byte, Void>
{

    private final int mask;

    public SET(Accessor<Byte> primary, int mask)
    {
        super(primary, null);
        this.mask = mask;
    }

    @Override
    public void accept(CPU cpu)
    {
        primary.accept(cpu, (byte) (primary.apply(cpu) | mask));
    }
}