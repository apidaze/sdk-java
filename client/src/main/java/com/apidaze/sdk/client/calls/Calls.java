package com.apidaze.sdk.client.calls;

import com.apidaze.sdk.client.messages.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Optional;

public interface Calls {

    @AllArgsConstructor
    enum Type {
        NUMBER("number"),
        SIP_ACCOUNT("sipaccount");

        @Getter
        private final String value;
    }

    @Data
    class CreateResponse {
        @JsonProperty("ok")
        private Optional<String> callId = Optional.empty();

        private Optional<String> failure = Optional.empty();
    }

    /**
     * Place a call
     *
     * @param callerId    The phone number to present as the callerid (country code included, no + sign). Example: 15558675309
     * @param origin      The phone number or SIP account to ring first.
     * @param destination The destination passed as a parameter to your External Script URL.
     * @param type        The type of the terminal to ring first.
     * @return {@link CreateResponse} with {@code callId} containing id of initiated call in case of success, otherwise the field {@code failure} is present containing the failure reason.
     */
    CreateResponse create(PhoneNumber callerId, String origin, String destination, Type type);
}
