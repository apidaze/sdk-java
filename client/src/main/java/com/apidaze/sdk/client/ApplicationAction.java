package com.apidaze.sdk.client;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.calls.Call;
import com.apidaze.sdk.client.calls.Calls;
import com.apidaze.sdk.client.calls.CallsClient;
import com.apidaze.sdk.client.cdrhttphandlers.CdrHttpHandler;
import com.apidaze.sdk.client.cdrhttphandlers.CdrHttpHandlers;
import com.apidaze.sdk.client.cdrhttphandlers.CdrHttpHandlersClient;
import com.apidaze.sdk.client.common.PhoneNumber;
import com.apidaze.sdk.client.common.URL;
import com.apidaze.sdk.client.externalscripts.ExternalScript;
import com.apidaze.sdk.client.externalscripts.ExternalScripts;
import com.apidaze.sdk.client.externalscripts.ExternalScriptsClient;
import com.apidaze.sdk.client.mediafiles.MediaFile;
import com.apidaze.sdk.client.mediafiles.MediaFiles;
import com.apidaze.sdk.client.mediafiles.MediaFilesClient;
import com.apidaze.sdk.client.messages.Message;
import com.apidaze.sdk.client.messages.MessageClient;
import com.apidaze.sdk.client.recordings.Recordings;
import com.apidaze.sdk.client.recordings.RecordingsClient;
import com.apidaze.sdk.client.sipusers.SipUser;
import com.apidaze.sdk.client.sipusers.SipUserStatus;
import com.apidaze.sdk.client.sipusers.SipUsers;
import com.apidaze.sdk.client.sipusers.SipUsersClient;
import com.apidaze.sdk.client.validates.CredentialsValidator;
import com.apidaze.sdk.client.validates.CredentialsValidatorClient;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

/**
 * The class used to make calls, send text messages and other application specific functions.
 */
@AllArgsConstructor(access = PRIVATE)
public class ApplicationAction implements
        Calls,
        Message,
        ExternalScripts,
        Recordings,
        CredentialsValidator,
        CdrHttpHandlers,
        SipUsers,
        MediaFiles {

    private final Calls calls;
    private final ExternalScripts externalScripts;
    private final Message message;
    private final Recordings recordings;
    private final CredentialsValidator credentialsValidator;
    private final CdrHttpHandlers cdrHttpHandlers;
    private final SipUsers sipUsers;
    private final MediaFiles mediaFiles;

    /**
     * Initiates an object of this class.
     *
     * @param credentials an application specific key and secret
     * @return An initiated object of this class
     */
    public static ApplicationAction create(Credentials credentials) {
        requireNonNull(credentials, "Credentials must not be null.");
        return new ApplicationAction(
                CallsClient.create(credentials),
                ExternalScriptsClient.create(credentials),
                MessageClient.create(credentials),
                RecordingsClient.create(credentials),
                CredentialsValidatorClient.create(credentials),
                CdrHttpHandlersClient.create(credentials),
                SipUsersClient.create(credentials),
                MediaFilesClient.create(credentials));
    }

    @Override
    public UUID createCall(PhoneNumber callerId, String origin, String destination, CallType callType) throws IOException {
        return calls.createCall(callerId, origin, destination, callType);
    }

    @Override
    public List<Call> getCalls() throws IOException {
        return calls.getCalls();
    }

    @Override
    public Optional<Call> getCall(UUID id) throws IOException {
        return calls.getCall(id);
    }

    @Override
    public void deleteCall(UUID id) throws IOException {
        calls.deleteCall(id);
    }

    @Override
    public String sendTextMessage(PhoneNumber from, PhoneNumber to, String body) throws IOException {
        return message.sendTextMessage(from, to, body);
    }

    @Override
    public List<ExternalScript> getExternalScripts() throws IOException {
        return externalScripts.getExternalScripts();
    }

    @Override
    public Optional<ExternalScript> getExternalScript(Long id) throws IOException {
        return externalScripts.getExternalScript(id);
    }

    @Override
    public ExternalScript updateExternalScript(Long id, String name, URL url) throws IOException {
        return externalScripts.updateExternalScript(id, name, url);
    }

    @Override
    public ExternalScript updateExternalScriptUrl(Long id, URL url) throws IOException {
        return externalScripts.updateExternalScriptUrl(id, url);
    }

    @Override
    public List<String> getRecordingsList() throws IOException {
        return recordings.getRecordingsList();
    }

    @Override
    public InputStream downloadRecording(String sourceFileName) throws IOException {
        return recordings.downloadRecording(sourceFileName);
    }

    @Override
    public void downloadRecordingToFileAsync(String sourceFileName, Path target, DownloadCallback downloadCallback) {
        recordings.downloadRecordingToFileAsync(sourceFileName, target, downloadCallback);
    }

    @Override
    public void downloadRecordingToFileAsync(String sourceFileName, Path target, boolean replaceExisting, DownloadCallback downloadCallback) {
        recordings.downloadRecordingToFileAsync(sourceFileName, target, replaceExisting, downloadCallback);
    }

    @Override
    public File downloadRecordingToFile(String sourceFileName, Path target) throws IOException {
        return recordings.downloadRecordingToFile(sourceFileName, target);
    }

    @Override
    public File downloadRecordingToFile(String sourceFileName, Path target, boolean replaceExisting) throws IOException {
        return recordings.downloadRecordingToFile(sourceFileName, target, replaceExisting);
    }

    @Override
    public void deleteRecording(String fileName) throws IOException {
        recordings.deleteRecording(fileName);
    }

    @Override
    public boolean validateCredentials() throws IOException {
        return credentialsValidator.validateCredentials();
    }

    @Override
    public List<CdrHttpHandler> getCdrHttpHandlers() throws IOException {
        return cdrHttpHandlers.getCdrHttpHandlers();
    }

    @Override
    public CdrHttpHandler createCdrHttpHandler(String name, URL url) throws IOException {
        return cdrHttpHandlers.createCdrHttpHandler(name, url);
    }

    @Override
    public CdrHttpHandler updateCdrHttpHandler(Long id, String name, URL url) throws IOException {
        return cdrHttpHandlers.updateCdrHttpHandler(id, name, url);
    }

    @Override
    public CdrHttpHandler updateCdrHttpHandlerName(Long id, String name) throws IOException {
        return cdrHttpHandlers.updateCdrHttpHandlerName(id, name);
    }

    @Override
    public CdrHttpHandler updateCdrHttpHandlerUrl(Long id, URL url) throws IOException {
        return cdrHttpHandlers.updateCdrHttpHandlerUrl(id, url);
    }

    @Override
    public void deleteCdrHttpHandler(Long id) throws IOException {
        cdrHttpHandlers.deleteCdrHttpHandler(id);
    }

    @Override
    public List<SipUser> getSipUsers() throws IOException {
        return sipUsers.getSipUsers();
    }

    @Override
    public SipUser createSipUser(String username,
                                 String name,
                                 String emailAddress,
                                 String internalCallerIdNumber,
                                 String externalCallerIdNumber) throws IOException {
        return sipUsers.createSipUser(username, name, emailAddress, internalCallerIdNumber, externalCallerIdNumber);
    }

    @Override
    public Optional<SipUser> getSipUser(Long id) throws IOException {
        return sipUsers.getSipUser(id);
    }

    @Override
    public SipUser updateSipUser(Long id, String name, String internalCallerIdNumber, String externalCallerIdNumber) throws IOException {
        return sipUsers.updateSipUser(id, name, internalCallerIdNumber, externalCallerIdNumber);
    }

    @Override
    public SipUser updateSipUser(Long id, String name, String internalCallerIdNumber, String externalCallerIdNumber, boolean resetPassword) throws IOException {
        return sipUsers.updateSipUser(id, name, internalCallerIdNumber, externalCallerIdNumber, resetPassword);
    }

    @Override
    public void deleteSipUser(Long id) throws IOException {
        sipUsers.deleteSipUser(id);
    }

    @Override
    public Optional<SipUserStatus> getSipUserStatus(Long id) throws IOException {
        return sipUsers.getSipUserStatus(id);
    }

    @Override
    public Optional<SipUser> resetSipUserPassword(Long id) throws IOException {
        return sipUsers.resetSipUserPassword(id);
    }

    @Override
    public Result<String> getMediaFileNames() throws IOException {
        return mediaFiles.getMediaFileNames();
    }

    @Override
    public Result<String> getMediaFileNames(String filter, Integer maxItems, String lastToken) throws IOException {
        return mediaFiles.getMediaFileNames(filter, maxItems, lastToken);
    }

    @Override
    public Result<MediaFile> getMediaFiles() throws IOException {
        return mediaFiles.getMediaFiles();
    }

    @Override
    public Result<MediaFile> getMediaFiles(String filter, Integer maxItems, String lastToken) throws IOException {
        return mediaFiles.getMediaFiles(filter, maxItems, lastToken);
    }

    @Override
    public String uploadMediaFile(Path filePath) throws IOException {
        return mediaFiles.uploadMediaFile(filePath);
    }

    @Override
    public String uploadMediaFile(Path filePath, String fileName) throws IOException {
        return mediaFiles.uploadMediaFile(filePath, fileName);
    }

    @Override
    public InputStream downloadMediaFile(String fileName) throws IOException {
        return mediaFiles.downloadMediaFile(fileName);
    }

    @Override
    public MediaFileSummary getMediaFileSummary(String fileName) throws IOException {
        return mediaFiles.getMediaFileSummary(fileName);
    }

    @Override
    public void deleteMediaFile(String fileName) throws IOException {
        mediaFiles.deleteMediaFile(fileName);
    }
}
