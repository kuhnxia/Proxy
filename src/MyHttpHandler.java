import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyHttpHandler implements HttpHandler {
    private final String filename;
    public MyHttpHandler(String filename) {
        this.filename = filename;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Handling request for: " + exchange.getRequestURI());

        // Get the file path
        Path path = Paths.get(filename);
        // Get the response body stream
        OutputStream outputStream = exchange.getResponseBody();

        // Check if the file exists
        if (Files.exists(path)) {
            // Set the response headers
            if (filename.toLowerCase().endsWith(".jpg")) {
                exchange.getResponseHeaders().set("Content-Type", "image/jpeg");
            } else {
                exchange.getResponseHeaders().set("Content-Type", "text/html");
            }

            // Send 200 ok.
            exchange.sendResponseHeaders(200, 0);

            // Write the file content to the output stream
            Files.copy(path, outputStream);
            System.out.println("Success!");

        } else {
            // If file not exits, send 404 not found response.
            String response = "File not found: " + filename;
            exchange.sendResponseHeaders(404, response.length());
            outputStream.write(response.getBytes());
        }

        // Close the output stream
        outputStream.close();
    }
}
