package com.apidaze.sdk.client.recordings;

import com.apidaze.sdk.client.GenericRequest;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
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

import static com.apidaze.sdk.client.GenericResponse.list;
import static com.apidaze.sdk.client.TestUtil.*;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.deleteIfExists;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.INTERNAL_SERVER_ERROR_500;
import static org.mockserver.model.HttpStatusCode.NO_CONTENT_204;

public class RecordingsClientTest extends GenericRequest {

    private final static String SOURCE_FILES_DIR = "src/test/resources/data";
    private final static String SOURCE_FILE_NAME = "mediafile.wav";
    private final static File SOURCE_FILE = Paths.get(SOURCE_FILES_DIR, SOURCE_FILE_NAME).toFile();
    private final static File EMPTY_FILE = Paths.get(SOURCE_FILES_DIR, "empty.wav").toFile();
    private final static Path TARGET_DIR = Paths.get("target");
    private final static File TARGET_FILE = TARGET_DIR.resolve(SOURCE_FILE_NAME).toFile();
    private final static File TARGET_FILE_WITH_CHANGED_NAME = TARGET_DIR.resolve("new-file.wav").toFile();

    @Getter
    private final String basePath = "recordings";

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private Recordings client = RecordingsClient.create(CREDENTIALS, BASE_URL);

    @Before
    public void setUp() throws IOException {
        mockServer.reset();
        deleteIfExists(TARGET_FILE.toPath());
        deleteIfExists(TARGET_FILE_WITH_CHANGED_NAME.toPath());
    }

    @AfterClass
    public static void cleanUp() throws IOException {
        deleteIfExists(TARGET_FILE.toPath());
        deleteIfExists(TARGET_FILE_WITH_CHANGED_NAME.toPath());
    }

    @Test
    public void shouldReturnListOfRecordingFiles() throws IOException {
        val files = ImmutableList.of("file1.wav", "file2.wav", "file3.wav");

        mockServer
                .when(getAll())
                .respond(list(files));

        val result = client.getRecordingsList();

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

        client.deleteRecording(fileName);

        mockServer.verify(delete(fileName));
    }

    @Test
    public void shouldReturnInputStreamOfDownloadedFile() throws IOException {
        val expectedStream = new FileInputStream(SOURCE_FILE);

        mockServer
                .when(getById(SOURCE_FILE_NAME))
                .respond(responseWithAudioFile(SOURCE_FILE));

        val resultStream = client.downloadRecording(SOURCE_FILE_NAME);

        assertThat(resultStream).hasSameContentAs(expectedStream);
        mockServer.verify(getById(SOURCE_FILE_NAME));

        resultStream.close();
        expectedStream.close();
    }

    @Test
    public void shouldDownloadFileInAsyncMode() throws IOException {
        val callback = mock(Recordings.DownloadCallback.class);
        val downloadedFile = ArgumentCaptor.forClass(File.class);

        mockServer
                .when(getById(SOURCE_FILE_NAME))
                .respond(responseWithAudioFile(SOURCE_FILE));

        client.downloadRecordingToFileAsync(SOURCE_FILE_NAME, TARGET_DIR, callback);

        await().untilAsserted(() -> verify(callback).onSuccess(downloadedFile.capture()));
        verify(callback, never()).onFailure(anyString(), any(), any());
        verifyDownloadedFile(downloadedFile.getValue());
        mockServer.verify(getById(SOURCE_FILE_NAME));
    }

    @Test
    public void shouldInvokeOnFailureMethodInAsyncMode_ifFileAlreadyExistsAndOverwriteModeIsDisabled() throws IOException {
        val callback = mock(Recordings.DownloadCallback.class);
        val overwrite = false;

        copy(EMPTY_FILE.toPath(), TARGET_FILE.toPath());

        mockServer
                .when(getById(SOURCE_FILE_NAME))
                .respond(responseWithAudioFile(SOURCE_FILE));

        client.downloadRecordingToFileAsync(SOURCE_FILE_NAME, TARGET_DIR, overwrite, callback);

        await().untilAsserted(() -> verify(callback).onFailure(
                eq(SOURCE_FILE_NAME),
                eq(TARGET_DIR),
                any(FileAlreadyExistsException.class)));
        verify(callback, never()).onSuccess(any());
        mockServer.verifyZeroInteractions();
    }

    @Test
    public void shouldInvokeOnFailureMethodInAsyncMode_ifApiReturnsAnError() {
        val callback = mock(Recordings.DownloadCallback.class);

        mockServer
                .when(getById(SOURCE_FILE_NAME))
                .respond(response().withStatusCode(INTERNAL_SERVER_ERROR_500.code()));

        client.downloadRecordingToFileAsync(SOURCE_FILE_NAME, TARGET_DIR, callback);

        await().untilAsserted(() -> verify(callback).onFailure(
                eq(SOURCE_FILE_NAME),
                eq(TARGET_DIR),
                any(IOException.class)));
        verify(callback, never()).onSuccess(any());
        mockServer.verify(getById(SOURCE_FILE_NAME));
    }

    @Test
    public void shouldDownloadFileToLocalFolderWithOriginalName() throws IOException {
        mockServer
                .when(getById(SOURCE_FILE_NAME))
                .respond(responseWithAudioFile(SOURCE_FILE));

        val downloadedFile = client.downloadRecordingToFile(SOURCE_FILE_NAME, TARGET_DIR);

        verifyDownloadedFile(downloadedFile);
        mockServer.verify(getById(SOURCE_FILE_NAME));
    }

    @Test
    public void shouldDownloadFileToLocalFolderWithChangedName() throws IOException {
        mockServer
                .when(getById(SOURCE_FILE_NAME))
                .respond(responseWithAudioFile(SOURCE_FILE));

        val downloadedFile = client.downloadRecordingToFile(SOURCE_FILE_NAME, TARGET_FILE_WITH_CHANGED_NAME.toPath());

        verifyDownloadedFile(downloadedFile, TARGET_FILE_WITH_CHANGED_NAME.getName());
        mockServer.verify(getById(SOURCE_FILE_NAME));
    }

    @Test
    public void shouldOverwriteExistingFile_ifOverwriteModeIsEnabled() throws IOException {
        val overwrite = true;

        copy(EMPTY_FILE.toPath(), TARGET_FILE.toPath());

        assertThat(getBinaryContent(TARGET_FILE).length)
                .isNotEqualTo(getBinaryContent(SOURCE_FILE).length);

        mockServer
                .when(getById(SOURCE_FILE_NAME))
                .respond(responseWithAudioFile(SOURCE_FILE));

        val downloadedFile = client.downloadRecordingToFile(SOURCE_FILE_NAME, TARGET_DIR, overwrite);

        verifyDownloadedFile(downloadedFile);
        mockServer.verify(getById(SOURCE_FILE_NAME));
    }

    @Test
    public void shouldThrowException_ifFileAlreadyExistsAndOverwriteModeIsDisabled() throws IOException {
        val overwrite = false;

        copy(EMPTY_FILE.toPath(), TARGET_FILE.toPath());

        mockServer
                .when(getById(SOURCE_FILE_NAME))
                .respond(responseWithAudioFile(SOURCE_FILE));

        assertThatExceptionOfType(FileAlreadyExistsException.class)
                .isThrownBy(() -> client.downloadRecordingToFile(SOURCE_FILE_NAME, TARGET_DIR, overwrite))
                .withMessage(TARGET_FILE.getAbsolutePath());
        mockServer.verifyZeroInteractions();
    }

    private static AbstractFileAssert<?> verifyDownloadedFile(File file) throws IOException {
        return verifyDownloadedFile(file, SOURCE_FILE_NAME);
    }

    private static AbstractFileAssert<?> verifyDownloadedFile(File file, String expectedName) throws IOException {
        return assertThat(file)
                .hasBinaryContent(getBinaryContent(SOURCE_FILE))
                .hasParent(TARGET_DIR.toFile())
                .hasName(expectedName);
    }
}
