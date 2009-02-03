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

import com.volantis.map.ics.configuration.ImageConstants;


/**
 * @author pszul This class encapsulates the algorithm of one-dimensional
 *         clipping, such that the defined preserved area is never clipped. The
 *         preserved area if defined by coordinates of its left and right
 *         bounds (0-based).
 */

public class ClipX {


    /**
     * The right 0-based coordinate of the preserved area
     */
    private int preserveRight;

    /**
     * The left 0-based coordinate of the preserved area
     */

    private int preserveLeft;

    /**
     * The size of the original area
     */
    private int size;

    /**
     * This is the calculated value of the offset of the clipped area.
     */

    private int clipOffset;

    /**
     * This is the calculated value of the size of the clipped area.
     */

    private int clipSize;

    public ClipX(int size) {
        this(size, ImageConstants.NO_CLIP_LEFT, ImageConstants.NO_CLIP_RIGHT);
    }

    public ClipX(int size, int preserveLeft, int preserveRight) {
        this.size = size;
        this.preserveLeft = preserveLeft;
        this.preserveRight = preserveRight;
    }

    ClipX(ClipX org) {
        this.size = org.size;
        this.preserveLeft = org.preserveLeft;
        this.preserveRight = org.preserveRight;
        this.clipOffset = org.clipOffset;
        this.clipSize = org.clipSize;
    }

    /**
     * Tries to clip the original area to the desired width while preserving
     * the protected area.. If the actSize is >= size the area is not clipped
     * If the  size  > actSize > protectedAreaSize the image is clipped to the
     * actSize proportionally on both sides of the protected area. If rounding
     * is necessary bigger side is rounded up or if both sides are equsal then
     * the right size is rounded up. If protectedAreaSize >= actSize the image
     * is clipped to the protected area
     *
     * @param actSize desired size
     */
    public void clipTo(int actSize) {
        int clipLeftAct = 0;
        int clipRightAct = 0;
        int clipWidthAct = size - actSize;
        if (clipWidthAct > 0) {
            // the clippable left side size
            int clipLeftDef = (preserveLeft != ImageConstants.NO_CLIP_LEFT) ?
                preserveLeft : 0;
            if (clipLeftDef < 0) {
                clipLeftDef = 0;
            }
            if (clipLeftDef >= size) {
                throw new IllegalArgumentException(
                    "Left bound of the preserved area beyond the image");
            }
            // the clippable right side size
            int clipRightDef = (preserveRight != ImageConstants.NO_CLIP_RIGHT) ?
                size - preserveRight - 1 : 0;
            if (clipRightDef < 0) {
                clipRightDef = 0;
            }
            if (clipRightDef >= size) {
                throw new IllegalArgumentException(
                    "Right bound of the preserved area beyond the image");
            }

            // the total size of the clippable area
            int clipWidthDef = clipLeftDef + clipRightDef;
            if (clipWidthDef >= size) {
                throw new IllegalArgumentException(
                    "Clippable area greated then the image");
            }

            // cannot clip more than is allowed
            if (clipWidthAct > clipWidthDef) {
                clipWidthAct = clipWidthDef;
            }

            if (clipWidthDef > 0) {
                if (clipLeftDef <= clipRightDef) {
                    clipLeftAct = clipLeftDef * clipWidthAct / clipWidthDef;
                    clipRightAct = clipWidthAct - clipLeftAct;
                } else {
                    clipRightAct = clipRightDef * clipWidthAct / clipWidthDef;
                    clipLeftAct = clipWidthAct - clipRightAct;
                }
            }
        } else {
            clipWidthAct = 0;
        }

        clipOffset = clipLeftAct;
        clipSize = size - clipWidthAct;
    }

    /**
     * Adjusts calculated clip area to actual frame size
     * It should be called after clipTo method which calculates original
     * offset and size.
     * If frame is set outside of the protected area, clip is set to
     * preserve 1x1px frame outside of visible plot for animation purposes
     * @param minX frame minimum x coordinate
     * @param minWidth frame width
     */
    public void adjustTo(int minX, int minWidth) {
        int useX = clipOffset;
        int useWidth = clipSize;

        if (minX > clipOffset + clipSize || minX + minWidth < clipOffset) {
            //frame outside crop area so only 1x1px will be kept for animation timing
            clipOffset = minX;
            clipSize = 1;
        } else {

            // frame is on the right of calculated clip so actual clip has to be
            // moved to frame beginning
            if (minX > 0 && minX > clipOffset) {
                useX = minX;
            }

            // calculate actual clip width.
            if (clipOffset + clipSize > minX + minWidth) {
                // frame ends before calculated clip
                useWidth = minWidth - (useX - minX) ;
            } else {
                // frame ends after calculated clip
                useWidth = clipSize - (useX - clipOffset);
            }

            clipOffset = useX;
            clipSize = useWidth;
        }
    }

    /**
     * This gets the calculated offset of the clipped area
     *
     * @return calculated offset of the clipped area
     */
    public int getClipOffset() {
        return clipOffset;
    }

    /**
     * This gets the calculates size of the clipped area
     *
     * @return calculates size of the clipped area
     */
    public int getClipSize() {
        return clipSize;
    }

    /**
     * This gets the left bound of the protected area
     *
     * @return the left bound of the protected area
     */
    public int getPreserveLeft() {
        return preserveLeft;
    }

    /**
     * This sets the left bound of the protected area
     *
     * @param preserveLeft he left bound of the protected area
     */
    public void setPreserveLeft(int preserveLeft) {
        this.preserveLeft = preserveLeft;
    }

    /**
     * This gets the right bound of the protected area
     *
     * @return the right bound of the protected area
     */
    public int getPreserveRight() {
        return preserveRight;
    }


    /**
     * This sets the right bound of the protected area
     *
     * @param preserveRight right bound of the protected area
     */
    public void setPreserveRight(int preserveRight) {
        this.preserveRight = preserveRight;
    }


    /**
     * This gets the size of the original area
     *
     * @return the size of the original area
     */
    public int getSize() {
        return size;
    }

    /**
     * This sets the size of the original area
     *
     * @param size the size of the original area
     */
    public void setSize(int size) {
        this.size = size;
    }

    // javadoc unnecessary
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ClipInfo: ");
        sb.append("size: ").append(getSize());
        sb.append(", pL: ").append(getPreserveLeft());
        sb.append(", pR: ").append(getPreserveRight());
        sb.append(", offset: ").append(getClipOffset());
        sb.append(", clip size: ").append(getClipSize());

        return sb.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Nov-05	554/5	pszul	VBM:2005102504 intendation corrected to 4 spaces

 04-Nov-05	554/3	pszul	VBM:2005102504 intelligent clipping implemented

 ===========================================================================
 */
