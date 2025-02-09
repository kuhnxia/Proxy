import java.io.*;
import java.net.Socket;

/**
 * Client class for interacting with a proxy server.
 * Author: Kun Xia
 */
public class Client {
    /**
     * The main method for the Client class.
     * Establishes a connection to a proxy server and sends HTTP GET requests.
     *
     * @param args Command-line arguments (not used in this program).
     */
    public static void main(String[] args) {

        try {
            // Connect to the proxy server at localhost:8080
            // You can change the ip address and port to the real host server.
            Socket socket = new Socket("localhost", 8080);

            // Input stream to read from the user.
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Output Stream to send the user-provided URL to the proxy server.
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Input stream to read the response from the proxy server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            while (true){
                System.out.println("\nEnter the URL of the resource you want to retrieve, or 'exit' to stop: \n");
                String resourceURL = userInput.readLine();

                if ("exit".equals(resourceURL)) {
                    break;
                }
                // Send an HTTP GET request to the proxy server.
                out.println("GET " + resourceURL + " HTTP/1.1");

                // Read and print the response from the proxy server
                String responseLine;
                while ((responseLine = in.readLine()) != null){
                    if (responseLine.equals("Response End")) break;
                    System.out.println(responseLine);
                }

            }

            // Close the streams and socket.
            out.close();
            in.close();
            userInput.close();
            socket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
