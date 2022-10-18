package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.Predicate;

public class RET implements Instruction
{
    private final Predicate<CPU> call;

    public RET(Predicate<CPU> call)
    {
        this.call = call;
    }

    @Override
    public void accept(CPU cpu)
    {
        if (call.test(cpu))
        {
            char sp = cpu.getSP();
            byte pcLower = cpu.readByte(sp++);
            byte pcHigher = cpu.readByte(sp++);
            cpu.setSP(sp);
            cpu.setPC((char) (pcHigher << 8 | pcLower));
        }
    }
}
