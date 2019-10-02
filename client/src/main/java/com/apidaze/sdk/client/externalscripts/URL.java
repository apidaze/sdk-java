package com.apidaze.sdk.client.externalscripts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.ToString;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.Objects;

@ToString(includeFieldNames = false)
public class URL {

    @JsonValue private final String value;

    private URL(String value) {
        this.value = value;
    }

    @JsonCreator
    public static URL fromString(String str) {
        if (UrlValidator.getInstance().isValid(str)) {
            return new URL(str);
        } else {
            throw new InvalidURLException(str);
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URL url = (URL) o;
        return Objects.equals(value, url.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
