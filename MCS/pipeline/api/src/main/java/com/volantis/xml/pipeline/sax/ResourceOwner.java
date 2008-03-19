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

package com.volantis.xml.pipeline.sax;

/**
 * Implementation of this interface indicates that the object owns, or could
 * own resources that need cleaning up.
 * <p>Resources in this sense is a very asbstract term as it could refer to
 * external resources such as sockets, or database connections but equally it
 * could just refer to a Java object that can be reused and so needs adding
 * back to an object pool.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate 
 */
public interface ResourceOwner {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Release any resources associated with this object.
     * <p>This will be called if an error has occurred so it must be written
     * defensively to ensure that it will work no matter what state the object
     * or any dependent objects is in.</p>
     */
    public void release();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 18-Jul-03	213/1	doug	VBM:2003071615 Refactored XMLProcess interface

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
