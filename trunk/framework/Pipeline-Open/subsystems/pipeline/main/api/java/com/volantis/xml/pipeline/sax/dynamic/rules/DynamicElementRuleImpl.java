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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.dynamic.rules;

import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLProcessingException;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Base class for {@link DynamicElementRule}s.
 *
 * <p>This class provides a number of methods to simplify the writing of a
 * {@link DynamicElementRule}.</p>
 */
public abstract class DynamicElementRuleImpl
        implements DynamicElementRule {

    /**
     * Forward an error down the pipeline.
     *
     * @param dynamicProcess The process to which the error is passed.
     * @param message        The message to send.
     * @throws SAXException If an exception was thrown down stream.
     */
    protected void forwardError(
            DynamicProcess dynamicProcess, final String message)
            throws SAXException {

        SAXParseException exception = createException(dynamicProcess, message);
        dynamicProcess.error(exception);
    }

    /**
     * Forward a fatal error down the pipeline.
     *
     * @param dynamicProcess The process to which the error is passed.
     * @param message        The message to send.
     * @throws SAXException If an exception was thrown down stream.
     */
    protected SAXParseException forwardFatalError(
            DynamicProcess dynamicProcess, final String message)
            throws SAXException {

        SAXParseException exception = createException(dynamicProcess, message);
        dynamicProcess.error(exception);
        return exception;
    }

    /**
     * Create an exception for the current locator.
     *
     * @param dynamicProcess The dynamic process.
     * @param message        The message.
     * @return The exception to forward.
     */
    private SAXParseException createException(
            DynamicProcess dynamicProcess, final String message) {
        XMLPipelineContext context = dynamicProcess.getPipelineContext();
        Locator locator = context.getCurrentLocator();
        SAXParseException exception = new XMLPipelineException(message,
                locator);
        return exception;
    }

    /**
     * Get the process associated with this pipeline to which events should
     * be targeted.
     *
     * <p>If the pipeline is not empty then it returns the head process,
     * otherwise it returns the next process after the
     * {@link XMLPipeline#getPipelineProcess()}. If that is null then it
     * throws a {@link org.xml.sax.SAXException}.</p>
     *
     * @param pipeline The pipeline whose target process is requested.
     * @return Target process.
     */
    protected XMLProcess getTargetProcess(XMLPipeline pipeline) {
        XMLProcess target = pipeline.getHeadProcess();
        if (target == null) {
            target = pipeline.getPipelineProcess().getNextProcess();
        }
        if (target == null) {
            throw new IllegalStateException("Cannot find target process");
        }
        return target;
    }

    /**
     * Get the process associated with the dynamic process.
     *
     * @see #getTargetProcess(XMLPipeline) 
     */
    protected XMLProcess getTargetProcess(DynamicProcess dynamicProcess) {
        return getTargetProcess(dynamicProcess.getPipeline());
    }
}
