package com.apidaze.sdk.client.externalscripts;

import java.io.IOException;
import java.util.List;

public interface ExternalScripts {

    List<ExternalScript> list() throws IOException;

    ExternalScript create(String name, URL url) throws IOException;

    ExternalScript get(Long id) throws IOException;

    ExternalScript update(Long id, String name, URL url) throws IOException;

    ExternalScript updateUrl(Long id, URL url) throws IOException;

    void delete(Long id) throws IOException;
}
