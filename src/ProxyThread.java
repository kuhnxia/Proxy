import java.io.*;
import java.net.*;

/**
 * ProxyThread class representing a thread for handling communication
 * between the client and the server.
 * Author: Kun Xia
 */
public class ProxyThread extends Thread{
    private Socket clientSocket;
    private BufferedReader clientReader;
    private PrintWriter clientOut;
    private URLConnection serverConnection;

    /**
     * Constructor for ProxyThread class.
     *
     * @param clientSocket The client socket connected to the proxy.
     */
    public ProxyThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * The run method for the ProxyThread class.
     * Handles communication between the client and the server.
     */
    @Override
    public void run() {

        try {

            clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientOut = new PrintWriter(clientSocket.getOutputStream(), true);

            while (true) {
                System.out.println("\nThe proxy is waiting for client's input: ");
                // Read the client's request
                String request = clientReader.readLine();

                if (request == null) break;

                // Extract the request URL
                String url = extractURL(request);
                System.out.println("\nThe request URL is: " + url);

                //Get connection to the server.
                serverConnection = connectionToServer(url);

                // Forward server response to the client
                if (serverConnection != null)
                    forwardResponseToClient();

                // Indicate the end of the Response
                clientOut.println("Response End");
            }

            clientOut.close();
            clientReader.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extracts the URL from the client's request.
     *
     * @param request The client's request string.
     * @return The extracted URL.
     */
    private String extractURL(String request) {
        String[] strings = request.split(" ");
        if (strings.length > 2) return strings[1];
        return null;
    }

    /**
     * Establishes a connection to the server based on the provided URL.
     *
     * @param url The URL to connect to.
     * @return The URLConnection object representing the connection to the server.
     * @throws IOException If an I/O error occurs while establishing the connection.
     */
    private URLConnection connectionToServer(String url) throws IOException {
        // Check if the URL starts with "http://" or "https://" or "ftp://".
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("ftp://")) {
            System.out.println("\nError: The URL must include the protocol (http or https or ftp).");
            clientOut.println("\nError: The URL must include the protocol (http or https or ftp).");
            return null;
        }

        URL serverURL = new URL(url);
        URLConnection serverConnection = serverURL.openConnection();

        return serverConnection;
    }

    /**
     * Forwards the server's response headers and body to the connected client.
     * This method reads the response headers from the server connection,
     * writes them to the client output stream, and then reads and writes
     * the response body. If an error occurs during the process, appropriate
     * error messages are printed to the console and sent to the client.
     *
     * @throws IOException If an I/O error occurs while reading from the server
     *                     or writing to the client.
     * @throws FileNotFoundException If the requested resource on the server is not found.
     * @throws Exception If any other unexpected exception occurs during the forwarding process.
     */
    private void forwardResponseToClient() {
        try {

            // Read and forward the response headers
            for (String headKey: serverConnection.getHeaderFields().keySet()) {
                clientOut.println(headKey + ": " + serverConnection.getHeaderField(headKey));
            }

            // Read and forward the response body
            BufferedReader bodyReader =
                    new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));
            String line;
            while (true){
                line = bodyReader.readLine();
                if (line == null) break;
                clientOut.println(line);

            }
            bodyReader.close();

        }
        catch (FileNotFoundException e){
            System.out.println("\nFile not found: " + e.getMessage());
            clientOut.println("\nFile not found: " + e.getMessage());
        }
        catch (IOException e){
            System.out.println("\nIO Exception: " + e.getMessage());
            clientOut.println("\nIO Exception: " + e.getMessage());
        }
        catch (Exception e){
            System.out.println("\nException: " + e.getMessage());
            clientOut.println("\nException: " + e.getMessage());
        }
    }

}
