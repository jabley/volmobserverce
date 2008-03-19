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
import com.volantis.xml.utilities.sax.TeeContentHandler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Test implementation of the XDIMERequestProcessorHelper
 */
public class XDIMERequestProcessorHelperStub implements XDIMERequestProcessorHelper{
    /**
     * string representation of the xdime page
     */
    private final String xdimeContentString;

    /**
     * the marinerServletRequestContext being used
     */
    private MarinerServletRequestContext servletRequestContext;

    /**
     * The response object which will store the result of the xdime processing.
     * This object is only instantiated using the value passed to the
     * createServletRequestContext method
     */
    private ServletResponse cachingResponse;

    /**
     * the caching scope being used
     */
    private String mode;

    //constructor
    public XDIMERequestProcessorHelperStub(
            MarinerServletRequestContext servletRequestContext,
            String xdimeContentString,
            String mode) {
        this.xdimeContentString = xdimeContentString;
        this.servletRequestContext = servletRequestContext;
        this.mode = mode;
    }

    /**
     * mock the creation of the servlet request context using the object
     * specified to the constructor and record the servlet response being used
     * @param context
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws MarinerContextException
     */
    public MarinerServletRequestContext createServletRequestContext(
            ServletContext context, ServletRequest request,
            ServletResponse response) throws IOException,
            MarinerContextException {
        cachingResponse = response;
        return servletRequestContext;
    }

    /**
     * mock the actions of mcs by writting values in the caching responses
     * writier
     * @param servletRequestContext
     * @param xdimeContent
     * @param contentHandler
     * @throws IOException
     * @throws ServletException
     * @throws SAXException
     */
    public void parseXDIME(MarinerServletRequestContext servletRequestContext,
                           CachedContent xdimeContent,
                           ContentHandler contentHandler) throws IOException,
            ServletException, SAXException {

        if (!(contentHandler instanceof TeeContentHandler)) {
            cachingResponse.setContentType("TEXT");
            PrintWriter writer = cachingResponse.getWriter();
            writer.write(xdimeContentString);
        }
    }

    //javadoc inherited
    public String getXDIMEString(CachedContent xdimeContent)
            throws IOException {
        return xdimeContentString;
    }

    /**
     * method called when processing values through the pipeline - this
     * functionality is ignored
     * @param marinerRequestContext
     * @return
     */
    public MCSInternalContentHandler getContentHandler(
            MarinerRequestContext marinerRequestContext) {
        return null;
    }

    /**
     * method called after values have been processed through the pipeline.
     * we want to ignore this processing but need to write values into the
     * cachingResponses writer to mock the processing.
     * @param recording
     * @param requestContext
     * @throws SAXException
     */
    public void replayEventsIntoMCS(PipelineRecording recording,
                                    MarinerServletRequestContext requestContext)

            throws SAXException {
        if (!mode.equals("safe")) {
            throw new IllegalStateException("Events should not be played " +
                    "into mcs if not in safe mode");
        }

        cachingResponse.setContentType("TEXT");
        try {
            PrintWriter writer = cachingResponse.getWriter();
            writer.write(xdimeContentString);
        } catch (IOException e) {
            //don't need to do anything here. if there is an exception no
            //content will be returned which in turn will cause the test to
            //fail
        }
    }

    public void setServletRequestContext(
            final MarinerServletRequestContext servletRequestContext) {

        this.servletRequestContext = servletRequestContext;
    }
}
