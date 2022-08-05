package io.github.coreyforsyth.gbemulator.memory;

public interface ReadWrite
{
    byte read(char address);
    void write(char address, byte value);
}
