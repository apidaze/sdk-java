package com.apidaze.sdk.examples.applications;

import com.apidaze.sdk.client.ApplicationManager;
import com.apidaze.sdk.client.base.Credentials;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;

@Slf4j
public class SubApplicationExternalScriptsExample {

    public static void main(String... args) throws IOException {

        val apiKey = "1cdef287";
        val apiSecret = "ab65e79f5527b4d1dc72fab357bb6ac3";

        // initiate ApplicationAction
        val applicationManager = ApplicationManager.create(new Credentials(apiKey, apiSecret));

        // sub-application id
        val id = 3041L;

        val applicationAction = applicationManager.getApplicationActionById(id);
        applicationAction.ifPresent(action -> {
            try {
                val scripts = action.getExternalScripts();
                log.info("Retrieved scripts {}", scripts);
            } catch (IOException e) {
                log.error("An error occurred during communicating with API", e);
            }
        });

    }
}
