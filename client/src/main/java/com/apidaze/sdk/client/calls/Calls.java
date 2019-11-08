package com.apidaze.sdk.client.calls;

import com.apidaze.sdk.client.common.PhoneNumber;
import com.apidaze.sdk.client.http.HttpResponseException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Calls {

    /**
     * Place a call to the phone number or SIP account.
     *
     * @param callerId    The phone number to present as the callerId (country code included, no + sign). Example: 15558675309
     * @param origin      The phone number or SIP account to ring first.
     * @param destination The destination passed as a parameter to your External Script URL.
     * @param callType    The type of the terminal to ring first. See possible values {@link CallType}
     *
     * @return id of created call.
     *
     * @throws IOException             if the request could not be executed due to a connectivity problem or timeout.
     * @throws HttpResponseException   if REST API returned an unhappy HTTP response code like 404 or 500.
     * @throws CreateResponseException if the call could not be placed.
     */
    UUID createCall(PhoneNumber callerId, String origin, String destination, CallType callType) throws IOException;

    /**
     * Get active calls list.
     * @return list of active calls.
     * @throws IOException           if the request could not be executed due to a connectivity problem or timeout.
     * @throws HttpResponseException if REST API returned an unhappy HTTP response code like 404 or 500.
     */
    List<ActiveCall> getActiveCalls() throws IOException;

    /**
     * Get active call details.
     * @param id id of active call to fetch.
     * @return an active call details if present, otherwise {@code Optional.empty()}
     * @throws IOException           if the request could not be executed due to a connectivity problem or timeout.
     * @throws HttpResponseException if REST API returned an unhappy HTTP response code like 404 or 500.
     */
    Optional<ActiveCall> getActiveCall(UUID id) throws IOException;

    /**
     *  Hung up an active call.
     * @param id of the active call which should be deleted.
     * @throws IOException             if the request could not be executed due to a connectivity problem or timeout.
     * @throws HttpResponseException   if REST API returned an unhappy HTTP response code like 404 or 500.
     * @throws DeleteResponseException if the call could not be deleted.
     */
    void deleteActiveCall(UUID id) throws IOException;

    /**
     * The type of the terminal which can be called.
     * <li>{@link #NUMBER}</li>
     * <li>{@link #SIP_ACCOUNT}</li>
     */
    @AllArgsConstructor
    enum CallType {
        /**
         * The phone number
         */
        NUMBER("number"),
        /**
         * SIP account
         */
        SIP_ACCOUNT("sipaccount");

        @Getter
        private final String value;
    }

    /**
     * A call state
     */
    enum CallState {
        DOWN,
        DIALING,
        RINGING,
        EARLY,
        ACTIVE,
        HANGUP
    }

    class CreateResponseException extends RuntimeException {
        CreateResponseException(String message) {
            super(message);
        }
    }

    class DeleteResponseException extends RuntimeException {
        DeleteResponseException(String message) {
            super(message);
        }
    }
}
