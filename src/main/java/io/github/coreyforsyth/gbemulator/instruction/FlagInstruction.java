package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public abstract class FlagInstruction<T> implements Instruction
{

    private final Function<CPU, T> a;
    private final Function<CPU, T> b;
    private final BiConsumer<CPU, T> setter;

    @Override
    public void accept(CPU cpu)
    {
        T a = this.a.apply(cpu);
        T b = this.b.apply(cpu);
        int intResult = applyOperation(cpu, a, b);
        T result = castResult(intResult);
        setter.accept(cpu, result);
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
