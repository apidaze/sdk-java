package com.apidaze.sdk.examples.xml;


import com.apidaze.sdk.xml.ApidazeScript;
import com.apidaze.sdk.xml.Dial;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;

import static com.apidaze.sdk.examples.xml.HttpHandlerUtil.writeResponse;

public class FindMeOrFollowMeExample implements HttpHandler {

    private static final String FIRST_NUMBER = "123456788";
    private static final String SECOND_NUMBER = "123456799";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost",8080), 0);
        server.createContext("/", new FindMeOrFollowMeExample());
        server.start();

        System.out.println("Server is running at " + server.getAddress());
    }

    private String response() throws JsonProcessingException {
        return ApidazeScript.builder()
                .node(Dial.builder()
                        .timeout(Duration.ofSeconds(12))
                        .node(new Dial.Number(FIRST_NUMBER))
                        .build())
                .node(Dial.builder()
                        .node(new Dial.Number(SECOND_NUMBER))
                        .build())
                .build()
                .toXmlWithPrettyPrinter();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        writeResponse(httpExchange, response());
    }
}
