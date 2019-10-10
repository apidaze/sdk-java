package com.apidaze.sdk.client.recordings;

import java.io.IOException;
import java.util.List;

public interface Recordings {
    List<String> list() throws IOException;
}
