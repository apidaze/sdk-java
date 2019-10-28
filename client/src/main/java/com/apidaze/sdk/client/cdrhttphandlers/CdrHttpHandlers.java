package com.apidaze.sdk.client.cdrhttphandlers;

import java.io.IOException;
import java.util.List;

public interface CdrHttpHandlers {

    List<CdrHttpHandler> getCdrHttpHandlers() throws IOException;
}
