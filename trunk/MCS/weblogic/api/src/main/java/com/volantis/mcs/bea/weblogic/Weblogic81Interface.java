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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.bea.weblogic;

/**
 * An {@link com.volantis.mcs.integration.AppServerInterface
 * AppServerInterface} implementation appropriate to Weblogic 8.1.
 */
public class Weblogic81Interface extends Weblogic60Interface {
    /**
     * Initializes the new instance.
     */
    public Weblogic81Interface() {
    }

    // javadoc inherited
    protected String appServerVersion() {
        return "8.1";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Jul-05	8920/1	philws	VBM:2005060606 Provide installer compatible AppServerInterface implementations

 ===========================================================================
*/
