package com.apidaze.sdk.xml.serializers;

import com.apidaze.sdk.xml.ApidazeScript;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ApidazeScriptSerializer extends JsonSerializer<ApidazeScript> {

    @Override
    public void serialize(ApidazeScript script, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeObjectFieldStart("work");

        for (ApidazeScript.Node node : script.getNodes()){
            gen.writeObjectField(node.tag(), node);
        }

        gen.writeEndObject();
        gen.writeEndObject();
    }
}
