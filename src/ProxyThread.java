
import java.io.*;
import java.net.*;
import javax.net.ssl.SSLHandshakeException;

public class ProxyThread extends Thread{
    private Socket clientSocket;
    private BufferedReader clientReader;
    private PrintWriter clientOut;

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
                HttpURLConnection serverConnection = connectionToServer(url);

                // Forward server response to the client
                if (serverConnection != null)
                    forwardResponseToClient(serverConnection);

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

    private HttpURLConnection connectionToServer(String url) throws IOException {
        // Check if the URL starts with "http://" or "https://" or "ftp://"
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("ftp://")) {
            System.out.println("\nError: The URL must include the protocol (http or https or ftp).");
            clientOut.println("\nError: The URL must include the protocol (http or https or ftp).");
            System.out.println("\nResponse End");
            return null;
        }

        URL serverURL = new URL(url);
        HttpURLConnection serverConnection = (HttpURLConnection) serverURL.openConnection();
        serverConnection.setRequestMethod("GET");
        return serverConnection;
    }

    private void forwardResponseToClient(HttpURLConnection serverConnection) throws IOException {
        try {
            // Read and forward the Http status code
            int responseCode = serverConnection.getResponseCode();
            // Get the HTTP status message (description)
            String statusMessage = serverConnection.getResponseMessage();
            clientOut.println("\nStatus : " + responseCode +" " + statusMessage);


            // Read and forward the response headers
            clientOut.println("\nBelow is the response headers: \n");
            for (String headKey: serverConnection.getHeaderFields().keySet()) {
                clientOut.println(headKey + ": " + serverConnection.getHeaderField(headKey));
            }

            // Handle for bad response.
            Boolean badResponse = responseCode >= 400 && responseCode < 600;
            if (!badResponse){
                // Read and forward the response body
                BufferedReader bodyReader =
                        new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));
                clientOut.println("\nBelow is the response body: \n");
                String line;
                while (true){
                    line = bodyReader.readLine();
                    if (line == null) break;
                    clientOut.println(line);

                }
                bodyReader.close();
            } else {
                clientOut.println();
                clientOut.println(statusMessage + ": No body content available");
            }

        } catch (SocketException e) {
            System.out.println("\nError reading response from the server: " + e.getMessage());
            clientOut.println("\nError reading response from the server: " + e.getMessage());
        }
        catch (SSLHandshakeException sslHandshakeException) {
                System.out.println("\nSSL Handshake failed: " + sslHandshakeException.getMessage());
                clientOut.println("\nSSL Handshake failed: " + sslHandshakeException.getMessage());
        }

        System.out.println("\nResponse End");
    }


}
