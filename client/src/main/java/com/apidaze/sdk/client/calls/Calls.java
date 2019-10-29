package com.apidaze.sdk.client.calls;

import com.apidaze.sdk.client.common.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface Calls {

    /**
     * Place a call
     *
     * @param callerId    The phone number to present as the callerid (country code included, no + sign). Example: 15558675309
     * @param origin      The phone number or SIP account to ring first.
     * @param destination The destination passed as a parameter to your External Script URL.
     * @param callType    The type of the terminal to ring first.
     * @return Call id
     */
    UUID createCall(PhoneNumber callerId, String origin, String destination, CallType callType) throws IOException;

    List<ActiveCall> getActiveCalls() throws IOException;

    ActiveCall getActiveCall(UUID id) throws IOException;

    void deleteActiveCall(UUID id) throws IOException;

    @AllArgsConstructor
    enum CallType {
        NUMBER("number"),
        SIP_ACCOUNT("sipaccount");

        @Getter
        private final String value;
    }

    enum CallState {
        DOWN,
        DIALING,
        RINGING,
        EARLY,
        ACTIVE,
        HANGUP
    }
}
