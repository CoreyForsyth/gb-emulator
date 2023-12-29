package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.Accessor;
import io.github.coreyforsyth.gbemulator.CPU;

public abstract class CharCarryInstruction extends FlagInstruction<Character>
{

    public CharCarryInstruction(Accessor<Character> primary, Accessor<Character> secondary)
    {
        super(primary, secondary);
    }

    @Override
    public Character castResult(int result)
    {
        return (char) result;
    }

    @Override
    public void setHC(CPU cpu, Character result, Character a, Character b)
    {
        cpu.setHalfCarry(((applyOperation(cpu, (char) (a & 0x0FFF), (char) (b & 0x0FFF))) & 0x1000) == 0x1000);
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
