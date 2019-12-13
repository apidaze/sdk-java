package com.apidaze.sdk.xml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.*;

import java.time.Duration;
import java.util.List;

import static java.util.Objects.isNull;

@Value
@Builder
public class Speak implements ApidazeScript.Node {

    @JacksonXmlText
    String text;

    @JacksonXmlProperty(isAttribute = true)
    Lang lang;

    @JacksonXmlProperty(isAttribute = true)
    Voice voice;

    @JsonIgnore
    Duration inputTimeout;

    @Singular
    @JacksonXmlProperty(localName = "bind")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<Bind> binds;

    public static Speak withText(String text) {
        return Speak.builder().text(text).build();
    }

    @JacksonXmlProperty(localName = "input-timeout", isAttribute = true)
    public Long getInputTimeoutMillis() {
        return isNull(inputTimeout) ? null : inputTimeout.toMillis();
    }

    @Override
    public String tag() {
        return "speak";
    }

    @AllArgsConstructor
    public enum Lang {
        DANISH("da-DK"),
        DUTCH("nl-NL"),
        ITALIAN("it-IT"),
        JAPANESE("ja-JP"),
        NORWEGIAN("nb-NO"),
        PORTUGESE_BRAZIL("pt-BR"),
        PORTUGESE_PORTUGAL("pt-PT"),
        SLOVAK("sk-SK"),
        SPANISH("es-ES"),
        SWEDISH("sv-SE"),
        UKRANIAN("uk-UA"),
        ENGLISH_AUSTRALIA("en-AU"),
        ENGLISH_UK("en-GB"),
        ENGLISH_US("en-US"),
        FRENCH_CANADA("fr-CA"),
        FRENCH_FRANCE("fr-FR"),
        GERMAN("de-DE"),
        KOREAN("ko-KR"),
        POLISH("pl-PL"),
        RUSSIAN("ru-RU"),
        TURKISH("tr-TR");

        @Getter
        @JsonValue
        private final String value;
    }

    @AllArgsConstructor
    public enum Voice {
        FEMALE_A("female-a"),
        FEMALE_B("female-b"),
        FEMALE_C("female-c"),
        MALE_A("male-a"),
        MALE_B("male-b"),
        MALE_C("male-c");

        @Getter
        @JsonValue
        private final String value;
    }
}
