package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SwapInstruction implements Instruction
{

    private final Function<CPU, Byte> getter;
    private final BiConsumer<CPU, Byte> setter;

    public SwapInstruction(Function<CPU, Byte> getter, BiConsumer<CPU, Byte> setter)
    {
        this.getter = getter;
        this.setter = setter;
    }


    @Override
    public void accept(CPU cpu)
    {
        Byte value = getter.apply(cpu);
        byte u = (byte) ((value & 0x0F << 4) | (value & 0xF0 >> 4));
        setter.accept(cpu, u);
        cpu.clearFlags();
        cpu.setZero(u == 0);
    }
}
