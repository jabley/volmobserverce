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
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-03-03     Doug            VBM:2003030405 - Created. Adapter class that
 *                              "adapts" a XMLPipelineProcess to an XMLFilter.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax;

import org.xml.sax.SAXException;

/**
 * Adapter class that provides a XMLPipelineFilter interface onto a
 * XMLPipeline instance.
 */
public class XMLPipelineFilterAdapter
        extends XMLProcessFilterAdapter
        implements XMLPipelineFilter {

    /**
     * Instance of the XMLPipelineProcess that is being adapteed to an
     * XMLPipelineFilter.
     */
    private XMLPipeline pipelineAdaptee;

    /**
     * Creates a new XMLPipelineFilterAdapter instance
     * @param pipeline the XMLPipelineProcess that is to be adapted to a
     * XMLPipelineFilter.
     */
    public XMLPipelineFilterAdapter(XMLPipeline pipeline) {
        super(pipeline.getPipelineProcess());
        this.pipelineAdaptee = pipeline;
    }

    // javadoc inherited
    public XMLPipelineContext getPipelineContext() {
        return pipelineAdaptee.getPipelineContext();
    }

    // javadoc inherited
    public void addHeadProcess(XMLProcess process) throws SAXException {
        pipelineAdaptee.addHeadProcess(process);
    }

    // javadoc inherited
    public XMLProcess removeHeadProcess() throws SAXException {
        return pipelineAdaptee.removeHeadProcess();
    }

    // javadoc inherited
    public XMLProcess getHeadProcess() {
        return pipelineAdaptee.getHeadProcess();
    }

    // javadoc inherited
    public void addTailProcess(XMLProcess process)
            throws SAXException {
        pipelineAdaptee.addTailProcess(process);
    }

    // javadoc inherited
    public XMLProcess removeTailProcess()
            throws SAXException {
        return pipelineAdaptee.removeTailProcess();
    }

    // javadoc inherited
    public XMLProcess getTailProcess() {
        return pipelineAdaptee.getTailProcess();
    }

    // javadoc inherited
    public XMLProcess getPipelineProcess() {
        return pipelineAdaptee.getPipelineProcess();
    }

    // javadoc inherited
    public void release() {
        pipelineAdaptee.release();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 28-Jul-03	261/1	doug	VBM:2003072806 Refactored XMLPipelineFilterAdapter to adapt an XMLPipeline instance

 22-Jul-03	225/1	doug	VBM:2003071805 Refactored the XMLPipeline interface to reflect the new public API

 18-Jul-03	213/1	doug	VBM:2003071615 Refactored XMLProcess interface

 ===========================================================================
*/
