package com.apidaze.sdk.examples.externalscripts;

import com.apidaze.sdk.client.credentials.Credentials;
import com.apidaze.sdk.client.externalscripts.ExternalScriptsClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class CreateExternalScript {

    public static void main(String... args) {

        if (args.length < 4) {
            System.err.println("You must provide: <apiKey> <apiSecret> <scriptName> <scriptUrl> in the  argument list!");
            System.exit(1);
        }

        val apiKey = args[0];
        val apiSecret = args[1];
        val scriptName = args[2];
        val scriptUrl = args[3];

        // initiate the client using create method
        val externalScripts = ExternalScriptsClient.create(new Credentials(apiKey, apiSecret));

        // create external script
        val createdScript = externalScripts.create(scriptName, scriptUrl).block();
        log.info("Created {}", createdScript);
    }
}
