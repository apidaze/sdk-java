package com.apidaze.sdk.examples.xml;

import com.sun.net.httpserver.HttpExchange;
import lombok.val;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class HttpHandlerUtil {

    static void writeResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/xml");
        exchange.sendResponseHeaders(200, 0);
        val os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();

        if (Objects.isNull(query)) {
            return result;
        }

        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }

}
