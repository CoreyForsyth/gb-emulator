package io.github.coreyforsyth.gbemulator.memory;

public class VRam implements ReadWrite
{
    final byte[] ram;

    public VRam()
    {
        this.ram = new byte[0x2000];
    }

    public int getOffset(int sp) {
        if (sp < 0x8000 || sp > 0xA000) {
            throw new RuntimeException("WorkRam access issue");
        }
        return sp - 0x8000;
    }

    @Override
    public byte read(char address)
    {
        return ram[getOffset(address)];
    }

    @Override
    public void write(char address, byte value)
    {
        ram[getOffset(address)] = value;
    }

}
