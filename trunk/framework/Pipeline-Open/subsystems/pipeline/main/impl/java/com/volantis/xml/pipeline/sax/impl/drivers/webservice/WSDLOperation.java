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
package com.volantis.xml.pipeline.sax.impl.drivers.webservice;

import com.volantis.xml.pipeline.sax.impl.drivers.webservice.Operation;

/**
 * An implementation of Operation that knows up front what its WSDL URI is.
 */
public class WSDLOperation implements Operation {

    /**
     * The WSDL URI for this Operation.
     */
    private String wsdlURI;

    /**
     * The port type for this operation.
     */
    private String portType;

    /**
     * The name of this operation.
     */
    private String operationName;

    /**
     * Construct a new WSDLOperation with all properties unset.
     */
    public WSDLOperation() {
    }

    /**
     * Construct a new WSDLOperation with the specified values.
     * @param wsdlURI The WSDL URI.
     * @param portType The port type.
     * @param operationName The operation name.
     */
    public WSDLOperation(String wsdlURI, String portType,
                         String operationName) {
        this.wsdlURI = wsdlURI;
        this.portType = portType;
        this.operationName = operationName;
    }

    // javadoc inherited
    public String retrieveWSDLURI() {
        return wsdlURI;
    }

    // javadoc inherited
    public String getPortType() {
        return portType;
    }

    // javadoc inherited
    public String getOperationName() {
        return operationName;
    }

    /**
     * Set the WSDL URI for this operation.
     * @param wsdlURI The WSDL URI.
     */
    public void setWsdlURI(String wsdlURI) {
        this.wsdlURI = wsdlURI;
    }

    /**
     * Set the port type for this operation.
     * @param portType The port type.
     */
    public void setPortType(String portType) {
        this.portType = portType;
    }

    /**
     * Set the operation name for this operation.
     * @param operationName The operation name.
     */
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 29-Jun-03	98/2	allan	VBM:2003022822 Added some tests for RequestOperationProcess - could do with more though. Added some possibly final touches

 ===========================================================================
*/
