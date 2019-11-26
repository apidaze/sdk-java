package com.apidaze.sdk.client.applications;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface Applications {

    List<Application> getApplications() throws IOException;

    Optional<Application> getApplicationById(Long id) throws IOException;
    Optional<Application> getApplicationByApiKey(String apiKey) throws IOException;
    Optional<Application> getApplicationByName(String name) throws IOException;
}
