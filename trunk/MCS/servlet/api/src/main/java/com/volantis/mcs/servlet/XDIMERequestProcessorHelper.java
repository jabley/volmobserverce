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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet;

import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.marlin.sax.MCSInternalContentHandler;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecording;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Helper Class for the XDIMERequestProcessors. The methods in this class
 * provide functionality used by the XDIMERequestProcessors but are not
 * dependant of the state of the XDIMERequestProcessor.
 */
public interface XDIMERequestProcessorHelper {
    /**
     * Factory method for creating {@link MarinerServletRequestContext}
     * instances
     * @param context the ServletContext
     * @param request the ServletRequest
     * @param response the ServletResponse
     * @return a MarinerServletRequestContext instance
     * @throws java.io.IOException if an error occurs
     * @throws com.volantis.mcs.context.MarinerContextException if an error occurs
     */
    MarinerServletRequestContext createServletRequestContext(
                ServletContext context,
                ServletRequest request,
                ServletResponse response)
                throws IOException, MarinerContextException;

    /**
     * Parse the XDIME in order to convert to device markup and populate the
     * response
     * @param servletRequestContext the request context
     * @param xdimeContent used to read out the XDIME
     * @throws java.io.IOException if an error occurs
     * @throws javax.servlet.ServletException if an error occurs
     * @throws org.xml.sax.SAXException if an error occurs
     */
    void parseXDIME(
                MarinerServletRequestContext servletRequestContext,
                CachedContent xdimeContent,
                ContentHandler contentHandler)
                throws IOException, ServletException, SAXException;

    /**
     * using the CachedContent process the content into a string of xdime
     * @param xdimeContent
     * @return string of xdime
     * @throws java.io.IOException
     */
    String getXDIMEString(CachedContent xdimeContent)
            throws IOException;

    /**
     * using the mariner request context retrieve a content handler for use
     * in mcs
     * @param marinerRequestContext
     * @return
     */
    MCSInternalContentHandler getContentHandler(
            MarinerRequestContext marinerRequestContext);

    /**
     * This method replays SAX XDIME events from a {@link com.volantis.xml.sax.recorder.SAXRecording}
     * into MCS in order for them to be converted into device specific markup
     *
     * @param recording The <code>SAXRecording</code> that must
     *                      have been populated with the XDIME SAX events.
     *                      device specific markup.
     * @throws org.xml.sax.SAXException if an error occurs
     */
    void replayEventsIntoMCS(
            PipelineRecording recording,
            MarinerServletRequestContext requestContext)

            throws SAXException;
}
