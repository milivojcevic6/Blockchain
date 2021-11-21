package Network;

public class MessageHeader {
    private long timestamp;
    private String contentType;
    private Protocol protocol;
    private String signature;
    private String publicKey;
    private MessageType type;

    public MessageHeader(long timestamp, String contentType, Protocol protocol, MessageType type) {
        this.timestamp = timestamp;
        this.contentType = contentType;
        this.protocol = protocol;
        this.type = type;
    }

    public MessageHeader(Protocol protocol, MessageType type) {
        this.timestamp = System.currentTimeMillis();
        this.type = type;
        this.contentType = "JSON";
        this.protocol = protocol;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getContentType() {
        return contentType;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
