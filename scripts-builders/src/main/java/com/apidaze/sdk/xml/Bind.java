package com.apidaze.sdk.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Value;

@Value
public class Bind implements ApidazeScript.Node {

    @JacksonXmlProperty(isAttribute = true)
    String action;

    @JacksonXmlText
    String value;
}
