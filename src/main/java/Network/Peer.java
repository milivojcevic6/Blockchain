package Network;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Peer extends Thread{
    private Socket socket;
    private String ip;
    private int port;
    private String id;
    private PrintWriter out;
    private BufferedReader in;
    private Gson gson;
    private NetworkManager networkManager;

    public Peer(Socket socket, NetworkManager networkManager){
        this.socket = socket;
        this.gson = new Gson();
        this.networkManager = networkManager;
        try {
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        System.out.println("Handling peer: " + socket.getInetAddress() +" : " + socket.getPort());
        String payload;
        try {
            while((payload = in.readLine()) !=null) {
                //TODO: Verify JSON before parsing
                Message message = gson.fromJson(payload, Message.class);
                System.out.println("Message recieved");
                networkManager.getPeerList().putIfAbsent(message.getHeader().getPublicKey(),this);
                if(networkManager.verifyMessage(message)) {
                   networkManager.enqueue(message);
                    System.out.println("Message enqueueed");
                }else{
                    System.out.println("Message verification failed!");
                    disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void send(Message messages){
        System.out.println("Sending message: ");
        out.println(gson.toJson(messages));
        System.out.println(gson.toJson(messages));
        out.flush();
        System.out.println("Message sent");
    }

    public void disconnect(){
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void setId(String id) {
        this.id = id;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }
}
