package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;

public class JR extends JP
{
    public JR(Accessor<Boolean> primary, boolean negative)
    {
        super(primary, Accessor.REL_PC_IM8, negative);
    }
}
