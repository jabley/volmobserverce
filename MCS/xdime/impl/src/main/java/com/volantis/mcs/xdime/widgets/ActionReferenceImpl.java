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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.protocols.widgets.ActionReference;
import com.volantis.mcs.protocols.widgets.MemberType;
import com.volantis.mcs.xdime.XDIMEException;

/**
 * An implementation of ActionReference.
 */
public class ActionReferenceImpl extends MemberReferenceImpl implements
        ActionReference {

    /**
     * Initializes this reference.
     * 
     * @param actionAttributeValue The value of the action attribute.
     */
    public ActionReferenceImpl(String actionAttributeValue) throws XDIMEException {
        super(actionAttributeValue);
    }

    // Javadoc inherited
    protected MemberType getMemberType() {
        return MemberType.ACTION;
    }
}
