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
package com.volantis.mcs.protocols.menu.shared.model;

import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.menu.model.EventTarget;
import com.volantis.mcs.protocols.menu.model.EventType;

/**
 * Because the {@link EventTarget} interface is used in contexts where an
 * abstract implementation cannot be used (we would require Java to support
 * multiple inheritance), this interface is introduced to provide an abstracted
 * way of setting event handlers on implementations of
 * <code>EventTarget</code>.
 */
public interface MutableEventTarget extends EventTarget {
    /**
     * Allows a handler to be set for the given event.  If any handlers are
     * already set for this type they will be replaced.  If the old value is
     * important then this method call should be coupled with a call to the
     * {@link #getEventHandler(EventType)} method.
     *
     * @param type    The event type which the handler should be set for
     * @param handler The handler to use for the given event.
     */
    public void setEventHandler(EventType type, ScriptAssetReference handler);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3491/1	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 ===========================================================================
*/
