package io.github.coreyforsyth.gbemulator;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Accessor<T> implements Function<CPU, T>, BiConsumer<CPU, T>
{

	public static Accessor<Byte> A = new Accessor<>("A", CPU::getA, CPU::setA);
    public static Accessor<Byte> A_NO_SET = new Accessor<>("A", CPU::getA, (cpu, value) -> {});
	public static Accessor<Byte> B = new Accessor<>("B", CPU::getB, CPU::setB);
	public static Accessor<Byte> C = new Accessor<>("C", CPU::getC, CPU::setC);
	public static Accessor<Byte> D = new Accessor<>("D", CPU::getD, CPU::setD);
	public static Accessor<Byte> E = new Accessor<>("E", CPU::getE, CPU::setE);
	public static Accessor<Byte> H = new Accessor<>("H", CPU::getH, CPU::setH);
	public static Accessor<Byte> L = new Accessor<>("L", CPU::getL, CPU::setL);
	public static Accessor<Character> BC = new Accessor<>("BC", CPU::getBC, CPU::setBC);
	public static Accessor<Character> DE = new Accessor<>("DE", CPU::getDE, CPU::setDE);
	public static Accessor<Character> HL = new Accessor<>("HL", CPU::getHL, CPU::setHL);
	public static Accessor<Character> SP = new Accessor<>("SP", CPU::getSP, CPU::setSP);
	public static Accessor<Character> PC = new Accessor<>("PC", CPU::getPC, CPU::setPC);
    public static Accessor<Character> AF = new Accessor<>("AF", CPU::getAF, CPU::setAF);
    public static Accessor<Character> HL_SP = new Accessor<>("SP", CPU::getSP, CPU::setHL);
    public static Accessor<Boolean> Z = new Accessor<>("Z", CPU::isZero, ((cpu, value) -> {}));
    public static Accessor<Boolean> N = new Accessor<>("N", CPU::isSubtraction, ((cpu, value) -> {}));
    public static Accessor<Boolean> HC = new Accessor<>("H", CPU::isHalfCarry, ((cpu, value) -> {}));
    public static Accessor<Boolean> FC = new Accessor<>("C", CPU::isCarry, ((cpu, value) -> {}));
    public static Accessor<Byte> ADR_BC = new Accessor<>("(BC)", CPU::readBC, CPU::writeBC);
    public static Accessor<Byte> ADR_DE = new Accessor<>("(DE)", CPU::readDE, CPU::writeDE);
	public static Accessor<Byte> ADR_HL = new Accessor<>("(HL)", CPU::readHL, CPU::writeHL);
    public static Accessor<Byte> ADR_HLI = new Accessor<>("(HL+)", cpu -> {
        byte value = cpu.readHL();
        cpu.setHL((char) (cpu.getHL() + 1));
        return value;
    }, (cpu, value) -> {
        cpu.writeHL(value);
        cpu.setHL((char) (cpu.getHL() + 1));
    });
    public static Accessor<Byte> ADR_HLD = new Accessor<>("(HL-)", cpu -> {
        byte value = cpu.readHL();
        cpu.setHL((char) (cpu.getHL() - 1));
        return value;
    }, (cpu, value) -> {
        cpu.writeHL(value);
        cpu.setHL((char) (cpu.getHL() - 1));
    });
    public static Accessor<Byte> IM8 = new Accessor<>("IM8", CPU::nextByte, (cpu, value) -> {}, 1) {
        @Override
        public String friendlyName(CPU cpu, char pcAddress)
        {
            byte value = cpu.readByte((char) (pcAddress + 1));
            return String.format("0x%02X", value & 0xFF);
        }
    };
    public static Accessor<Character> IM16 = new Accessor<>("IM16", CPU::nextChar, (cpu, value) -> {}, 2) {
        @Override
        public String friendlyName(CPU cpu, char pcAddress)
        {
            byte low = cpu.readByte((char) (pcAddress + 1));
            byte high = cpu.readByte((char) (pcAddress + 2));
            char value = (char) ( high << 8 | low & 0x00FF);
            return String.format("0x%04X", value & 0xFFFF);
        }
    };
    public static Accessor<Character> ADR_IM16_CHAR = new Accessor<>("(a16)", cpu -> null, (cpu, value) -> {
        char c = cpu.nextChar();
        cpu.cpuWriteByte(c, (byte) (value & 0xFF));
        cpu.cpuWriteByte((char) (c + 1), (byte) ((value & 0xFF00) >> 8));
    }, 2);
    public static Accessor<Byte> ONE = new Accessor<>("1", cpu -> (byte) 1, (cpu, value) -> {});
    public static Accessor<Boolean> TRUE = new Accessor<>("true", cpu -> true, (cpu, value) -> {});
    public static Accessor<Character> REL_PC_IM8 = new Accessor<>("s8", cpu -> (char) (cpu.nextByte() + cpu.getPC()), (cpu, value) -> {}, 1) {
        @Override
        public String friendlyName(CPU cpu, char pcAddress)
        {
            byte b = cpu.readByte((char) (pcAddress + 1));
            return String.format("0x%04X", (pcAddress + b + 2) & 0xFFFF);
        }
    };
    public static Accessor<Character> IM8_CHAR = new Accessor<>("s8", cpu -> (char) (cpu.nextByte()), (cpu, value) -> {}, 1);
    public static Accessor<Byte> REL_FF_IM8 = new Accessor<>("a8", cpu -> cpu.cpuReadByte((char) (0xFF00 | (cpu.nextByte() & 0xFF))),
        (cpu, value) -> cpu.cpuWriteByte((char) (0xFF00 | (cpu.nextByte() & 0xFF)), value), 1) {
        @Override
        public String friendlyName(CPU cpu, char pcAddress)
        {
            byte low = cpu.readByte((char) (pcAddress + 1));
            char value = (char) ( 0xFF00 | low & 0x00FF);
            return String.format("(0x%04X)", value & 0xFFFF);
        }
    };
    public static Accessor<Byte> REL_FF_C = new Accessor<>("a8", cpu -> cpu.cpuReadByte((char) (0xFF00 | (cpu.getC() & 0xFF))),
        (cpu, value) -> cpu.cpuWriteByte((char) (0xFF00 | (cpu.getC() & 0xFF)), value)) {
        @Override
        public String friendlyName(CPU cpu, char pcAddress)
        {
            return String.format("(0x%04X)", 0xFF00 | (cpu.getC() & 0xFF));
        }
    };
    public static Accessor<Byte> ADR_IM16_BYTE = new Accessor<>("(a16)", cpu -> cpu.cpuReadByte(cpu.nextChar()),
        (cpu, value) -> cpu.cpuWriteByte(cpu.nextChar(), value), 2) {
        @Override
        public String friendlyName(CPU cpu, char pcAddress)
        {
            byte low = cpu.readByte((char) (pcAddress + 1));
            byte high = cpu.readByte((char) (pcAddress + 2));
            char value = (char) ( high << 8 | low & 0x00FF);
            return String.format("(0x%04X)", value & 0xFFFF);
        }
    };



	private final String name;
	private final Function<CPU, T> getter;
	private final BiConsumer<CPU, T> setter;
    private final int size;

	public Accessor(String name, Function<CPU, T> getter, BiConsumer<CPU, T> setter, int size)
	{
        this.name = name;
        this.getter = getter;
        this.setter = setter;
        this.size = size;
    }

    public Accessor(String name, Function<CPU, T> getter, BiConsumer<CPU, T> setter)
    {
        this(name, getter, setter, 0);
    }

    public int getSize()
    {
        return size;
    }

    public String friendlyName(CPU cpu, char pcAddress)
	{
		return name;
	}

	@Override
	public T apply(CPU cpu)
	{
		return getter.apply(cpu);
	}

	@Override
	public void accept(CPU cpu, T value)
	{
		setter.accept(cpu, value);
	}
}
