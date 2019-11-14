package com.apidaze.sdk.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Value;

@Value
@JacksonXmlRootElement(localName = "conference")
public class Conference implements ApidazeScript.Node {
    String name;
}
