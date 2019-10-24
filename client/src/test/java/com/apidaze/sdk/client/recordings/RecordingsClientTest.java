package com.apidaze.sdk.client.recordings;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.assertj.core.api.AbstractFileAssert;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;

import java.io.File;
import java.io.FileInputStream;
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
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.*;

public class RecordingsClientTest {

    private final static String SOURCE_FILES_DIR = "src/test/resources/data";
    private final static String SOURCE_FILE_NAME = "mediafile.wav";
    private final static File SOURCE_FILE = Paths.get(SOURCE_FILES_DIR, SOURCE_FILE_NAME).toFile();
    private final static File EMPTY_FILE = Paths.get(SOURCE_FILES_DIR, "empty.wav").toFile();
    private final static Path TARGET_DIR = Paths.get("target");
    private final static File TARGET_FILE = TARGET_DIR.resolve(SOURCE_FILE_NAME).toFile();

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private Recordings client = RecordingsClient.create(CREDENTIALS, BASE_URL);

    @Before
    public void setUp() throws IOException {
        mockServer.reset();
        deleteIfExists(TARGET_FILE.toPath());
    }

    @AfterClass
    public static void cleanUp() throws IOException {
        deleteIfExists(TARGET_FILE.toPath());
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
    public void shouldReturnInputStreamOfDownloadedFile() throws IOException {
        val expectedStream = new FileInputStream(SOURCE_FILE);

        mockServer
                .when(download(SOURCE_FILE_NAME))
                .respond(responseWithFile(SOURCE_FILE));

        val resultStream = client.download(SOURCE_FILE_NAME);

        assertThat(resultStream).hasSameContentAs(expectedStream);
        mockServer.verify(download(SOURCE_FILE_NAME));

        resultStream.close();
        expectedStream.close();
    }

    @Test
    public void shouldDownloadFileInAsyncMode() throws IOException {
        val callback = mock(Recordings.Callback.class);
        val downloadedFile = ArgumentCaptor.forClass(File.class);

        mockServer
                .when(download(SOURCE_FILE_NAME))
                .respond(responseWithFile(SOURCE_FILE));

        client.downloadToFileAsync(SOURCE_FILE_NAME, TARGET_DIR, callback);

        await().untilAsserted(() -> verify(callback).onSuccess(downloadedFile.capture()));
        verify(callback, never()).onFailure(any());
        verifyDownloadedFile(downloadedFile.getValue());
        mockServer.verify(download(SOURCE_FILE_NAME));
    }

    @Test
    public void shouldInvokeOnFailureMethodInAsyncMode_ifFileAlreadyExistsAndOverwriteModeIsDisabled() throws IOException {
        val callback = mock(Recordings.Callback.class);
        val overwrite = false;

        copy(EMPTY_FILE.toPath(), TARGET_FILE.toPath());

        mockServer
                .when(download(SOURCE_FILE_NAME))
                .respond(responseWithFile(SOURCE_FILE));

        client.downloadToFileAsync(SOURCE_FILE_NAME, TARGET_DIR, overwrite, callback);

        await().untilAsserted(() -> verify(callback).onFailure(any(FileAlreadyExistsException.class)));
        verify(callback, never()).onSuccess(any());
        mockServer.verifyZeroInteractions();
    }

    @Test
    public void shouldInvokeOnFailureMethodInAsyncMode_ifApiReturnsError() throws IOException {
        val callback = mock(Recordings.Callback.class);

        mockServer
                .when(download(SOURCE_FILE_NAME))
                .respond(response().withStatusCode(INTERNAL_SERVER_ERROR_500.code()));

        client.downloadToFileAsync(SOURCE_FILE_NAME, TARGET_DIR, callback);

        await().untilAsserted(() -> verify(callback).onFailure(any(IOException.class)));
        verify(callback, never()).onSuccess(any());
        mockServer.verify(download(SOURCE_FILE_NAME));
    }

    @Test
    public void shouldDownloadFileDirectlyToLocalFolder() throws IOException {
        mockServer
                .when(download(SOURCE_FILE_NAME))
                .respond(responseWithFile(SOURCE_FILE));

        val downloadedFile = client.downloadToFile(SOURCE_FILE_NAME, TARGET_DIR);

        verifyDownloadedFile(downloadedFile);
        mockServer.verify(download(SOURCE_FILE_NAME));
    }

    @Test
    public void shouldOverwriteExistingFile_ifOverwriteModeIsEnabled() throws IOException {
        val overwrite = true;

        copy(EMPTY_FILE.toPath(), TARGET_FILE.toPath());

        assertThat(getBinaryContent(TARGET_FILE).length)
                .isNotEqualTo(getBinaryContent(SOURCE_FILE).length);

        mockServer
                .when(download(SOURCE_FILE_NAME))
                .respond(responseWithFile(SOURCE_FILE));

        val downloadedFile = client.downloadToFile(SOURCE_FILE_NAME, TARGET_DIR, overwrite);

        verifyDownloadedFile(downloadedFile);
        mockServer.verify(download(SOURCE_FILE_NAME));
    }

    @Test
    public void shouldThrowException_ifFileAlreadyExistsAndOverwriteModeIsDisabled() throws IOException {
        val overwrite = false;

        copy(EMPTY_FILE.toPath(), TARGET_FILE.toPath());

        mockServer
                .when(download(SOURCE_FILE_NAME))
                .respond(responseWithFile(SOURCE_FILE));

        assertThatExceptionOfType(FileAlreadyExistsException.class)
                .isThrownBy(() -> client.downloadToFile(SOURCE_FILE_NAME, TARGET_DIR, overwrite))
                .withMessage(TARGET_FILE.getAbsolutePath());
        mockServer.verifyZeroInteractions();
    }

    private static AbstractFileAssert<?> verifyDownloadedFile(File file) throws IOException {
        return assertThat(file)
                .hasBinaryContent(getBinaryContent(SOURCE_FILE))
                .hasParent(TARGET_DIR.toFile())
                .hasName(SOURCE_FILE_NAME);
    }
}
