package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class ADDC extends CharCarryInstruction
{

    public ADDC(Accessor<Character> primary, Accessor<Character> secondary)
    {
        super(primary, secondary);
    }

    @Override
    public int applyOperation(CPU cpu, Character a, Character b)
    {
        return a + b;
    }

    @Override
    public void accept(CPU cpu)
    {
        cpu.cycle();
        super.accept(cpu);
    }

    @Override
    public void setZ(CPU cpu, Character result)
    {

    }
}
