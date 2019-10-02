package com.apidaze.sdk.client.externalscripts;

import java.util.List;

public interface ExternalScripts {

    List<ExternalScript> list();

    ExternalScript create(String name, URL url);

    ExternalScript get(Long id);

    ExternalScript update(Long id, String name, URL url);

    ExternalScript updateUrl(Long id, URL url);

    Void delete(Long id);
}
