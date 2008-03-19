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

package com.volantis.mcs.utilities;

/**
 * @author pszul
 * This class represens a rectagular area of an image that should be preserved
 * when the image is clipped.
 * The area is defined by the 0 based coordinate of its left and right columns.
 */

public class PreservedArea {
    
    /**
     * This constants indicates that the proteced area should start at the left side of the image.
     */
    public final static int  PRESERVE_ALL_LEFT = -1;
    
    /**
     *  This constants indicates that the proteced area should endat the right side of the image.
     */
    public final static int  PRESERVE_ALL_RIGHT = -1;
    
    /**
     *  This is preserved aread that covers the entire image area.
     */
    public final static PreservedArea PRESERVE_ALL = new PreservedArea();
    
    /**
     * This is the 0 based coordinate of the left column of the protected area.
     */
    private int preserveLeft;
    
    /**
     * This is the 0 based coordinate of the right column of the protected area.
     */
    private int preserveRight;
    
    /**
     * This construct a protected area object.
     * @param preserveLeft 0 based coordinate of the left column of the protected area
     * @param preserveRight 0 based coordinate of the right  column of the protected area
     */
    public PreservedArea(int preserveLeft, 
            int preserveRight )
    {
        this.preserveLeft = preserveLeft;
        this.preserveRight = preserveRight;
    }
    
    /**
     * This construct a protected area object that covers the entire image.
     */
    public PreservedArea()
    {
        this(PRESERVE_ALL_LEFT,PRESERVE_ALL_RIGHT);
    }
    
    
    /**
     * This gets the left column of the protected area
     * @return 0 based coordinate of the left column of the protected area
     */
    public int getPreserveLeft() {
        return preserveLeft;
    }
    
    /**
     * This sets the left column of the protected area
     * @param preserveLeft 0 based coordinate of the left column of the protected area
     */
    public void setPreserveLeft(int preserveLeft) {
        this.preserveLeft = preserveLeft;
    }
    
    /**
     * This gets the right column of the protected area
     * @return 0 based coordinate of the right column of the protected area
     */
    public int getPreserveRight() {
        return preserveRight;
    }
    
    /**
     * This sets the right column of the protected area
     * @param preserveRight 0 based coordinate of the right column of the protected area
     */
    public void setPreserveRight(int preserveRight) {
        this.preserveRight = preserveRight;
    }
    
    
    /**
     * This convers the protected are to a string that can be passed as an argument in 
     * URL (for instance to ICS). 
     * @return string representation of the protected ares
     */
    public String toUrlString() {
        if (preserveLeft == PRESERVE_ALL_LEFT && preserveRight == PRESERVE_ALL_RIGHT)
            return "";
        else
            return "" + (preserveLeft != PRESERVE_ALL_LEFT?preserveLeft:0) + 
            (preserveRight != PRESERVE_ALL_RIGHT?"," + preserveRight:"");
    }
    
    /**
     * This returns an PreserveArea object with given bounds.
     * @param pl 0 based coordinate of the left column of the protected area or PRESERVE_ALL_LEFT
     * @param pr 0 based coordinate of the right column of the protected area or PRESERVE_ALL_RIGHT
     * @param nullIfWhole if true null is returned for the unbounded protected area otherwise PRESERVE_ALL
     * @return PreserveArea object with given bounds 
     */
    public static PreservedArea get(int pl, int pr,boolean nullIfWhole) {
        if (pl == PRESERVE_ALL_LEFT && pr == PRESERVE_ALL_RIGHT) 
            return nullIfWhole?null:PRESERVE_ALL;
        else
            return new PreservedArea(pl,pr);
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Nov-05	10170/3	ianw	VBM:2005102504 port forward web clipping

 04-Nov-05	9999/2	pszul	VBM:2005102504 preserver area implemented in ConvertibleImageAsset

 ===========================================================================
*/
