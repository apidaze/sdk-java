package com.apidaze.sdk.client.externalscripts;

import lombok.val;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class URLTest {

    @Test
    public void fromStringShouldReturnUrlInstance_ifUrlStringIsValid() {
        val urlString = "http://some.domain.com";

        val url = URL.fromString(urlString);

        assertThat(url).isNotNull();
        assertThat(url.getValue()).isEqualTo(urlString);
    }

    @Test
    public void fromStringShouldThrowAnException_ifUrlStringIsInvalid() {
        val urlString = "invalid.url";

        assertThatExceptionOfType(InvalidURLException.class)
                .isThrownBy(() -> URL.fromString(urlString))
                .withMessageContaining(urlString);
    }
}
