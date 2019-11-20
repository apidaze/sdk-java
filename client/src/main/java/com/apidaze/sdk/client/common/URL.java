package com.apidaze.sdk.client.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.validator.routines.UrlValidator;

import static java.util.Objects.requireNonNull;

/**
 * A value class representing the URL.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(includeFieldNames = false)
public class URL {

    /**
     * A string representation the URL
     */
    @JsonValue
    String value;

    /**
     * Obtains a {@code URL} from a text string such as {@code http://domain.com}
     * Note that the method checks that the domain is valid.
     *
     * @param text the text string representing an URL address, not null
     * @return an instance of {@code URL}
     * @throws InvalidURLException if the given text is not a valid url address
     */
    @JsonCreator
    public static URL fromString(String text) throws InvalidURLException {
        requireNonNull(text, "text must not be null");

        if (UrlValidator.getInstance().isValid(text)) {
            return new URL(text);
        } else {
            throw new InvalidURLException(text);
        }
    }
}
