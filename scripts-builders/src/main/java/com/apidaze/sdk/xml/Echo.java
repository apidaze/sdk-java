package com.apidaze.sdk.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Duration;

import static java.util.Objects.requireNonNull;


@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Echo implements ApidazeScript.Node {

    @JacksonXmlText
    long delay;

    public static Echo withDelay(Duration delay) {
        requireNonNull(delay, "delay must not be null");
        return new Echo(delay.toMillis());
    }

    @Override
    public String tag() {
        return "echo";
    }
}
