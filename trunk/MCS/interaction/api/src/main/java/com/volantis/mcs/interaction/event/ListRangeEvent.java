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

public abstract class ListRangeEvent extends ListEvent {
    /**
     * The index of the first item that was inserted.
     */
    private final int firstItemIndex;
    /**
     * The index of the last item that was inserted.
     */
    private final int lastItemIndex;

    /**
     * Initialise.
     *
     * @param listProxy      The list proxy that was the source of the event.
     * @param firstItemIndex The index of the first proxy item affected.
     * @param lastItemIndex  The index of the last proxy item affected.
     * @param originator Indicates this is an originating event or not.
     */
    public ListRangeEvent(ListProxy listProxy,
                          int firstItemIndex,
                          int lastItemIndex,
                          boolean originator) {
        super(listProxy, originator);
        this.firstItemIndex = firstItemIndex;
        this.lastItemIndex = lastItemIndex;
    }

    /**
     * Get the index of the first proxy item affected.
     *
     * @return The index of the first proxy item affected.
     */
    public int getFirstItemIndex() {
        return firstItemIndex;
    }

    /**
     * Get the index of the last proxy item affected.
     *
     * @return The index of the last proxy item affected.
     */
    public int getLastItemIndex() {
        return lastItemIndex;
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        if (!(obj instanceof ListRangeEvent)) {
            return false;
        }

        ListRangeEvent other = (ListRangeEvent) obj;
        return other.getFirstItemIndex() == firstItemIndex
                && other.getLastItemIndex() == lastItemIndex;
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = 37 * result + firstItemIndex;
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added basic list operations

 ===========================================================================
*/
