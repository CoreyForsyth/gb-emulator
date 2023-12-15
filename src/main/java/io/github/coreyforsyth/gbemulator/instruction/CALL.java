package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.Function;
import java.util.function.Predicate;

public class CALL implements Instruction
{
    private final Predicate<CPU> call;
	private final Function<CPU, Character> getter;

    public CALL(Predicate<CPU> call, Function<CPU, Character> getter)
    {
        this.call = call;
		this.getter = getter;
	}

    @Override
    public void accept(CPU cpu)
    {
        char routineAddress = getter.apply(cpu);
        if (call.test(cpu))
        {
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
