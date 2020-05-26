package com.apidaze.sdk.client.sipusers;

import com.apidaze.sdk.client.http.HttpResponseException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * The interface to manage SIP Users
 */
public interface SipUsers {

    /**
     * Returns a list of all SIP Users.
     *
     * @return list of {@link SipUser} instances
     * @throws IOException
     * @throws HttpResponseException
     */
    List<SipUser> getSipUsers() throws IOException;


    /**
     * Creates a SIP User for devices to register to Apidaze Freeswitches.
     *
     * @param username               username for the new SIP User
     * @param name                   name for the new SIP User
     * @param emailAddress           email address for the Sip User
     * @param internalCallerIdNumber
     * @param externalCallerIdNumber
     * @return a created {@link SipUser} instance
     * @throws IOException
     * @throws HttpResponseException
     */
    SipUser createSipUser(String username,
                          String name,
                          String emailAddress,
                          String internalCallerIdNumber,
                          String externalCallerIdNumber) throws IOException;

    /**
     * Returns the details of a single SIP User.
     *
     * @param id the id of the user to fetch
     * @return SIP User object if present, otherwise {@code Optional.empty()}
     * @throws IOException
     * @throws HttpResponseException
     */
    Optional<SipUser> getSipUser(Long id) throws IOException;

    /**
     * Updates a SIP User.
     *
     * @param id                     id of the user to update
     * @param name                   Updated name for the the SIP User.
     *                               Use {@code null} to ignore the updating of this field.
     * @param internalCallerIdNumber Updated Internal Caller ID Number for the SIP User.
     *                               Use {@code null} to ignore the updating of this field.
     * @param externalCallerIdNumber Updated External Caller ID Number for the SIP User.
     *                               Use {@code null} to ignore the updating of this field.
     * @return an updated {@link SipUser} instance
     * @throws IOException
     * @throws HttpResponseException
     */
    SipUser updateSipUser(Long id,
                          String name,
                          String internalCallerIdNumber,
                          String externalCallerIdNumber) throws IOException;

    /**
     * Updates a SIP User with the possibility to reset the password.
     *
     * @param id                     id of the user to update
     * @param name                   Updated name for the the SIP User.
     *                               Use {@code null} to ignore the updating of this field.
     * @param internalCallerIdNumber Updated Internal Caller ID Number for the SIP User.
     *                               Use {@code null} to ignore the updating of this field.
     * @param externalCallerIdNumber Updated External Caller ID Number for the SIP User.
     *                               Use {@code null} to ignore the updating of this field.
     * @param resetPassword          If true, a new password will be generated for the SIP User.
     * @return an updated {@link SipUser} instance
     * @throws IOException
     * @throws HttpResponseException
     */
    SipUser updateSipUser(Long id,
                          String name,
                          String internalCallerIdNumber,
                          String externalCallerIdNumber,
                          boolean resetPassword) throws IOException;

    /**
     * Delete a SIP User registered to an Apidaze Freeswitch.
     * @param id id of the user to delete
     * @throws IOException
     * @throws HttpResponseException
     */
    void deleteSipUser(Long id) throws IOException;

    /**
     * Shows the status of a SIP User.
     * @param id id of the user to check active registration status
     * @return SIP User status if present, otherwise {@code Optional.empty()}
     * @throws IOException
     * @throws HttpResponseException
     */
    Optional<SipUserStatus> getSipUserStatus(Long id) throws IOException;

    /**
     * Resets the password for a SIP User.
     * @param id id of the user whose password will be reset
     * @return SIP User object with new password if the user exists, otherwise {@code Optional.empty()}
     * @throws IOException
     * @throws HttpResponseException
     */
    Optional<SipUser> resetSipUserPassword(Long id) throws IOException;
}
