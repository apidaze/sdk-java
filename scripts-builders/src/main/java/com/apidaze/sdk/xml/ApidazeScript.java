package com.apidaze.sdk.xml;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
@JacksonXmlRootElement(localName = "document")
@JsonSerialize(using = ApidazeScriptSerializer.class)
public class ApidazeScript {

    private final ObjectMapper mapper = new XmlMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    public interface Node {
        String tag();
    }

    @Getter
    @Singular
    private final List<Node> nodes;

    public String toXml() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }

    public String toXmlWithPrettyPrinter() throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }
}
