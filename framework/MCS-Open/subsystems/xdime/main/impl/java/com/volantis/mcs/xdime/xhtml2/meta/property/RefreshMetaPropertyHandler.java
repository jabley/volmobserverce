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

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.protocols.TimedRefreshInfo;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xhtml2.meta.datatype.DataType;
import com.volantis.mcs.xdime.xhtml2.meta.datatype.RefreshContentProcessor;

/**
 * A property handler for mcs:refresh
 */

public class RefreshMetaPropertyHandler extends AbstractMetaPropertyHandler {

    // javadoc inherited
    public DataType getDefaultDataType() {
        return RefreshContentProcessor.REFRESH_TYPE;
    }

    // javadoc inherited
    protected boolean hasPageScope() {
        return true;
    }

    // javadoc inherited
    protected void checkContent(final Object content,
                                final XDIMEContextInternal context)
            throws XDIMEException {
        // check the right type
        checkContentType(content, TimedRefreshInfo.class);

    }

    // javadoc inherited
    public void process(Object content, XDIMEContextInternal context,
                        String id, String propertyName) throws XDIMEException {
        super.process(content, context, id, propertyName);
        MarinerPageContext ctx = ContextInternals.getMarinerPageContext(
            context.getInitialRequestContext());
        TimedRefreshInfo tri = (TimedRefreshInfo) content;
        if (null != tri) {
            try {
                ctx.getProtocol().writeTimedRefresh(tri);
            } catch (ProtocolException e) {
                throw new XDIMEException(e);
            }
        }
    }
}
