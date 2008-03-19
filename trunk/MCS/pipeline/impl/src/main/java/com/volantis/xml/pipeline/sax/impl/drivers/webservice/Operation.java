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

/**
 * A WebService Operation. Implementations must provide means to acquire the
 * URI for the WSDL, the port name and the operation name.
 */
public interface Operation {

    /**
     * Retrieve the URI for the WSDL that describes this operation. The
     * WSDL URI may not be a property of an operation (in the java bean sence)
     * since it may be necessary to find it at runtime.
     * @return The String representing the URI for the WSDL.
     */
    public String retrieveWSDLURI();

    /**
     * Get the name of the port to use. The port name determines the
     * binding.
     * @return The port name to use for this operation.
     */
    public String getPortType();

    /**
     * Get the name of the operation.
     * @return The name of this operation.
     */
    public String getOperationName();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 20-Jun-03	98/1	allan	VBM:2003022822 Add new classes to make them available to others

 ===========================================================================
*/
