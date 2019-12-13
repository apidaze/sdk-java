package com.apidaze.sdk.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.NonNull;
import lombok.Value;

@Value
public class Conference implements ApidazeScript.Node {

    @NonNull
    @JacksonXmlText
    String name;

    @Override
    public String tag() {
        return "conference";
    }
}
