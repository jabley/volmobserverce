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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.pipeline;

import com.volantis.xml.pipeline.sax.adapter.AbstractAdapterProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineProcess;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLStreamingException;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.protocols.OutputBuffer;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;



import java.io.IOException;

/**
 * This process handles full integration of included pipelines with the
 * Mariner JSP Tag framework.
 */
public class PipelineIntegrationAdapterProcess
        extends AbstractPipelineIntegrationAdapterProcess {
     /**
     * Either this or {@link #outputBuffer} must be defined.
     */
    PAPIElement containingElement = null;

    /**
     * Either this or {@link #containingElement} must be defined.
     */
    OutputBuffer outputBuffer = null;

    /**
     * This must be defined along with the {@link #containingElement}.
     */
    MarinerRequestContext context = null;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param containingElement the PAPI element that contains the element
     *                          generating the pipeline that will hold this
     *                          process
     * @param context           the MarinerRequestContext given to the element
     *                          generating the pipeline that will hold this
     *                          process
     * @param outputBuffer      the buffer to which output must be directed if
     *                          there is no containingElement
     */
    public PipelineIntegrationAdapterProcess(
        PAPIElement containingElement,
        MarinerRequestContext context,
        OutputBuffer outputBuffer) {
        if ((outputBuffer == null) &&
            ((containingElement == null) ||
             (context == null))) {
            throw new NullPointerException(
                "Either the containingElement and context must be non-null " +
                "outputBuffer must be non-null");
        } else {
            this.containingElement = containingElement;
            this.outputBuffer = outputBuffer;
            this.context = context;
        }
    }

    // javadoc inherited
    public void characters(char[] chars, int start, int length)
        throws SAXException {
        if (depth > 0) {
            super.characters(chars, start, length);
        } else if (containingElement != null) {
            try {
                containingElement.getContentWriter(context, null).
                    write(chars, start, length);
            } catch (PAPIException e) {
                fatalError(new XMLStreamingException(
                    "PAPIException encountered during characters event " +
                    "processing",
                    getPipelineContext().getCurrentLocator(),
                    e));
            } catch (IOException e) {
                fatalError(new XMLStreamingException(
                    "IOException encountered during characters event " +
                    "processing (containingElement)",
                    getPipelineContext().getCurrentLocator(),
                    e));
            }
        } else {
            try {
                outputBuffer.getWriter().write(chars, start, length);
            } catch (IOException e) {
                fatalError(new XMLStreamingException(
                    "IOException encountered during characters event " +
                    "processing (outputBuffer)",
                    getPipelineContext().getCurrentLocator(),
                    e));
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Dec-04	6541/1	philws	VBM:2004122101 Fix pipeline integration issues and tidy up parts of the build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/4	ianw	VBM:2004090605 New Build system

 19-Apr-04	3924/1	steve	VBM:2004040709 fix usepipeline tag

 19-Apr-04	3918/1	steve	VBM:2004040709 fix usepipeline tag

 21-Nov-03	2000/1	steve	VBM:2003111907 Patched from Proteus2

 20-Nov-03	1970/1	mat	VBM:2003111907 Get the correct body content from the containing tag

 18-Jul-03	825/1	doug	VBM:2003071615 Refactored XMLProcess interface

 30-Jun-03	552/3	philws	VBM:2003062507 Provide JSP and XML variants of the vt:usePipeline and vt:include markup

 ===========================================================================
*/
