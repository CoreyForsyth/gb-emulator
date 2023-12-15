package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Instructions
{

    public static final Instruction[] instructions;
	public static final Instruction[] interruptInstruction;
    private static final Instruction nop = cpu -> {
    };

    static
    {
        instructions = new Instruction[0x100];
        instructions[0x00] = cpu -> {
        };
        instructions[0x01] = cpu -> cpu.setBC(cpu.nextChar());
        instructions[0x02] = cpu -> cpu.writeByte(cpu.getBC(), cpu.getA());
        instructions[0x03] = cpu -> cpu.setBC((char) (cpu.getBC() + 1));
        instructions[0x04] = new INC(CPU::getB, CPU::setB);
        instructions[0x05] = new DEC(CPU::getB, CPU::setB);
        instructions[0x06] = cpu -> cpu.setB(cpu.nextByte());
        instructions[0x07] = cpu -> {
            boolean carry = (cpu.getA() & 0x80) == 0x80;
            cpu.setA((byte) ((cpu.getA() << 1) | (carry ? 1 : 0)));
            cpu.clearFlags();
            cpu.setCarry(carry);
        };
        instructions[0x08] = cpu -> {
            char c = cpu.nextChar();
            cpu.writeByte(c, (byte) (cpu.getSP() & 0xFF));
            cpu.writeByte((char) (c + 1), (byte) ((cpu.getSP() & 0xFF00) >> 8));
        };
        instructions[0x09] = new ADDChar(CPU::getHL, CPU::getBC, CPU::setHL);
        instructions[0x0A] = cpu -> cpu.setA(cpu.cpuReadByte(cpu.getBC()));
        instructions[0x0B] = cpu -> cpu.setBC((char) (cpu.getBC() - 1));
        instructions[0x0C] = new INC(CPU::getC, CPU::setC);
        instructions[0x0D] = new DEC(CPU::getC, CPU::setC);
        instructions[0x0E] = cpu -> cpu.setC(cpu.nextByte());
        instructions[0x0F] = cpu -> {
            boolean carry = (cpu.getA() & 0x01) == 0x01;
            cpu.setA((byte) ((cpu.getA() >> 1) | (carry ? 0x80 : 0)));
            cpu.clearFlags();
            cpu.setCarry(carry);
        };
        instructions[0x10] = nop;
        instructions[0x11] = cpu -> cpu.setDE(cpu.nextChar());
        instructions[0x12] = cpu -> cpu.writeByte(cpu.getDE(), cpu.getA());
        instructions[0x13] = cpu -> cpu.setDE((char) (cpu.getDE() + 1));
        instructions[0x14] = new INC(CPU::getD, CPU::setD);
        instructions[0x15] = new DEC(CPU::getD, CPU::setD);
        instructions[0x16] = cpu -> cpu.setD(cpu.nextByte());
        instructions[0x17] = cpu -> {
            boolean carry = (cpu.getA() & 0x80) == 0x80;
            cpu.setA((byte) ((cpu.getA() >> 1) | (cpu.isCarry() ? 1 : 0)));
            cpu.clearFlags();
            cpu.setCarry(carry);
        };
        instructions[0x18] = new JR(cpu -> true);
        instructions[0x19] = new ADDChar(CPU::getHL, CPU::getDE, CPU::setHL);
        instructions[0x1A] = cpu -> cpu.setA(cpu.cpuReadByte(cpu.getDE()));
        instructions[0x1B] = cpu -> cpu.setDE((char) (cpu.getDE() - 1));
        instructions[0x1C] = new INC(CPU::getE, CPU::setE);
        instructions[0x1D] = new DEC(CPU::getE, CPU::setE);
        instructions[0x1E] = cpu -> cpu.setE(cpu.nextByte());
        instructions[0x1F] = cpu -> {
            boolean carry = (cpu.getA() & 0x01) == 0x01;
            cpu.setA((byte) ((cpu.getA() >> 1) | (cpu.isCarry() ? 0x80 : 0)));
            cpu.clearFlags();
            cpu.setCarry(carry);
        };
        instructions[0x20] = new JR(cpu -> !cpu.isZero());
        instructions[0x21] = cpu -> cpu.setHL(cpu.nextChar());
        instructions[0x22] = cpu -> {
            cpu.writeHL(cpu.getA());
            cpu.setHL((char) (cpu.getHL() + 1));
        };
        instructions[0x23] = cpu -> cpu.setHL((char) (cpu.getHL() + 1));
        instructions[0x24] = new INC(CPU::getH, CPU::setH);
        instructions[0x25] = new DEC(CPU::getH, CPU::setH);
        instructions[0x26] = cpu -> cpu.setH(cpu.nextByte());
        instructions[0x27] = cpu -> {
            if (cpu.isSubtraction())
            {
                if (cpu.isCarry())
                {
                    cpu.setA((byte) (cpu.getA() - 0x60));
                }
                if (cpu.isHalfCarry())
                {
                    cpu.setA((byte) (cpu.getA() - 0x06));
                }
            }
            else
            {
                if (cpu.isCarry() || (Byte.toUnsignedInt(cpu.getA())) > 0x99)
                {
                    cpu.setA((byte) (cpu.getA() + 0x60));
                    cpu.setCarry(true);
                }
                if (cpu.isHalfCarry() || (cpu.getA() & 0x0f) > 0x09)
                {
                    cpu.setA((byte) (cpu.getA() + 0x06));
                }
            }
            cpu.setZero(cpu.getA() == 0);
            cpu.setHalfCarry(false);
        };
        instructions[0x28] = new JR(CPU::isZero);
        // ADD HL, HL
        instructions[0x29] = new ADDChar(CPU::getHL, CPU::getHL, CPU::setHL);
        // LD A, (HL+)
        instructions[0x2A] = cpu -> {
            byte hlContent = cpu.readHL();
            cpu.setA(hlContent);
            cpu.writeHL((byte) (hlContent + 1));
        };
        instructions[0x2B] = cpu -> cpu.setHL((char) (cpu.getHL() - 1));
        instructions[0x2C] = new INC(CPU::getL, CPU::setL);
        instructions[0x2D] = new DEC(CPU::getL, CPU::setL);
        instructions[0x2E] = cpu -> cpu.setL(cpu.nextByte());
        instructions[0x2F] = cpu -> {
            cpu.setA((byte) ~cpu.getA());
            cpu.setSubtraction(true);
            cpu.setHalfCarry(true);
        };

        instructions[0x30] = new JR(cpu -> !cpu.isCarry());
        instructions[0x31] = cpu -> cpu.setSP(cpu.nextChar());
        instructions[0x32] = cpu -> {
            cpu.writeHL(cpu.getA());
            cpu.setHL((char) (cpu.getHL() - 1));
        };
        instructions[0x33] = cpu -> cpu.setSP((char) (cpu.getSP() + 1));
        instructions[0x34] = new INC(CPU::readHL, CPU::writeHL);
        instructions[0x35] = new DEC(CPU::readHL, CPU::writeHL);
        instructions[0x36] = cpu -> cpu.writeHL(cpu.nextByte());
        instructions[0x37] = cpu -> {
            cpu.setCarry(true);
            cpu.setHalfCarry(false);
            cpu.setSubtraction(false);
        };
        instructions[0x38] = new JR(CPU::isCarry);
        instructions[0x39] = new ADDChar(CPU::getHL, CPU::getSP, CPU::setHL);
        instructions[0x3A] = cpu -> {
            byte hlContent = cpu.readHL();
            cpu.setA(hlContent);
            cpu.writeHL((byte) (hlContent - 1));
        };
        instructions[0x3B] = cpu -> cpu.setSP((char) (cpu.getSP() - 1));
        instructions[0x3C] = new INC(CPU::getA, CPU::setA);
        instructions[0x3D] = new DEC(CPU::getA, CPU::setA);
        // LD A, d8
        instructions[0x3E] = cpu -> cpu.setA(cpu.nextByte());
        instructions[0x3F] = cpu -> {
            cpu.setSubtraction(false);
            cpu.setHalfCarry(false);
            cpu.setCarry(!cpu.isCarry());
        };
        instructions[0X40] = cpu -> cpu.setB(cpu.getB());
        instructions[0X41] = cpu -> cpu.setB(cpu.getC());
        instructions[0X42] = cpu -> cpu.setB(cpu.getD());
        instructions[0X43] = cpu -> cpu.setB(cpu.getE());
        instructions[0X44] = cpu -> cpu.setB(cpu.getH());
        instructions[0X45] = cpu -> cpu.setB(cpu.getL());
        instructions[0X46] = cpu -> cpu.setB(cpu.cpuReadByte(cpu.getHL()));
        instructions[0X47] = cpu -> cpu.setB(cpu.getA());
        instructions[0X48] = cpu -> cpu.setC(cpu.getB());
        instructions[0X49] = cpu -> cpu.setC(cpu.getC());
        instructions[0X4A] = cpu -> cpu.setC(cpu.getD());
        instructions[0X4B] = cpu -> cpu.setC(cpu.getE());
        instructions[0X4C] = cpu -> cpu.setC(cpu.getH());
        instructions[0X4D] = cpu -> cpu.setC(cpu.getL());
        instructions[0X4E] = cpu -> cpu.setC(cpu.cpuReadByte(cpu.getHL()));
        instructions[0X4F] = cpu -> cpu.setC(cpu.getA());
        instructions[0X50] = cpu -> cpu.setD(cpu.getB());
        instructions[0X51] = cpu -> cpu.setD(cpu.getC());
        instructions[0X52] = cpu -> cpu.setD(cpu.getD());
        instructions[0X53] = cpu -> cpu.setD(cpu.getE());
        instructions[0X54] = cpu -> cpu.setD(cpu.getH());
        instructions[0X55] = cpu -> cpu.setD(cpu.getL());
        instructions[0X56] = cpu -> cpu.setD(cpu.cpuReadByte(cpu.getHL()));
        instructions[0X57] = cpu -> cpu.setD(cpu.getA());
        instructions[0X58] = cpu -> cpu.setE(cpu.getB());
        instructions[0X59] = cpu -> cpu.setE(cpu.getC());
        instructions[0X5A] = cpu -> cpu.setE(cpu.getD());
        instructions[0X5B] = cpu -> cpu.setE(cpu.getE());
        instructions[0X5C] = cpu -> cpu.setE(cpu.getH());
        instructions[0X5D] = cpu -> cpu.setE(cpu.getL());
        instructions[0X5E] = cpu -> cpu.setE(cpu.cpuReadByte(cpu.getHL()));
        instructions[0X5F] = cpu -> cpu.setE(cpu.getA());
        instructions[0X60] = cpu -> cpu.setH(cpu.getB());
        instructions[0X61] = cpu -> cpu.setH(cpu.getC());
        instructions[0X62] = cpu -> cpu.setH(cpu.getD());
        instructions[0X63] = cpu -> cpu.setH(cpu.getE());
        instructions[0X64] = cpu -> cpu.setH(cpu.getH());
        instructions[0X65] = cpu -> cpu.setH(cpu.getL());
        instructions[0X66] = cpu -> cpu.setH(cpu.cpuReadByte(cpu.getHL()));
        instructions[0X67] = cpu -> cpu.setH(cpu.getA());
        instructions[0X68] = cpu -> cpu.setL(cpu.getB());
        instructions[0X69] = cpu -> cpu.setL(cpu.getC());
        instructions[0X6A] = cpu -> cpu.setL(cpu.getD());
        instructions[0X6B] = cpu -> cpu.setL(cpu.getE());
        instructions[0X6C] = cpu -> cpu.setL(cpu.getH());
        instructions[0X6D] = cpu -> cpu.setL(cpu.getL());
        instructions[0X6E] = cpu -> cpu.setL(cpu.cpuReadByte(cpu.getHL()));
        instructions[0X6F] = cpu -> cpu.setL(cpu.getA());
        instructions[0X70] = cpu -> cpu.writeByte(cpu.getHL(), cpu.getB());
        instructions[0X71] = cpu -> cpu.writeByte(cpu.getHL(), cpu.getC());
        instructions[0X72] = cpu -> cpu.writeByte(cpu.getHL(), cpu.getD());
        instructions[0X73] = cpu -> cpu.writeByte(cpu.getHL(), cpu.getE());
        instructions[0X74] = cpu -> cpu.writeByte(cpu.getHL(), cpu.getH());
        instructions[0X75] = cpu -> cpu.writeByte(cpu.getHL(), cpu.getL());
        instructions[0X76] = nop; // TODO: HALT
        instructions[0X77] = cpu -> cpu.writeByte(cpu.getHL(), cpu.getA());
        instructions[0X78] = cpu -> cpu.setA(cpu.getB());
        instructions[0X79] = cpu -> cpu.setA(cpu.getC());
        instructions[0X7A] = cpu -> cpu.setA(cpu.getD());
        instructions[0X7B] = cpu -> cpu.setA(cpu.getE());
        instructions[0X7C] = cpu -> cpu.setA(cpu.getH());
        instructions[0X7D] = cpu -> cpu.setA(cpu.getL());
        instructions[0X7E] = cpu -> cpu.setA(cpu.cpuReadByte(cpu.getHL()));
        instructions[0X7F] = cpu -> cpu.setA(cpu.getA());
        instructions[0X80] = new ADD(CPU::getA, CPU::getB, CPU::setA);
        instructions[0X81] = new ADD(CPU::getA, CPU::getC, CPU::setA);
        instructions[0X82] = new ADD(CPU::getA, CPU::getD, CPU::setA);
        instructions[0X83] = new ADD(CPU::getA, CPU::getE, CPU::setA);
        instructions[0X84] = new ADD(CPU::getA, CPU::getH, CPU::setA);
        instructions[0X85] = new ADD(CPU::getA, CPU::getL, CPU::setA);
        instructions[0X86] = new ADD(CPU::getA, CPU::readHL, CPU::setA);
        instructions[0X87] = new ADD(CPU::getA, CPU::getA, CPU::setA);
        instructions[0X88] = new ADC(CPU::getA, CPU::getB, CPU::setA);
        instructions[0X89] = new ADC(CPU::getA, CPU::getC, CPU::setA);
        instructions[0X8A] = new ADC(CPU::getA, CPU::getD, CPU::setA);
        instructions[0X8B] = new ADC(CPU::getA, CPU::getE, CPU::setA);
        instructions[0X8C] = new ADC(CPU::getA, CPU::getH, CPU::setA);
        instructions[0X8D] = new ADC(CPU::getA, CPU::getL, CPU::setA);
        instructions[0X8E] = new ADC(CPU::getA, CPU::readHL, CPU::setA);
        instructions[0X8F] = new ADC(CPU::getA, CPU::getA, CPU::setA);
        // SUB B
        instructions[0x90] = new SUB(CPU::getA, CPU::getB, CPU::setA);
        instructions[0X91] = new SUB(CPU::getA, CPU::getC, CPU::setA);
        instructions[0X92] = new SUB(CPU::getA, CPU::getD, CPU::setA);
        instructions[0X93] = new SUB(CPU::getA, CPU::getE, CPU::setA);
        instructions[0X94] = new SUB(CPU::getA, CPU::getH, CPU::setA);
        instructions[0X95] = new SUB(CPU::getA, CPU::getL, CPU::setA);
        instructions[0X96] = new SUB(CPU::getA, CPU::readHL, CPU::setA);
        instructions[0X97] = new SUB(CPU::getA, CPU::getA, CPU::setA);
        instructions[0X98] = new SBC(CPU::getA, CPU::getB, CPU::setA);
        instructions[0X99] = new SBC(CPU::getA, CPU::getC, CPU::setA);
        instructions[0X9A] = new SBC(CPU::getA, CPU::getD, CPU::setA);
        instructions[0X9B] = new SBC(CPU::getA, CPU::getE, CPU::setA);
        instructions[0X9C] = new SBC(CPU::getA, CPU::getH, CPU::setA);
        instructions[0X9D] = new SBC(CPU::getA, CPU::getL, CPU::setA);
        instructions[0X9E] = new SBC(CPU::getA, CPU::readHL, CPU::setA);
        instructions[0X9F] = new SBC(CPU::getA, CPU::getA, CPU::setA);
        instructions[0XA0] = new AND(CPU::getA, CPU::getB, CPU::setA);
        instructions[0XA1] = new AND(CPU::getA, CPU::getC, CPU::setA);
        instructions[0XA2] = new AND(CPU::getA, CPU::getD, CPU::setA);
        instructions[0XA3] = new AND(CPU::getA, CPU::getE, CPU::setA);
        instructions[0XA4] = new AND(CPU::getA, CPU::getH, CPU::setA);
        instructions[0XA5] = new AND(CPU::getA, CPU::getL, CPU::setA);
        instructions[0XA6] = new AND(CPU::getA, CPU::readHL, CPU::setA);
        instructions[0XA7] = new AND(CPU::getA, CPU::getA, CPU::setA);
        instructions[0XA8] = new XOR(CPU::getA, CPU::getB, CPU::setA);
        instructions[0XA9] = new XOR(CPU::getA, CPU::getC, CPU::setA);
        instructions[0XAA] = new XOR(CPU::getA, CPU::getD, CPU::setA);
        instructions[0XAB] = new XOR(CPU::getA, CPU::getE, CPU::setA);
        instructions[0XAC] = new XOR(CPU::getA, CPU::getH, CPU::setA);
        instructions[0XAD] = new XOR(CPU::getA, CPU::getL, CPU::setA);
        instructions[0XAE] = new XOR(CPU::getA, CPU::readHL, CPU::setA);
        instructions[0xAF] = new XOR(CPU::getA, CPU::getA, CPU::setA);
        instructions[0XB0] = new OR(CPU::getA, CPU::getB, CPU::setA);
        instructions[0XB1] = new OR(CPU::getA, CPU::getC, CPU::setA);
        instructions[0XB2] = new OR(CPU::getA, CPU::getD, CPU::setA);
        instructions[0XB3] = new OR(CPU::getA, CPU::getE, CPU::setA);
        instructions[0XB4] = new OR(CPU::getA, CPU::getH, CPU::setA);
        instructions[0XB5] = new OR(CPU::getA, CPU::getL, CPU::setA);
        instructions[0XB6] = new OR(CPU::getA, CPU::readHL, CPU::setA);
        instructions[0XB7] = new OR(CPU::getA, CPU::getA, CPU::setA);
        instructions[0XB8] = new SUB(CPU::getA, CPU::getB, (cpu, value) -> {});
        instructions[0XB9] = new SUB(CPU::getA, CPU::getC, (cpu, value) -> {});
        instructions[0XBA] = new SUB(CPU::getA, CPU::getD, (cpu, value) -> {});
        instructions[0XBB] = new SUB(CPU::getA, CPU::getE, (cpu, value) -> {});
        instructions[0XBC] = new SUB(CPU::getA, CPU::getH, (cpu, value) -> {});
        instructions[0XBD] = new SUB(CPU::getA, CPU::getL, (cpu, value) -> {});
        instructions[0XBE] = new SUB(CPU::getA, CPU::readHL, (cpu, value) -> {});
        instructions[0XBF] = new SUB(CPU::getA, CPU::getB, (cpu, value) -> {});
        instructions[0XC0] = new RET(cpu -> !cpu.isZero());
        instructions[0XC1] = new POP(CPU::setBC);
        instructions[0XC2] = new JP(CPU::nextChar, cpu -> !cpu.isZero());
        instructions[0xC3] = new JP(CPU::nextChar, cpu -> true);
        instructions[0XC4] = new CALL(cpu -> !cpu.isZero(), CPU::nextChar);
        instructions[0XC5] = new PUSH(CPU::getBC);
        instructions[0XC6] = new ADD(CPU::getA, CPU::nextByte, CPU::setA);
        instructions[0XC7] = new RST((byte) 0x00);
        instructions[0XC8] = new RET(CPU::isZero);
        instructions[0XC9] = new RET(cpu -> true);
        instructions[0XCA] = new JP(CPU::nextChar, CPU::isZero);
        instructions[0XCB] = CBInstructions::next;
        instructions[0XCC] = new CALL(CPU::isZero, CPU::nextChar);
        instructions[0XCD] = new CALL(cpu -> true, CPU::nextChar);
        instructions[0XCE] = new ADC(CPU::getA, CPU::nextByte, CPU::setA);
        instructions[0XCF] = new RST((byte) 0x08);
        instructions[0XD0] = new RET(cpu -> !cpu.isCarry());
        instructions[0XD1] = new POP(CPU::setDE);
        instructions[0XD2] = new JP(CPU::nextChar, cpu -> !cpu.isCarry());
        instructions[0XD3] = nop;
        instructions[0XD4] = new CALL(cpu -> !cpu.isCarry(), CPU::nextChar);
        instructions[0XD5] = new PUSH(CPU::getDE);
        instructions[0XD6] = new SUB(CPU::getA, CPU::nextByte, CPU::setA);
        instructions[0XD7] = new RST((byte) 0x10);
        instructions[0XD8] = new RET(CPU::isCarry);
        instructions[0XD9] = new RET(cpu -> {
			System.out.println("RETI, turning off IME flag");
            cpu.setInterruptEnabled(false);
            return true;
        });
        instructions[0XDA] = new JP(CPU::nextChar, CPU::isCarry);
        instructions[0XDB] = nop;
        instructions[0XDC] = new CALL(CPU::isCarry, CPU::nextChar);
        instructions[0XDD] = nop;
        instructions[0XDE] = new SBC(CPU::getA, CPU::nextByte, CPU::setA);
        instructions[0XDF] = new RST((byte) 0x18);
        instructions[0xE0] = cpu -> {
            byte b = cpu.nextByte();
            int i = 0xFF00 | (0xFF & b);
            char address = (char) i;
            cpu.writeByte(address, cpu.getA());
        };
        instructions[0XE1] = new POP(CPU::setHL);
        instructions[0XE2] = cpu -> cpu.writeByte((char) (0xff00 + (cpu.getC() & 0xff)), cpu.getA());
        instructions[0XE3] = nop;
        instructions[0XE4] = nop;
        instructions[0XE5] = new PUSH(CPU::getHL);
        instructions[0XE6] = new AND(CPU::getA, CPU::nextByte, CPU::setA);
        instructions[0XE7] = new RST((byte) 0x20);
        instructions[0XE8] = new ADDChar(CPU::getSP, cpu -> (char) cpu.nextByte(), CPU::setSP) {
            @Override
            public void setZ(CPU cpu, Character result)
            {
                cpu.setZero(false);
            }
        };
        instructions[0XE9] = new JP(CPU::getHL, cpu -> true);
        // LD (a16), A
        instructions[0xEA] = cpu -> cpu.writeByte(cpu.nextChar(), cpu.getA());
        instructions[0XEB] = nop;
        instructions[0XEC] = nop;
        instructions[0XED] = nop;
        instructions[0XEE] = nop;
        instructions[0XEF] = new RST((byte) 0x28);
        instructions[0XF0] = cpu -> {
            byte b = cpu.nextByte();
            int i = 0xFF00 | (0xFF & b);
            cpu.setA(cpu.cpuReadByte((char) i));
        };
        instructions[0XF1] = new POP((cpu, character) -> {
            cpu.setA((byte) (((character & 0xff00) >> 8) & 0xff));
            cpu.setZero((character & 0x80) == 0x80);
            cpu.setSubtraction((character & 0x70) == 0x70);
            cpu.setHalfCarry((character & 0x60) == 0x60);
            cpu.setCarry((character & 0x50) == 0x50);
        });
        instructions[0XF2] = nop;
        // DI
        instructions[0xF3] = cpu -> {
			System.out.println("DI, turning off IME flag");
			cpu.setInterruptEnabled(false);
		};
        instructions[0XF4] = nop;
        instructions[0XF5] = new PUSH((cpu) -> {
            int af = cpu.isZero() ? 0x80 : 0;
            af |= cpu.isSubtraction() ? 0x70 : 0;
            af |= cpu.isHalfCarry() ? 0x60 : 0;
            af |= cpu.isCarry() ? 0x50 : 0;
            af |= cpu.getA() << 8;
            return (char) af;
        });
        instructions[0XF6] = nop;
        instructions[0XF7] = new RST((byte) 0x30);
        instructions[0XF8] = nop;
        instructions[0XF9] = nop;
        instructions[0XFA] = cpu -> cpu.setA(cpu.cpuReadByte(cpu.nextChar()));
        // EI
        instructions[0xFB] = cpu -> {
			System.out.println("EI, turning on IME flag");
			cpu.setInterruptEnabled(true);
		};
        instructions[0XFC] = nop;
        instructions[0XFD] = nop;
        // CP d8
        instructions[0xFE] = new SUB(CPU::getA, CPU::nextByte, (a, b) -> {
        });
        instructions[0XFF] = new RST((byte) 0x38);

		interruptInstruction = new Instruction[5];
		interruptInstruction[0] = new CALL((cpu) -> {
			System.out.println("Executing interrupt 0");
			return true;
		}, (cpu) -> (char) 0x40);
		interruptInstruction[1] = new CALL((cpu) -> {
			System.out.println("Executing interrupt 1");
			return true;
		}, (cpu) -> (char) 0x48);
		interruptInstruction[2] = new CALL((cpu) -> {
			System.out.println("Executing interrupt 2");
			return true;
		}, (cpu) -> (char) 0x50);
		interruptInstruction[3] = new CALL((cpu) -> {
			System.out.println("Executing interrupt 3");
			return true;
		}, (cpu) -> (char) 0x58);
		interruptInstruction[4] = new CALL((cpu) -> {
			System.out.println("Executing interrupt 4");
			return true;
		}, (cpu) -> (char) 0x60);

    }


    public static int next(CPU cpu)
    {
		// 5A36
		Instruction instruction;
		byte b = 0;
		if (cpu.isInterruptEnabled() && (cpu.getInterruptRegister() & 0x1f) != 0 && (cpu.readByte((char) 0xFF0F) & 0x1F) != 0) {
			cpu.setInterruptEnabled(false);
			byte interruptRegister = cpu.getInterruptRegister();
			if ((interruptRegister & 0x01) != 0) {
				instruction = interruptInstruction[0];
			} else if ((interruptRegister & 0x02) != 0) {
				instruction = interruptInstruction[1];
			} else if ((interruptRegister & 0x04) != 0) {
				instruction = interruptInstruction[2];
			} else if ((interruptRegister & 0x08) != 0) {
				instruction = interruptInstruction[3];
			} else {
				instruction = interruptInstruction[4];
			}
		} else {
			b = cpu.nextByte();
			instruction = instructions[b & 0xFF];
		}

        if (instruction != nop)
        {
			if (cpu.isDebug()) {
				instruction.debug(cpu, b);
			} else {

				instruction.accept(cpu);
			}

//            log.info("Executing instruction: {} using {}", String.format("%02X", b), instruction);
        }
        else
        {
            System.out.printf("Implement instruction: %02X%n", b);
        }
        return b;
    }
}
