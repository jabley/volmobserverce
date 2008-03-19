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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.trans.TransformationConfiguration;

/**
 * This is a specialised TransMapper for i-Mode phones
 */
public class HTML_iModeTransMapper extends XHTMLBasicTransMapper{

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param configuration used when remapping elements to determine if the
     *                  element to be remapped has any style information that
     *                  should be preserved.
     */
    public HTML_iModeTransMapper(TransformationConfiguration configuration) {
        super(configuration);
    }

    /**
     * The valid attributes for the div tag.
     * @return the valid attributes for the div tag.
     */
    protected String[] getValidDivAttributes() {
        String validAttributes[] = {"align"};
        return validAttributes;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Oct-04	5773/6	tom	VBM:2004093007 created an i-mode transmapper to resolve align disappearance problem after table optimisation

 ===========================================================================
*/
