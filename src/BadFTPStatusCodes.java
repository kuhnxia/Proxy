import java.util.HashMap;
import java.util.Map;

public class BadFTPStatusCodes {

    // Define a map to store FTP response codes and their descriptions
    private static final Map<Integer, String> badStatusCodes;

    static {
        badStatusCodes = new HashMap<>();

        // 400 Series (Transient Negative Completion)
        badStatusCodes.put(421, "Service not available, closing control connection.");
        badStatusCodes.put(425, "Can't open data connection.");
        badStatusCodes.put(426, "Connection closed; transfer aborted.");
        badStatusCodes.put(450, "Requested file action not taken. File unavailable (e.g., file busy).");
        badStatusCodes.put(451, "Requested action aborted. Local error in processing.");
        badStatusCodes.put(452, "Requested action not taken. Insufficient storage space in system.");

        // 500 Series (Permanent Negative Completion)
        badStatusCodes.put(500, "Syntax error, command unrecognized. This may include errors such as command line too long.");
        badStatusCodes.put(501, "Syntax error in parameters or arguments.");
        badStatusCodes.put(502, "Command not implemented.");
        badStatusCodes.put(503, "Bad sequence of commands.");
        badStatusCodes.put(504, "Command not implemented for that parameter.");
        badStatusCodes.put(530, "Not logged in.");
        badStatusCodes.put(532, "Need account for storing files.");
        badStatusCodes.put(550, "Requested action not taken. File not found or no access.");
        badStatusCodes.put(552, "Requested file action aborted. Exceeded storage allocation.");
        badStatusCodes.put(553, "Requested action not taken. File name not allowed.");
    }

    // Get the description associated with a response code
    public static String getBadStatusMessage(int responseCode) {
        return badStatusCodes.get(responseCode);
    }
}
