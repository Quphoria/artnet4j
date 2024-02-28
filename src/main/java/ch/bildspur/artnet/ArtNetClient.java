package ch.bildspur.artnet;

import ch.bildspur.artnet.events.ArtNetServerEventAdapter;
import ch.bildspur.artnet.packets.ArtDmxPacket;
import ch.bildspur.artnet.packets.ArtNetPacket;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static ch.bildspur.artnet.packets.PacketType.ART_DMX;

public class ArtNetClient {
    private ArtNetServer server;

    private int sequenceId = 0;
    private boolean isRunning = false;

    private ArtNetBuffer inputBuffer;

    /**
     * Creates a new ArtNetClient.
     */
    public ArtNetClient() {
        this(new ArtNetBuffer());
    }

    /**
     * Creates a new ArtNetClient.
     * @param inputBuffer Input buffer implementation. If null, no data is received.
     */
    public ArtNetClient(ArtNetBuffer inputBuffer) {
        this(inputBuffer, ArtNetServer.DEFAULT_PORT, ArtNetServer.DEFAULT_PORT);
    }

    /**
     * Creates a new ArtNetClient.
     * @param inputBuffer Input buffer implementation. If null, no data is received.
     * @param serverPort UDP server port where the data is read from.
     * @param clientPort UDP client port where the data is sent to.
     */
    public ArtNetClient(ArtNetBuffer inputBuffer, int serverPort, int clientPort) {
        // init input buffer
        this.inputBuffer = inputBuffer;
        server = new ArtNetServer(serverPort, clientPort);
    }

    /**
     * Start client with default arguments (listen on broadcast).
     */
    public void start() {
        // use default network interface
        this.start((InetAddress)null);
    }

    /**
     * Start client with specific network interface address.
     * @param networkInterfaceAddress Network interface address to listen to.
     */
    public void start(String networkInterfaceAddress)
    {
        try {
            this.start(InetAddress.getByName(networkInterfaceAddress));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start client with specific network interface address.
     * @param networkInterfaceAddress Network interface address to listen to.
     */
    public void start(InetAddress networkInterfaceAddress) {
        if (isRunning)
            return;

        // reset buffer if present
        if (inputBuffer != null)
            inputBuffer.clear();

        try {
            server.addListener(
                    new ArtNetServerEventAdapter() {
                        @Override
                        public void artNetPacketReceived(ArtNetPacket packet) {
                            onPacketReceived(packet);
                        }
                    });

            server.start(networkInterfaceAddress);

            isRunning = true;
        } catch (SocketException | ArtNetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the client and udp server.
     */
    public void stop() {
        if (!isRunning)
            return;

        server.stop();

        isRunning = false;
    }

    /**
     * Send a dmx package as broadcast package.
     * @param portAddress Receiving port address.
     * @param dmxData Dmx data to send.
     */
    public void broadcastDmx(int portAddress, byte[] dmxData) {
        server.broadcastPacket(createDmxPacket(portAddress, dmxData));
    }

    /**
     * Send a dmx package to a specific unicast address.
     * @param address Receiver address.
     * @param portAddress Receiving port address.
     * @param dmxData Dmx data to send.
     */
    public void unicastDmx(String address, int portAddress, byte[] dmxData) {
        try {
            this.unicastDmx(InetAddress.getByName(address), portAddress, dmxData);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a dmx package to a specific unicast address.
     * @param node Receiver node.
     * @param portAddress Receiving port address.
     * @param dmxData Dmx data to send.
     */
    public void unicastDmx(ArtNetNode node, int portAddress, byte[] dmxData) {
        server.unicastPacket(createDmxPacket(portAddress, dmxData), node.getIPAddress());
    }

    /**
     * Send a dmx package to a specific unicast address.
     * @param address Receiver address.
     * @param portAddress Receiving port address.
     * @param dmxData Dmx data to send.
     */
    public void unicastDmx(InetAddress address, int portAddress, byte[] dmxData) {
        server.unicastPacket(createDmxPacket(portAddress, dmxData), address);
    }

    private ArtDmxPacket createDmxPacket(int portAddress, byte[] dmxData) {
        ArtDmxPacket dmx = new ArtDmxPacket();

        dmx.setPortAddress(portAddress);
        dmx.setSequenceID(++sequenceId);
        dmx.setDMX(dmxData, dmxData.length);

        sequenceId %= 256;

        return dmx;
    }

    private void onPacketReceived(final ArtNetPacket packet) {
        // only store input data if buffer is created
        if (inputBuffer == null)
            return;

        if (packet.getType() != ART_DMX)
            return;

        ArtDmxPacket dmxPacket = (ArtDmxPacket) packet;
        short portAddress = (short) dmxPacket.getPortAddress();

        inputBuffer.setDmxData(portAddress, dmxPacket.getDmxData());
    }

    /**
     * Read dmx data from the buffer implementation.
     * @param portAddress Port address to read from.
     * @return Dmx data array.
     */
    public byte[] readDmxData(short portAddress) {
        return readDmxData(portAddress);
    }

    /**
     * Read dmx data from the buffer implementation.
     * @param portAddress Port address to read from.
     * @return Dmx data array.
     */
    public byte[] readDmxData(int portAddress) {
        return inputBuffer.getDmxData((short) portAddress);
    }

    public ArtNetServer getArtNetServer() {
        return server;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public ArtNetBuffer getInputBuffer() {
        return inputBuffer;
    }

    public void setInputBuffer(ArtNetBuffer inputBuffer) {
        this.inputBuffer = inputBuffer;
    }
}
