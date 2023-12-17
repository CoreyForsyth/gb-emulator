package io.github.coreyforsyth.gbemulator;

import io.github.coreyforsyth.gbemulator.graphics.Display;
import io.github.coreyforsyth.gbemulator.memory.Cartridge;
import io.github.coreyforsyth.gbemulator.memory.HRam;
import io.github.coreyforsyth.gbemulator.memory.IO;
import io.github.coreyforsyth.gbemulator.memory.Oam;
import io.github.coreyforsyth.gbemulator.memory.VRam;
import io.github.coreyforsyth.gbemulator.memory.WorkRam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CPU
{
    private final Cartridge cartridge;
    private final WorkRam workRam;
    private final HRam hRam;
	private final VRam vRam;
	private final Oam oam;
	private final IO io;
	private final Display display;

    private boolean interruptEnabled;
	private byte interruptRegister;

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

	private boolean debug;

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
		display.cycle(4);
	}

	public byte readByte(char address) {
        if (address < 0x8000) {
            return cartridge.read(address);
        } else if (address < 0xA000) {
            return vRam.read(address);
        } else if (address < 0xC000) {
            return cartridge.read(address);
        } else if (address < 0xE000) {
            return workRam.read(address);
        } else if (address < 0xFE00) {
            // mirror
            return 0;
        } else if (address < 0xFEA0) {
			return oam.read(address);
        } else if (address < 0xFF00) {
            // unusable
            return 0;
        } else if (address < 0xFF80) {
            return io.read(address);
        } else if (address < 0xFFFF) {
            return hRam.read(address);
        } else {
			System.out.println("Read Interrupt Flag");
            return interruptRegister;
        }
    }

    public void cpuWriteByte(char address, byte value) {
        writeByte(address, value);
        cycle();
    }

    public void writeByte(char address, byte value) {
        if (address < 0x8000) {
            cartridge.write(address, value);
        } else if (address < 0xA000) {
			vRam.write(address, value);
		} else if (address < 0xC000) {
            cartridge.write(address, value);
        } else if (address < 0xE000) {
            workRam.write(address, value);
        } else if (address < 0xFE00) {
            // mirror
            System.out.printf("Writing %02X into mirror address %04X\n", value, (int) address);
		} else if (address < 0xFEA0) {
			oam.write(address, value);
		} else if (address < 0xFF00) {
            // forbidden
            System.out.printf("Writing %02X into forbin address %04X\n", value, (int) address);
		} else if (address < 0xFF80) {
			io.write(address, value);
			if (address == 0xFF46) {
				char dmaStart = (char) (value * 0x0100);
				for (int i = 0; i < 160; i++)
				{
					char sourceAddress = (char) (dmaStart + i);
					char destinationAddress = (char) (0xFE00 + i);
					this.writeByte(destinationAddress, readByte(sourceAddress));
				}
			}
		} else if (address < 0xFFFF) {
            hRam.write(address, value);
        } else {
			System.out.println("Wrote Interrupt Flag");
			interruptRegister = value;
		}
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
        int AF = isZero() ? 0x80 : 0;
        AF |= isSubtraction() ? 0x40 : 0;
        AF |= isHalfCarry() ? 0x20 : 0;
        AF |= isCarry() ? 0x10 : 0;
        AF |= getA() << 8;
        return (char) AF;
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
        writeByte(getBC(), value);
    }

    public byte readDE() {
        return cpuReadByte(getDE());
    }

    public void writeDE(byte value) {
        writeByte(getDE(), value);
    }

    public byte readHL() {
        return cpuReadByte(getHL());
    }

    public void writeHL(byte value) {
        writeByte(getHL(), value);
    }

    public void clearFlags() {
        zero = false;
        subtraction = false;
        halfCarry = false;
        carry = false;
    }

}





























