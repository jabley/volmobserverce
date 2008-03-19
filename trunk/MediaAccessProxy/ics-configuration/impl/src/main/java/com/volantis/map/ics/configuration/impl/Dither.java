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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.configuration.impl;

import com.volantis.map.ics.configuration.DitherMode;

/**
 * Dither holds the dithering method for a given bit depth hashcode and equals
 * return equal values for equal bitDepths
 */
public class Dither {

    private int bitDepth;

    private DitherMode mode;

    public int getBitDepth() {
        return bitDepth;
    }

    public DitherMode getMode() {
        return mode;
    }

    public void setBitDepth(int bitDepth) {
        this.bitDepth = bitDepth;
    }

    public void setMode(DitherMode mode) {
        this.mode = mode;
    }

    public boolean equals(Object o) {
        boolean result = false;
        if (null != o && getClass() == o.getClass()) {
            Dither other = (Dither) o;
            result = bitDepth == other.bitDepth;
        }
        return result;
    }

    public int hashCode() {
        return bitDepth;
    }
}
