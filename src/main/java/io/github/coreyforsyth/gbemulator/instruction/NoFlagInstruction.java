package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NoFlagInstruction<T> implements Instruction
{

    private Supplier<T> supplier;
    private Consumer<T> consumer;

    @Override
    public void accept(CPU cpu)
    {
        consumer.accept(supplier.get());
    }
}
