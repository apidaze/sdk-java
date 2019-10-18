package com.apidaze.sdk.client.recordings;

import com.google.common.collect.ImmutableList;
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
import static com.apidaze.sdk.client.recordings.RecordingsClientRequest.*;
import static com.apidaze.sdk.client.recordings.RecordingsClientResponse.list;
import static com.apidaze.sdk.client.recordings.RecordingsClientResponse.responseWithFile;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.deleteIfExists;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.NO_CONTENT_204;

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
    public void shouldReturnListOfRecordingFiles() throws IOException {
        val files = ImmutableList.of("file1.wav", "file2.wav", "file3.wav");

        mockServer
                .when(getAll())
                .respond(list(files));

        val result = client.list();

        mockServer.verify(getAll());
        assertThat(result).containsExactlyElementsOf(files);
    }

    @Test
    public void shouldDeleteRecordingFile() throws IOException {
        val fileName = "file1.wav";

        mockServer
                .when(delete(fileName))
                .respond(response()
                        .withStatusCode(NO_CONTENT_204.code()));

        client.delete(fileName);

        mockServer.verify(delete(fileName));
    }

    @Test
    public void shouldDownloadFile() throws IOException {
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
