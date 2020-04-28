package com.apidaze.sdk.client.sipusers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SipUsers {
    List<SipUser> getSipUsers() throws IOException;

    SipUser createSipUser(String username,
                          String name,
                          String emailAddress,
                          String internalCallerIdNumber,
                          String externalCallerIdNumber) throws IOException;

    Optional<SipUser> getSipUser(Long id) throws IOException;

    SipUser updateSipUser(Long id,
                          String name,
                          String internalCallerIdNumber,
                          String externalCallerIdNumber) throws IOException;

    SipUser updateSipUser(Long id,
                          String name,
                          String internalCallerIdNumber,
                          String externalCallerIdNumber,
                          boolean resetPassword) throws IOException;

    void deleteSipUser(Long id) throws IOException;

    Optional<SipUserStatus> getSipUserStatus(Long id) throws IOException;

    Optional<SipUser> resetSipUserPassword(Long id) throws IOException;
}
