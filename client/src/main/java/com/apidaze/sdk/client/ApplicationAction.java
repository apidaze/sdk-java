package com.apidaze.sdk.client;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.calls.ActiveCall;
import com.apidaze.sdk.client.calls.Calls;
import com.apidaze.sdk.client.calls.CallsClient;
import com.apidaze.sdk.client.externalscripts.ExternalScript;
import com.apidaze.sdk.client.externalscripts.ExternalScripts;
import com.apidaze.sdk.client.externalscripts.ExternalScriptsClient;
import com.apidaze.sdk.client.externalscripts.URL;
import com.apidaze.sdk.client.messages.Message;
import com.apidaze.sdk.client.messages.MessageClient;
import com.apidaze.sdk.client.messages.PhoneNumber;
import com.apidaze.sdk.client.recordings.Recordings;
import com.apidaze.sdk.client.recordings.RecordingsClient;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class ApplicationAction implements Calls, Message, ExternalScripts, Recordings {

    private final Calls calls;
    private final ExternalScripts externalScripts;
    private final Message message;
    private final Recordings recordings;

    public static ApplicationAction create(Credentials credentials) {
        requireNonNull(credentials, "Credentials must not be null.");
        return new ApplicationAction(
                CallsClient.create(credentials),
                ExternalScriptsClient.create(credentials),
                MessageClient.create(credentials),
                RecordingsClient.create(credentials));
    }

    @Override
    public UUID createCall(PhoneNumber callerId, String origin, String destination, CallType callType) throws IOException {
        return calls.createCall(callerId, origin, destination, callType);
    }

    @Override
    public List<ActiveCall> getActiveCalls() throws IOException {
        return calls.getActiveCalls();
    }

    @Override
    public ActiveCall getActiveCall(UUID id) throws IOException {
        return calls.getActiveCall(id);
    }

    @Override
    public void deleteActiveCall(UUID id) throws IOException {
        calls.deleteActiveCall(id);
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
    public ExternalScript createExternalScript(String name, URL url) throws IOException {
        return externalScripts.createExternalScript(name, url);
    }

    @Override
    public ExternalScript getExternalScript(Long id) throws IOException {
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
    public void deleteExternalScript(Long id) throws IOException {
        externalScripts.deleteExternalScript(id);
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
}
