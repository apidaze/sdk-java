package com.apidaze.sdk.client.common;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

import static java.util.Objects.requireNonNull;

/**
 * A value class representing a phone number.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(includeFieldNames = false)
public class PhoneNumber {

    private static final String NUMBER_PATTERN = "^([1-9][0-9]+)$";

    /**
     * A string representation the phone number.
     */
    String number;

    /**
     * Obtains a {@code PhoneNumber} from a text string such as {@code 123456789}
     * <p>The string must start with a positive number and contain only numeric characters.</p>
     *
     * @param number the text string representing a phone number, not null
     * @return an instance of {@code PhoneNumber}
     * @throws InvalidPhoneNumberException if given {@code number} does not match the pattern
     */
    public static PhoneNumber of(String number) {
        requireNonNull(number, "number must not be null");

        if (number.matches(NUMBER_PATTERN)) {
            return new PhoneNumber(number);
        } else {
            throw new InvalidPhoneNumberException(number);
        }
    }

}
