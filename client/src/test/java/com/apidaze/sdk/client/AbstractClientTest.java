package com.apidaze.sdk.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.model.JsonBody;
import org.mockserver.serialization.ObjectMapperFactory;

import java.time.ZonedDateTime;
import java.util.Comparator;

import static java.util.Objects.nonNull;

public class AbstractClientTest {

    public static final String API_KEY = "some-api-key";
    public static final String API_SECRET = "some-api-secret";

    protected Comparator<ZonedDateTime> dateTimeComparator = (d1, d2) -> {
        return ((d1 == d2) || (nonNull(d1) && nonNull(d2) && d1.isEqual(d2))) ? 0 : 1;
    };

    private static final ObjectMapper mapper = ObjectMapperFactory.createObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    public static JsonBody json(Object object) {
        return new JsonBody(toJson(object));
    }

    private static String toJson(Object object) {
        String json = "";
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            new MockServerLogger(JsonBody.class).error("error mapping object for json body to JSON", e);
        }
        return json;
    }
}
