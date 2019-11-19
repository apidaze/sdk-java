package com.apidaze.sdk.xml;

import com.apidaze.sdk.xml.serializers.DialSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.*;

import java.time.Duration;
import java.util.List;

@Value
@Builder
@JsonSerialize(using = DialSerializer.class)
public class Dial implements ApidazeScript.Node {
    Duration timeout;
    Duration maxCallDuration;
    Strategy strategy;
    String action;
    String answerUrl;
    String callerHangupUrl;
    @Singular
    List<Node> nodes;

    @Override
    public String tag() {
        return "dial";
    }

    public interface Node {
        String tag();
    }

    @Value
    public static class Number implements Node {
        @JacksonXmlText
        String value;

        @Override
        public String tag() {
            return "number";
        }
    }

    @Value
    public static class SipAccount implements Node {
        @JacksonXmlText
        String value;

        @Override
        public String tag() {
            return "sipaccount";
        }
    }

    @Value
    public static class SipUri implements Node {
        @JacksonXmlText
        String value;

        @Override
        public String tag() {
            return "sipuri";
        }
    }

    @AllArgsConstructor
    public enum Strategy {
        SIMULTANEOUS("simultaneous"),
        SEQUENCE("sequence");

        @Getter
        private final String value;
    }
}
