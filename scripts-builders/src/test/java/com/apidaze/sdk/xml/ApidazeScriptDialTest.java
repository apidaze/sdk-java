package com.apidaze.sdk.xml;

import lombok.val;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class ApidazeScriptDialTest {

    @Test
    public void testDialNumber() throws IOException, XMLStreamException {
        val expectedOutput = new File("src/test/resources/dial-number.xml");

        val dial = Dial.builder()
                .node(new Dial.Number("1234567890"))
                .build();

        val script = ApidazeScript.builder()
                .node(dial)
                .node(new Hangup())
                .build();

        val result = script.toXml();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testDialSipAccount() throws IOException, XMLStreamException {
        val expectedOutput = new File("src/test/resources/dial-sipaccount.xml");

        val dial = Dial.builder()
                .node(new Dial.SipAccount("targetsipaccount"))
                .build();

        val script = ApidazeScript.builder()
                .node(dial)
                .node(new Hangup())
                .build();

        val result = script.toXml();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testDialSipUri() throws IOException, XMLStreamException {
        val expectedOutput = new File("src/test/resources/dial-sipuri.xml");

        val dial = Dial.builder()
                .node(new Dial.SipUri("phone_number@anysipdomain.com"))
                .build();

        val script = ApidazeScript.builder()
                .node(dial)
                .node(new Hangup())
                .build();

        val result = script.toXml();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testDialWithAllAttributesAndDestinationTypes() throws IOException, XMLStreamException {
        val expectedOutput = new File("src/test/resources/dial-all-in-one.xml");

        val dial = Dial.builder()
                .timeout(Duration.ofSeconds(60))
                .maxCallDuration(Duration.ofSeconds(300))
                .strategy(Dial.Strategy.SEQUENCE)
                .action("http://action.url.com")
                .answerUrl("http://answer-url.com")
                .callerHangupUrl("http://caller-hangup-url.com")
                .node(new Dial.Number("1234567890"))
                .node(new Dial.SipAccount("targetsipaccount"))
                .node(new Dial.SipUri("phone_number@anysipdomain.com"))
                .build();

        val script = ApidazeScript.builder()
                .node(dial)
                .node(new Hangup())
                .build();

        val result = script.toXml();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }
}
