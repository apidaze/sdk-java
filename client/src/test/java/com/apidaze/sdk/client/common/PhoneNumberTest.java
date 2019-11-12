package com.apidaze.sdk.client.common;

import lombok.val;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class PhoneNumberTest {

    @Test
    public void ofShouldCreatePhoneNumberInstance_ifNumberIsValid() {
        val number = "123456789";

        val url = PhoneNumber.of(number);

        assertThat(url).isNotNull();
        assertThat(url.getNumber()).isEqualTo(number);
    }

    @Test
    public void ofShouldThrowAnException_ifNumberStartsWithZero() {
        val number = "012345678";

        assertThatExceptionOfType(InvalidPhoneNumberException.class)
                .isThrownBy(() -> PhoneNumber.of(number))
                .withMessageContaining(number);
    }

    @Test
    public void ofShouldThrowAnException_ifNumberContainsIllegalCharacter() {
        val number = "1234xxx5678";

        assertThatExceptionOfType(InvalidPhoneNumberException.class)
                .isThrownBy(() -> PhoneNumber.of(number))
                .withMessageContaining(number);
    }
}
