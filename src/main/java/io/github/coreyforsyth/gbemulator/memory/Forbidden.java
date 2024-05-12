package io.github.coreyforsyth.gbemulator.memory;

public class Forbidden implements ReadWrite
{
    @Override
    public byte read(char address)
    {
        return (byte) 0xFF;
    }

    @Override
    public void write(char address, byte value)
    {

    }
}
