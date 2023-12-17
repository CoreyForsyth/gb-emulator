package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SWAP extends Instruction<Byte, Void>
{

    public SWAP(Accessor<Byte> primary)
    {
        super(primary, null);
    }

    @Override
    public void accept(CPU cpu)
    {
        Byte value = primary.apply(cpu);
        byte u = (byte) ((value & 0x0F << 4) | (value & 0xF0 >> 4));
        primary.accept(cpu, u);
        cpu.clearFlags();
        cpu.setZero(u == 0);
    }
}
