package com.apidaze.sdk.xml;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import java.io.IOException;

import static java.util.Objects.isNull;

public class DialSerializer extends JsonSerializer<Dial> {

    @Override
    public void serialize(Dial dial, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        final ToXmlGenerator xmlGenerator = (ToXmlGenerator) gen;
        xmlGenerator.writeStartObject();

        // write attributes
        xmlGenerator.setNextIsAttribute(true);

        if (!isNull(dial.getTimeout())) {
            xmlGenerator.writeObjectField("timeout", dial.getTimeout().getSeconds());
        }
        if (!isNull(dial.getMaxCallDuration())) {
            xmlGenerator.writeObjectField("max-call-duration", dial.getMaxCallDuration().getSeconds());
        }
        if (!isNull(dial.getStrategy())) {
            xmlGenerator.writeStringField("strategy", dial.getStrategy().getValue());
        }
        if (!isNull(dial.getAction())) {
            xmlGenerator.writeStringField("action", dial.getAction());
        }
        if (!isNull(dial.getAnswerUrl())) {
            xmlGenerator.writeStringField("answer-url", dial.getAnswerUrl());
        }
        if (!isNull(dial.getCallerHangupUrl())) {
            xmlGenerator.writeStringField("caller-hangup-url", dial.getCallerHangupUrl());
        }

        // write nodes
        xmlGenerator.setNextIsAttribute(false);
        for (Dial.Node node : dial.getNodes()) {
            xmlGenerator.writeObjectField(node.getTag(), node.getValue());
        }

        xmlGenerator.writeEndObject();
    }
}
