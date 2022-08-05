package io.github.coreyforsyth.gbemulator;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Register
{
    private byte A;
    private byte B;
    private byte C;
    private byte D;
    private byte E;
    private byte F;
    private byte H;
    private byte L;
    private char PC;
    private char SP;

    public char getBC() {
        return (char) ( B << 8 | C & 0x00FF);
    }

    public char getDE() {
        return (char) ( D << 8 | E & 0x00FF);
    }

    public char getHL() {
        return (char) ( H << 8 | L & 0x00FF);
    }

    public void setBC(char BC) {
        B = (byte) (BC >> 8 & 0xFF);
        C = (byte) (BC & 0xFF);
    }

    public void setDE(char DE) {
        D = (byte) (DE >> 8 & 0xFF);
        E = (byte) (DE & 0xFF);
    }

    public void setHL(char HL) {
        H = (byte) (HL >> 8 & 0xFF);
        L = (byte) (HL & 0xFF);
    }

    public char readIncrementPC() {
        return PC++;
    }
}
