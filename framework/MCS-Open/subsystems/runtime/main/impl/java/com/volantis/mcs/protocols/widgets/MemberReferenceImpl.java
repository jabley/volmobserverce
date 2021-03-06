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
package com.volantis.mcs.protocols.widgets;

import java.util.List;

/**
 * A runtime implementation of MemberReference.
 */
public class MemberReferenceImpl implements MemberReference {
    private final String widgetId;
    private final List memberNames;
    
    /**
     * Initializes this member reference with widget ID and a list of member names.
     * 
     * @param widgetId The widget ID.
     * @param memberNames A list of member names.
     */
    public MemberReferenceImpl(String widgetId, List memberNames) {
        this.widgetId = widgetId;
        this.memberNames = memberNames;
    }
    
    // Javadoc inherited
    public String getWidgetId() {
        return widgetId;
    }

    // Javadoc inherited
    public List getMemberNames() {
        return memberNames;
    }
}
