package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.Consumer;

public abstract class Instruction<P, S> implements Consumer<CPU> {
    Byte ZERO_BYTE = 0;
    Character ZERO_CHAR = 0;

    protected final Accessor<P> primary;
    protected final Accessor<S> secondary;

	public Instruction(Accessor<P> primary, Accessor<S> secondary)
	{
        this.primary = primary;
        this.secondary = secondary;
    }

	public void debug(CPU cpu, byte b) {
		System.out.printf("Executing instruction: %s\n", String.format("%02X", b));
		char pc = cpu.getPC();
		for (int i = 0; i < 10; i++)
		{

			System.out.printf("%02X", cpu.cpuReadByte((char) (pc + i)) & 0xff);
		}
		System.out.println();

		accept(cpu);
	}

    public final int size() {
        int primarySize = primary != null ? primary.getSize() : 0;
        int secondarySize = secondary != null ? secondary.getSize() : 0;
        return 1 + primarySize + secondarySize;
    }
}
