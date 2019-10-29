package com.apidaze.sdk.client.cdrhttphandlers;

import com.apidaze.sdk.client.common.URL;

import java.io.IOException;
import java.util.List;

public interface CdrHttpHandlers {

    List<CdrHttpHandler> getCdrHttpHandlers() throws IOException;

    CdrHttpHandler createCdrHttpHandler(String name, URL url) throws IOException;
}
