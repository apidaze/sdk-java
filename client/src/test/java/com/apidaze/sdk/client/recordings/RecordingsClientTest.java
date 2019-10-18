package com.apidaze.sdk.client.recordings;

import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.apidaze.sdk.client.TestUtil.*;
import static com.apidaze.sdk.client.recordings.RecordingsClientRequest.download;
import static com.apidaze.sdk.client.recordings.RecordingsClientResponse.responseWithFile;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.deleteIfExists;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class RecordingsClientTest {

    private final static String SOURCE_FILES_DIR = "src/test/resources/data";
    private final static String SOURCE_FILE_NAME = "mediafile.wav";
    private final static File SOURCE_FILE = Paths.get(SOURCE_FILES_DIR, SOURCE_FILE_NAME).toFile();
    private final static Path TARGET_DIR = Paths.get("target");

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private Recordings client = RecordingsClient.create(CREDENTIALS, BASE_URL);

    @Before
    public void setUp() throws IOException {
        mockServer.reset();
        deleteIfExists(TARGET_DIR.resolve(SOURCE_FILE_NAME));
    }

    @Test
    public void shouldDownloadAFile() throws IOException {
        mockServer
                .when(download(SOURCE_FILE_NAME))
                .respond(responseWithFile(SOURCE_FILE));

        val downloadedFile = client.download(SOURCE_FILE_NAME, TARGET_DIR);

        assertThat(downloadedFile)
                .hasBinaryContent(getBinaryContent(SOURCE_FILE))
                .hasName(SOURCE_FILE_NAME);

        mockServer.verify(download(SOURCE_FILE_NAME));
    }

    @Test
    public void shouldOverwriteExistingFile_ifOverwriteModeIsEnabled() throws IOException {
        val targetFile = TARGET_DIR.resolve(SOURCE_FILE_NAME).toFile();
        val overwrite = true;

        copy(Paths.get(SOURCE_FILES_DIR, "empty.wav"), targetFile.toPath());

        assertThat(getBinaryContent(targetFile).length)
                .isNotEqualTo(getBinaryContent(SOURCE_FILE).length);

        mockServer
                .when(download(SOURCE_FILE_NAME))
                .respond(responseWithFile(SOURCE_FILE));

        client.download(SOURCE_FILE_NAME, TARGET_DIR, overwrite);

        assertThat(targetFile)
                .hasBinaryContent(getBinaryContent(SOURCE_FILE))
                .hasName(SOURCE_FILE_NAME);
        mockServer.verify(download(SOURCE_FILE_NAME));
    }

    @Test
    public void shouldThrowException_ifFileAlreadyExistsAndOverwriteModeIsDisabled() throws IOException {
        val targetFile = TARGET_DIR.resolve(SOURCE_FILE_NAME).toFile();
        val overwrite = false;

        copy(Paths.get(SOURCE_FILES_DIR, "empty.wav"), targetFile.toPath());

        mockServer
                .when(download(SOURCE_FILE_NAME))
                .respond(responseWithFile(SOURCE_FILE));

        assertThatExceptionOfType(FileAlreadyExistsException.class)
                .isThrownBy(() -> client.download(SOURCE_FILE_NAME, TARGET_DIR, overwrite))
                .withMessage(targetFile.getAbsolutePath());
    }
}
