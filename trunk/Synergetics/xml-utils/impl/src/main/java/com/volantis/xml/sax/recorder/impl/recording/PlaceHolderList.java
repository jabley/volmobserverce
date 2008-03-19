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

import java.util.ArrayList;
import java.util.List;

/**
 * A list of place holders.
 */
public class PlaceHolderList {

    /**
     * The place holders.
     */
    private final List placeHolders;

    /**
     * Initialise.
     */
    public PlaceHolderList() {
        this.placeHolders = new ArrayList();
    }

    /**
     * Add a place holder to the list.
     *
     * @param placeHolder The place holder to add.
     */
    public void addPlaceHolder(PlaceHolder placeHolder) {
        placeHolders.add(placeHolder);
    }

    /**
     * Set the place holders' values.
     *
     * @param newValue The new value for the place holders.
     */
    public void setValue(int newValue) {
        for (int i = 0; i < placeHolders.size(); i++) {
            PlaceHolder placeHolder = (PlaceHolder) placeHolders.get(i);
            placeHolder.setValue(newValue);
        }
    }
}
