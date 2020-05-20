package com.apidaze.sdk.client.mediafiles;

import com.apidaze.sdk.client.GenericRequest;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.val;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.ZonedDateTime;

import static com.apidaze.sdk.client.GenericResponse.list;
import static com.apidaze.sdk.client.TestUtil.*;
import static com.apidaze.sdk.client.mediafiles.MediaFilesClient.*;
import static io.netty.handler.codec.http.HttpMethod.POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.NO_CONTENT_204;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.StringBody.subString;

public class MediaFilesClientTest extends GenericRequest {

    private final static String SOURCE_FILES_DIR = "src/test/resources/data";
    private final static String SOURCE_FILE_NAME = "mediafile.wav";
    private final static File SOURCE_FILE = Paths.get(SOURCE_FILES_DIR, SOURCE_FILE_NAME).toFile();

    @Getter
    private final String basePath = "mediafiles";

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private MediaFiles client = MediaFilesClient.create(CREDENTIALS, BASE_URL);

    @Test
    public void shouldReturnListOfMediaFileNamesWithoutLastToken() throws IOException {
        val files = ImmutableList.of("file1.wav", "file2.wav", "file3.wav");

        mockServer
                .when(getAll())
                .respond(list(files));

        val result = client.getMediaFileNames();

        mockServer.verify(getAll());
        assertThat(result.getLastToken()).isNull();
        assertThat(result.getMediaFiles()).containsExactlyElementsOf(files);
    }

    @Test
    public void shouldReturnListOfMediaFileNamesWithLastToken() throws IOException {
        val fileNames = ImmutableList.of("file1.wav", "file2.wav", "file3.wav");
        val lastToken = "some_last_token";

        mockServer
                .when(getAll())
                .respond(list(fileNames).withHeader(HEADER_LIST_TRUNCATION_TOKEN, lastToken));

        val result = client.getMediaFileNames();

        mockServer.verify(getAll());
        assertThat(result.getLastToken()).isEqualTo(lastToken);
        assertThat(result.getMediaFiles()).containsExactlyElementsOf(fileNames);
    }

    @Test
    public void shouldGetMediaFileNamesByParameters() throws IOException {
        val files = ImmutableList.of("file2.wav", "file3.wav");
        val filter = "file";
        val maxItems = 2;
        val lastToken = "some_last_token";

        val expectedRequest = getByParameters(
                param(PARAM_FILTER, filter),
                param(PARAM_MAX_ITEMS, String.valueOf(maxItems)),
                param(PARAM_LAST_TOKEN, lastToken));

        mockServer
                .when(expectedRequest)
                .respond(list(files));

        val result = client.getMediaFileNames(filter, maxItems, lastToken);

        mockServer.verify(expectedRequest);
        assertThat(result.getMediaFiles()).containsExactlyElementsOf(files);
    }

    @Test
    public void getMediaFileNames_shouldNotInvokeApi_ifParameterMaxItemsIsLessThanOne() {
        val maxItems = 0;

        assertThatIllegalArgumentException()
                .isThrownBy(() -> client.getMediaFileNames(null, maxItems, null))
                .withMessageContaining("maxItems must be greater than 0");
        mockServer.verifyZeroInteractions();
    }

    @Test
    public void shouldReturnListOfMediaFilesWithoutLastToken() throws IOException {
        val mediaFiles = ImmutableList.of(mediaFile1, mediaFile2, mediaFile3);

        mockServer
                .when(getAll())
                .respond(list(mediaFiles));

        val result = client.getMediaFiles();

        mockServer.verify(getAll());
        assertThat(result.getLastToken()).isNull();
        assertThat(result.getMediaFiles())
                .usingComparatorForElementFieldsWithType(dateTimeComparator, ZonedDateTime.class)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(mediaFiles);
    }

    @Test
    public void shouldReturnListOfMediaFilesWithLastToken() throws IOException {
        val mediaFiles = ImmutableList.of(mediaFile1, mediaFile2, mediaFile3);
        val lastToken = "some_token";

        mockServer
                .when(getAll())
                .respond(list(mediaFiles).withHeader(HEADER_LIST_TRUNCATION_TOKEN, lastToken));

        val result = client.getMediaFiles();

        mockServer.verify(getAll());
        assertThat(result.getLastToken()).isEqualTo(lastToken);
        assertThat(result.getMediaFiles())
                .usingComparatorForElementFieldsWithType(dateTimeComparator, ZonedDateTime.class)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(mediaFiles);
    }

    @Test
    public void shouldGetMediaFilesByParameters() throws IOException {
        val mediaFiles = ImmutableList.of(mediaFile2, mediaFile3);
        val filter = "file";
        val maxItems = 2;
        val lastToken = "some_last_token";

        val expectedRequest = getByParameters(
                param(PARAM_FILTER, filter),
                param(PARAM_MAX_ITEMS, String.valueOf(maxItems)),
                param(PARAM_LAST_TOKEN, lastToken));

        mockServer
                .when(expectedRequest)
                .respond(list(mediaFiles));

        val result = client.getMediaFiles(filter, maxItems, lastToken);

        mockServer.verify(expectedRequest);
        assertThat(result.getMediaFiles())
                .usingComparatorForElementFieldsWithType(dateTimeComparator, ZonedDateTime.class)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(mediaFiles);
    }


    @Test
    public void getMediaFiles_shouldNotInvokeApi_ifParameterMaxItemsIsLessThanOne() {
        val maxItems = 0;

        assertThatIllegalArgumentException()
                .isThrownBy(() -> client.getMediaFiles(null, maxItems, null))
                .withMessageContaining("maxItems must be greater than 0");
        mockServer.verifyZeroInteractions();
    }

    @Test
    public void shouldUploadMediaFile() throws IOException {
        val file = SOURCE_FILE;
        val expectedResponse = "File has been successfully uploaded.";

        mockServer
                .when(uploadMediaFile(file.getName()))
                .respond(response(expectedResponse));

        val result = client.uploadMediaFile(file.toPath());

        assertThat(result).isEqualTo(expectedResponse);
        mockServer.verify(uploadMediaFile(file.getName()));
    }

    @Test
    public void shouldUploadMediaFileWithNewName() throws IOException {
        val file = SOURCE_FILE;
        val newName = "new_name.wav";
        val expectedResponse = "File has been successfully uploaded.";

        assertThat(file.getName())
                .isNotEqualTo(newName);

        mockServer
                .when(uploadMediaFile(newName))
                .respond(response(expectedResponse));

        val result = client.uploadMediaFile(file.toPath(), newName);

        assertThat(result).isEqualTo(expectedResponse);
        mockServer.verify(uploadMediaFile(newName));
    }

    @Test
    public void shouldReturnInputStreamWithDownloadedFile() throws IOException {
        val expectedStream = new FileInputStream(SOURCE_FILE);

        mockServer
                .when(getById(SOURCE_FILE_NAME))
                .respond(responseWithAudioFile(SOURCE_FILE));

        val resultStream = client.downloadMediaFile(SOURCE_FILE_NAME);

        mockServer.verify(getById(SOURCE_FILE_NAME));
        assertThat(resultStream).hasSameContentAs(expectedStream);

        resultStream.close();
        expectedStream.close();
    }

    @Test
    public void shouldDeleteRecordingFile() throws IOException {
        val fileName = "file1.wav";

        mockServer
                .when(delete(fileName))
                .respond(response()
                        .withStatusCode(NO_CONTENT_204.code()));

        client.deleteMediaFile(fileName);

        mockServer.verify(delete(fileName));
    }

    private final MediaFile mediaFile1 = MediaFile.builder()
            .name("file1.wav")
            .size(10L)
            .build();

    private final MediaFile mediaFile2 = MediaFile.builder()
            .name("file2.wav")
            .size(500L)
            .build();

    private final MediaFile mediaFile3 = MediaFile.builder()
            .name("file3.wav")
            .size(1500L)
            .modified(ZonedDateTime.parse("2019-12-09T13:44:18.123Z"))
            .build();

    private HttpRequest uploadMediaFile(String fileName) {
        val contentDisposition = "form-data; name=\"mediafile\"; filename=\"" + fileName + "\"";

        return request()
                .withMethod(POST.name())
                .withPath("/" + API_KEY + "/" + getBasePath())
                .withQueryStringParameters(param(PARAM_API_SECRET, API_SECRET))
                .withBody(subString(contentDisposition));
    }
}
