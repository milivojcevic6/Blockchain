package Protocols;

import Network.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class PeerDiscovery {
    NetworkManager networkManager;
    Gson gson;
    public PeerDiscovery(NetworkManager networkManager){
        this.networkManager = networkManager;
        this.gson = new Gson();
    }

    public void digest(Message message, Peer peer){
        if(message.getHeader().getType() == MessageType.REQUEST){
            PeerInfo peerInfo = new Gson().fromJson(message.getBody(),PeerInfo.class);
            peer.setIp(peerInfo.getIp());
            peer.setPort(peerInfo.getPort());
            peer.setId(peerInfo.getPubKey());
            //pripravi seznam in poslji
            PeerInfo [] peerInfos = networkManager.getKnownNodes().values().toArray(new PeerInfo[
                    networkManager.getKnownNodes().size()]);
            Message reply = networkManager.createMessage(
                    new MessageHeader(Protocol.DISCOVERY,MessageType.RESPONSE),
                    gson.toJson(peerInfos,PeerInfo[].class)
            );
            peer.send(reply);
            System.out.println("Sending peer list to: " + peer.getIp());
            networkManager.getKnownNodes().put(peerInfo.getPubKey(),peerInfo);
            networkManager.getPeerList().put(peerInfo.getPubKey(),peer);
        }else if(message.getHeader().getType() == MessageType.RESPONSE){
            PeerInfo [] peerInfos = gson.fromJson(message.getBody(),PeerInfo[].class);
            Arrays.stream(peerInfos).forEach(candidate->{

                    if(!networkManager.getKnownNodes().containsKey(candidate.getPubKey())){
                        networkManager.getKnownNodes().put(candidate.getPubKey(),candidate);
                        if(networkManager.getConfiguration().maxConnection > networkManager.getPeerList().size()){
                            try {
                                //connect to new peer
                                Socket socket = new Socket(candidate.getIp(),candidate.getPort());
                                Peer newPeer = new Peer(socket, networkManager);
                                newPeer.start();
                                init(newPeer); //initialize discovery protocol
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            });
        }else{
            System.out.println("Unknown message type");
        }
    }

    public void init(Peer peer){
        PeerInfo peerInfo = new PeerInfo(networkManager.getMyIp(),
                networkManager.getConfiguration().listeningPort,
                networkManager.getCrypto().getPublicKey());
        Message message = networkManager.createMessage(new MessageHeader(Protocol.DISCOVERY,MessageType.REQUEST),
                gson.toJson(peerInfo,PeerInfo.class));
        peer.send(message);
    }
}
