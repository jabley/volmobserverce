/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.devrep.repository.api.devices;

import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.devrep.repository.api.devices.logging.HeadersEntry;
import com.volantis.devrep.repository.api.devices.logging.UnknownDevicesLogger;
import com.volantis.mcs.http.HTTPHeadersHelper;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.log.LogDispatcher;
import our.apache.commons.httpclient.Header;
import our.apache.commons.httpclient.HeaderElement;
import our.apache.commons.httpclient.HttpException;
import our.apache.commons.httpclient.NameValuePair;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A Devices Helper class.
 * <p/>
 * Having this helper class define public static methods isn't great OO.
 * However, the alternatives aren't great (Singleton, common superclass, etc.)
 * This 'technique' is thread-safe and quick and easy which has the disadvantage
 * of no information hiding.
 */
public class DevicesHelper {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(DevicesHelper.class);

    /**
     * The UAProf prefix.
     */
    public static final String UAPROF_PREFIX = "Profile";

    /**
     * The X-WAP-PROFILE secondary header id.
     */
    public static final String X_WAP_PROFILE = "X-WAP-PROFILE";

    /**
     * Get the requested device from the DeviceRepositoryManager.
     *
     * @param connection RepositoryConnection to get the device with
     * @param deviceName String name of the device to get
     * @param accessor   the device repository accessor.
     * @return requested InternalDevice
     * @throws RepositoryException if the named device cannot be found
     */
    public static DefaultDevice getDevice(RepositoryConnection connection,
                                           String deviceName,
                                           DeviceRepositoryAccessor accessor)
        throws RepositoryException {

        return accessor.getDeviceFallbackChain(connection, deviceName);
    }

    /**
     * Get the device name using the headers in the servlet request.
     *
     * @param connection           Connection to the repository
     * @param headers              The servlet request
     * @param accessor             the device repository accessor.
     * @param unknownDevicesLogger the logger for unknown or abstract devices
     * @return The device object
     * @throws RepositoryException A problem in the repository
     */
    public static DeviceIdentificationResult getDevice(
                final RepositoryConnection connection,
                final HttpHeaders headers,
                final DeviceRepositoryAccessor accessor,
                final UnknownDevicesLogger unknownDevicesLogger)
            throws RepositoryException {
        return getDevice(connection,
                         headers,
                         accessor,
                         unknownDevicesLogger,
                         null);
    }

    /**
     * Get the device name using the headers in the servlet request.
     *
     * If the device is unknown, fallback to the provided default device
     *
     *
     * @param connection           Connection to the repository
     * @param headers              The servlet request
     * @param accessor             the device repository accessor.
     * @param unknownDevicesLogger the logger for unknown or abstract devices
     * @param defaultDeviceName    device name to be used as fallback for unknown devices
     * @return The device object
     * @throws RepositoryException A problem in the repository
     */
    public static DeviceIdentificationResult getDevice(
                final RepositoryConnection connection,
                final HttpHeaders headers,
                final DeviceRepositoryAccessor accessor,
                final UnknownDevicesLogger unknownDevicesLogger,
                final String defaultDeviceName)
            throws RepositoryException {

        final Set headersUsed = new HashSet();
        // Get the optional user agent headser.
        String agent = headers.getHeader("User-Agent");
        headersUsed.add("User-Agent");

        if (agent == null) {
            // Check USER_AGENT if User-Agent not found.
            if (logger.isDebugEnabled()) {
                logger.debug("User-Agent not found," + " trying USER_AGENT");
            }
            agent = headers.getHeader("USER_AGENT");
            headersUsed.add("User_Agent");
        }

        if (logger.isDebugEnabled()) {
            final Enumeration enumeration = headers.getHeaders("User-Agent");
            for (int i = 0; enumeration.hasMoreElements(); i++) {
                logger.debug("User-Agent Header " + i + ": " +
                    enumeration.nextElement());
            }
        }

        // Get some other optional headers.
        final String uapixels = headers.getHeader("UA-pixels");
        final String uacolor = headers.getHeader("UA-color");
        final String uacpu = headers.getHeader("UA-CPU");
        final String uaos = headers.getHeader("UA-OS");
        headersUsed.add("UA-pixels");
        headersUsed.add("UA-color");
        headersUsed.add("UA-CPU");
        headersUsed.add("UA-OS");

        // Create a StringBuffer which we will use to construct the string
        // to check for in the database.
        final StringBuffer ua = new StringBuffer();

        // Create a user agent string ua which encapsulates the user agent
        // string and a couple of other optional headers.
        if (agent != null) {
            ua.append(agent);
        }

        if (uapixels != null) {
            ua.append(':').append(uapixels);
        }

        if (uacolor != null) {
            ua.append(':').append(uacolor);
        }

        if (uacpu != null) {
            ua.append(':').append(uacpu);
        }

        if (uaos != null) {
            ua.append(':').append(uaos);
        }

        // Log User Agent
        if (logger.isDebugEnabled()) {
            logger.debug("User Agent is " + ua);
        }

        // Try the string that we constructed from the ua as long as it is not
        // empty.
        String deviceName = null;
        if (ua.length() > 0) {
            deviceName = accessor.retrieveMatchingDeviceName(ua.toString());
        }

        boolean unknownDevice = false;

        // If deviceName is not found then we need to default to something.
        if (deviceName == null) {
            logger.warn("ua-device-not-found", new Object[]{ua});

            boolean hasMimeType = false;

            String[] mimeTypes = HTTPHeadersHelper.getAcceptMimeTypes(headers);
            if (mimeTypes != null) {
                for (int i = 0; !hasMimeType && (i < mimeTypes.length); i++) {
                    hasMimeType = (mimeTypes[i].indexOf("wap.wml") >= 0);
                }
            }

            if (hasMimeType) {
                deviceName = "WAP-Handset";
            } else if (null != defaultDeviceName) {
                deviceName = defaultDeviceName;
            } else {
                deviceName = "PC";
            }
            unknownDevice = true;
            if (unknownDevicesLogger != null) {
                // log unknown device
                try {
                    unknownDevicesLogger.appendEntry(new HeadersEntry(deviceName,
                        UnknownDevicesLogger.DEVICE_TYPE_UNKNOWN, headers));
                } catch (IOException e) {
                    logger.warn("cannot-write-unknown-devices-log-file");
                }
            }
            logger.info("device-name-info", new Object[]{deviceName});
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Primary device name is " + deviceName);
        }

        // Get the device.  Note that this is called using the connection, not
        // the localConnection.
        DefaultDevice dev = getDevice(connection, deviceName, accessor);
        if (dev == null) {
            throw new IllegalStateException(
                "Cannot find device object for name " + deviceName);
        }

        String secondary = dev.getSecondaryIDHeaderName();
        // Follow any secondary paths
        Set cyclicReferenceMap = new HashSet();
        while (secondary != null) {
            String hdr;
            if (secondary.equalsIgnoreCase("NN-Profile")) {
                hdr = null;
                // get the possible profile header names
                final SortedSet profileHeaderNames =
                    getProfileHeaderNames(headers, headersUsed);

                // check if any of them contains a profile URL we can recognise
                for (Iterator iter = profileHeaderNames.iterator();
                        iter.hasNext() && hdr == null; ) {

                    // get the profile URL's stored in the NN-Profile header
                    final String profileHeaderName = (String) iter.next();
                    final List profileUrls =
                        getProfileUrls(headers, profileHeaderName);
                    headersUsed.add(profileHeaderName);

                    // iterate over the profile URL's and see if there is a
                    // matching device
                    String secondaryDeviceName = null;
                    for (Iterator urlsIter = profileUrls.iterator();
                            urlsIter.hasNext() && secondaryDeviceName == null; ) {

                        final String profileUrl = (String) urlsIter.next();
                        secondaryDeviceName = getDeviceName(
                            accessor, secondary, profileUrl, dev.getName());
                        if (deviceName != null) {
                            hdr = profileUrl;
                        }
                    }
                }
            } else {
                headersUsed.add(secondary);
                hdr = headers.getHeader(secondary);
            }
            DefaultDevice newDev = dev.getSecondaryDevice(hdr);
            if (newDev == null) {
                deviceName =
                    getDeviceName(accessor, secondary, hdr, dev.getName());
                if (deviceName != null) {
                    newDev = getDevice(connection, deviceName, accessor);
                    if (newDev != null) {
                        if (!cyclicReferenceMap.add(newDev.getName())) {
                            logger.warn("cyclic-secondary-header",
                                new Object[]{dev.getName(), secondary});
                            // set the secondary to null so that we break out
                            // of the loop
                            secondary = null;
                        } else {
                            dev.addSecondaryDevice(hdr, newDev);
                            dev = newDev;
                            secondary = dev.getSecondaryIDHeaderName();
                            if (logger.isDebugEnabled()) {
                                logger.debug("Secondary device name is " +
                                    dev.getName());
                            }
                        }
                    } else {
                        secondary = null;
                    }
                } else {
                    secondary = null;
                }
            } else {
                if (!cyclicReferenceMap.add(newDev.getName())) {
                    logger.warn("cyclic-secondary-header",
                        new Object[]{dev.getName(), secondary});
                    // set the secondary to null so that we break out
                    // of the loop
                    secondary = null;
                } else {
                    dev = newDev;
                    secondary = dev.getSecondaryIDHeaderName();
                    if (logger.isDebugEnabled()) {
                        logger.debug("Secondary device name is " +
                            dev.getName());
                    }
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Device name is " + dev.getName());
        }

        if (!unknownDevice &&
            "abstract_device".equals(dev.getComputedPolicyValue("entrytype"))) {
            // log abstract device
            if (unknownDevicesLogger != null) {
                try {
                    unknownDevicesLogger.appendEntry(
                        new HeadersEntry(dev.getName(),
                            UnknownDevicesLogger.DEVICE_TYPE_ABSTRACT, headers));
                } catch (IOException e) {
                    logger.warn("cannot-write-unknown-devices-log-file");
                }
            }
        }
        return new DeviceIdentificationResult(dev, headersUsed);
    }

    /**
     * Generate a (secondary identification) user agent string in the form:
     * <p/>
     * <pre>
     * '&lt;header>: &lt;headerValue>[ &lt;device name>]'
     * </pre>
     * <p/>
     * which is used to retrieve a device name or null if no matching device
     * name was found. Note that a header named {@link #X_WAP_PROFILE} will
     * automatically be treated as if {@link #UAPROF_PREFIX} was supplied.
     *
     * @param accessor    the device repository accessor.
     * @param headerName  the name of the header for secondary identification,
     *                    must not be null
     * @param headerValue the value of the header for secondary identification
     * @param deviceName  the name of the device being followed, For example,
     *                    'Blazer-3_0'
     * @return the device name that was matched using the uraprofURL, or null
     *         if none was found.
     * @throws RepositoryException A problem in the repository
     */
    public static String getDeviceName(
                final DeviceRepositoryAccessor accessor,
                String headerName,
                final String headerValue,
                final String deviceName)
            throws RepositoryException {

        // As required by Requirement 23, the header name must be "normalized"
        // (to lower case) in order to perform the header matches.
        if (X_WAP_PROFILE.equalsIgnoreCase(headerName) ||
                "NN-Profile".equalsIgnoreCase(headerName)) {
            headerName = UAPROF_PREFIX;
        }

        final StringBuffer buff = new StringBuffer(headerName.length() + 2 +
            (headerValue != null ? headerValue.length() : 0) +
            (deviceName != null ? 1 + deviceName.length() : 0));

        buff.append(headerName.toLowerCase()).append(": ").append(headerValue);

        if (deviceName != null) {
            buff.append(" ").append(deviceName);
        }

        final String userAgent = buff.toString();

        if (logger.isDebugEnabled()) {
            logger.debug("Loading secondary device, pattern is " + userAgent);
        }

        return accessor.retrieveMatchingDeviceName(userAgent);
    }

    /**
     * Get the CC/PP Exchange profile header names found.
     * 
     * <p>If no valid CC/PP Exchange namespace declaration found then returns
     * the list of NN-profile header names found among the headers as there are
     * devices that doesn't follow the CC/PP Exchange specification correctly.
     * </p>
     *
     * @param headers     the headers
     * @param headersUsed
     * @return the list of profile header names found.
     */
    public static SortedSet getProfileHeaderNames(final HttpHeaders headers,
                                                     final Set headersUsed) {

        headersUsed.add("Opt");

        // collect namespaces
        final List namespaces = collectNamespaces(headers);

        final SortedSet profileHeaderNames =
            new TreeSet(String.CASE_INSENSITIVE_ORDER);
        if (namespaces.size() != 0) {
            for (Iterator iter = namespaces.iterator(); iter.hasNext();) {
                final String namespace = (String) iter.next();
                // add *-profile header with this namespace
                profileHeaderNames.add(namespace + "-profile");
            }
        } else {
            // go through the header names and collect every NN-profile header
            // name
            for (Enumeration headerNames = headers.getHeaderNames();
                 headerNames.hasMoreElements();) {
                final String headerName = (String) headerNames.nextElement();
                if (headerName.length() == "-profile".length() + 2) {
                    final char ch1 = headerName.charAt(0);
                    final char ch2 = headerName.charAt(1);
                    if (ch1 >= '0' && ch1 <= '9' && ch2 >= '0' && ch2 <= '9') {
                        profileHeaderNames.add(headerName);
                    }
                }
            }
        }
        return profileHeaderNames;
    }

    /**
     * Returns the list of profile URL's found in headers with the specified
     * name.
     *
     * @param headers    the available headers
     * @param headerName the name of the headers we are interested in
     * @return the list of the extracted profile URL's
     */
    public static List getProfileUrls(final HttpHeaders headers,
                                         final String headerName) {

        // no headers, no URL's
        if (headers == null) {
            return Collections.EMPTY_LIST;
        }
        final List profileUrls = new LinkedList();
        // go through the headers with the right name
        for (Enumeration enumeration = headers.getHeaders(headerName);
             enumeration != null && enumeration.hasMoreElements();) {

            final String headerValue = (String) enumeration.nextElement();
            final Header header = new Header(headerName, headerValue);
            try {
                // check the elements in the value
                final HeaderElement[] elements = header.getValues();
                for (int i = 0; i < elements.length; i++) {
                    final HeaderElement element = elements[i];
                    // no element value expected here
                    if (element.getValue() == null) {
                        final String elementName = element.getName();
                        // if the value contains a colon then it is a profile
                        // URL, otherwise a profile-diff name
                        if (elementName.indexOf(':') >= 0) {
                            // only interested in profile URLs
                            String url = elementName;
                            if (url.startsWith("\"")) {
                                // trim off the quote chars
                                url = url.substring(1, url.length() - 1);
                            }
                            // add it to the beginning of the list, because
                            // according to the CC/PP Exchange specifications:
                            // "The latest reference in the Profile header
                            // field-value has the highest priority."
                            profileUrls.add(0, url);
                        }
                    }
                }
            } catch (HttpException e) {
                // ignore this header
                logger.warn("parse-header-error", headerName);
            }
        }
        return profileUrls;
    }

    /**
     * Collects the namespace ID's declared in the CC/PP Exchange Opt headers
     * for *-profile headers.
     *
     * @param headers     the headers to check
     * @return the list of namespace ID's
     */
    private static List collectNamespaces(final HttpHeaders headers) {
        if (headers == null) {
            return Collections.EMPTY_LIST;
        }
        final List namespaces = new LinkedList();
        // iterate over the Opt headers (if there is any)
        for (Enumeration enumeration = headers.getHeaders("Opt");
             enumeration != null && enumeration.hasMoreElements();) {

            final String optValue = (String) enumeration.nextElement();
            final Header header = new Header("Opt", optValue);
            try {
                // iterate over the values to collect namespace ID's
                final HeaderElement[] values = header.getValues();
                for (int i = 0; i < values.length; i++) {
                    final HeaderElement element = values[i];
                    // remove the optional " characters from the beginning and
                    // the end of the name
                    String elementName = element.getName();
                    if (elementName.length() > 1 &&
                        elementName.charAt(0) == '"' &&
                        elementName.charAt(elementName.length() - 1) == '"') {
                        elementName =
                            elementName.substring(1, elementName.length() - 1);
                    }
                    // we are interested in the CC/PP Exchange namespace only
                    if (element.getValue() == null &&
                        elementName.equalsIgnoreCase(
                            "http://www.w3.org/1999/06/24-CCPPexchange")) {
                        final NameValuePair parameter =
                            element.getParameterByName("ns");
                        String paramValue = parameter.getValue();
                        if (paramValue != null) {
                            String namespace = null;
                            paramValue = paramValue.trim();
                            // check if it is a 2 digit number
                            if (paramValue.length() == 2) {
                                final char ch1 = paramValue.charAt(0);
                                final char ch2 = paramValue.charAt(1);
                                if (ch1 >= '0' && ch1 <= '9' &&
                                    ch2 >= '0' && ch2 <= '9') {
                                    namespace = paramValue;
                                }
                            }
                            if (namespace != null) {
                                namespaces.add(namespace);
                            } else {
                                if (logger.isDebugEnabled()) {
                                    logger.debug(
                                        "ns parameter has an invalid format, " +
                                            "ignoring: \"" + paramValue + "\"");
                                }
                            }
                        }
                    }
                }
            } catch (HttpException e) {
                // ignore this header
                logger.warn("parse-header-error", "Opt");
            }
        }
        return namespaces;
    }
}
