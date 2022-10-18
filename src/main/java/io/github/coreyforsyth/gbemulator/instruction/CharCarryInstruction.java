package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class CharCarryInstruction extends FlagInstruction<Character>
{

    public CharCarryInstruction(Function<CPU, Character> a, Function<CPU, Character> b, BiConsumer<CPU, Character> setter)
    {
        super(a, b, setter);
    }

    @Override
    public Character castResult(int result)
    {
        return (char) result;
    }

    @Override
    public void setHC(CPU cpu, Character result, Character a, Character b)
    {
        cpu.setHalfCarry(((applyOperation(cpu, (char) ((a & 0x0F00) >> 8), (char) ((b & 0x0F00) >> 8))) & 0x1000) == 0x1000);
    }

    @Override
    public void setCy(CPU cpu, int result, Character a, Character b)
    {
        if (cpu.isSubtraction()) {
            cpu.setCarry(result < 0);
        } else {
            cpu.setCarry((result & 0x10000) == 0x10000);
        }
    }
}
