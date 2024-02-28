package ch.bildspur.artnet;

import java.util.HashMap;
import java.util.Map;

public class ArtNetBuffer {
    private static final int DEFAULT_CHANNEL_SIZE = 512;

    private final int channelSize;
    private Map<Integer, byte[]> data;

    public ArtNetBuffer()
    {
        this(DEFAULT_CHANNEL_SIZE);
    }

    public ArtNetBuffer(int channelSize)
    {
        this.channelSize = channelSize;
        data = new HashMap<>();
    }

    public byte[] getDmxData(short portAddress)
    {
        int key = portAddress;

        if(!data.containsKey(key))
            data.put(key, new byte[channelSize]);

        return data.get(key);
    }

    public void setDmxData(short portAddress, final byte[] dmxData)
    {
    	int key = portAddress;
        data.put(key, dmxData);
    }

    public void clear()
    {
        data.clear();
    }
}
