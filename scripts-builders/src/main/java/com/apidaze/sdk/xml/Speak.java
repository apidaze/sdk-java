package com.apidaze.sdk.xml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.*;

import java.time.Duration;
import java.util.List;

import static java.util.Objects.isNull;

@Value
@Builder
@JacksonXmlRootElement(localName = "speak")
public class Speak implements ApidazeScript.Node {

    @JacksonXmlText
    String text;

    @JacksonXmlProperty(isAttribute = true)
    Lang lang;

    @JsonIgnore
    Duration inputTimeout;

    @Singular
    @JacksonXmlProperty(localName = "bind")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<Bind> binds;

    @JacksonXmlProperty(localName = "input-timeout", isAttribute = true)
    public Long getInputTimeoutMillis() {
        if (isNull(inputTimeout)) {
            return null;
        }
        return inputTimeout.toMillis();
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Lang {
        EN("en-US"),
        FR("fr-FR"),
        IT("it-IT"),
        ES("es-ES");

        @Getter
        @JsonValue
        private final String value;
    }
}
