import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {
    public static void main(String[] args) throws IOException {
        // Create a server on localhost with port 8000
        int port = 80;
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);

        // Create contexts for the web server
        httpServer.createContext("/testfile.html", new MyHttpHandler("testfile.html"));
        httpServer.createContext("/testfile.jpg", new MyHttpHandler("testfile.jpg"));

        // Star the server
        httpServer.setExecutor(null);
        httpServer.start();
        System.out.println("Server is listening on port 80");
    }
}
