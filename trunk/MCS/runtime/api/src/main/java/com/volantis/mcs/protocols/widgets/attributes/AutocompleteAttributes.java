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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.widgets.attributes;

/**
 * Autocomplete element attribute
 */
public class AutocompleteAttributes extends WidgetAttributes {

    // url for getting ajax response
    private String src;
    // id of parent input element
    private String inputId;

    /**
     * @return Returns the inputId.
     */
    public String getInputId() {
        return inputId;
    }

    /**
     * @param inputId The inputId to set.
     */
    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

    /**
     * @return Returns the src.
     */
    public String getSrc() {
        return src;
    }

    /**
     * @param src The src to set.
     */
    public void setSrc(String src) {
        this.src = src;
    }

}
