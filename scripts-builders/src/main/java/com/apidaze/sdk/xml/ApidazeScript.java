package com.apidaze.sdk.xml;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Builder;
import lombok.Singular;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@Builder
public class ApidazeScript {

    public interface Node {}

    @Singular
    private final List<Node> nodes;

    public String toXml() throws XMLStreamException, IOException {
        // First create Stax components we need
        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
        xmlOutputFactory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, true);
        StringWriter out = new StringWriter();
        XMLStreamWriter sw = xmlOutputFactory.createXMLStreamWriter(out);

        // then Jackson components
        XmlMapper mapper = new XmlMapper(xmlInputFactory);

        sw.writeStartDocument();
        sw.writeStartElement("document");
        sw.writeStartElement("work");

        for (Node node : nodes) {
            mapper.writeValue(sw, node);
        }

        // and/or regular Stax output
        sw.writeEndElement();
        sw.writeEndElement();
        sw.writeEndDocument();

        return out.toString();
    }
}
