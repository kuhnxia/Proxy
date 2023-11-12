import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyServer {
    public static void main(String[] args) {
        int proxyPort = 8080;
        ServerSocket serverSocket;
        Socket clientSocket;
        ProxyThread proxyThread;

        try {
            serverSocket = new ServerSocket(proxyPort);
            System.out.println("Proxy server is running on port " + proxyPort);

            while (true){
                clientSocket = serverSocket.accept();
                proxyThread = new ProxyThread(clientSocket);
                proxyThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
