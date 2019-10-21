package com.apidaze.sdk.client.recordings;

import org.mockserver.model.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.apidaze.sdk.client.TestUtil.*;
import static com.google.common.net.HttpHeaders.CONTENT_DISPOSITION;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.OK_200;

class RecordingsClientResponse {

    static HttpResponse list(List<String> files) {
        return response()
                .withStatusCode(OK_200.code())
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withBody(json(files));
    }

    static HttpResponse responseWithFile(File file) throws IOException {
        return HttpResponse.response()
                .withStatusCode(OK_200.code())
                .withHeaders(
                        header(CONTENT_TYPE, "audio/x-wav"),
                        header(CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                )
                .withBody(getBinaryContent(file));
    }
}