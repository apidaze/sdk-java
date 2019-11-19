package com.apidaze.sdk.xml;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class Intercept implements ApidazeScript.Node {

    @JacksonXmlText
    UUID uuid;

    @Override
    public String tag() {
        return "intercept";
    }
}
