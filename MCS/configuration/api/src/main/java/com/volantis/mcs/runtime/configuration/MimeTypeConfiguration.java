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

package com.volantis.mcs.runtime.configuration;

/**
 * Provides a bean implementation for the mime-type configuration.
 */
public class MimeTypeConfiguration {

    /**
     * The value attribute of the mime-type element.
     */
    private String value;

    /**
     * Gets the mime-type value.
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the mime-type value.
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jan-05	6413/1	pcameron	VBM:2004120702 Servlet filter integration for XDIME

 ===========================================================================
*/
