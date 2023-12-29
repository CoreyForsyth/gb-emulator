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
        super.accept(cpu);
        System.out.println("RETI, turning on IME flag");
        cpu.setInterruptEnabled(true);
    }
}
