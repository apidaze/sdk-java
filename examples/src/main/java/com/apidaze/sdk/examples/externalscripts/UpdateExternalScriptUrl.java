package com.apidaze.sdk.examples.externalscripts;

import com.apidaze.sdk.client.credentials.Credentials;
import com.apidaze.sdk.client.externalscripts.ExternalScriptsClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import static java.lang.Long.parseLong;

@Slf4j
public class UpdateExternalScriptUrl {

    public static void main(String... args) {

        if (args.length < 4) {
            System.err.println("You must provide: <apiKey> <apiSecret> <scriptId> <newScriptUrl> in the  argument list!");
            System.exit(1);
        }

        val apiKey = args[0];
        val apiSecret = args[1];
        val scriptId = args[2];
        val newScriptUrl = args[3];

        // initiate the client
        val externalScripts = ExternalScriptsClient.builder().credentials(new Credentials(apiKey, apiSecret)).build();

        // create external script
        try {
            val id = parseLong(scriptId);
            val script = externalScripts.updateUrl(id, newScriptUrl).block();
            log.info("Updated {}", script);
        } catch (NumberFormatException e) {
            System.err.println("scriptId must be a number");
        }
    }
}
