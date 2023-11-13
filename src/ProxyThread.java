
import java.io.*;
import java.net.*;

public class ProxyThread extends Thread{
    private Socket clientSocket;
    private BufferedReader clientReader;
    private PrintWriter clientOut;
    private URLConnection serverConnection;

    public ProxyThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

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

    private String extractURL(String request) {
        String[] strings = request.split(" ");
        if (strings.length > 2) return strings[1];
        return null;
    }

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

    private void forwardResponseToClient() throws IOException {
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
