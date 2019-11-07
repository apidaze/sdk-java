package com.apidaze.sdk.client.validates;

import java.io.IOException;

public interface CredentialsValidator {

    boolean validateCredentials() throws IOException;
}
