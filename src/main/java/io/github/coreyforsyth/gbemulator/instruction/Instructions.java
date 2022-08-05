package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Instructions
{

    public static Instruction[] instructions;
    private static final Instruction nop = cpu -> {};

    static  {
        instructions = new Instruction[0x100];
        // nop
        instructions[0x00] = cpu -> {};
        // LD BC, d16
        instructions[0x01] = cpu -> cpu.setBC(cpu.nextChar());
        // LD (BC), A
        instructions[0x02] = cpu -> cpu.writeByte(cpu.getBC(), cpu.getA());
        // LD (BC), A
        instructions[0x03] = cpu -> cpu.setBC((char) (cpu.getBC() + 1));
        // INC B
        instructions[0x04] = cpu -> cpu.setB((byte) (cpu.getB() + 1));
        // DEC B
        instructions[0x05] = cpu -> cpu.setB((byte) (cpu.getB() - 1));
        // LD B, d8
        instructions[0x06] = cpu -> cpu.setB(cpu.nextByte());
        // RLCA
        instructions[0x07] = nop;
        // LD (a16), SP
        instructions[0x08] = cpu -> {
            char c = cpu.nextChar();
            cpu.writeByte(c, (byte) (cpu.getSP() & 0xFF));
            cpu.writeByte((char) (c + 1), (byte) ((cpu.getSP() & 0xFF00) >> 8));
        };
        // ADD HL, BC
        instructions[0x09] = new CharAdditionInstruction(CPU::getHL, CPU::getBC, CPU::setHL);
        // LD A, (BC)
        instructions[0x0A] = cpu -> cpu.setA(cpu.readByte(cpu.getBC()));
        // DEC BC
        instructions[0x0B] = cpu -> cpu.setBC((char) (cpu.getBC() - 1));
        // INC C
        instructions[0x0C] = cpu -> cpu.setC((byte) (cpu.getC() + 1));
        // DEC C
        instructions[0x0D] = cpu -> cpu.setC((byte) (cpu.getC() - 1));
        // LD C, d8
        instructions[0x0E] = cpu -> cpu.setC(cpu.nextByte());
        // RRCA
        instructions[0x0F] = nop;
        // stop
        instructions[0x10] = nop;
        // LD DE, d16
        instructions[0x11] = cpu -> cpu.setDE(cpu.nextChar());
        // LD (DE), A
        instructions[0x12] = cpu -> cpu.writeByte(cpu.getDE(), cpu.getA());
        // INC DE
        instructions[0x13] = cpu -> cpu.setDE((char) (cpu.getDE() + 1));
        // INC B
        instructions[0x14] = cpu -> cpu.setD((byte) (cpu.getD() + 1));
        // DEC D
        instructions[0x15] = cpu -> cpu.setD((byte) (cpu.getD() - 1));
        // LD D, d8
        instructions[0x16] = cpu -> cpu.setD(cpu.nextByte());
        // RLA
        instructions[0x17] = nop;
        // JR s8
        instructions[0x18] = cpu -> cpu.setPC((char) (cpu.nextByte() + cpu.getPC()));
        // ADD HL, DE
        instructions[0x19] = new CharAdditionInstruction(CPU::getHL, CPU::getDE, CPU::setHL);
        // LD A, (DE)
        instructions[0x1A] = cpu -> cpu.setA(cpu.readByte(cpu.getDE()));
        // DEC DE
        instructions[0x1B] = cpu -> cpu.setDE((char) (cpu.getDE() - 1));
        // INC E
        instructions[0x1C] = cpu -> cpu.setE((byte) (cpu.getE() + 1));
        // DEC E
        instructions[0x1D] = cpu -> cpu.setE((byte) (cpu.getE() - 1));
        // LD E, d8
        instructions[0x1E] = cpu -> cpu.setE(cpu.nextByte());
        // RRA
        instructions[0x1F] = cpu -> {
            byte a = cpu.getA();
            cpu.setA((byte) ((a & 0xFF >> 1) | (cpu.isCarry() ? 0x80 : 0)));
            cpu.clearFlags();
            cpu.setCarry((a & 1) == 1);
        };
        // JR NZ, s8
        instructions[0x20] = cpu -> {
            if (!cpu.isZero()) {
                cpu.setPC((char) (cpu.getPC() + cpu.nextByte()));
            }
        };
        // LD HL,d16
        instructions[0x21] = cpu -> cpu.setHL(cpu.nextChar());
        // LD (HL+), A
        instructions[0x22] = cpu -> {
            cpu.writeByte(cpu.getHL(), cpu.getA());
            cpu.setHL((char) (cpu.getHL() + 1));
        };
        // INC HL
        instructions[0x23] = cpu -> cpu.setHL((char) (cpu.getHL() + 1));
        // INC H
        instructions[0x24] = cpu -> cpu.setH((byte) (cpu.getH() + 1));
        // DEC H
        instructions[0x25] = cpu -> cpu.setH((byte) (cpu.getH() - 1));
        // LD H, d8
        instructions[0x26] = cpu -> cpu.setH(cpu.nextByte());
        // DAA
        instructions[0x27] = cpu -> {};
        // JR Z, s8
        instructions[0x28] = cpu -> {
            byte b = cpu.nextByte();
            if (cpu.isZero()) {
                cpu.setPC((char) (cpu.getPC() + b));
            }
        };
        // ADD HL, HL
        instructions[0x29] = new CharAdditionInstruction(CPU::getHL, CPU::getHL, CPU::setHL);
        // LD A, (HL+)
        instructions[0x2A] = cpu -> {
            cpu.setA(cpu.readByte(cpu.getHL()));
            cpu.setHL((char) (cpu.getHL() + 1));
        };

        instructions[0x30] = nop;
        instructions[0x31] = nop;
        instructions[0x32] = nop;
        instructions[0x33] = nop;
        instructions[0x34] = nop;
        instructions[0x35] = nop;
        instructions[0x36] = nop;
        instructions[0x37] = nop;
        instructions[0x38] = nop;
        instructions[0x39] = nop;
        instructions[0x3A] = nop;
        instructions[0x3B] = nop;
        instructions[0x3C] = nop;
        instructions[0x3D] = nop;
        // LD A, d8
        instructions[0x3E] = cpu -> cpu.setA(cpu.nextByte());
        instructions[0x3F] = nop;
        instructions[0X40] = nop;
        instructions[0X41] = nop;
        instructions[0X42] = nop;
        instructions[0X43] = nop;
        instructions[0X44] = nop;
        instructions[0X45] = nop;
        instructions[0X46] = nop;
        // LD B, A
        instructions[0X47] = cpu -> cpu.setB(cpu.getA());
        instructions[0X48] = nop;
        instructions[0X49] = nop;
        instructions[0X4A] = nop;
        instructions[0X4B] = nop;
        instructions[0X4C] = nop;
        instructions[0X4D] = nop;
        instructions[0X4E] = nop;
        instructions[0X4F] = nop;
        instructions[0X50] = nop;
        instructions[0X51] = nop;
        instructions[0X52] = nop;
        instructions[0X53] = nop;
        instructions[0X54] = nop;
        instructions[0X55] = nop;
        instructions[0X56] = nop;
        instructions[0X57] = nop;
        instructions[0X58] = nop;
        instructions[0X59] = nop;
        instructions[0X5A] = nop;
        instructions[0X5B] = nop;
        instructions[0X5C] = nop;
        instructions[0X5D] = nop;
        instructions[0X5E] = nop;
        instructions[0X5F] = nop;
        instructions[0X60] = nop;
        instructions[0X61] = nop;
        instructions[0X62] = nop;
        instructions[0X63] = nop;
        instructions[0X64] = nop;
        instructions[0X65] = nop;
        instructions[0X66] = nop;
        instructions[0X67] = nop;
        instructions[0X68] = nop;
        instructions[0X69] = nop;
        instructions[0X6A] = nop;
        instructions[0X6B] = nop;
        instructions[0X6C] = nop;
        instructions[0X6D] = nop;
        instructions[0X6E] = nop;
        instructions[0X6F] = nop;
        instructions[0X70] = nop;
        instructions[0X71] = nop;
        instructions[0X72] = nop;
        instructions[0X73] = nop;
        instructions[0X74] = nop;
        instructions[0X75] = nop;
        instructions[0X76] = nop;
        instructions[0X77] = nop;
        instructions[0X78] = nop;
        instructions[0X79] = nop;
        instructions[0X7A] = nop;
        instructions[0X7B] = nop;
        instructions[0X7C] = nop;
        instructions[0X7D] = nop;
        instructions[0X7E] = nop;
        instructions[0X7F] = nop;
        instructions[0X80] = new ByteAdditionInstruction(CPU::getA, CPU::getB, CPU::setA);
        instructions[0X81] = nop;
        instructions[0X82] = nop;
        instructions[0X83] = nop;
        instructions[0X84] = nop;
        instructions[0X85] = nop;
        instructions[0X86] = nop;
        instructions[0X87] = nop;
        instructions[0X88] = nop;
        instructions[0X89] = nop;
        instructions[0X8A] = nop;
        instructions[0X8B] = nop;
        instructions[0X8C] = nop;
        instructions[0X8D] = nop;
        instructions[0X8E] = nop;
        instructions[0X8F] = nop;
        // SUB B
        instructions[0x90] = new ByteSubtractionInstruction(CPU::getA, CPU::getB, CPU::setA);
        instructions[0X91] = nop;
        instructions[0X92] = nop;
        instructions[0X93] = nop;
        instructions[0X94] = nop;
        instructions[0X95] = nop;
        instructions[0X96] = nop;
        instructions[0X97] = nop;
        instructions[0X98] = nop;
        instructions[0X99] = nop;
        instructions[0X9A] = nop;
        instructions[0X9B] = nop;
        instructions[0X9C] = nop;
        instructions[0X9D] = nop;
        instructions[0X9E] = nop;
        instructions[0X9F] = nop;
        instructions[0XA0] = nop;
        instructions[0XA1] = nop;
        instructions[0XA2] = nop;
        instructions[0XA3] = nop;
        instructions[0XA4] = nop;
        instructions[0XA5] = nop;
        instructions[0XA6] = nop;
        instructions[0XA7] = nop;
        instructions[0XA8] = nop;
        instructions[0XA9] = nop;
        instructions[0XAA] = nop;
        instructions[0XAB] = nop;
        instructions[0XAC] = nop;
        instructions[0XAD] = nop;
        instructions[0XAE] = nop;
        // XOR A
        instructions[0xAF] = new XorInstruction(CPU::getA, CPU::getA, CPU::setA);
        instructions[0XB0] = nop;
        instructions[0XB1] = nop;
        instructions[0XB2] = nop;
        instructions[0XB3] = nop;
        instructions[0XB4] = nop;
        instructions[0XB5] = nop;
        instructions[0XB6] = nop;
        instructions[0XB7] = nop;
        instructions[0XB8] = nop;
        instructions[0XB9] = nop;
        instructions[0XBA] = nop;
        instructions[0XBB] = nop;
        instructions[0XBC] = nop;
        instructions[0XBD] = nop;
        instructions[0XBE] = nop;
        instructions[0XBF] = nop;
        instructions[0XC0] = nop;
        instructions[0XC1] = nop;
        instructions[0XC2] = nop;
        // JP a16
        instructions[0xC3] = cpu -> cpu.setPC(cpu.nextChar());
        instructions[0XC4] = nop;
        instructions[0XC5] = nop;
        instructions[0XC6] = nop;
        instructions[0XC7] = nop;
        instructions[0XC8] = nop;
        instructions[0XC9] = nop;
        instructions[0XCA] = nop;
        instructions[0XCB] = CBInstructions::next;
        instructions[0XCC] = nop;
        instructions[0XCD] = new CallInstruction(() -> true);
        instructions[0XCE] = nop;
        instructions[0XCF] = nop;
        instructions[0XD0] = nop;
        instructions[0XD1] = nop;
        instructions[0XD2] = nop;
        instructions[0XD3] = nop;
        instructions[0XD4] = nop;
        instructions[0XD5] = nop;
        instructions[0XD6] = nop;
        instructions[0XD7] = nop;
        instructions[0XD8] = nop;
        instructions[0XD9] = nop;
        instructions[0XDA] = nop;
        instructions[0XDB] = nop;
        instructions[0XDC] = nop;
        instructions[0XDD] = nop;
        instructions[0XDE] = nop;
        instructions[0XDF] = nop;
        // LD (a16), A
        instructions[0xE0] = cpu -> {
            byte b = cpu.nextByte();
            int i = 0xFF00 + (0xFF & b);
            char address = (char) i;
            cpu.writeByte(address, cpu.getA());
        };
        instructions[0XE1] = nop;
        instructions[0XE2] = nop;
        instructions[0XE3] = nop;
        instructions[0XE4] = nop;
        instructions[0XE5] = nop;
        instructions[0XE6] = nop;
        instructions[0XE7] = nop;
        instructions[0XE8] = nop;
        instructions[0XE9] = nop;
        // LD (a16), A
        instructions[0xEA] = cpu -> cpu.writeByte(cpu.nextChar(), cpu.getA());
        instructions[0XEB] = nop;
        instructions[0XEC] = nop;
        instructions[0XED] = nop;
        instructions[0XEE] = nop;
        instructions[0XEF] = nop;
        instructions[0XF0] = cpu -> cpu.setA(cpu.readByte((char) (0xFF00 + 0xFF & cpu.nextByte())));
        instructions[0XF1] = nop;
        instructions[0XF2] = nop;
        // DI
        instructions[0xF3] = cpu -> cpu.setInterruptEnabled(false);
        instructions[0XF4] = nop;
        instructions[0XF5] = nop;
        instructions[0XF6] = nop;
        instructions[0XF7] = nop;
        instructions[0XF8] = nop;
        instructions[0XF9] = nop;
        instructions[0XFA] = nop;
        // EI
        instructions[0xFB] = cpu -> cpu.setInterruptEnabled(true);
        instructions[0XFC] = nop;
        instructions[0XFD] = nop;
        // CP d8
        instructions[0xFE] = new ByteSubtractionInstruction(CPU::getA, CPU::nextByte, (a, b) -> {});
        instructions[0XFF] = nop;

    }



    public static byte next(CPU cpu) {
        byte b = cpu.nextByte();
        Instruction instruction = instructions[b & 0xFF];
        if (instruction != nop) {
//            log.info("Executing instruction: {} using {}", String.format("%02X", b), instruction);
            instruction.accept(cpu);
        } else {
            System.out.printf("%02X%n", b);
        }
        return b;
    }
}
