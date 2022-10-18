package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;

public class RST implements Instruction
{

    private final byte lower;

    public RST(byte lower)
    {
        this.lower = lower;
    }

    @Override
    public void accept(CPU cpu)
    {
        char sp = cpu.getSP();
        cpu.writeByte(--sp, cpu.getPCHigher());
        cpu.writeByte(--sp, cpu.getPCLower());
        cpu.setPC((char) (lower & 0xff));
    }
}
