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

import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLProcessingException;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.protocols.OutputBuffer;


import org.xml.sax.SAXException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * Contains some helper methods that can be used by Mariner pipeline tags to
 * ensure that the pipeline is correctly integrated with Mariner's tag
 * interleaving mechanism.
 */
public class PipelineIntegrationUtilities {
    /**
     * Creates the required pipeline integration adapter process, initializing
     * it with the given PAPI element and request context. If this method
     * returns true, a balancing call to {@link #tearDownIntegration(XMLPipeline)}
     * must be made.
     *
     * @param target       the pipeline into which the integration process is
     *                     to be inserted
     * @param element      the containing PAPI element or null
     * @param context      the request context (only used if element is
     *                     non-null)
     * @param outputBuffer the current output buffer (only used if element is
     *                     null)
     * @return true if an integration process was added
     * @throws PAPIException if there was a problem when setting up the
     *                       integration
     */
    public static boolean setUpIntegration(
        XMLPipeline target,
        PAPIElement element,
        MarinerRequestContext context,
        OutputBuffer outputBuffer) throws PAPIException {
        boolean inserted = false;

        XMLPipelineContext pipelineContext = target.getPipelineContext();

        PipelineIntegrationAdapterProcess pipelineIntegration =
            new PipelineIntegrationAdapterProcess(
                element,
                context,
                outputBuffer);

        try {
            // Insert the integration adapter process into the pipeline so
            // it is always the last process in the pipeline before the
            // content handler
            try {
                target.addHeadProcess(pipelineIntegration);

                inserted = true;
            } catch (SAXException e) {
                target.getPipelineProcess().fatalError(
                    new XMLProcessingException(
                        "Could not add the pipeline integration adapter " +
                        "process to the pipeline",
                        pipelineContext.getCurrentLocator(),
                        e));
            }
        } catch (SAXException e) {
            throw new PAPIException(e);
        }

        return inserted;
    }

    /**
     * Permits any pipeline integration adapter process installed by
     * {@link #setUpIntegration(XMLPipeline,PAPIElement,MarinerRequestContext,OutputBuffer)} 
     * to be removed when no longer required.
     *
     * @param target          the pipeline from which the integration process
     *                        is to be removed
     * @throws PAPIException if a problem is encountered while removing the
     *                         process
     */
    public static void tearDownIntegration(
        XMLPipeline target) throws PAPIException {
        XMLPipelineContext pipelineContext = target.getPipelineContext();
        // If the head process is a pipeline integration adapter
        // process added in {@link #setUpIntegration}, remove it
        XMLProcess head = target.getHeadProcess();

        try {
            if (head instanceof PipelineIntegrationAdapterProcess) {
                try {
                    target.removeHeadProcess();
                } catch (SAXException e) {
                    target.getPipelineProcess().fatalError(
                        new XMLProcessingException(
                            "Could not add the pipeline integration adapter " +
                            "process to the pipeline",
                            pipelineContext.getCurrentLocator(),
                            e));
                }
            }
        } catch (SAXException e) {
            throw new PAPIException(e);
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

 07-Dec-04	5800/3	ianw	VBM:2004090605 New Build system

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 21-Nov-03	2000/1	steve	VBM:2003111907 Patched from Proteus2

 20-Nov-03	1970/1	mat	VBM:2003111907 Get the correct body content from the containing tag

 22-Jul-03	831/1	doug	VBM:2003071805 Refactored the XMLPipeline interface

 30-Jun-03	552/3	philws	VBM:2003062507 Provide JSP and XML variants of the vt:usePipeline and vt:include markup

 ===========================================================================
*/
