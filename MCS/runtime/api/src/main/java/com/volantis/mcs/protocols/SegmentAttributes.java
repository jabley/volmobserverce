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
 * $Header: /src/voyager/com/volantis/mcs/protocols/SegmentAttributes.java,v 1.8 2002/03/18 12:41:17 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Jul-01    Paul            VBM:2001070509 - Added this header and set the
 *                              default tag name.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 21-Sep-01    Doug            VBM:2001090302 - Changed src's type to
 *                              Object to allow LinkComponentNames to be
 *                              passed through to the protocol.
 * 12-Oct-01    Paul            VBM:2001101203 - Changed invalid @see
 *                              reference from #getResize to #isResize. Also,
 *                              added getFrameBorder method as it was missing.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.assets.LinkAssetReference;

/**
 * Class: SegmentAttributes, attribute definition for Segment. All of
 * these attributes are set in the Layout except name and longdesc and src.
 * Segment attributes from the Layout are derived from HTML 4 frame attributes.
 *
 * @mock.generate base="MCSAttributes"
 */
public class SegmentAttributes
        extends MCSAttributes {

    /**
     * Constant used to set scrolling attribute to automatic.
     */
    public static final int SCROLLING_AUTOMATIC = 0;

    /**
     * Constant used to set scrolling attribute to no.
     */
    public static final int SCROLLING_NO = 1;

    /**
     * Constant used to set scrolling attribute to yes.
     */
    public static final int SCROLLING_YES = 2;

    /**
     * Border color.
     *
     * @serial
     * @see #getBorderColor
     * @see #setBorderColor
     */
    private String borderColor;

    /**
     * Frame Border.
     *
     * @serial
     * @see #getFrameBorder
     * @see #setFrameBorder
     */
    private boolean frameBorder;

    /**
     * Long description of the segment.
     *
     * @serial
     * @see #getLongDesc
     * @see #setLongDesc
     */
    private String longDesc;

    /**
     * Margin height.
     *
     * @serial
     * @see #getMarginHeight
     * @see #setMarginHeight
     */
    private int marginHeight;

    /**
     * Margin width.
     *
     * @serial
     * @see #getMarginWidth
     * @see #setMarginWidth
     */
    private int marginWidth;

    /**
     * Name of this segment - should correspond to a segment defined in a
     * layout.
     *
     * @serial
     * @see #getName
     * @see #setName
     */
    private String name;

    /**
     * Resize.
     *
     * @serial
     * @see #isResize
     * @see #setResize
     */
    private boolean resize;

    /**
     * Scrolling.
     *
     * @serial
     * @see #getScrolling
     * @see #setScrolling
     */
    private int scrolling;

    /**
     * The source url for the contents of this segment.
     *
     * @serial
     * @see #getSrc
     * @see #setSrc
     */
    private LinkAssetReference src;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public SegmentAttributes() {
        initialise();
    }

    /**
     * This method should reset the state of this object back to its
     * state immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();

        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise();
    }

    /**
     * Initialise all the data members. This is called from the constructor
     * and also from resetAttributes.
     */
    private void initialise() {

        // Set the default tag name, this is the name of the tag which makes
        // the most use of this class.
        setTagName("segment");

        borderColor = null;
        frameBorder = false;
        longDesc = null;
        marginHeight = 0;
        marginWidth = 0;
        name = null;
        resize = false;
        scrolling = SCROLLING_AUTOMATIC;
        src = null;
    }

    /**
     * Set the borderColor property.
     *
     * @param borderColor The new value of the borderColor property.
     */
    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * Get the value of the borderColor property.
     *
     * @return The value of the borderColor property.
     */
    public String getBorderColor() {
        return borderColor;
    }

    /**
     * Set the frameBorder property.
     *
     * @param frameBorder The new value of the frameBorder property.
     */
    public void setFrameBorder(boolean frameBorder) {
        this.frameBorder = frameBorder;
    }

    /**
     * Get the value of the frameBorder property.
     *
     * @return The value of the frameBorder property.
     */
    public boolean getFrameBorder() {
        return frameBorder;
    }

    /**
     * Get the value of the frameBorder property.
     *
     * @return The value of the frameBorder property.
     */
    public boolean isFrameBorder() {
        return frameBorder;
    }

    /**
     * Set the longDesc property.
     *
     * @param longDesc The new value of the longDesc property.
     */
    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    /**
     * Get the value of the longDesc property.
     *
     * @return The value of the longDesc property.
     */
    public String getLongDesc() {
        return longDesc;
    }

    /**
     * Set the marginHeight property.
     *
     * @param marginHeight The new value of the marginHeight property.
     */
    public void setMarginHeight(int marginHeight) {
        this.marginHeight = marginHeight;
    }

    /**
     * Get the value of the marginHeight property.
     *
     * @return The value of the marginHeight property.
     */
    public int getMarginHeight() {
        return marginHeight;
    }

    /**
     * Set the marginWidth property.
     *
     * @param marginWidth The new value of the marginWidth property.
     */
    public void setMarginWidth(int marginWidth) {
        this.marginWidth = marginWidth;
    }

    /**
     * Get the value of the marginWidth property.
     *
     * @return The value of the marginWidth property.
     */
    public int getMarginWidth() {
        return marginWidth;
    }

    /**
     * Set the name property.
     *
     * @param name The new value of the name property.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value of the name property.
     *
     * @return The value of the name property.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the resize property.
     *
     * @param resize The new value of the resize property.
     */
    public void setResize(boolean resize) {
        this.resize = resize;
    }

    /**
     * Get the value of the resize property.
     *
     * @return The value of the resize property.
     */
    public boolean isResize() {
        return resize;
    }

    /**
     * Set the scrolling property.
     *
     * @param scrolling The new value of the scrolling property.
     */
    public void setScrolling(int scrolling) {
        this.scrolling = scrolling;
    }

    /**
     * Get the value of the scrolling property.
     *
     * @return The value of the scrolling property.
     */
    public int getScrolling() {
        return scrolling;
    }

    /**
     * Set the src property.
     *
     * @param src The new value of the src property.
     */
    public void setSrc(LinkAssetReference src) {
        this.src = src;
    }

    /**
     * Get the value of the src property.
     *
     * @return The value of the src property.
     */
    public LinkAssetReference getSrc() {
        return src;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
