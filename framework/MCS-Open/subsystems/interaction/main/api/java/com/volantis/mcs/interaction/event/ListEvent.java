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

package com.volantis.mcs.interaction.event;

import com.volantis.mcs.interaction.ListProxy;

/**
 * The base of all list related events.
 */
public abstract class ListEvent
        extends InteractionEvent {

    /**
     * Initialise.
     *
     * @param source The list proxy that was the source of the event.
     * @param originator Indicats if this event is the originator.
     */
    public ListEvent(ListProxy source, boolean originator) {
        super(source, originator);
    }

    /**
     * Get the list proxy that was the source of the event.
     *
     * @return The list proxy that was the source of the event.
     */
    public ListProxy getListProxy() {
        return (ListProxy) getSource();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
