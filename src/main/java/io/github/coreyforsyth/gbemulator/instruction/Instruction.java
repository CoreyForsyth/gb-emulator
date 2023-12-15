package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.Consumer;

public interface Instruction extends Consumer<CPU>{
    Byte ZERO_BYTE = 0;
    Character ZERO_CHAR = 0;
	default void debug(CPU cpu, byte b) {
		System.out.printf("Executing instruction: %s\n", String.format("%02X", b));
		char pc = cpu.getPC();
		for (int i = 0; i < 10; i++)
		{

			System.out.printf("%02X", cpu.cpuReadByte((char) (pc + i)) & 0xff);
		}
		System.out.println();

		accept(cpu);
	}
}
