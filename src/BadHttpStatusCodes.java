import java.util.HashMap;
import java.util.Map;

public class BadHttpStatusCodes {

    private static final Map<Integer, String> badStatusCodes;

    static {
        badStatusCodes = new HashMap<>();
        badStatusCodes.put(400, "Bad Request");
        badStatusCodes.put(401, "Unauthorized");
        badStatusCodes.put(403, "Forbidden");
        badStatusCodes.put(404, "Not Found");
        badStatusCodes.put(405, "Method Not Allowed");
        badStatusCodes.put(408, "Request Timeout");
        badStatusCodes.put(429, "Too Many Requests");
        badStatusCodes.put(500, "Internal Server Error");
        badStatusCodes.put(502, "Bad Gateway");
        badStatusCodes.put(503, "Service Unavailable");
        badStatusCodes.put(504, "Gateway Timeout");
    }

    public static String getBadStatusMessage(int statusCode) {
        return badStatusCodes.get(statusCode);
    }
}

