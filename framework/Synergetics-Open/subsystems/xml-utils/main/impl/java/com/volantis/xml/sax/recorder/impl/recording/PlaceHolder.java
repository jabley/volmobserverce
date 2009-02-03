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

package com.volantis.xml.sax.recorder.impl.recording;

/**
 * A place holder for information that has not yet been determined, e.g. the
 * position of a following event, etc.
 */
public class PlaceHolder {

    /**
     * The accessor to use to access the place holder value.
     */
    private final PlaceHolderAccessor accessor;

    /**
     * The index of the slot in the array that needs to be updated at a later
     * date.
     */
    private int placeHolderIndex;

    /**
     * Initialise.
     *
     * @param accessor         The accessor to use to access the place holder
     *                         value.
     */
    public PlaceHolder(PlaceHolderAccessor accessor) {
        this.accessor = accessor;
        this.placeHolderIndex = accessor.getCurrentPosition();
    }

    /**
     * Set the place holder value.
     *
     * @param newValue The new value for the place holder.
     */
    public void setValue(int newValue) {
        accessor.setPlaceHolderValue(placeHolderIndex, newValue);
    }

    /**
     * Get the place holder value.
     *
     * @return The place holder value.
     */
    public int getValue() {
        return accessor.getPlaceHolderValue(placeHolderIndex);
    }

    public void move() {
        placeHolderIndex = accessor.getCurrentPosition();
    }

    /**
     * Return the index of the placeholder
     *
     * @return the index of the placeholder
     */
    public int getIndex() {
        return placeHolderIndex;
    }
}
