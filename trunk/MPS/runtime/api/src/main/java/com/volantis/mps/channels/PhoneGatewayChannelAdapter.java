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

package com.volantis.mps.channels;

import com.volantis.mps.message.MessageException;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import our.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This is a partial implementation of a {@link MessageChannel} for use with
 * any channel that is initialised with a host url and default country code.
 *
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class PhoneGatewayChannelAdapter extends MessageChannel {

    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(PhoneGatewayChannelAdapter.class);

    /**
     * The exception localizer instance for this class.
     */
    private static final ExceptionLocalizer localizer =
            LocalizationFactory.createExceptionLocalizer(
                    PhoneGatewayChannelAdapter.class);

    /**
     * Name of the initialisation property used to store the url
     */
    public static final String URL = "url";

    /**
     * Name of the initialisation property used to store the default country
     * code.
     */
    public static final String DEFAULT_COUNTRY_CODE = "default-country-code";

    /**
     * The prefix used to indicate an international dialling code
     */
    protected static final String INTERNATIONAL_PREFIX = "+";

    /**
     * The prefix for an {@link #INTERNATIONAL_PREFIX} encoded in a form that
     * is valid for use in URLs
     */
    protected static final String ENCODED_INTERNATIONAL_PREFIX = "%2B";

    /**
     * The URL and port that the send should be directed to where some
     * implementation to further handle the send will be listening.
     */
    protected final String serverURL;

    /**
     * The default country code for MSISDN
     */
    protected final String defaultCountryCode;

    /**
     * The helper object used for making requests.
     */
    protected final HTTPHelper httpHelper;

    /**
     * Initialise a new instance of PhoneGatewayChannelAdapter
     *
     * @param channelName The name of the channel
     * @param channelInfo The parameters defined for the channel
     *
     * @throws MessageException If there is a problem initialising the channel
     */
    public PhoneGatewayChannelAdapter(String channelName,
                                      Map channelInfo) throws MessageException {
        this.channelName = channelName;
        serverURL = (String) channelInfo.get(URL);
        defaultCountryCode = (String) channelInfo.get(DEFAULT_COUNTRY_CODE);

        // Ensure the server to send to has been initialised and is not
        // an empty string
        if (serverURL == null || serverURL.equals("")) {
            throw new MessageException(
                    localizer.format("server-url-missing-invalid"));
        }

        // Obtain the HTTP helper to use for sending requests
        httpHelper = HTTPHelper.getDefaultInstance();
    }

    /**
     * This will make a request to the server and use the parameters provided
     * as an <strong>encoded</strong> string.
     *
     * @param paramString The parameter string to use as the GET part of the
     *                    request.  Note, these must already be encoded.
     *
     * @return True if the send succeeded, false if not.
     *
     * @throws IOException           If there is a problem in making the
     *                               connection.
     * @throws MalformedURLException If the URL being created for the request
     *                               is invalid.
     */
    protected boolean sendAsGetRequest(String paramString)
            throws IOException, MalformedURLException {
        // Ensure the parameters supplied as the key/value pairs for the get
        // request exist
        if (paramString == null || paramString.equals("")) {
            throw new MalformedURLException(
                    "The paramter string needs to be specified");
        }

        // Construct the URL by building up the component parts.
        // Start with the server.  Assumed to have a protocol specified
        StringBuffer fullURL = new StringBuffer(serverURL);

        if (!serverURL.endsWith("/")) {
            // If there is no end slash at the end of the server URL, provide
            // one so that the addition of parameters is fine
            fullURL.append("/");
        }
        if (!paramString.startsWith("?")) {
            // Add a ? to indicate the start of the parameters if there is not
            // one there already
            fullURL.append("?");
        }

        // Add the paramters (these should already be encoded)
        fullURL.append(paramString);

        // Now (finally!) create a http connection based on the url built up
        GetMethod method = new GetMethod(fullURL.toString());

        boolean succeeded = false;

        try {
            // Execute the request, synchonizing on the HTTPHelper to ensure
            // it's safe for use by multiple threads.
            synchronized(httpHelper) {
                int statusCode = httpHelper.executeRequest(method,
                        fullURL.toString());
                
                // Convert the response code to a binary "did this send work or not"
                succeeded = (statusCode == HttpURLConnection.HTTP_OK);
            }
        } finally {
            // Release the connection.
            method.releaseConnection();
        }

        // Return send status
        return succeeded;
    }

    /**
     * Given a list of keyValuePairs, construct a parameter string of all pairs
     * concatenated together.  If required, the key and value will be encoded
     * to ensure URL legal characters in the returned string.
     * <p>
     * Each parameter key-value pair will be assumed to be a two valued
     * {@link String} array, although they do not have to be encoded for use
     * as URLs as this method will do so where necessary if encode is true.
     * <p>
     * An example would be<br />
     * <pre>
     * String[] pairOne = new String[] {"key", "some value"};
     * String[] pairTwo = new String[] {"another key", "value"};
     * List parameters = new ArrayList();
     * parameters.add(pairOne);
     * parameters.add(pairTwo);
     * </pre>
     * which should return something of the form (depending on encoding)<br />
     * <pre>
     * key=some+value&another+key=value
     * </pre>
     *
     * @param keyValuePairs The list of key-value pairs to be added to the
     *                      parameter string being constructed.
     * @param encode        True if the keys and values should be encoded to
     *                      ensure they only contain characters that are valid
     *                      in a URL.
     *
     * @return A parameter string containing all key-value pairs concatenated
     *         together.
     */
    protected String constructParamString(List keyValuePairs, boolean encode) {
        // Initialise the buffer with 10 characters for each key-value pair,
        // which is a rough estimate to aid efficiency.
        StringBuffer paramString = new StringBuffer(10 * keyValuePairs.size());

        for (Iterator i = keyValuePairs.iterator(); i.hasNext(); ) {
            String[] pair = (String[]) i.next();
            // Only process if not going to not cause IndexOutOfBounds
            if (pair.length == 2) {
                // Add the key to the buffer, encoding if encode is true
                paramString.append(
                        encode ? URLEncoder.encode(pair[0]) : pair[0]);
                paramString.append("=");
                // Add the value to the buffer, encoding if encode is true
                paramString.append(
                        encode ? URLEncoder.encode(pair[1]) : pair[1]);
                if (i.hasNext()) {
                    paramString.append("&");
                }
            } else {
                logger.warn("pair-parameter-inclusion-failure", pair);
            }
        }
        return paramString.toString();
    }

    /**
     * Format a MMSC MSISDN number.  This converts the number provided to a
     * valiid MSIDSN number using a country code.
     *
     * @param num        The number to format.
     * @param appendType True if a type should be appended to the number.  This
     *                   may be required by some libraries but not others.
     * @param encodePrefix True if the + that represnts the start of the
     *                   international dialing prefix should be encoded.  This
     *                   is required, amongst other things, if the number is to
     *                   be used in URLs.
     *
     * @return A formatted represention of the number provided
     */
    protected String formatMSISDN(String num,
                                  boolean appendType,
                                  boolean encodePrefix) {

        // Jump out if there is no number
        if (num == null) {
            return null;
        }

        StringBuffer buff = new StringBuffer();
        if (num.startsWith(INTERNATIONAL_PREFIX)) {
            // If the number starts with '+' then assume that it is a valid
            // international number
            if (encodePrefix) {
                buff.append(ENCODED_INTERNATIONAL_PREFIX);
                buff.append(num.substring(1));
            } else {
                buff.append(num);
            }
        } else {
            // If we have no country code, give up
            if (defaultCountryCode == null) {
                return null;
            }

            // Start with the country code then add the mobile number
            // without the '0' on the front if there is one
            addPrefix(encodePrefix, buff);
            if (!defaultCountryCode.startsWith(INTERNATIONAL_PREFIX)) {
                // No need to remove any prefix as they forgot to put
                // it on anyway!
                buff.append(defaultCountryCode);
            } else {
                // They remembered but we already potentially encoded it!
                buff.append(defaultCountryCode.substring(1));
            }

            if (num.startsWith("0")) {
                // Remove leading zero if there is one as the country code
                // has been added
                buff.append(num.substring(1));
            } else {
                buff.append(num);
            }
        }

        if (appendType) {
            // If the type needs to be append then add it last
            buff.append("/TYPE=PLMN");
        }

        return buff.toString();
    }

    /**
     * A small utility method that adds the international prefix to the
     * <code>StringBuffer</code> provided.  This will either be added as
     * normal, or if encodePrefix is true, then it will be added in a form
     * that is compatible with URLs.
     *
     * @param encodePrefix True if the prefix that will be added should be
     *                     encoded.
     * @param buff         The buffer to append the prefix to.
     */
    private void addPrefix(boolean encodePrefix, StringBuffer buff) {
        if (encodePrefix) {
            buff.append(ENCODED_INTERNATIONAL_PREFIX);
        } else {
            buff.append(INTERNATIONAL_PREFIX);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	829/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 01-Jul-05	776/2	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 29-Nov-04	243/3	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 17-Nov-04	238/1	pcameron	VBM:2004111608 PublicAPI doc fixes and additions

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 13-Aug-04	155/1	claire	VBM:2004073006 WAP Push for MPS: Servlet to store and retrieve messages

 11-Aug-04	149/3	claire	VBM:2004073005 WAP Push for MPS: New channel adapter, generating messages as URLs, config update

 ===========================================================================
*/
