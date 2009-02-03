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
package com.volantis.map.ics.imageprocessor.tool.clip;

/**
 * This encapsulates an algorithm for two dimensional clipping operation on a
 * rectangular area with a protected( (not clippable) part.
 */

public class ClipXY {

    /**
     * This is the X coordinate clipper
     */
    private ClipX xClip;


    /**
     * This is the Y coordinate clipper
     */
    private ClipX yClip;


    /**
     * Constructs a two dimensional clipper
     *
     * @param sizeX          width of the original area in px
     * @param preserveXLeft  left column of the protected area (0-based) px
     * @param preserveXRight right column of the protected area (0-based) px
     * @param sizeY          height of the original area in px
     * @param preserveYLeft  bottom row of the protected area (0-based) px
     * @param preserveYRight top row of the protected area (0-based) px
     */
    public ClipXY(int sizeX, int preserveXLeft, int preserveXRight,
                  int sizeY, int preserveYLeft, int preserveYRight) {
        xClip = new ClipX(sizeX, preserveXLeft, preserveXRight);
        yClip = new ClipX(sizeY, preserveYLeft, preserveYRight);
    }

    /**
     * private constructor for cloning purposes
     */
    private ClipXY(ClipX x, ClipX y) {
        xClip = x;
        yClip = y;
    }

    /**
     * Clips the original area to the specified width and height preserving the
     * protected area.
     *
     * @param width  desired width in px
     * @param heigth desired height in px
     */
    public void clipTo(int width, int height) {
        xClip.clipTo(width);
        yClip.clipTo(height);
    }

    /**
     * Adjust preserved area to current frame sizes
     * @param minX minimum x value of current frame
     * @param minWidth width of current frame
     * @param minY minimum y value of current frame
     * @param minHeight height of current frame
     */
    public void adjustTo(int minX, int minWidth, int minY, int minHeight) {
        xClip.adjustTo(minX, minWidth);
        yClip.adjustTo(minY, minHeight);
    }

    /**
     * This returns the X coordinate clipper
     *
     * @return the X coordinate clipper
     */
    public ClipX getX() {
        return xClip;
    }

    /**
     * This returns the Y coordinate clipper
     *
     * @return the Y coordinate clipper
     */

    public ClipX getY() {
        return yClip;
    }

    // javadoc inherited
    public Object clone() {
        return new ClipXY(new ClipX(xClip), new ClipX(yClip));
    }

    // javadoc unnecessary
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nX:").append(getX().toString());
        sb.append("\nY:").append(getY().toString());
        return sb.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Nov-05	554/4	pszul	VBM:2005102504 intendation corrected to 4 spaces

 04-Nov-05	554/2	pszul	VBM:2005102504 intelligent clipping implemented

 ===========================================================================
 */
