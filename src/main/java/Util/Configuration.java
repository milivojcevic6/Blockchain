package Util;

public class Configuration {
    public final String trustedAddress;
    public final int trustedPort;
    public final int listeningPort;
    public final int maxConnection;

    public Configuration(String trustedAddress, int trustedPort, int listeningPort, int maxConnection) {
        this.trustedAddress = trustedAddress;
        this.trustedPort = trustedPort;
        this.listeningPort = listeningPort;
        this.maxConnection = maxConnection;
    }
}
