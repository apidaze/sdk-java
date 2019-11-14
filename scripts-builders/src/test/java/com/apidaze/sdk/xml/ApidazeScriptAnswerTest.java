package com.apidaze.sdk.xml;

import lombok.val;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ApidazeScriptAnswerTest {

    @Test
    public void testAnswerWithPlayback() throws IOException, XMLStreamException {
        val expectedOutput = new File("src/test/resources/answer-with-playback.xml");

        val builder = ApidazeScript.builder()
                .node(new Answer())
                .node(Playback.fromFile("http://www.mydomain.com/welcome.wav"))
                .node(new Hangup())
                .build();

        val result = builder.toXml();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

}
