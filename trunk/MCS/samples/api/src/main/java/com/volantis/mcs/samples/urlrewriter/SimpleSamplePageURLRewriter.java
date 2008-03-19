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
package com.volantis.mcs.samples.urlrewriter;

import com.volantis.mcs.integration.PageURLRewriter;
import com.volantis.mcs.integration.PageURLDetails;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.context.MarinerRequestContext;

import java.util.HashMap;
import java.util.Collections;
import java.util.Map;
import javax.servlet.ServletContext;

/**
 * This class implements a very trivial PageURLRewriter. This class exists
 * merely to demonstrate how a PageURLRewriter implementation can be used
 * to rewrite urls in MCS generated pages and then translate them back again.
 * The implementation is NOT in any way intended to provide a recommended
 * algorithm for rewriting urls or for persisting rewritten urls.
 *
 * The url rewrite algorithm implemented by this class simply rewrites the
 * url to a jsp followed by an argument that is quite likely to be unique (but
 * not designed or guaranteed to be unique - whereas a production quality
 * PageURLRewriter should ensure that rewritten urls are unique). The
 * algorithm uses the device name in the rewritten url but this is not a
 * necessity in order to provide urls that are unique to individual devices.
 * Neither is it necessary to use a jsp for the rewritten and translation of
 * the url - a jsp is used  for simplicity. Using a servlet for example would
 * allow the rewritten url to be more compact.
 */
public class SimpleSamplePageURLRewriter implements PageURLRewriter {

    /**
     * The prefix or base for all rewritten urls. This url will make the
     * server invoke the jsp that will cause the rewritten url to be
     * translated back into the original.
     */
    private static final String BASE_URL =
            "http://localhost:8080/volantis/SimpleSamplePageURLRewriter.jsp?";

    /**
     * The key with which to store instances of SimpleSamplePageURLRewriter
     * in the application context.
     */
    private static final String SAMPLE_URL_REWRITER =
            "SimpleSamplePageURLRewriter";

    /**
     * A map mapping rewritten urls to their original value.
     */
    private static Map urlMappings =
            Collections.synchronizedMap(new HashMap());

    /**
     * Flag indicating whether or not his instance of
     * SimpleSamplePageURLRewriter has been initialized.
     */
    private static boolean initialized = false;


    /**
     * Set this SimpleSamplePageURLRewriter in the servlet context to allow
     * it to be accessed elsewhere (e.g. a jsp).
     * @param context the ServletContext
     */
    public synchronized void initialize(ServletContext context) {
        if(!initialized) {
            context.setAttribute(SAMPLE_URL_REWRITER, this);
            initialized = true;
        }
    }

    /**
     * Implement the PageURLRewriter.rewriteURL() method.
     *
     * All this implementation does is combine the hashcode of a newly
     * generated object with the device name associated with the request.
     *
     * @param context the MarinerRequestContext
     * @param url the url to be rewritten
     * @param details the PageURLDetails of the url that is to be rewritten
     * @return the rewritten MarinerURL
     */
    public MarinerURL rewriteURL(MarinerRequestContext context,
                                 MarinerURL url,
                                 PageURLDetails details) {
        String deviceName = context.getDeviceName();
        int hashCode = new Object().hashCode();
        String hashString = String.valueOf(hashCode);
        StringBuffer buffer = new StringBuffer(BASE_URL.length() +
                deviceName.length() + hashString.length() + 1);
        buffer.append(BASE_URL).append(hashString).append('=').
                append(deviceName);
        String queryString = buffer.substring(BASE_URL.length());
        urlMappings.put(queryString, url);

        return new MarinerURL(buffer.toString());
    }

    /**
     * Translate a previously rewritten url back to its original. This method
     * is specific to SimpleSamplePageURLRewriter (i.e. not part of the
     * PageURLRewriter interface).
     *
     * This method will simply throw a NullPointerException if the original
     * url cannot be found.
     *
     * @param url the url to translate
     * @return the original url.
     */
    public String translateURL(String url) {
        MarinerURL translatedURL = (MarinerURL) urlMappings.get(url);
        return translatedURL.getExternalForm();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Jul-04	4801/2	allan	VBM:2004070113 Sample PageURLRewriter java and jsp.

 ===========================================================================
*/
