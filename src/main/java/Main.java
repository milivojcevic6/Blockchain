import Helper.Crypto;
import Network.*;
import Util.Configuration;
import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Configuration configuration = new Gson().fromJson(new FileReader("config.json"),Configuration.class);
        Crypto crypto = new Crypto();
        NetworkManager networkManager = new NetworkManager(crypto, configuration);
        if(!networkManager.isTrusted()){
            Socket socket = null;
            try {
                socket = new Socket(configuration.trustedAddress,configuration.trustedPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Peer trusted = new Peer(socket,networkManager);
            trusted.start();
            networkManager.getPeerDiscovery().init(trusted);
        }else{
            System.out.println("I'm trusted, very important!");
        }
        try {

            ServerSocket serverSocket = new ServerSocket(configuration.listeningPort);
            System.out.println("Server running. Listening on port : "+configuration.listeningPort);
            while(true) {
                new Peer(serverSocket.accept(), networkManager).start();
            }
        } catch (IOException e) {
            System.out.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
