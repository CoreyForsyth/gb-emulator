package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;

public class RST extends CALL
{

    public RST(byte lower)
    {
        super(Accessor.TRUE, new Accessor<>("FF" + lower, cpu -> (char) (lower & 0xff), ((cpu, character) -> {})), false);
    }

}
