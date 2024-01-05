package io.github.coreyforsyth.gbemulator.memory;

public class Oam implements ReadWrite
{
    final ObjectAttribute[] objectAttributes;

	public Oam()
	{
		this.objectAttributes = new ObjectAttribute[40];
        for (int i = 0; i < 40; i++)
        {
            objectAttributes[i] = new ObjectAttribute();
        }
	}

	public int getOffset(int sp) {
		if (sp < 0xFE00 || sp > 0xFE9F) {
			throw new RuntimeException("WorkRam access issue");
		}
		return sp - 0xFE00;
	}

	@Override
	public byte read(char address)
	{
        int offset = getOffset(address);
        ObjectAttribute objectAttribute = objectAttributes[offset / 4];
        int objectAttributeIndex = offset % 4;
        return switch (objectAttributeIndex) {
            case 0 -> objectAttribute.getY();
            case 1 -> objectAttribute.getX();
            case 2 -> objectAttribute.getTileIndex();
            case 3 -> objectAttribute.getAttributes();
            default -> throw new IllegalStateException("Unexpected value: " + objectAttributeIndex);
        };
	}

	@Override
	public void write(char address, byte value)
	{
        int offset = getOffset(address);
        ObjectAttribute objectAttribute = objectAttributes[offset / 4];
        int objectAttributeIndex = offset % 4;
        switch (objectAttributeIndex) {
            case 0 -> objectAttribute.setY(value);
            case 1 -> objectAttribute.setX(value);
            case 2 -> objectAttribute.setTileIndex(value);
            case 3 -> objectAttribute.setAttributes(value);
            default -> throw new IllegalStateException("Unexpected value: " + objectAttributeIndex);
        }
    }

    public ObjectAttribute[] getObjectAttributes()
    {
        return objectAttributes;
    }
}
