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
 * $Header: /src/voyager/com/volantis/mcs/layouts/Segment.java,v 1.25 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Jun-01    Paul            VBM:2001062704 - Added this change history
 * 09-Jul-01    Paul            VBM:2001062810 - Changed the visit method to
 *                              return a boolean.
 * 24-Jul-01    Paul            VBM:2001071101 - Added the pseudo default
 *                              segment name attribute to the list of ones
 *                              which can be modified by the user.
 * 26-Jul-01    Paul            VBM:2001071707 - Modified to make it compatible
 *                              with some changes to the SegmentAttributes
 *                              class.
 * 27-Jul-01    Paul            VBM:2001072603 - Renamed getDefaultSegment
 *                              method to getDefaultSegmentName.
 * 30-Jul-01    Paul            VBM:2001071609 - Removed some unnecessary code.
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout and all methods relating to the
 *                              runtime generation of layouts have been
 *                              moved into protocols.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 14-Mar-02    Adrian          VBM:2002020101 - Added methods
 *                              checkUnsetDefaultSegment
 *                              checkResetDefaultSegment to manage the state
 *                              of the DeviceLayout defaultSegment.  If the
 *                              segment is removed it remembers if it was the
 *                              default in case it is readded.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 10-Dec-02    Allan           VBM:2002110102 -
 *                              Modified setAttribute() to make more use of
 *                              super.setAttribute(). Removed setDeviceLayout.
 *                              to string.
 * 19-Feb-03    Allan           VBM:2003021803 - Implement equals() and 
 *                              hashCode(). 
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() method.
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.synergetics.ObjectHelper;

/**
 * A format for segments (e.g. frames).
 *
 * @mock.generate base="Format"
 */
public class Segment
        extends Format
        implements BorderColourAttribute, FrameBorderAttribute {

    private static String[] userAttributes = new String[]{
        FormatConstants.NAME_ATTRIBUTE,
        FormatConstants.FRAME_BORDER_ATTRIBUTE,
        FormatConstants.BORDER_COLOUR_ATTRIBUTE,
        FormatConstants.SCROLLING_ATTRIBUTE,
        FormatConstants.MARGIN_HEIGHT_ATTRIBUTE,
        FormatConstants.MARGIN_WIDTH_ATTRIBUTE,
        FormatConstants.RESIZE_ATTRIBUTE,
        FormatConstants.DEFAULT_SEGMENT_NAME_PSEUDO_ATTRIBUTE
    };

    private static String[] defaultAttributes = new String[]{
        FormatConstants.FRAME_BORDER_ATTRIBUTE,
        FormatConstants.SCROLLING_ATTRIBUTE,
        FormatConstants.MARGIN_HEIGHT_ATTRIBUTE,
        FormatConstants.MARGIN_WIDTH_ATTRIBUTE,
        FormatConstants.RESIZE_ATTRIBUTE
    };

    private static String[] persistentAttributes = new String[]{
        FormatConstants.NAME_ATTRIBUTE,
        FormatConstants.FRAME_BORDER_ATTRIBUTE,
        FormatConstants.BORDER_COLOUR_ATTRIBUTE,
        FormatConstants.SCROLLING_ATTRIBUTE,
        FormatConstants.MARGIN_HEIGHT_ATTRIBUTE,
        FormatConstants.MARGIN_WIDTH_ATTRIBUTE,
        FormatConstants.RESIZE_ATTRIBUTE
    };

    /**
     * If this is the Layout DefaultSegment we need to remember if
     * orphaned from the parent incase we get a new parent and are
     * therefore still part of the layout and thereby still the default.
     */
    private boolean orphanedDefaultSegment = false;

    /**
     * Create a new segment
     *
     * @param montageLayout The Layout to which this segment belongs
     */
    public Segment(MontageLayout montageLayout) {
        super(0, montageLayout);
    }

    public FormatType getFormatType() {
        return FormatType.SEGMENT;
    }

    public String[] getUserAttributes() {
        return userAttributes;
    }

    public String[] getDefaultAttributes() {
        return defaultAttributes;
    }

    public String[] getPersistentAttributes() {
        return persistentAttributes;
    }

    /**
     * Override the SimpleAttributeContainer.setAttribute method to enable
     * special processing to be done.
     */
    public void setAttribute(String name, Object value) {
        if (name.equals(FormatConstants.DEFAULT_SEGMENT_NAME_PSEUDO_ATTRIBUTE)) {
            Layout layout = getLayout();
            String defaultSegmentName = layout.getDefaultSegmentName();
            String segmentName = getName();

            if ("true".equals(value)) {
                layout.setDefaultSegmentName(segmentName);
            } else if (segmentName.equals(defaultSegmentName)) {
                layout.setDefaultSegmentName(null);
            }
        } else {
            super.setAttribute(name, value);
        }
    }

    /**
     * Override the SimpleAttributeContainer.getAttribute method to enable
     * special processing to be done.
     */
    public Object getAttribute(String name) {
        if (name.equals(FormatConstants.DEFAULT_SEGMENT_NAME_PSEUDO_ATTRIBUTE)) {
            String defaultSegmentName = getLayout().getDefaultSegmentName();
            String segmentName = getName();
            if (segmentName.equals(defaultSegmentName)) {
                return "true";
            }
            return "false";
        } else {
            return super.getAttribute(name);
        }
    }

    /**
     * Sets the frame border.
     * @param frameBorder the frame border.
     */
    public void setFrameBorder(String frameBorder) {
        setAttribute(FormatConstants.FRAME_BORDER_ATTRIBUTE,
                     frameBorder);
    }

    /**
     * Gets the frame border.
     * @return the frame border.
     */
    public String getFrameBorder() {
        return (String) getAttribute(FormatConstants.FRAME_BORDER_ATTRIBUTE);
    }

    /**
     * Sets the scrolling.
     * @param scrolling the scrolling.
     */
    public void setScrolling(String scrolling) {
        setAttribute(FormatConstants.SCROLLING_ATTRIBUTE,
                     scrolling);
    }

    /**
     * Gets the scrolling.
     * @return the scrolling.
     */
    public String getScrolling() {
        return (String) getAttribute(FormatConstants.SCROLLING_ATTRIBUTE);
    }

    /**
     * Sets the margin height.
     * @param marginHeight the margin height.
     */
    public void setMarginHeight(String marginHeight) {
        setAttribute(FormatConstants.MARGIN_HEIGHT_ATTRIBUTE,
                     marginHeight);
    }

    /**
     * Gets the margin height.
     * @return the margin height.
     */
    public String getMarginHeight() {
        return (String) getAttribute(FormatConstants.MARGIN_HEIGHT_ATTRIBUTE);
    }

    /**
     * Sets the margin width.
     * @param marginWidth the margin width.
     */
    public void setMarginWidth(String marginWidth) {
        setAttribute(FormatConstants.MARGIN_WIDTH_ATTRIBUTE,
                     marginWidth);
    }

    /**
     * Gets the margin width.
     * @return the margin width.
     */
    public String getMarginWidth() {
        return (String) getAttribute(FormatConstants.MARGIN_WIDTH_ATTRIBUTE);
    }

    /**
     * Sets the default segment name.
     * @param defaultSegmentName the default segment name.
     */
    public void setDefaultSegmentName(String defaultSegmentName) {
        setAttribute(FormatConstants.DEFAULT_SEGMENT_NAME_PSEUDO_ATTRIBUTE,
                     defaultSegmentName);
    }

    /**
     * Gets the default segment name.
     * @return the default segment name.
     */
    public String getDefaultSegmentName() {
        return (String)
                getAttribute(FormatConstants.DEFAULT_SEGMENT_NAME_PSEUDO_ATTRIBUTE);
    }

    /**
     * Sets the border color.
     * @param borderColour the border color.
     */
    public void setBorderColour(String borderColour) {
        setAttribute(FormatConstants.BORDER_COLOUR_ATTRIBUTE,
                     borderColour);
    }

    public String getBorderColour() {
        return (String) getAttribute(FormatConstants.BORDER_COLOUR_ATTRIBUTE);
    }

    /**
     * Sets the resize.
     * @param resize the resize.
     */
    public void setResize(String resize) {
        setAttribute(FormatConstants.RESIZE_ATTRIBUTE,
                     resize);
    }

    public String getResize() {
        return (String) getAttribute(FormatConstants.RESIZE_ATTRIBUTE);
    }

    // Javadoc inherited from super class.
    public boolean visit(FormatVisitor visitor, Object object)
            throws FormatVisitorException {
        return visitor.visit(this, object);
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {
        // todo: later: add validation for segments and segment grids
    }

    /**
     * Check if this is the <code>Layout</code> default segment.
     * If so, set this to be false and remember that it used to be the default
     * in case this segment is readded later.
     *
     * @return true if this was the default segment
     */
    public boolean checkUnsetDefaultSegment() {
        String isDefault = (String)
                getAttribute(FormatConstants.DEFAULT_SEGMENT_NAME_PSEUDO_ATTRIBUTE);
        if (isDefault.equals("true")) {
            setAttribute(FormatConstants.DEFAULT_SEGMENT_NAME_PSEUDO_ATTRIBUTE,
                         "false");
            orphanedDefaultSegment = true;
            return true;

        }
        return false;
    }

    /**
     * Check if this was the <code>Layout</code> default segment.
     * If so, reset this to be true as this segment has been readded to the
     * Layout.
     *
     * @return true if this was reset to be the default segment
     */
    public boolean checkResetDefaultSegment() {
        if (orphanedDefaultSegment) {
            setAttribute(FormatConstants.DEFAULT_SEGMENT_NAME_PSEUDO_ATTRIBUTE,
                         "true");
            orphanedDefaultSegment = false;
            return true;
        }
        return false;
    }

    // javadoc inherited
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        if (!super.equals(other)) return false;

        final Segment segment = (Segment) other;
        return orphanedDefaultSegment == segment.orphanedDefaultSegment;
    }
    
    // javadoc inherited
    public int hashCode() {
        return super.hashCode() * 31 + (orphanedDefaultSegment? 1: 0);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Oct-05	9652/1	gkoch	VBM:2005092204 completely custom marshalling/unmarshalling of layoutFormat

 30-Sep-05	9590/3	schaloner	VBM:2005092204 finished regular JiBX bindings

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
