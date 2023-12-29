package io.github.coreyforsyth.gbemulator.memory;

public class EchoRam implements ReadWrite
{

    private final WorkRam workRam;

    public EchoRam(WorkRam workRam)
    {
        this.workRam = workRam;
    }

    @Override
    public byte read(char address)
    {
        return workRam.read((char) ((address & 0xFFFF) - 0x2000));
    }

    @Override
    public void write(char address, byte value)
    {

        workRam.write((char) ((address & 0xFFFF) - 0x2000), value);
    }
}
