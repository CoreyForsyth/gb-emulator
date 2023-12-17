package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class RETI extends RET
{
    public RETI()
    {
        super(Accessor.TRUE, false);
    }

    @Override
    public void accept(CPU cpu)
    {
        System.out.println("RETI, turning off IME flag");
        cpu.setInterruptEnabled(false);
    }
}
