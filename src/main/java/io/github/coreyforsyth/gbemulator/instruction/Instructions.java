package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Instructions
{

    public static final Instruction<?, ?>[] instructions;
    public static final Instruction<Boolean, Character> vblankInterrupt = new RST((byte) 0x40);
    public static final Instruction<Boolean, Character> statInterrupt = new RST((byte) 0x48);
    public static final Instruction<Boolean, Character> timerInterrupt = new RST((byte) 0x50);
    public static final Instruction<Boolean, Character> serialInterrupt = new RST((byte) 0x58);
    public static final Instruction<Boolean, Character> joypadInterrupt = new RST((byte) 0x60);

    private static final Instruction<Void,Void> nop = new Instruction<>(null, null)
    {
        @Override
        public void accept(CPU cpu)
        {

        }
    };

    static
    {

        instructions = new Instruction[0x100];
        instructions[0x00] = nop;
        instructions[0x01] = new LDC(Accessor.BC, Accessor.IM16);
        instructions[0x02] = new LD(Accessor.ADR_BC, Accessor.A);
        instructions[0x03] = new INCC(Accessor.BC);
        instructions[0x04] = new INC(Accessor.B);
        instructions[0x05] = new DEC(Accessor.B);
        instructions[0x06] = new LD(Accessor.B, Accessor.IM8);
        instructions[0x07] = new RLC(Accessor.A);
        instructions[0x08] = new LDC(Accessor.ADR_IM16_CHAR, Accessor.SP);
        instructions[0x09] = new ADDC(Accessor.HL, Accessor.BC);
        instructions[0x0A] = new LD(Accessor.A, Accessor.ADR_BC);
        instructions[0x0B] = new DECC(Accessor.BC);
        instructions[0x0C] = new INC(Accessor.C);
        instructions[0x0D] = new DEC(Accessor.C);
        instructions[0x0E] = new LD(Accessor.C, Accessor.IM8);
        instructions[0x0F] = new RRC(Accessor.A);
        instructions[0x10] = nop;
        instructions[0x11] = new LDC(Accessor.DE, Accessor.IM16);
        instructions[0x12] = new LD(Accessor.ADR_DE, Accessor.A);
        instructions[0x13] = new INCC(Accessor.DE);
        instructions[0x14] = new INC(Accessor.D);
        instructions[0x15] = new DEC(Accessor.D);
        instructions[0x16] = new LD(Accessor.D, Accessor.ADR_DE);
        instructions[0x17] = new RL(Accessor.A);
        instructions[0x18] = new JR(Accessor.TRUE, false);
        instructions[0x19] = new ADDC(Accessor.HL, Accessor.DE);
        instructions[0x1A] = new LD(Accessor.A, Accessor.ADR_DE);
        instructions[0x1B] = new DECC(Accessor.DE);
        instructions[0x1C] = new INC(Accessor.E);
        instructions[0x1D] = new DEC(Accessor.E);
        instructions[0x1E] = new LD(Accessor.E, Accessor.IM8);
        instructions[0x1F] = new RR(Accessor.A);
        instructions[0x20] = new JR(Accessor.Z, true);
        instructions[0x21] = new LDC(Accessor.HL, Accessor.IM16);
        instructions[0x22] = new LD(Accessor.ADR_HLI, Accessor.A);
        instructions[0x23] = new INCC(Accessor.HL);
        instructions[0x24] = new INC(Accessor.H);
        instructions[0x25] = new DEC(Accessor.H);
        instructions[0x26] = new LD(Accessor.H, Accessor.IM8);
        instructions[0x27] = new DAA();
        instructions[0x28] = new JR(Accessor.Z, false);
        instructions[0x29] = new ADDC(Accessor.HL, Accessor.HL);
        // LD A, (HL+)
        instructions[0x2A] = new LD(Accessor.A, Accessor.ADR_HLI);
        instructions[0x2B] = new DECC(Accessor.HL);
        instructions[0x2C] = new INC(Accessor.L);
        instructions[0x2D] = new DEC(Accessor.L);
        instructions[0x2E] = new LD(Accessor.L, Accessor.IM8);
        instructions[0x2F] = new CPL();
        instructions[0x30] = new JR(Accessor.FC, true);
        instructions[0x31] = new LDC(Accessor.SP, Accessor.IM16);
        instructions[0x32] = new LD(Accessor.ADR_HLD, Accessor.A);
        instructions[0x33] = new INCC(Accessor.SP);
        instructions[0x34] = new INC(Accessor.ADR_HL);
        instructions[0x35] = new DEC(Accessor.ADR_HL);
        instructions[0x36] = new LD(Accessor.ADR_HL, Accessor.IM8);
        instructions[0x37] = new SCF();
        instructions[0x38] = new JR(Accessor.FC, false);
        instructions[0x39] = new ADDC(Accessor.HL, Accessor.SP);
        instructions[0x3A] = new LD(Accessor.A, Accessor.ADR_HLD);
        instructions[0x3B] = new DECC(Accessor.SP);
        instructions[0x3C] = new INC(Accessor.A);
        instructions[0x3D] = new DEC(Accessor.A);
        instructions[0x3E] = new LD(Accessor.A, Accessor.IM8);
        instructions[0x3F] = new CCF();
        instructions[0X40] = new LD(Accessor.B, Accessor.B);
        instructions[0X41] = new LD(Accessor.B, Accessor.C);
        instructions[0X42] = new LD(Accessor.B, Accessor.D);
        instructions[0X43] = new LD(Accessor.B, Accessor.E);
        instructions[0X44] = new LD(Accessor.B, Accessor.H);
        instructions[0X45] = new LD(Accessor.B, Accessor.L);
        instructions[0X46] = new LD(Accessor.B, Accessor.ADR_HL);
        instructions[0X47] = new LD(Accessor.B, Accessor.A);
        instructions[0X48] = new LD(Accessor.C, Accessor.B);
        instructions[0X49] = new LD(Accessor.C, Accessor.C);
        instructions[0X4A] = new LD(Accessor.C, Accessor.D);
        instructions[0X4B] = new LD(Accessor.C, Accessor.E);
        instructions[0X4C] = new LD(Accessor.C, Accessor.H);
        instructions[0X4D] = new LD(Accessor.C, Accessor.L);
        instructions[0X4E] = new LD(Accessor.C, Accessor.ADR_HL);
        instructions[0X4F] = new LD(Accessor.C, Accessor.A);
        instructions[0X50] = new LD(Accessor.D, Accessor.B);
        instructions[0X51] = new LD(Accessor.D, Accessor.C);
        instructions[0X52] = new LD(Accessor.D, Accessor.D);
        instructions[0X53] = new LD(Accessor.D, Accessor.E);
        instructions[0X54] = new LD(Accessor.D, Accessor.H);
        instructions[0X55] = new LD(Accessor.D, Accessor.L);
        instructions[0X56] = new LD(Accessor.D, Accessor.ADR_HL);
        instructions[0X57] = new LD(Accessor.D, Accessor.A);
        instructions[0X58] = new LD(Accessor.E, Accessor.B);
        instructions[0X59] = new LD(Accessor.E, Accessor.C);
        instructions[0X5A] = new LD(Accessor.E, Accessor.D);
        instructions[0X5B] = new LD(Accessor.E, Accessor.E);
        instructions[0X5C] = new LD(Accessor.E, Accessor.H);
        instructions[0X5D] = new LD(Accessor.E, Accessor.L);
        instructions[0X5E] = new LD(Accessor.E, Accessor.ADR_HL);
        instructions[0X5F] = new LD(Accessor.E, Accessor.A);
        instructions[0X60] = new LD(Accessor.H, Accessor.B);
        instructions[0X61] = new LD(Accessor.H, Accessor.C);
        instructions[0X62] = new LD(Accessor.H, Accessor.D);
        instructions[0X63] = new LD(Accessor.H, Accessor.E);
        instructions[0X64] = new LD(Accessor.H, Accessor.H);
        instructions[0X65] = new LD(Accessor.H, Accessor.L);
        instructions[0X66] = new LD(Accessor.H, Accessor.ADR_HL);
        instructions[0X67] = new LD(Accessor.H, Accessor.A);
        instructions[0X68] = new LD(Accessor.L, Accessor.B);
        instructions[0X69] = new LD(Accessor.L, Accessor.C);
        instructions[0X6A] = new LD(Accessor.L, Accessor.D);
        instructions[0X6B] = new LD(Accessor.L, Accessor.E);
        instructions[0X6C] = new LD(Accessor.L, Accessor.H);
        instructions[0X6D] = new LD(Accessor.L, Accessor.L);
        instructions[0X6E] = new LD(Accessor.L, Accessor.ADR_HL);
        instructions[0X6F] = new LD(Accessor.L, Accessor.A);
        instructions[0X70] = new LD(Accessor.ADR_HL, Accessor.B);
        instructions[0X71] = new LD(Accessor.ADR_HL, Accessor.C);
        instructions[0X72] = new LD(Accessor.ADR_HL, Accessor.D);
        instructions[0X73] = new LD(Accessor.ADR_HL, Accessor.E);
        instructions[0X74] = new LD(Accessor.ADR_HL, Accessor.H);
        instructions[0X75] = new LD(Accessor.ADR_HL, Accessor.L);
        instructions[0X76] = nop; // TODO: HALT
        instructions[0X77] = new LD(Accessor.ADR_HL, Accessor.A);
        instructions[0X78] = new LD(Accessor.A, Accessor.B);
        instructions[0X79] = new LD(Accessor.A, Accessor.C);
        instructions[0X7A] = new LD(Accessor.A, Accessor.D);
        instructions[0X7B] = new LD(Accessor.A, Accessor.E);
        instructions[0X7C] = new LD(Accessor.A, Accessor.H);
        instructions[0X7D] = new LD(Accessor.A, Accessor.L);
        instructions[0X7E] = new LD(Accessor.A, Accessor.ADR_HL);
        instructions[0X7F] = new LD(Accessor.A, Accessor.A);
        instructions[0X80] = new ADD(Accessor.A, Accessor.B);
        instructions[0X81] = new ADD(Accessor.A, Accessor.C);
        instructions[0X82] = new ADD(Accessor.A, Accessor.D);
        instructions[0X83] = new ADD(Accessor.A, Accessor.E);
        instructions[0X84] = new ADD(Accessor.A, Accessor.H);
        instructions[0X85] = new ADD(Accessor.A, Accessor.L);
        instructions[0X86] = new ADD(Accessor.A, Accessor.ADR_HL);
        instructions[0X87] = new ADD(Accessor.A, Accessor.A);
        instructions[0X88] = new ADC(Accessor.A, Accessor.B);
        instructions[0X89] = new ADC(Accessor.A, Accessor.C);
        instructions[0X8A] = new ADC(Accessor.A, Accessor.D);
        instructions[0X8B] = new ADC(Accessor.A, Accessor.E);
        instructions[0X8C] = new ADC(Accessor.A, Accessor.H);
        instructions[0X8D] = new ADC(Accessor.A, Accessor.L);
        instructions[0X8E] = new ADC(Accessor.A, Accessor.ADR_HL);
        instructions[0X8F] = new ADC(Accessor.A, Accessor.A);
        instructions[0x90] = new SUB(Accessor.A, Accessor.B);
        instructions[0X91] = new SUB(Accessor.A, Accessor.C);
        instructions[0X92] = new SUB(Accessor.A, Accessor.D);
        instructions[0X93] = new SUB(Accessor.A, Accessor.E);
        instructions[0X94] = new SUB(Accessor.A, Accessor.H);
        instructions[0X95] = new SUB(Accessor.A, Accessor.L);
        instructions[0X96] = new SUB(Accessor.A, Accessor.ADR_HL);
        instructions[0X97] = new SUB(Accessor.A, Accessor.A);
        instructions[0X98] = new SBC(Accessor.A, Accessor.B);
        instructions[0X99] = new SBC(Accessor.A, Accessor.C);
        instructions[0X9A] = new SBC(Accessor.A, Accessor.D);
        instructions[0X9B] = new SBC(Accessor.A, Accessor.E);
        instructions[0X9C] = new SBC(Accessor.A, Accessor.H);
        instructions[0X9D] = new SBC(Accessor.A, Accessor.L);
        instructions[0X9E] = new SBC(Accessor.A, Accessor.ADR_HL);
        instructions[0X9F] = new SBC(Accessor.A, Accessor.A);
        instructions[0XA0] = new AND(Accessor.B);
        instructions[0XA1] = new AND(Accessor.C);
        instructions[0XA2] = new AND(Accessor.D);
        instructions[0XA3] = new AND(Accessor.E);
        instructions[0XA4] = new AND(Accessor.H);
        instructions[0XA5] = new AND(Accessor.L);
        instructions[0XA6] = new AND(Accessor.ADR_HL);
        instructions[0XA7] = new AND(Accessor.A);
        instructions[0XA8] = new XOR(Accessor.B);
        instructions[0XA9] = new XOR(Accessor.C);
        instructions[0XAA] = new XOR(Accessor.D);
        instructions[0XAB] = new XOR(Accessor.E);
        instructions[0XAC] = new XOR(Accessor.H);
        instructions[0XAD] = new XOR(Accessor.L);
        instructions[0XAE] = new XOR(Accessor.ADR_HL);
        instructions[0xAF] = new XOR(Accessor.A);
        instructions[0XB0] = new OR(Accessor.B);
        instructions[0XB1] = new OR(Accessor.C);
        instructions[0XB2] = new OR(Accessor.D);
        instructions[0XB3] = new OR(Accessor.E);
        instructions[0XB4] = new OR(Accessor.H);
        instructions[0XB5] = new OR(Accessor.L);
        instructions[0XB6] = new OR(Accessor.ADR_HL);
        instructions[0XB7] = new OR(Accessor.A);
        instructions[0XB8] = new CP(Accessor.B);
        instructions[0XB9] = new CP(Accessor.C);
        instructions[0XBA] = new CP(Accessor.D);
        instructions[0XBB] = new CP(Accessor.E);
        instructions[0XBC] = new CP(Accessor.H);
        instructions[0XBD] = new CP(Accessor.L);
        instructions[0XBE] = new CP(Accessor.ADR_HL);
        instructions[0XBF] = new CP(Accessor.B);
        instructions[0XC0] = new RET(Accessor.Z, true);
        instructions[0XC1] = new POP(Accessor.BC);
        instructions[0XC2] = new JP(Accessor.Z, Accessor.IM16, true);
        instructions[0xC3] = new JP(Accessor.TRUE, Accessor.IM16, false);
        instructions[0XC4] = new CALL(Accessor.Z, Accessor.IM16, true);
        instructions[0XC5] = new PUSH(Accessor.BC);
        instructions[0XC6] = new ADD(Accessor.A, Accessor.IM8);
        instructions[0XC7] = new RST((byte) 0x00);
        instructions[0XC8] = new RET(Accessor.Z, false);
        instructions[0XC9] = new RET(Accessor.TRUE, false);
        instructions[0XCA] = new JP(Accessor.Z, Accessor.IM16, false);
        instructions[0XCB] = new CB();
        instructions[0XCC] = new CALL(Accessor.Z, Accessor.IM16, false);
        instructions[0XCD] = new CALL(Accessor.TRUE, Accessor.IM16, false);
        instructions[0XCE] = new ADC(Accessor.A, Accessor.IM8);
        instructions[0XCF] = new RST((byte) 0x08);
        instructions[0XD0] = new RET(Accessor.FC, true);
        instructions[0XD1] = new POP(Accessor.DE);
        instructions[0XD2] = new JP(Accessor.FC, Accessor.IM16, true);
        instructions[0XD3] = nop;
        instructions[0XD4] = new CALL(Accessor.FC, Accessor.IM16, true);
        instructions[0XD5] = new PUSH(Accessor.DE);
        instructions[0XD6] = new SUB(Accessor.A, Accessor.IM8);
        instructions[0XD7] = new RST((byte) 0x10);
        instructions[0XD8] = new RET(Accessor.FC, false);
        instructions[0XD9] = new RETI();
        instructions[0XDA] = new JP(Accessor.FC, Accessor.IM16, false);
        instructions[0XDB] = nop;
        instructions[0XDC] = new CALL(Accessor.FC, Accessor.IM16, false);
        instructions[0XDD] = nop;
        instructions[0XDE] = new SBC(Accessor.A, Accessor.IM8);
        instructions[0XDF] = new RST((byte) 0x18);
        instructions[0xE0] = new LD(Accessor.REL_FF_IM8, Accessor.A);
        instructions[0XE1] = new POP(Accessor.HL);
        instructions[0XE2] = new LD(Accessor.REL_FF_C, Accessor.A);
        instructions[0XE3] = nop;
        instructions[0XE4] = nop;
        instructions[0XE5] = new PUSH(Accessor.HL);
        instructions[0XE6] = new AND(Accessor.IM8);
        instructions[0XE7] = new RST((byte) 0x20);
        instructions[0XE8] = new ADDC(Accessor.SP, Accessor.IM8_CHAR) {
            @Override
            public void setZ(CPU cpu, Character result)
            {
                cpu.setZero(false);
            }
        };
        instructions[0XE9] = new JP(Accessor.TRUE, Accessor.HL, false);
        // LD (a16), A
        instructions[0xEA] = new LD(Accessor.ADR_IM16_BYTE, Accessor.A);
        instructions[0XEB] = nop;
        instructions[0XEC] = nop;
        instructions[0XED] = nop;
        instructions[0XEE] = new XOR(Accessor.IM8);
        instructions[0XEF] = new RST((byte) 0x28);
        instructions[0XF0] = new LD(Accessor.A, Accessor.REL_FF_IM8);
        int i = 0b10000000;
        instructions[0XF1] = new POP(Accessor.AF);
        instructions[0XF2] = new LD(Accessor.A, Accessor.REL_FF_C);
        // DI
        instructions[0xF3] = new DI();
        instructions[0XF4] = nop;
        instructions[0XF5] = new PUSH(Accessor.AF);
        instructions[0XF6] = new OR(Accessor.IM8);
        instructions[0XF7] = new RST((byte) 0x30);
        instructions[0XF8] = new ADDC(Accessor.HL_SP, Accessor.IM8_CHAR) {
            @Override
            public void setZ(CPU cpu, Character result)
            {
                cpu.setZero(false);
            }
        };
        instructions[0XF9] = new LDC(Accessor.SP, Accessor.HL);
        instructions[0XFA] = new LD(Accessor.A, Accessor.ADR_IM16_BYTE);
        // EI
        instructions[0xFB] = new EI();
        instructions[0XFC] = nop;
        instructions[0XFD] = nop;
        // CP d8
        instructions[0xFE] = new CP(Accessor.IM8);
        instructions[0XFF] = new RST((byte) 0x38);

    }


    public static int next(CPU cpu)
    {
        // 5A36
        Instruction<?, ?> instruction;
        byte b = 0;
        if (cpu.isInterruptEnabled() && (cpu.getInterruptRegister() & 0x1f) != 0 && (cpu.readByte((char) 0xFF0F) & 0x1F) != 0) {
            cpu.setInterruptEnabled(false);
            byte interruptRegister = cpu.getInterruptRegister();
            if ((interruptRegister & 0x01) != 0) {
                instruction = vblankInterrupt;
            } else if ((interruptRegister & 0x02) != 0) {
                instruction = statInterrupt;
            } else if ((interruptRegister & 0x04) != 0) {
                instruction = timerInterrupt;
            } else if ((interruptRegister & 0x08) != 0) {
                instruction = serialInterrupt;
            } else {
                instruction = joypadInterrupt;
            }
        } else {
            b = cpu.nextByte();
            instruction = instructions[b & 0xFF];
        }

        if (instruction != nop && (b & 0xFF ) != 0)
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
