package com.apidaze.sdk.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Value;

@Value
public class Conference implements ApidazeScript.Node {

    @JacksonXmlText
    String name;

    @Override
    public String tag() {
        return "conference";
    }
}
