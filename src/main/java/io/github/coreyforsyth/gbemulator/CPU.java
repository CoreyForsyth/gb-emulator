package io.github.coreyforsyth.gbemulator;

import io.github.coreyforsyth.gbemulator.memory.HRam;
import io.github.coreyforsyth.gbemulator.memory.Cartridge;
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
    private boolean interruptEnabled;

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

    public byte nextByte() {
        return readByte(readIncrementPC());
    }

    public char nextChar() {
        byte low = readByte(readIncrementPC());
        byte high = readByte(readIncrementPC());
        return (char) ( high << 8 | low & 0x00FF);
    }

    public byte peakNextInstruction() {
        return readByte(PC);
    }

    public byte readByte(char address) {
        if (address < 0x8000) {
            return cartridge.read(address);
        } else if (address < 0xA000) {
            // vram
            return 0;
        } else if (address < 0xC000) {
            return cartridge.read(address);
        } else if (address < 0xE000) {
            return workRam.read(address);
        } else if (address < 0xFE00) {
            // mirror
            return 0;
        } else if (address < 0xFEA0) {
            // sprite attribute table
            return 0;
        } else if (address < 0xFF00) {
            // sprite attribute table
            return 0;
        } else if (address < 0xFF80) {
            // IO
            return 0;
        } else if (address < 0xFFFF) {
            return hRam.read(address);
        } else {
            // Interrupt Enable register
            return 0;
        }
    }

    public void writeByte(char address, byte value) {
        if (address < 0x8000) {
            System.out.printf("Writing %02X into cart   address %04X\n", value, (int) address);
            cartridge.write(address, value);
        } else if (address < 0xA000) {
            // vram
            System.out.printf("Writing %02X into vidram address %04X\n", value, (int) address);
            return;
        } else if (address < 0xC000) {
            System.out.printf("Writing %02X into crtram address %04X\n", value, (int) address);
            cartridge.write(address, value);
        } else if (address < 0xE000) {
            System.out.printf("Writing %02X into workr  address %04X\n", value, (int) address);
            workRam.write(address, value);
        } else if (address < 0xFE00) {
            // mirror
            System.out.printf("Writing %02X into mirror address %04X\n", value, (int) address);
            return;
        } else if (address < 0xFEA0) {
            // sprite attribute table
            System.out.printf("Writing %02X into sprite address %04X\n", value, (int) address);
            return;
        } else if (address < 0xFF00) {
            // forbidden
            System.out.printf("Writing %02X into forbin address %04X\n", value, (int) address);
            return;
        } else if (address < 0xFF80) {
            // IO
            System.out.printf("Writing %02X into IO     address %04X\n", value, (int) address);
            return;
        } else if (address < 0xFFFF) {
            System.out.printf("Writing %02X into hram   address %04X\n", value, (int) address);
            hRam.write(address, value);
        } else {
            // Interrupt Enable register
            return;
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

    public char readIncrementPC() {
        return PC++;
    }

    public void clearFlags() {
        zero = false;
        subtraction = false;
        halfCarry = false;
        carry = false;
    }

}
