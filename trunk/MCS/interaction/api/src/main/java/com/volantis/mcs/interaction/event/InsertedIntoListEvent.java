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
 * Fired when a number of proxy items are inserted into a list proxy.
 */
public class InsertedIntoListEvent extends ListRangeEvent {

    /**
     * Initialise.
     *
     * @param listProxy      The list proxy that was the source of the event.
     * @param firstItemIndex The index of the first proxy item affected.
     * @param lastItemIndex  The index of the last proxy item affected.
     * @param originator Indicates this is an originating event or not.
     */
    public InsertedIntoListEvent(
            ListProxy listProxy, int firstItemIndex, int lastItemIndex,
            boolean originator) {
        super(listProxy, firstItemIndex, lastItemIndex, originator);
    }

    // Javadoc inherited.
    public void dispatch(InteractionEventListener listener) {
        listener.addedToList(this);
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        return (obj instanceof InsertedIntoListEvent);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Oct-05	9961/7	pduffin	VBM:2005101811 Added basic list operations

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
