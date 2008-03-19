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
package com.volantis.mcs.context;

import com.volantis.charset.Encoding;
import com.volantis.charset.NoEncoding;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;

/**
 * Test Mariner Page Context.
 */
public class RuntimeTestMarinerPageContext extends MarinerPageContext {

    private RuntimeDeviceLayout layout;
    private Encoding charsetEncoding;

    // Javadoc inherited
    public RuntimeDeviceLayout getDeviceLayout(String name)
        throws RepositoryException {
        return layout;
    }

    // Javadoc inherited
    public void setDeviceLayout(RuntimeDeviceLayout newLayout) {
        layout = newLayout;
    }

    // Javadoc inherited
    public Encoding getCharsetEncoding() {

        if (charsetEncoding == null) {
            charsetEncoding = new NoEncoding("utf8", 106);
        }
        return charsetEncoding;
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 ===========================================================================
*/
