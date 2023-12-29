package io.github.coreyforsyth.gbemulator;

import io.github.coreyforsyth.gbemulator.memory.ReadWrite;
import java.util.TreeMap;

public class Bus implements ReadWrite
{

    public static final char JOYP = 0xFF00;
    public static final char SB = 0xFF01;
    public static final char SC = 0xFF02;
    public static final char DIV = 0xFF04;
    public static final char TIMA = 0xFF05;
    public static final char TMA = 0xFF06;
    public static final char TAC = 0xFF07;
    public static final char IF = 0xFF0F;
    public static final char NR10 = 0xFF10;
    public static final char NR11 = 0xFF11;
    public static final char NR12 = 0xFF12;
    public static final char NR13 = 0xFF13;
    public static final char NR14 = 0xFF14;
    public static final char NR21 = 0xFF16;
    public static final char NR22 = 0xFF17;
    public static final char NR23 = 0xFF18;
    public static final char NR24 = 0xFF19;
    public static final char NR30 = 0xFF1A;
    public static final char NR31 = 0xFF1B;
    public static final char NR32 = 0xFF1C;
    public static final char NR33 = 0xFF1D;
    public static final char NR34 = 0xFF1E;
    public static final char NR41 = 0xFF20;
    public static final char NR42 = 0xFF21;
    public static final char NR43 = 0xFF22;
    public static final char NR44 = 0xFF23;
    public static final char NR50 = 0xFF24;
    public static final char NR51 = 0xFF25;
    public static final char NR52 = 0xFF26;
    public static final char LCDC = 0xFF40;
    public static final char STAT = 0xFF41;
    public static final char SCY = 0xFF42;
    public static final char SCX = 0xFF43;
    public static final char LY = 0xFF44;
    public static final char LYC = 0xFF45;
    public static final char DMA = 0xFF46;
    public static final char BGP = 0xFF47;
    public static final char OBP0 = 0xFF48;
    public static final char OBP1 = 0xFF49;
    public static final char WY = 0xFF4A;
    public static final char WX = 0xFF4B;
    public static final char KEY1 = 0xFF4D;
    public static final char VBK = 0xFF4F;
    public static final char HDMA1 = 0xFF51;
    public static final char HDMA2 = 0xFF52;
    public static final char HDMA3 = 0xFF53;
    public static final char HDMA4 = 0xFF54;
    public static final char HDMA5 = 0xFF55;
    public static final char RP = 0xFF56;
    public static final char BCP = 0xFF68;
    public static final char BCPD = 0xFF69;
    public static final char OCPS = 0xFF6A;
    public static final char OCPD = 0xFF6B;
    public static final char OPRI = 0xFF6C;
    public static final char SVBK = 0xFF70;
    public static final char PCM12 = 0xFF76;
    public static final char PCM34 = 0xFF77;
    public static final char IE = 0xFFFF;

    private final TreeMap<Integer, ReadWrite> map;

    public Bus()
    {
        this.map = new TreeMap<>();
    }

    @Override
    public byte read(char address)
    {
        return map.floorEntry(address & 0xFFFF).getValue().read(address);
    }

    @Override
    public void write(char address, byte value)
    {
        map.floorEntry(address & 0xFFFF).getValue().write(address, value);
    }

    public void register(int start, ReadWrite mappedObject) {
        map.put(start, mappedObject);
    }

    public void requestInterrupt(int mask)
    {
        write(IF, (byte) (read(IF) | mask));
    }
}
