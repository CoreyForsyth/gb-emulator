package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public abstract class ByteCarryInstruction extends FlagInstruction<Byte>
{

    public ByteCarryInstruction(Accessor<Byte> primary, Accessor<Byte> secondary)
    {
        super(primary, secondary);
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
        if (cpu.isSubtraction()) {
            cpu.setCarry(result < 0);
        } else {
            cpu.setCarry((result & 0x100) == 0x100);
        }
    }
}
