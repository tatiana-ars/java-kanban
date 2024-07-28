import com.sun.net.httpserver.HttpExchange;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.OutputStream;

public class BaseHttpHandler {

    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        sendText(exchange, "{\"error\":\"Ресурс не найден\"}", 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        sendText(exchange, "{\"error\":\"Задача пересекается с существующими\"}", 406);
    }

    protected void sendInternalError(HttpExchange exchange) throws IOException {
        sendText(exchange, "{\"error\":\"Internal server error\"}", 500);
    }
}
