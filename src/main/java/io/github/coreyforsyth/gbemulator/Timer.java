package io.github.coreyforsyth.gbemulator;

import io.github.coreyforsyth.gbemulator.memory.ReadWrite;

public class Timer implements ReadWrite
{

    private final Bus bus;

    private byte div;
    private byte tima;
    private byte tma;
    private byte tac;

    private byte divLower;
    private boolean timaLatch;

    public Timer(Bus bus)
    {
        this.bus = bus;
    }

    @Override
    public byte read(char address)
    {
        return switch (address)
            {
                case Bus.DIV:
                    yield div;
                case Bus.TIMA:
                    yield tima;
                case Bus.TMA:
                    yield tma;
                case Bus.TAC:
                    yield tac;
                default:
                    yield 0;
            };
    }

    @Override
    public void write(char address, byte value)
    {
        switch (address)
        {
            case Bus.DIV ->
            {
                div = 0;
                divLower = 0;
            }
            case Bus.TIMA -> tima = value;
            case Bus.TMA -> tma = value;
            case Bus.TAC ->
            {
                tac = value;
                cycleTima();
            }
        }
    }

    public void cycle()
    {
        divLower += 4;
        if (divLower == 0)
        {
            div++;
        }

        cycleTima();
    }

    private void cycleTima()
    {
        if ((tac & 0x04) == 0)
        {
            return;
        }
        int clockSelect = tac & 3;
        boolean decrementPinLow;
        if (clockSelect == 0)
        {
            decrementPinLow = (div & 0x02) == 0;
        }
        else if (clockSelect == 1)
        {
            decrementPinLow = (divLower & 0x08) == 0;
        }
        else if (clockSelect == 2)
        {
            decrementPinLow = (divLower & 0x20) == 0;
        }
        else
        {
            decrementPinLow = (divLower & 0x80) == 0;
        }

        if (decrementPinLow)
        {
            if (!timaLatch)
            {
                timaLatch = true;
                tima++;
                if (tima == 0)
                {
                    tima = tma;
//                    System.out.printf("timer interrupted, tima reset to %02X\n", tima);
                    bus.requestInterrupt(0x4);
                }
            }
        }
        else
        {
            timaLatch = false;
        }
    }
}
