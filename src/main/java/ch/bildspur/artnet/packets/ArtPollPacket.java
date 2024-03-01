/*
 * This file is part of artnet4j.
 * 
 * Copyright 2009 Karsten Schmidt (PostSpectacular Ltd.)
 * 
 * artnet4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * artnet4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with artnet4j. If not, see <http://www.gnu.org/licenses/>.
 */

package ch.bildspur.artnet.packets;

public class ArtPollPacket extends ArtNetPacket {

    protected static int MIN_LENGTH = HEADER_LENGTH+2;
    protected static int MAX_LENGTH = MIN_LENGTH+8;

    private boolean replyOnChanges = false;
    private boolean diagnosticsEnabled = false;
    private boolean diagnosticsUnicast = false;
    private int     diagnosticsPriority = 0;
    private boolean vlcTransmissionEnabled = false;
    private boolean targetedMode = false;
    
    // The following is used when targeted mode is enabled
    private int 	targetPortAddressTop = 0;
    private int 	targetPortAddressBottom = 0;
    
    // The following is used to describe the node sending the packet
    private int 	estaManCode = 0;
    private int 	oemCode = 0;

    public ArtPollPacket() {
        this(false);
    }

    public ArtPollPacket(boolean replyOnChanges) {
        super(PacketType.ART_POLL);
        this.replyOnChanges = replyOnChanges;
    }

    public boolean isReplyOnChanges() {
        return replyOnChanges;
    }

    public void setReplyOnChanges(boolean replyOnChanges) {
        this.replyOnChanges = replyOnChanges;
    }

    public boolean isDiagnosticsEnabled() {
        return diagnosticsEnabled;
    }
    
    public boolean isDiagnosticsUnicast() {
        return diagnosticsUnicast;
    }
    
    public int getDiagnosticsPriority() {
        return diagnosticsPriority;
    }

    public void setDiagnostics(boolean enabled, boolean unicast, int priority) {
        this.diagnosticsEnabled = enabled;
        this.diagnosticsUnicast = unicast;
        this.diagnosticsPriority = priority;
    }

    public boolean isVlcTransmissionEnabled() {
        return vlcTransmissionEnabled;
    }
    
    public void setVlcTransmission(boolean enabled) {
        this.vlcTransmissionEnabled = enabled;
    }

    public boolean isTargetedMode() {
        return targetedMode;
    }

    public int getTargetPortAddressTop() {
        return targetPortAddressTop;
    }

    public int getTargetPortAddressBottom() {
        return targetPortAddressBottom;
    }
    
    public void setTargetedMode(boolean enabled, int targetPortAddressTop, int targetPortAddressBottom) {
        this.targetedMode = enabled;
        this.targetPortAddressTop = targetPortAddressTop;
        this.targetPortAddressBottom = targetPortAddressBottom;
    }

    public int getOemCode() {
        return oemCode;
    }

    public int getEstaManCode() {
        return estaManCode;
    }

    public void setPacketSender(int oemCode, int estaManCode) {
        this.oemCode = oemCode;
        this.estaManCode = estaManCode;
    }

    @Override
    public boolean parse(byte[] raw) {
        if (raw.length < MIN_LENGTH) return false;
        setData(raw, MAX_LENGTH); // Set buffer size to MAX_LENGTH to fill extra fields with zeros
        int flags = data.getInt8(12);
        replyOnChanges = 			(flags & (1<<1)) != 0;
        diagnosticsEnabled = 		(flags & (1<<2)) != 0;
        diagnosticsUnicast = 		(flags & (1<<3)) != 0;
        vlcTransmissionEnabled = 	(flags & (1<<4)) != 0;
        targetedMode = 				(flags & (1<<5)) != 0;

        // The following fields are optional
        // However, the buffer is large enough so they are all just null bytes
        
        diagnosticsPriority = data.getInt8(13);
        targetPortAddressTop = data.getInt16(14);
        targetPortAddressBottom = data.getInt16(16);
        estaManCode = data.getInt16(18);
        oemCode = data.getInt16(20);
        
        return true;
    }

    @Override
    public void serializeData() {
        super.serializeData();
        data.setInt8(
            (replyOnChanges ?           (1<<1) : 0) |
            (diagnosticsEnabled ?       (1<<2) : 0) |
            (diagnosticsUnicast ?       (1<<3) : 0) |
            (vlcTransmissionEnabled ?   (1<<4) : 0) |
            (targetedMode ?             (1<<5) : 0),
        12);
        data.setInt8(diagnosticsPriority, 13);
        data.setInt16(targetPortAddressTop, 14);
        data.setInt16(targetPortAddressBottom, 16);
        data.setInt16(estaManCode, 18);
        data.setInt16(oemCode, 20);
    }

    @Override
    public String toString() {
        return type +
            ": reply on changes:" + replyOnChanges +
            " vlc: " + vlcTransmissionEnabled +
            (diagnosticsEnabled ? ((
                " diagnostics: " + (diagnosticsUnicast ? "unicast" : "broadcast") +
                " priority: " + diagnosticsPriority
            )) : "");
    }
}
