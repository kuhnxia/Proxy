import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ProxyServer class representing a simple proxy server.
 * Author: Kun Xia
 */
public class ProxyServer {
    /**
     * The main method for the ProxyServer class.
     * Starts the proxy server on a specified port and listens for incoming client connections.
     *
     * @param args Command-line arguments (not used in this program).
     */
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
