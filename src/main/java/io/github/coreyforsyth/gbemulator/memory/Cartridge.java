package io.github.coreyforsyth.gbemulator.memory;

public class Cartridge implements ReadWrite
{
    final byte[] rom;
    final byte[] ram;
    int romBank;
    int ramBank;
    boolean ramRtcEnabled;
    boolean rtcEnabled;
    int rtcRegister = 0;

    public Cartridge(byte[] rom)
    {
        this.rom = rom;
        this.ram = new byte[0x8000];
        this.romBank = 0;
        this.ramBank = 0;
    }

    public int getRomOffset(int sp) {
        if (sp < 0x4000) {
            return sp;
        }
        return sp + romBank * 0x4000;
    }

    public int getRamOffset(int sp) {
        return sp - 0xA000 + 0x2000 * ramBank;
    }

    @Override
    public byte read(char address)
    {
        if (address < 0x8000) {
            return rom[getRomOffset(address)];
        } else if (ramRtcEnabled && address > 0x9FFF && address < 0xC000){
            if (rtcEnabled) {
                switch (rtcRegister)
                {
                    case 0x08:
                        return 0;
                    case 0x09:
                        return 0;
                    case 0x0A:
                        return 0;
                    case 0x0B:
                        return 0;
                    case 0x0C   :
                        return 0;
                }
            } else {
                return ram[getRamOffset(address)];
            }
        }
        return 0;
    }

    @Override
    public void write(char address, byte value)
    {
        if (address < 0x2000) {
            if (value == 0x0A) {
                ramRtcEnabled = true;
            } else if (value == 0) {
                ramRtcEnabled = false;
            }
        }  else if (address < 0x4000) {
            romBank = value & 0x7f;
        } else if (address < 0x6000) {
            if ((value & 0x03) == value) {
                ramBank = value;
            } else if (value > 0x07 && value < 0x0D){
                rtcRegister = value;
            }
        }

        else if (address > 0x9FFF && address < 0xC000) {
            ram[getRamOffset(address)] = value;
        }
    }
}
