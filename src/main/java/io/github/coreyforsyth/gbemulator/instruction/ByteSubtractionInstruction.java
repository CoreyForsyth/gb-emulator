package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.BiConsumer;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteSubtractionInstruction extends ByteCarryInstruction
{

    public ByteSubtractionInstruction(Function<CPU, Byte> a, Function<CPU, Byte> b, BiConsumer<CPU, Byte> setter)
    {
        super(a, b, setter);
    }

    @Override
    public int applyOperation(CPU cpu, Byte a, Byte b)
    {
        return a - b;
    }

    @Override
    public void setS(CPU cpu)
    {
        System.out.println("Setting s to true in ByteSubtractionInstruction");
        cpu.setSubtraction(true);
    }
}
