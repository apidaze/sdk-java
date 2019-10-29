package com.apidaze.sdk.client.externalscripts;

import com.apidaze.sdk.client.common.URL;

import java.io.IOException;
import java.util.List;

public interface ExternalScripts {

    List<ExternalScript> getExternalScripts() throws IOException;

    ExternalScript createExternalScript(String name, URL url) throws IOException;

    ExternalScript getExternalScript(Long id) throws IOException;

    ExternalScript updateExternalScript(Long id, String name, URL url) throws IOException;

    ExternalScript updateExternalScriptUrl(Long id, URL url) throws IOException;

    void deleteExternalScript(Long id) throws IOException;
}
