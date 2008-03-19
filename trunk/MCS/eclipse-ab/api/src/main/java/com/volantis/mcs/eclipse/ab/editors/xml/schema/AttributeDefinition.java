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
package com.volantis.mcs.eclipse.ab.editors.xml.schema;

/**
 * Represents the definition of an attribute within an
 * {@link ElementDefinition}. 
 */
public class AttributeDefinition {
    /**
     * This attribute's name.
     */
    private String name;

    /**
     * Indicates if the attribute is mandatory or optional.
     */
    private boolean required;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name     the attribute's name
     * @param required <code>true</code> if the attribute is mandatory
     */
    public AttributeDefinition(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    // javadoc unnecessary
    public String getName() {
        return name;
    }

    // javadoc unnecessary
    public boolean isRequired() {
        return required;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Jan-04	2309/1	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 ===========================================================================
*/
