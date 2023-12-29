package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SUB extends ByteCarryInstruction
{

    public SUB(Accessor<Byte> primary, Accessor<Byte> secondary)
    {
        super(primary, secondary);
    }

    @Override
    public int applyOperation(CPU cpu, Byte a, Byte b)
    {
        return (0xFF & a) - (0xFF & b);
    }

    @Override
    public void setS(CPU cpu)
    {
        cpu.setSubtraction(true);
    }
}
