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
package com.volantis.mcs.xdime.xhtml2.meta.property;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xhtml2.meta.datatype.DOMContentProcessor;
import com.volantis.mcs.xdime.xhtml2.meta.datatype.DataType;

/**
 * Meta property handler implementation for Fragment links.
 */
public class FragmentLinkMetaPropertyHandler extends AbstractMetaPropertyHandler {

    // javadoc inherited
    public DataType getDefaultDataType() {
        return DOMContentProcessor.DOM_TYPE;
    }

    protected boolean hasElementScope() {
        return true;
    }

    // javadoc inherited
    protected void checkContent(final Object content,
                                final XDIMEContextInternal context)
            throws XDIMEException {

        // check the right type
        checkContentType(content, DOMOutputBuffer.class);
    }
}
