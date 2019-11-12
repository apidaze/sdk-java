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
     * Places a call to the phone number or SIP account.
     *
     * @param callerId    The phone number to present as the callerId (country code included, no + sign). Example: 15558675309
     * @param origin      The phone number or SIP account to ring first.
     * @param destination The destination passed as a parameter to your External Script URL.
     * @param callType    The type of the terminal to ring first. See possible values {@link CallType}
     *
     * @return id of created call.
     *
     * @throws IOException
     * @throws HttpResponseException
     * @throws CreateResponseException
     */
    UUID createCall(PhoneNumber callerId, String origin, String destination, CallType callType) throws IOException;

    /**
     * Returns the list of active calls for your domain.
     * @return list of active calls.
     * @throws IOException
     * @throws HttpResponseException
     */
    List<ActiveCall> getActiveCalls() throws IOException;

    /**
     * Returns the active call details object by id.
     * @param id id of active call to fetch.
     * @return an active call details if present, otherwise {@code Optional.empty()}
     * @throws IOException
     * @throws HttpResponseException
     */
    Optional<ActiveCall> getActiveCall(UUID id) throws IOException;

    /**
     *  Hangs up an active call.
     * @param id of the active call which should be deleted.
     * @throws IOException
     * @throws HttpResponseException
     * @throws DeleteResponseException
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

    /**
     * An exception thrown if the call could not be placed. It contains error message returned by REST API.
     */
    class CreateResponseException extends RuntimeException {
        CreateResponseException(String message) {
            super(message);
        }
    }

    /**
     * An exception thrown if the call could not be deleted. It contains error message returned by REST API.
     */
    class DeleteResponseException extends RuntimeException {
        DeleteResponseException(String message) {
            super(message);
        }
    }
}
