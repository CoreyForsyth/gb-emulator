package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;

public class CP extends SUB
{
    public CP(Accessor<Byte> secondary)
    {
        super(Accessor.A_NO_SET, secondary);
    }
}
