package com.apidaze.sdk.client.applications;

import java.io.IOException;
import java.util.List;

public interface Applications {

    List<Application> getApplications() throws IOException;
}
