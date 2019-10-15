package com.apidaze.sdk.client.messages;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(includeFieldNames = false)
public class PhoneNumber {

    private static final String NUMBER_PATTERN = "^([1-9][0-9]+)$";

    String number;

    public static PhoneNumber of(String number) {
        requireNonNull(number, "number must not be null");

        if (number.matches(NUMBER_PATTERN)) {
            return new PhoneNumber(number);
        } else {
            throw new InvalidPhoneNumberException(number);
        }
    }

}
