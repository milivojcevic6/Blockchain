package Network;

public class Message {
    private MessageHeader header;
    private String body;

    public Message(MessageHeader header, String body) {
        this.header = header;
        this.body = body;
    }

    @Override
    public String toString() {
        return "Protocol: " + this.header.getProtocol() + " with body: " + this.body;
    }

    public MessageHeader getHeader() {
        return header;
    }

    public void setHeader(MessageHeader header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
