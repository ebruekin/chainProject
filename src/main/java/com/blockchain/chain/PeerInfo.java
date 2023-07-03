package com.blockchain.chain;

import java.util.Objects;

public class PeerInfo {
    private String ipAddress;
    private int port;

    private String peerId;

    public PeerInfo(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.peerId = generatePeerId(ipAddress, port);
    }

    // Creates unique hash according to the ip/port combination
    private String generatePeerId(String ipAddress, int port) {

        String combinedString = ipAddress + ":" + port;
        int hashCode = combinedString.hashCode();
        return String.valueOf(hashCode);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String getPeerId() {
        return peerId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PeerInfo peerInfo = (PeerInfo) obj;
        return port == peerInfo.port && ipAddress.equals(peerInfo.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, port);
    }

    @Override
    public String toString() {
        return "PeerInfo{" +
                "ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                '}';
    }
}