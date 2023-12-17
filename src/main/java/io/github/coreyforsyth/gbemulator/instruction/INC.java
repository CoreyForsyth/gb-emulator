package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public class INC extends ADD
{

    public INC(Accessor<Byte> primary)
    {
        super(primary, Accessor.ONE);
    }

    @Override
    public void setCy(CPU cpu, int result, Byte a, Byte b) {}
}
