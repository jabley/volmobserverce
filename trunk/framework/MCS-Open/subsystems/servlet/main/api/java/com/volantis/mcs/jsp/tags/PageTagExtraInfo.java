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

package com.volantis.mcs.jsp.tags;

import com.volantis.mcs.servlet.MarinerServletRequestContext;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * This class provides extra information about the context tag to the
 * JSP translator.
 */
public class PageTagExtraInfo
        extends TagExtraInfo {

    /**
     * The set of variables that are created by this tag.
     */
    private final VariableInfo[] variables;

    /**
     * Initialise.
     */
    public PageTagExtraInfo() {
        VariableInfo v;

        variables = new VariableInfo[1];

        v = new VariableInfo("marinerRequestContext",
                MarinerServletRequestContext.class.getName(),
                true,
                VariableInfo.NESTED);
        variables[0] = v;
    }

    // Javadoc inherited.
    public VariableInfo[] getVariableInfo(TagData data) {
        return variables;
    }
}

