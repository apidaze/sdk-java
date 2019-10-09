package com.apidaze.sdk.client.externalscripts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.validator.routines.UrlValidator;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(includeFieldNames = false)
public class URL {

    @JsonValue
    String value;

    @JsonCreator
    public static URL fromString(String str) {
        if (UrlValidator.getInstance().isValid(str)) {
            return new URL(str);
        } else {
            throw new InvalidURLException(str);
        }
    }
}
