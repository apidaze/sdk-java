package com.apidaze.sdk.client.messages;

import com.apidaze.sdk.client.common.PhoneNumber;
import com.apidaze.sdk.client.http.HttpResponseException;

import java.io.IOException;

/**
 * The interface used to send text messages.
 */
public interface Message {

    /** Send a text message.
     * @param from The number to send the text from. Must be an active number on your account.
     * @param to Destination number (no + sign)
     * @param body The message to send. Must not be empty.
     * @return Response returned by API
     * @throws IOException
     * @throws HttpResponseException
     */
    String sendTextMessage(PhoneNumber from, PhoneNumber to, String body) throws IOException;
}
