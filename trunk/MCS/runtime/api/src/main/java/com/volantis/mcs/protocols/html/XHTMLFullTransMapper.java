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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.trans.TransformationConfiguration;

/**
 * This is a specialised TransMapper for XHTMLFull devices.
 */
public class XHTMLFullTransMapper extends XHTMLBasicTransMapper {

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param configuration used when remapping elements to determine if the
     *                  element to be remapped has any style information that
     */
    public XHTMLFullTransMapper(TransformationConfiguration configuration) {
        super(configuration);
    }

    /**
     * Gets the valid attributes for the div tag.
     *
     * @return the valid attributes
     */
    protected String[] getValidDivAttributes() {

        // These attributes were obtained from the DTD for XHTML 1.0 strict.
        // There are three sets of attributes here: core, i18n and events.
        // The DTD is located at:
        // http://www.w3.org/TR/xhtml1/dtds.html#a_dtd_XHTML-1.0-Strict
        String validAttributes[] = {"id", "class", "style", "title",
                                    "dir", "lang", "xml:lang",
                                    "onclick", "ondblclick", "onmousedown",
                                    "onmouseup", "onmouseover", "onmousemove",
                                    "onmouseout", "onkeypress", "onkeydown",
                                    "onkeyup"};
        return validAttributes;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 12-Jul-05	8990/2	pcameron	VBM:2005052606 Allow devices to override protocol optimisation of tables

 ===========================================================================
*/
