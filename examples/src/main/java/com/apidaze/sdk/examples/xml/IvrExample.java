package com.apidaze.sdk.examples.xml;


import com.apidaze.sdk.xml.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.val;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;

import static com.apidaze.sdk.examples.xml.HttpHandlerUtil.queryToMap;
import static com.apidaze.sdk.examples.xml.HttpHandlerUtil.writeResponse;

public class IvrExample {

    static final String STEP_1_PATH = "/step1";
    static final String STEP_2_PATH = "/step2";
    static final String STEP_3_PATH = "/step3";
    static final String PLAYBACK_PATH = "/apidazeintro.wav";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost",8080), 0);
        server.createContext("/", new Intro());
        server.createContext(STEP_1_PATH, new Step1());
        server.createContext(STEP_2_PATH, new Step2());
        server.createContext(STEP_3_PATH, new Step3());
        server.createContext(PLAYBACK_PATH, new Playback());
        server.start();

        System.out.println("Server is running at " + server.getAddress());
    }

    static class Intro implements HttpHandler {

        private String response(String localUrl) throws JsonProcessingException {
            return ApidazeScript.builder()
                    .node(Ringback.defaultTone())
                    .node(new Wait(Duration.ofSeconds(6)))
                    .node(new Answer())
                    .node(Record.builder().name("example_recording").build())
                    .node(new Wait(Duration.ofSeconds(2)))
                    .node(com.apidaze.sdk.xml.Playback.fromFile(localUrl + PLAYBACK_PATH))
                    .node(Speak.builder()
                            .text("This example script will show you some things you can do with our API")
                            .build())
                    .node(new Wait(Duration.ofSeconds(2)))
                    .node(Speak.builder()
                            .text("Press 1 for an example of text to speech, press 2 to enter an echo line to check voice latency or press 3 to enter a conference.")
                            .bind(Bind.builder().value("1").action(localUrl + STEP_1_PATH).build())
                            .bind(Bind.builder().value("2").action(localUrl + STEP_2_PATH).build())
                            .bind(Bind.builder().value("3").action(localUrl + STEP_3_PATH).build())
                            .build())
                    .build()
                    .toXmlWithPrettyPrinter();
        }


        @Override
        public void handle(HttpExchange exchange) throws IOException {
            val params = queryToMap(exchange.getRequestURI().getQuery());
            val localUrl = params.getOrDefault("url", "http:/" + exchange.getLocalAddress().toString());
            writeResponse(exchange, response(localUrl));
        }
    }

    static class Step1 implements HttpHandler {

        private String response() throws JsonProcessingException {
            return ApidazeScript.builder()
                    .node(Speak.builder()
                            .text("Our text to speech leverages Google's cloud APIs to offer the best possible solution")
                            .build())
                    .node(new Wait(Duration.ofSeconds(1)))
                    .node(Speak.builder()
                            .lang(Speak.Lang.ENGLISH_AUSTRALIA)
                            .voice(Speak.Voice.MALE_A)
                            .text("A wide variety of voices and languages are available.  Here are just a few")
                            .build())
                    .node(new Wait(Duration.ofSeconds(1)))
                    .node(Speak.builder()
                            .lang(Speak.Lang.FRENCH_FRANCE)
                            .text("Je peux parler français")
                            .build())
                    .node(new Wait(Duration.ofSeconds(1)))
                    .node(Speak.builder()
                            .lang(Speak.Lang.GERMAN)
                            .text("Auch deutsch")
                            .build())
                    .node(new Wait(Duration.ofSeconds(1)))
                    .node(Speak.builder()
                            .lang(Speak.Lang.JAPANESE)
                            .text("そして日本人ですら")
                            .build())
                    .node(new Wait(Duration.ofSeconds(1)))
                    .node(Speak.builder()
                            .text("You can see our documentation for a full list of supported languages and voices for them.  We'll take you back to the menu for now.")
                            .build())
                    .node(new Wait(Duration.ofSeconds(2)))
                    .build()
                    .toXmlWithPrettyPrinter();
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            writeResponse(httpExchange, response());
        }
    }

    static class Step2 implements HttpHandler {

        private String response() throws JsonProcessingException {
            return ApidazeScript.builder()
                    .node(Speak.builder().text("You will now be joined to an echo line.").build())
                    .node(Echo.withDelay(Duration.ofMillis(500)))
                    .build()
                    .toXmlWithPrettyPrinter();
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            writeResponse(httpExchange, response());
        }
    }

    static class Step3 implements HttpHandler {

        private String response() throws JsonProcessingException {
            return ApidazeScript.builder()
                    .node(Speak.builder()
                            .text("You will be entered into a conference call now.  You can initiate more calls to join participants or hangup to leave")
                            .build())
                    .node(new Conference("SDKTestConference"))
                    .build()
                    .toXmlWithPrettyPrinter();
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            writeResponse(httpExchange, response());
        }
    }

    static class Playback implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Content-Type", "audio/wav");
            exchange.sendResponseHeaders(200, 0);

            val is = new FileInputStream("examples/src/main/resources/apidazeintro.wav");
            val os = exchange.getResponseBody();
            while (is.available() > 0) {
                os.write(is.read());
            }
            os.close();
            is.close();
        }
    }

}
