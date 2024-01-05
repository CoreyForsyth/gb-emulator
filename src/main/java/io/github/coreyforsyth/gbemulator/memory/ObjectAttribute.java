package io.github.coreyforsyth.gbemulator.memory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectAttribute
{
    private byte x;
    private byte y;
    private byte tileIndex;
    private boolean priority;
    private boolean yFlip;
    private boolean xFlip;
    private boolean dmgPalette;
    private boolean bank;
    private int cgbPalette;

    public byte getAttributes() {
        byte attributes = (byte) cgbPalette;
        attributes |= bank ? 0x08 : 0;
        attributes |= dmgPalette ? 0x10 : 0;
        attributes |= xFlip ? 0x20 : 0;
        attributes |= yFlip ? 0x40 : 0;
        attributes |= priority ? 0x80 : 0;
        return attributes;
    }

    public void setAttributes(byte attributes) {
        cgbPalette = attributes & 0x07;
        bank = (0x08 & attributes) == 0x08;
        dmgPalette = (0x10 & attributes) == 0x10;
        xFlip = (0x20 & attributes) == 0x20;
        yFlip = (0x40 & attributes) == 0x40;
        priority = (0x80 & attributes) == 0x80;
    }

}
