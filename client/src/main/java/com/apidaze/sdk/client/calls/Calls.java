package com.apidaze.sdk.client.calls;

import com.apidaze.sdk.client.messages.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
    UUID create(PhoneNumber callerId, String origin, String destination, Type callType);

    List<ActiveCall> getActiveCalls();

    ActiveCall getActiveCall(UUID id);

    void deleteActiveCall(UUID id);

    @AllArgsConstructor
    enum Type {
        NUMBER("number"),
        SIP_ACCOUNT("sipaccount");

        @Getter
        private final String value;
    }

    enum State {
        DOWN,
        DIALING,
        RINGING,
        EARLY,
        ACTIVE,
        HANGUP
    }
}
