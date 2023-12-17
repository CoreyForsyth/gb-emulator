package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;

public class DI extends Instruction<Void, Void>
{

    public DI()
    {
        super(null, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        System.out.println("DI, turning off IME flag");
        cpu.setInterruptEnabled(false);
    }
}
