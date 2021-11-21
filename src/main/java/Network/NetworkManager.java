package Network;

import Helper.Crypto;
import Protocols.PeerDiscovery;
import Util.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class NetworkManager {
    private Crypto crypto;
    private BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(1024);
    private HashMap<String, Peer> peerList;
    private HashMap<String, PeerInfo> knownNodes;
    private PeerDiscovery peerDiscovery;
    private Configuration configuration;
    private String myIp;
    private boolean isTrusted = false;

    public NetworkManager(Crypto crypto, Configuration configuration){
        this.crypto = crypto;
        this.configuration = configuration;
        peerList = new HashMap<>();
        knownNodes = new HashMap<>();
        try {
            this.myIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if(myIp.equals(configuration.trustedAddress)) this.isTrusted = true; //are we trusted
        this.peerDiscovery = new PeerDiscovery(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Booting MQ worker thread...");
                while(true) {
                    try {
                        Message message = messageQueue.take();
                        System.out.println("Processing: " +message);
                        switch (message.getHeader().getProtocol()){
                            case DISCOVERY:
                                peerDiscovery.digest(message, peerList.get(message.getHeader().getPublicKey()));
                                break;
                            case PING:
                                break;
                            default:
                                System.out.println("Error: protocol violation"); //disconnect
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public Message createMessage(MessageHeader header, String body){
        header.setSignature(crypto.sign(body+header.getTimestamp()+header.getProtocol()+header.getContentType()));
        header.setPublicKey(crypto.getPublicKey());
        return  new Message(header,body);
    }

    public boolean verifyMessage(Message message){
        return crypto.verify(message.getBody()+
                message.getHeader().getTimestamp() +
                message.getHeader().getProtocol() +
                message.getHeader().getContentType(),
                message.getHeader().getSignature(), //signature
                message.getHeader().getPublicKey()  //publicKey
        );
    }

    public void enqueue(Message message){
        this.messageQueue.add(message);
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public HashMap<String, Peer> getPeerList() {
        return peerList;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getMyIp() {
        return myIp;
    }

    public boolean isTrusted() {
        return isTrusted;
    }

    public HashMap<String, PeerInfo> getKnownNodes() {
        return knownNodes;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    public BlockingQueue<Message> getMessageQueue() {
        return messageQueue;
    }

    public void setMessageQueue(BlockingQueue<Message> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void setPeerList(HashMap<String, Peer> peerList) {
        this.peerList = peerList;
    }

    public void setKnownNodes(HashMap<String, PeerInfo> knownNodes) {
        this.knownNodes = knownNodes;
    }

    public PeerDiscovery getPeerDiscovery() {
        return peerDiscovery;
    }

    public void setPeerDiscovery(PeerDiscovery peerDiscovery) {
        this.peerDiscovery = peerDiscovery;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setMyIp(String myIp) {
        this.myIp = myIp;
    }

    public void setTrusted(boolean trusted) {
        isTrusted = trusted;
    }
}
