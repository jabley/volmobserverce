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
 * 31-03-03     Doug            VBM:2003030405 - Created. The interface for
 *                              an AdapterProcess. An AdapterProcess is an
 *                              XMLProcess that delegates processing to an
 *                              OperationProcess.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.adapter;

import com.volantis.xml.pipeline.sax.XMLProcess;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Interface for an AdapterProcess. An AdapterProcess is an XMLProcess that
 * delegates processing to an OperationProcess. An AdapterProcess is
 * responsible for configuring its OperationProcess.
 */
public interface AdapterProcess extends XMLProcess {

    /**
     * This method provides the details of the XMLPipeline Markup associated with
     * this particular process. This method is called before the
     * setPipeline(XMLPipelineContext) method.
     * @param namespaceURI The namespace URI, may be null.
     * @param localName The local name (without prefix).
     * @param qName The qualified name with the prefix.
     */
    public void setElementDetails(String namespaceURI, String localName,
                                  String qName);

    /**
     * This method passes the attributes for the pipeline mark up element
     * associated with this process. This method is called after the
     * setPipeline(XMLPipelineContext) method and before the startProcess method.
     * @param attributes The attributes
     */
    public void processAttributes(Attributes attributes) throws SAXException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 ===========================================================================
*/
