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
 * $Header: /src/voyager/com/volantis/mcs/themes/StyleInvalid.java,v 1.5 2002/08/08 13:57:43 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Jun-02    Mat             VBM:2002060601 - A StyleValue used to represent
 *                              an invalid styleValue
 * 06-Jun-02    Mat             VBM:2002060601 - Added fields to hold the 
 *                              property and value that were being set.
 * 19-Jul-02    Doug            VBM:2002071907 - Added public equals() & 
 *                              hashcode() methods.
 * 23-Jul-02    Ian             VBM:2002071802 - Enabled visit method so
 *                              StyleInvalid can be rendered in the GUI.
 * 06-Aug-02    Doug            VBM:2002072408 - Fixed bug in equals method.
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;
import com.volantis.mcs.themes.StyleInvalid;

/**
 */
public final class StyleInvalidImpl
        extends StyleValueImpl implements StyleInvalid {

    /**
     * The value that was being attempted
     */
    private String value;

    /**
     * Package private constructor for use by JiBX.
     */
    StyleInvalidImpl() {
    }

    /**
     * Create a new <code>StyleInherit</code>.
     */
    public StyleInvalidImpl(String value) {
        this.value = value;
    }

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        throw new UnsupportedOperationException();
    }

    /**
     * Override this method to call the correct method in the visitor.
     */
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return "Invalid " + value;
    }

    protected boolean equalsImpl(Object o) {
        if (!(o instanceof StyleInvalidImpl)) {
            return false;
        }

        StyleInvalidImpl other = (StyleInvalidImpl) o;

        return value.equals(other.value);
    }

    protected int hashCodeImpl() {
        return value.hashCode();
    }

    public String getStandardCSS() {
        return "<" + value + ">";
    }

    public int getStandardCost() {
        return 1 + value.length() + 1; 
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 16-Sep-05	9512/1	pduffin	VBM:2005091408 Added support for invalid style values

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 22-Aug-03	1176/1	adrian	VBM:2003081811 implemented hashcode in StyleValue classes

 ===========================================================================
*/
