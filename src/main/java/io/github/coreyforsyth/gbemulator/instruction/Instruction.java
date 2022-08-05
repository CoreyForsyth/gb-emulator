package io.github.coreyforsyth.gbemulator.instruction;

import io.github.coreyforsyth.gbemulator.CPU;
import java.util.function.Consumer;

public interface Instruction extends Consumer<CPU>{
    Byte ZERO_BYTE = 0;
    Character ZERO_CHAR = 0;
}
