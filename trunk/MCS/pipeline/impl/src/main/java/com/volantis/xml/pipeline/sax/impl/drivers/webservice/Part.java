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
 * A Part of a Message. This consists of a value name and the value itself
 * represented as an object.
 */
public class Part {
    /**
     * The name of the Part.
     */
    private String name;

    /**
     * The value.
     */
    private Object value;

    /**
     * Get the value name.
     * @return The value name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value name.
     * @param name The value name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value.
     * @return The value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set the value.
     * @param value The value.
     */
    public void setValue(Object value) {
        this.value = value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 29-Jun-03	98/2	allan	VBM:2003022822 Added some tests for RequestOperationProcess - could do with more though. Added some possibly final touches

 ===========================================================================
*/
