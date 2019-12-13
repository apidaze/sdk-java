package com.apidaze.sdk.xml;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class Intercept implements ApidazeScript.Node {

    @NonNull
    @JacksonXmlText
    UUID uuid;

    @Override
    public String tag() {
        return "intercept";
    }
}
