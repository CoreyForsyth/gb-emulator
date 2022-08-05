package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class ByteCarryInstruction extends FlagInstruction<Byte>
{

    public ByteCarryInstruction(Function<CPU, Byte> a, Function<CPU, Byte> b, BiConsumer<CPU, Byte> setter)
    {
        super(a, b, setter);
    }

    public Byte castResult(int result) {
        return (byte) result;
    }

    @Override
    public void setHC(CPU cpu, Byte result, Byte a, Byte b)
    {
        cpu.setHalfCarry(((applyOperation(cpu, (byte) (a & 0xF), (byte) (b & 0xF))) & 0x10) == 0x10);
    }

    @Override
    public void setCy(CPU cpu, int result, Byte a, Byte b)
    {
        cpu.setCarry((result & 0x100) == 0x100);
    }
}
