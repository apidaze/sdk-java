package com.apidaze.sdk.client.cdrhttphandlers;

import com.apidaze.sdk.client.common.URL;

import java.io.IOException;
import java.util.List;

public interface CdrHttpHandlers {

    List<CdrHttpHandler> getCdrHttpHandlers() throws IOException;

    CdrHttpHandler createCdrHttpHandler(String name, URL url) throws IOException;

    CdrHttpHandler updateCdrHttpHandler(Long id, String name, URL url) throws IOException;

    CdrHttpHandler updateCdrHttpHandlerName(Long id, String name) throws IOException;

    CdrHttpHandler updateCdrHttpHandlerUrl(Long id, URL url) throws IOException;

    void deleteCdrHttpHandler(Long id) throws IOException;
}
