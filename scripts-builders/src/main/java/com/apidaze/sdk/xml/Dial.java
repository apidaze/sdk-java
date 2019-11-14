package com.apidaze.sdk.xml;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.time.Duration;
import java.util.List;

@Value
@Builder
@JacksonXmlRootElement(localName = "dial")
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

    interface Node {
        String getTag();

        String getValue();
    }

    @Value
    public static class Number implements Node {
        String value;

        @Override
        public String getTag() {
            return "number";
        }
    }

    @Value
    public static class SipAccount implements Node {
        String value;

        @Override
        public String getTag() {
            return "sipaccount";
        }
    }

    @Value
    public static class SipUri implements Node {
        String value;

        @Override
        public String getTag() {
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
