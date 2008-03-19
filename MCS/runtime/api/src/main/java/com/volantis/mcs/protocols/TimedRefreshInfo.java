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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Store the information about a timed refresh. This contains the time
 * (in seconds) before the refresh and an optional uri to redirect to.
 */
public final class TimedRefreshInfo {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(TimedRefreshInfo.class);

    /**
     * the name of the meta property
     */
    public static final String NAME = "refresh";

    /**
     * The refresh interval in seconds
     */
    private final float refreshInterval;

    /**
     * The optional uri to load when the interval expires.
     */
    private URI uri;

    /**
     * Initialize the object with the specified refresh interval in seconds and
     * the optional uri
     * @param seconds the refresh interval in seconds
     * @param url the optional redirect uri. May be null.
     */
    public TimedRefreshInfo(float seconds, String url)
            throws ProtocolException {
        this.refreshInterval = seconds;
        if (refreshInterval < 0.0) {
            throw new NumberFormatException(EXCEPTION_LOCALIZER.format(
                "negative-number", new Float(refreshInterval)));
        }
        if (url != null) {
            try {
                setURI(url);
            } catch (URISyntaxException e) {
                throw new ProtocolException(EXCEPTION_LOCALIZER.format(
                                "meta-content-invalid-uri", url), e);
            }
        }
    }
    
    /**
     * Sets the URI 
     * @param stringURL uri to set
     * @throws URISyntaxException if error occurs
     */ 
    private void setURI(String stringURL) throws URISyntaxException {                        
        uri = (new URI(stringURL.trim())).normalize();                   
    }

    /**
     * Initialize the object from the string representation of the refresh
     * meta element content.
     * <p>"n[;][uri]"</p>
     * <p>where:</p>
     * <p> n is a positive real number. If a negative number is provided its
     * absolute value is used.</p>
     * <p>the semicolon and uri are optional.</p>
     *
     * @param content the refresh element in the form described
     */
    public TimedRefreshInfo(String content) throws NumberFormatException,
                                                   ProtocolException {
        String timedInterval = content;
        int seperator = content.indexOf(';');
        if (seperator > 0) {
            timedInterval = content.substring(0, seperator);
            if (content.length() > seperator) {
                String tmp = content.substring(seperator + 1).trim();
                if (tmp.length()>0) {

                    try {
                        setURI(tmp);
                    } catch (URISyntaxException e) {
                        throw new ProtocolException(EXCEPTION_LOCALIZER.format(
                                "meta-content-invalid-uri", tmp), e);
                    }
                }
            }
        }
        refreshInterval = Float.parseFloat(timedInterval);
        if (refreshInterval < 0.0) {
            throw new NumberFormatException(EXCEPTION_LOCALIZER.format(
                "negative-number", new Float(refreshInterval)));
        }
    }

    /**
     * Return the refresh interval in seconds
     *
     * @return the refresh interval in seconds
     */
    public int getIntervalInSeconds() {
        return Math.round(refreshInterval);
    }

    /**
     * Return the refresh interval in tenths of a second
     *
     * @return  the refresh interval in tenths of a second
     */
    public int getIntervalInTenthsOfSecond() {
        return Math.round(refreshInterval * 10);
    }

    /**
     * Return the optional refresh uri.
     *
     * @return the refresh uri or null.
     */
    public String getRefreshURL() {
        String result = null;
        if (null != this.uri) {
            result = this.uri.toString();
        }
        return result;
        
    }

    /**
     * Return the content that can be used in a "meta" element content
     * attribute.
     *
     * @param context the page context
     * @return the content that can be used in a "meta" elements content
     * attribute.
     */
    public String getHTMLContent(MarinerPageContext context)
            throws ProtocolException{

        String tmp = getRefreshURL();
        String result = "" + getIntervalInSeconds();
        if (tmp != null && !tmp.equals("")) {
            result = result + ";URL=" 
                + getResolvedRefreshURI(context).toString(); 
        }
        return result;
    }
    
    private URI getResolvedRefreshURI(MarinerPageContext pageContext)
            throws ProtocolException {
        URI requestPathURI = getRequestPathURI(pageContext);
        return requestPathURI.resolve(uri);
    }

    /**
     * Returns the URI path of the request.
     * 
     * @param pageContext The page context.
     * @return the URI path.
     */
    private URI getRequestPathURI(MarinerPageContext pageContext)
            throws ProtocolException {
        
        String requestFileString = pageContext.getRequestURL(false).getFile();

        // Convert the string to a File instance.
        File requestFile = new File(requestFileString);

        // Get the path component of the filename (as String).
        String requestPathString = requestFile.getParent() + "/";

        // Return an URI instance with path component.

        try {
            return new URI(requestPathString);
        } catch (URISyntaxException e) {
            throw new ProtocolException(EXCEPTION_LOCALIZER.format(
                                "meta-content-invalid-uri", requestPathString),
                                        e);
        }
    }    
    
    
}
