package Network;

public class PeerInfo {
    String ip;
    int port;
    String pubKey;

    public PeerInfo(String ip, int port, String pubKey) {
        this.ip = ip;
        this.port = port;
        this.pubKey = pubKey;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }
}
