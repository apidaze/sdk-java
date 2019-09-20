package com.apidaze.sdk.examples.externalscripts;

import com.apidaze.sdk.client.credentials.Credentials;
import com.apidaze.sdk.client.externalscripts.ExternalScript;
import com.apidaze.sdk.client.externalscripts.ExternalScripts;
import com.apidaze.sdk.client.externalscripts.ExternalScriptsClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class ListExternalScripts {

    public static void main(String... args) {

        if (args.length < 2) {
            System.err.println("You must provide: <apiKey> <apiSecret> in the  argument list!");
            System.exit(1);
        }

        val apiKey = args[0];
        val apiSecret = args[1];

        // initiate the client using create method
        val externalScripts = ExternalScriptsClient.create(new Credentials(apiKey, apiSecret));

        // get external scripts list
        val list = externalScripts.list().collectList().block();
        log.info("ExternalScripts list: {}", list);
    }
}
