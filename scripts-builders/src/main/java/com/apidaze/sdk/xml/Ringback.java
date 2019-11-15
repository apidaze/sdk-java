package com.apidaze.sdk.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(staticName = "defaultTone")
@AllArgsConstructor(staticName = "fromFile")
public class Ringback implements ApidazeScript.Node {

    @JacksonXmlText
    private String url;

    @Override
    public String tag() {
        return "ringback";
    }
}
