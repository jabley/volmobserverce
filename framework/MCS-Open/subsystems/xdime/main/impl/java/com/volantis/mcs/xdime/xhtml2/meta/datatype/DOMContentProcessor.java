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
package com.volantis.mcs.xdime.xhtml2.meta.datatype;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xdime.xhtml2.ElementUtils;
import com.volantis.mcs.xdime.xhtml2.MetaInformationElement;
import com.volantis.xml.namespace.ImmutableExpandedName;

/**
 * Content processor for DOM contents. Stores the result in a DOMOutputBuffer.
 */
public class DOMContentProcessor extends AbstractMetaContentProcessor {

    /**
     * QName for the data types
     */
    public static final ImmutableExpandedName EXPANDED_NAME_DOM =
        new ImmutableExpandedName(XDIMESchemata.XDIME2_MCS_NAMESPACE, "DOM");

    /**
     * The data type object.
     */
    public static final DataType DOM_TYPE = new DataType(EXPANDED_NAME_DOM);

    // javadoc inherited
    public DataType getType() {
        return DOM_TYPE;
    }

    // javadoc inherited
    public Object getResult() {
        return getBodyContentBuffer();
    }
}
