package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class FlagInstruction<T> extends Instruction<T, T> implements Consumer<CPU>
{

	public FlagInstruction(Accessor<T> primary, Accessor<T> secondary)
	{
        super(primary, secondary);
	}

	@Override
    public void accept(CPU cpu)
    {
        T a = this.primary.apply(cpu);
        T b = this.secondary.apply(cpu);
        int intResult = applyOperation(cpu, a, b);
        T result = castResult(intResult);
        primary.accept(cpu, result);
        setZ(cpu, result);
        setS(cpu);
        setHC(cpu, result, a, b);
        setCy(cpu, intResult, a, b);
    }

    public abstract int applyOperation(CPU cpu, T a, T b);

    public abstract T castResult(int result);

    public void setZ(CPU cpu, T result) {
        boolean z = result.equals(ZERO_BYTE) || result.equals(ZERO_CHAR);
        cpu.setZero(z);
    }

    public void setS(CPU cpu) {
        cpu.setSubtraction(false);
    }

    public void setHC(CPU cpu, T result, T a, T b) {
        cpu.setHalfCarry(false);
    }

    public void setCy(CPU cpu, int result, T a, T b) {
        cpu.setCarry(false);
    }

}
