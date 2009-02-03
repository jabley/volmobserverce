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
 * 31-03-03     Doug            VBM:2003030405 - Created. This class is a base
 *                              class for all Adapter Processes.
 * 30-May-03    Phil W-S        VBM:2003030610 - Added
 *                              getPreprocessingPipelineProcess.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.adapter;

import com.volantis.xml.pipeline.sax.XMLWrappingProcess;
import com.volantis.xml.pipeline.sax.XMLPreprocessingPipelineProcess;

/**
 * This class should be used as the base class for all concrete AdapterProcess
 * implementations. An AdapterProcess owns an OperationProcess so this class
 * extends the XMLWrappingProcess class.
 */
public abstract class AbstractAdapterProcess
        extends XMLWrappingProcess
        implements AdapterProcess {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The URI of the associated namespace
     */
    protected String processNamespaceURI;

    /**
     * The processLocalName of the pipeline markup element associated with this
     * process
     */
    protected String processLocalName;

    /**
     * The processPrefixedName of the pipeline markup element associated with this
     * process
     */
    protected String processPrefixedName;

    // javadoc inherited from the AdapterProcess interface
    public void setElementDetails(String namespaceURI, String localName,
                                  String prefixedName) {
        this.processNamespaceURI = namespaceURI;
        this.processLocalName = localName;
        this.processPrefixedName = prefixedName;
    }

    protected XMLPreprocessingPipelineProcess
            getPreprocessingPipelineProcess() {
        return (XMLPreprocessingPipelineProcess)getPipeline();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 18-Jul-03	213/1	doug	VBM:2003071615 Refactored XMLProcess interface

 ===========================================================================
*/
