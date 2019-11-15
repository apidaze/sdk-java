package com.apidaze.sdk.xml;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;
import org.junit.Test;

import java.io.File;
import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ApidazeScriptTest {

    @Test
    public void testAnswer() throws JsonProcessingException {
        val expectedOutput = new File("src/test/resources/answer.xml");

        val script = ApidazeScript.builder()
                .node(new Answer())
                .node(Playback.fromFile("http://www.mydomain.com/welcome.wav"))
                .node(new Hangup())
                .build();

        val result = script.toXmlWithPrettyPrinter();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testPlayback() throws JsonProcessingException {
        val expectedOutput = new File("src/test/resources/playback.xml");

        val script = ApidazeScript.builder()
                .node(new Answer())
                .node(Playback.fromFile("http://www.mydomain.com/welcome.wav"))
                .node(new Hangup())
                .build();

        val result = script.toXmlWithPrettyPrinter();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testRingback() throws JsonProcessingException {
        val expectedOutput = new File("src/test/resources/ringback.xml");

        val script = ApidazeScript.builder()
                .node(new Answer())
                .node(Ringback.fromFile("http://www.mydomain.com/welcome.wav"))
                .node(Dial.builder().node(new Dial.SipAccount("bob")).build())
                .node(new Hangup())
                .build();

        val result = script.toXmlWithPrettyPrinter();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testEcho() throws JsonProcessingException {
        val expectedOutput = new File("src/test/resources/echo.xml");

        val script = ApidazeScript.builder()
                .node(new Answer())
                .node(Echo.withDelay(Duration.ofMillis(500)))
                .build();

        val result = script.toXmlWithPrettyPrinter();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testHangup() throws JsonProcessingException {
        val expectedOutput = new File("src/test/resources/hangup.xml");

        val script = ApidazeScript.builder()
                .node(new Hangup())
                .build();

        val result = script.toXmlWithPrettyPrinter();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testIntercept() throws JsonProcessingException {
        val expectedOutput = new File("src/test/resources/intercept.xml");

        val script = ApidazeScript.builder()
                .node(new Answer())
                .node(Intercept.of(UUID.fromString("f28a3e29-dac4-462c-bf94-b1d518ddbe2d")))
                .node(new Hangup())
                .build();

        val result = script.toXmlWithPrettyPrinter();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testSpeak() throws JsonProcessingException {
        val expectedOutput = new File("src/test/resources/speak.xml");

        val script = ApidazeScript.builder()
                .node(new Answer())
                .node(Speak.builder()
                        .lang(Speak.Lang.FR)
                        .text("Bonjour et bienvenue chez APIDAIZE. Vous pouvez patienter, mais n'oubliez pas de raccrocher.")
                        .build())
                .node(new Wait(Duration.ofSeconds(5)))
                .build();

        val result = script.toXmlWithPrettyPrinter();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testBindWithSpeak() throws JsonProcessingException {
        val expectedOutput = new File("src/test/resources/bind-with-speak.xml");

        val script = ApidazeScript.builder()
                .node(new Answer())
                .node(Speak.builder()
                        .inputTimeout(Duration.ofSeconds(5))
                        .text("Press one to or two, or any digit, and we'll handle your call, or not.")
                        .bind(new Bind("http://www.mydomain.com/get_digits.php?bind=1", "1"))
                        .bind(new Bind("http://www.mydomain.com/get_digits.php?bind=2", "2"))
                        .bind(new Bind("http://www.mydomain.com/get_digits.php?bind=other", "~[3-9]"))
                        .build()
                )
                .build();

        val result = script.toXmlWithPrettyPrinter();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testBindWithPlayback() throws JsonProcessingException {
        val expectedOutput = new File("src/test/resources/bind-with-playback.xml");

        val script = ApidazeScript.builder()
                .node(new Answer())
                .node(Playback.builder()
                        .inputTimeout(Duration.ofSeconds(5))
                        .file("http://www.mydomain.com/welcome.wav")
                        .bind(new Bind("http://www.mydomain.com/get_digits.php?bind=1", "1"))
                        .bind(new Bind("http://www.mydomain.com/get_digits.php?bind=2", "2"))
                        .bind(new Bind("http://www.mydomain.com/get_digits.php?bind=other", "~[3-9]"))
                        .build())
                .build();

        val result = script.toXmlWithPrettyPrinter();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testWait() throws JsonProcessingException {
        val expectedOutput = new File("src/test/resources/wait.xml");

        val script = ApidazeScript.builder()
                .node(new Answer())
                .node(Speak.builder()
                        .lang(Speak.Lang.FR)
                        .text("Bonjour et bienvenue chez APIDAIZE. Vous pouvez patienter, mais n'oubliez pas de raccrocher.")
                        .build())
                .node(new Wait(Duration.ofSeconds(5)))
                .build();

        val result = script.toXmlWithPrettyPrinter();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testConference() throws JsonProcessingException {
        val expectedOutput = new File("src/test/resources/conference.xml");

        val script = ApidazeScript.builder()
                .node(Speak.builder().text("You will now be placed into the conference").build())
                .node(new Conference("my_meeting_room"))
                .build();

        val result = script.toXmlWithPrettyPrinter();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testRecord() throws JsonProcessingException {
        val expectedOutput = new File("src/test/resources/record.xml");

        val script = ApidazeScript.builder()
                .node(new Answer())
                .node(new Wait(Duration.ofSeconds(2)))
                .node(Speak.builder()
                        .lang(Speak.Lang.EN)
                        .text("Please leave a message.")
                        .build())
                .node(Record.builder().name("example1").build())
                .node(new Wait(Duration.ofSeconds(60)))
                .node(new Hangup())
                .build();

        val result = script.toXmlWithPrettyPrinter();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }

    @Test
    public void testRecordWithAllAttributes() throws JsonProcessingException {
        val expectedOutput = new File("src/test/resources/record-all-attributes.xml");

        val script = ApidazeScript.builder()
                .node(new Answer())
                .node(new Wait(Duration.ofSeconds(2)))
                .node(Speak.builder()
                        .lang(Speak.Lang.EN)
                        .text("Please leave a message.")
                        .build())
                .node(Record.builder()
                        .name("example1")
                        .onAnswered(true)
                        .aLeg(false)
                        .bLeg(false)
                        .build())
                .node(new Wait(Duration.ofSeconds(60)))
                .node(new Hangup())
                .build();

        val result = script.toXmlWithPrettyPrinter();

        assertThat(result).isXmlEqualToContentOf(expectedOutput);
    }
}
