package io.github.coreyforsyth.gbemulator;

import io.github.coreyforsyth.gbemulator.graphics.Display;
import io.github.coreyforsyth.gbemulator.memory.Cartridge;
import io.github.coreyforsyth.gbemulator.memory.EchoRam;
import io.github.coreyforsyth.gbemulator.memory.HRam;
import io.github.coreyforsyth.gbemulator.memory.IO;
import io.github.coreyforsyth.gbemulator.memory.Oam;
import io.github.coreyforsyth.gbemulator.memory.ReadWrite;
import io.github.coreyforsyth.gbemulator.memory.VRam;
import io.github.coreyforsyth.gbemulator.memory.WorkRam;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class CPU implements ReadWrite
{

    private final Bus bus;

	private final Display display;
    private final Timer timer;

    private boolean halt;
    private boolean interruptEnabled;
	private byte IE;

    private byte A;
    private byte B;
    private byte C;
    private byte D;
    private byte E;
    private boolean zero;
    private boolean subtraction;
    private boolean halfCarry;
    private boolean carry;
    private byte H;
    private byte L;
    private char PC;
    private char SP;

    private int cycleCount;


	private boolean debug;

    public CPU(Cartridge cartridge)
    {
        this.bus = new Bus();
        Oam oam = new Oam();
        VRam vRam = new VRam();
        this.display = new Display(bus, oam, vRam);
        this.timer = new Timer(bus);
        IO io = new IO();
        WorkRam mappedObject = new WorkRam();
        bus.register(0, cartridge);
        bus.register(0x8000, vRam);
        bus.register(0xA000, cartridge);
        bus.register(0xC000, mappedObject);
        bus.register(0xE000, new EchoRam(mappedObject));
        bus.register(0xFE00, oam);
        bus.register(0xFF00, io);
        bus.register(0xFF04, timer);
        bus.register(0xFF08, io);
        bus.register(0xFF40, display);
        bus.register(0xFF4D, io);
        bus.register(0xFF80, new HRam());
        bus.register(0xFFFF, this);
    }

    public byte nextByte() {
        return cpuReadByte(readIncrementPC());
    }

    public char nextChar() {
        byte low = nextByte();
        byte high = nextByte();
        return (char) ( high << 8 | low & 0x00FF);
    }

    public byte peakNextInstruction() {
        return cpuReadByte(PC);
    }

	public byte cpuReadByte(char address) {
		byte b = readByte(address);
		cycle();
		return b;
	}

	public void cycle()
	{
        cycleCount++;
		display.cycle();
        timer.cycle();
	}



    public void cpuWriteByte(char address, byte value) {
        writeByte(address, value);
        cycle();
    }

	public byte readByte(char address) {
        return bus.read(address);
    }

    public void writeByte(char address, byte value) {
        bus.write(address, value);
    }

    public byte getPCLower() {
        return (byte) (PC & 0xFF);
    }

    public byte getPCHigher() {
        return (byte) (PC >> 8 & 0xFF);
    }

    public char getBC() {
        return (char) ( B << 8 | C & 0x00FF);
    }

    public char getDE() {
        return (char) ( D << 8 | E & 0x00FF);
    }

    public char getHL() {
        return (char) ( H << 8 | L & 0x00FF);
    }

    public char getAF() {
        int AF = getF();
        AF |= getA() << 8;
        return (char) AF;
    }

    private int getF()
    {
        int F = isZero() ? 0x80 : 0;
        F |= isSubtraction() ? 0x40 : 0;
        F |= isHalfCarry() ? 0x20 : 0;
        F |= isCarry() ? 0x10 : 0;
        return F;
    }

    public void setBC(char BC) {
        B = (byte) (BC >> 8 & 0xFF);
        C = (byte) (BC & 0xFF);
    }

    public void setDE(char DE) {
        D = (byte) (DE >> 8 & 0xFF);
        E = (byte) (DE & 0xFF);
    }

    public void setHL(char HL) {
        H = (byte) (HL >> 8 & 0xFF);
        L = (byte) (HL & 0xFF);
    }

    public void setAF(char AF) {
        A = (byte) (AF >> 8 & 0xFF);
        byte F = (byte) (AF & 0xFF);
        setZero((F & 0x80) == 0x80);
        setSubtraction((F & 0x40) == 0x40);
        setHalfCarry((F & 0x20) == 0x20);
        setCarry((F & 0x10) == 0x10);
    }

    public char readIncrementPC() {
        return PC++;
    }

    public byte readBC() {
        return cpuReadByte(getBC());
    }

    public void writeBC(byte value) {
        cpuWriteByte(getBC(), value);
    }

    public byte readDE() {
        return cpuReadByte(getDE());
    }

    public void writeDE(byte value) {
        cpuWriteByte(getDE(), value);
    }

    public byte readHL() {
        return cpuReadByte(getHL());
    }

    public void writeHL(byte value) {
        cpuWriteByte(getHL(), value);
    }

    public void clearFlags() {
        zero = false;
        subtraction = false;
        halfCarry = false;
        carry = false;
    }

    public void logState() {
        boolean override = true;
        if (debug)
        {
            String format = String.format("A:%02X F:%02X B:%02X C:%02X D:%02X E:%02X H:%02X L:%02X SP:%04X PC:%04X PCMEM:%02X,%02X,%02X,%02X\n",
                getA(), getF(), getB(), getC(), getD(), getE(), getH(), getL(),
                (int) getSP(), (int) getPC(),
                0xFF & readByte(getPC()), 0xFF & readByte((char) (getPC() + 1)), 0xFF & readByte((char) (getPC() + 2)), 0xFF & readByte((char) (getPC() + 3)));

            log.info(format);
        }


//        String.format("A:%02X F:%02X B:%02X C:%02X D:%02X E:%02X H:%02X L:%02X SP:%04X PC:%04X PCMEM:%02X,%02X,%02X,%02X\n",
//            getA(), getF(), getB(), getC(), getD(), getE(), getH(), getL(),
//            (int) getSP(), (int) getPC(),
//            0xFF & readByte(getPC()), 0xFF & readByte((char) (getPC() + 1)), 0xFF & readByte((char) (getPC() + 2)), 0xFF & readByte((char) (getPC() + 3)));
    }

    @Override
    public byte read(char address)
    {
        if (address == 0xFFFF) {
            return IE;
        } else {
            return 0;
        }
    }

    @Override
    public void write(char address, byte value)
    {
        int a = 0b1101;
        if (address == 0xFFFF)
        {
            System.out.printf("Writing IF, value 0x%02X\n", value);
            IE = value;
        }
    }

}





























