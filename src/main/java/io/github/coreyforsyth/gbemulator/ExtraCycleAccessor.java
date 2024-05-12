package io.github.coreyforsyth.gbemulator;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ExtraCycleAccessor<T> extends Accessor<T>
{
    private final Accessor<T> accessor;

    public ExtraCycleAccessor(Accessor<T> accessor)
    {
        super(null, null, null, 0);
        this.accessor = accessor;
    }

    @Override
    public int getSize()
    {
        return accessor.getSize();
    }

    @Override
    public String friendlyName(CPU cpu, char pcAddress)
    {
        return accessor.friendlyName(cpu, pcAddress);
    }

    @Override
    public T apply(CPU cpu)
    {
        cpu.cycle();
        return accessor.apply(cpu);
    }

    @Override
    public void accept(CPU cpu, T value)
    {
        accessor.accept(cpu, value);
    }
}
