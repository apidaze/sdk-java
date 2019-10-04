package com.apidaze.sdk.client.messages;

public interface Message {

    String send(PhoneNumber from, PhoneNumber to, String body);
}
