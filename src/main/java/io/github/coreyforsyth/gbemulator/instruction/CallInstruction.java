package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.Supplier;

public class CallInstruction implements Instruction
{
    private final Supplier<Boolean> call;

    public CallInstruction(Supplier<Boolean> call)
    {
        this.call = call;
    }

    @Override
    public void accept(CPU cpu)
    {
        char routineAddress = cpu.nextChar();
        if (call.get()) {
            byte pcLower = cpu.getPCLower();
            byte pcHigher = cpu.getPCHigher();
            char sp = cpu.getSP();
            cpu.writeByte(--sp, pcHigher);
            cpu.writeByte(--sp, pcLower);
            cpu.setSP(sp);
            cpu.setPC(routineAddress);
        }
    }
}
