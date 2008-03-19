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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-May-03    Doug            VBM:2003030405 - Created. A basic
 *                              implementation of an
 *                              XMLPreprocessingPipelineProcess.
 * 27-May-03    Doug            VBM:2003030405 - Fixed problems with the
 *                              suppression mode. Overrode the startDocument()
 *                              ane endDocument() methods to handle exiting
 *                              from the "suppress" processing mode.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

import org.xml.sax.SAXException;

/**
 * A basic implementation of an XMLPreprocessingPipelineProcess.
 */
public class XMLPreprocessingPipelineProcessImpl
        extends XMLPipelineProcessImpl
        implements XMLPreprocessingPipelineProcess {

    /**
     * The XMLProcess that is acting as the pre processor.
     */
    private XMLProcess preprocessor;

    /**
     * Creates a new <code>XMLPreprocessingPipelineProcessImpl</code> instance.
     * You should use this constructor when you intend to add nest this
     * pipeline inside another pipeline. Any <code>XMLProcess<code> objects
     * added to this process will not be started until this pipeline has
     * been added to another pipeline.
     */
    public XMLPreprocessingPipelineProcessImpl() {
    }

    /**
     * Create a new XMLPreprocessingPipelineProcessImpl instance. This pipeline
     * will not forward "nested" startDocument() and endDocument() events
     * to any process that has been set as the "next" process.
     * @param pipelineContext the associated XMLPipelineContext
     */
    public XMLPreprocessingPipelineProcessImpl(
            XMLPipelineContext pipelineContext) {
        super(pipelineContext);
    }

    // javadoc inherited
    protected void initialisePreprocessor(XMLProcess process)
            throws SAXException {

        if (process == null) {
            throw new IllegalArgumentException("process cannot be null");
        }

        if (null != preprocessor) {
            throw new IllegalStateException("Cannot change preprocessor");
        }

        preprocessor = process;
        preprocessor.setPipeline(this);
        XMLProcess consumer = super.getConsumerProcess();
        if (consumer != null) {
            preprocessor.setNextProcess(consumer);
        }
        preprocessor.startProcess();
    }

    // javadoc inherited
    protected XMLProcess getConsumerProcess() {
        // if we have a preprocessor set then that should consume the events
        return (null == preprocessor)
                ? super.getConsumerProcess() : preprocessor;
    }

    // javadoc inherted
    protected XMLProcess removeHeadProcessImpl() {
        // we need to override this method to ensure that
        // the head process is always connected to the
        // preprocessor
        XMLProcess removed = super.removeHeadProcessImpl();
        if (null != preprocessor) {
            XMLProcess head = getHeadProcess();
            if (head != null) {
                preprocessor.setNextProcess(head);
            } else {
                preprocessor.setNextProcess(getNextProcess());
            }
        }
        return removed;
    }

    // javadoc inherted
    protected void addHeadProcessImpl(XMLProcess process) {
        // we need to override this method to ensure that
        // the head process is always connected to the
        // preprocessor
        super.addHeadProcessImpl(process);
        if (null != preprocessor) {
            preprocessor.setNextProcess(process);
        }
    }

    // javadoc inherited
    protected void addTailProcessImpl(XMLProcess process) {
        // we need to override this method to ensure that
        // if the pipeline is empty the tail process being
        // added is connected to the preprocessor
        super.addTailProcessImpl(process);
        if (getProcessCount() == 1 && null != preprocessor) {
            preprocessor.setNextProcess(process);
        }
    }

    // javadoc inherited
    protected XMLProcess removeTailProcessImpl() {
        // we need to override this method to ensure that
        // process being removed is the only process in the pipeline then
        // the preprocess is connected to the next process
        XMLProcess removed = super.removeTailProcessImpl();
        if (getProcessCount() == 0 && preprocessor != null) {
            preprocessor.setNextProcess(getNextProcess());
        }
        return removed;
    }

    // javadoc inherted
    public void setNextProcess(XMLProcess next) {
        this.next = next;
        XMLProcess tail = getTailProcess();
        if (null != tail) {
            tail.setNextProcess(next);
        } else if (null != preprocessor) {
            preprocessor.setNextProcess(next);
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 11-Aug-03	275/3	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 22-Jul-03	225/1	doug	VBM:2003071805 Refactored the XMLPipeline interface to reflect the new public API

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 23-Jun-03	95/3	doug	VBM:2003061605 Document Event Filtering changes

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
