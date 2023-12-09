package io.github.coreyforsyth.gbemulator;

public class Clock
{

    private final static int LY = 154;
    private final static int OAM = 80;
    private final static int DRAWING = 172;
    private final static int HBLANK = 204;
    private final static int LX = OAM + DRAWING + HBLANK;
    private final static int FRAME = LY * LX;


    private static int currentDot = 0;



    private boolean go;

    public void start(CPU cpu) {
        while (go) {
            loop(cpu);
        }
    }

    private void loop(CPU cpu)
    {
        currentDot++;
        if (currentDot > FRAME) {
            currentDot = 0;
        }
        cpu.writeByte((char) 0xff44, getCurrentLX());

    }

    private byte getCurrentLX() {
        return (byte) (currentDot / LY);
    }

    public void setGo(boolean go)
    {
        this.go = go;
    }
}
