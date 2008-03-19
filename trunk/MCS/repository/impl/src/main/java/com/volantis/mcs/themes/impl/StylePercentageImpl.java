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
 * $Header: /src/voyager/com/volantis/mcs/themes/StylePercentage.java,v 1.3 2002/06/29 01:04:52 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Apr-02    Doug            VBM:2002040803 - Created as part of
 *                              implementation of new internal themes structure
 * 29-May-02    Steve           VBM:2002052901 - Added equals()... the base
 *                              class equals() was called by the renderer
 *                              which always returns true for this style value.
 * 28-Jun-02    Paul            VBM:2002051302 - Made the toString value more
 *                              meaningful.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;

/**
 */
public final class StylePercentageImpl
        extends StyleValueImpl implements StylePercentage {

    /**
     * The percentage value.
     */
    private double percentage;

    /**
     * Package private constructor for use by JiBX.
     */
    StylePercentageImpl() {
    }

    /**
     * Initialise.
     *
     * @param percentage The percentage value.
     */
    public StylePercentageImpl(double percentage) {
        this(null, percentage);
    }

    /**
     * Initialise.
     *
     * @param location   The source location of the object, may be null.
     * @param percentage The percentage value.
     */
    public StylePercentageImpl(SourceLocation location, double percentage) {
        super(location);

        this.percentage = percentage;
    }

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        return StyleValueType.PERCENTAGE;
    }

    /**
     * Override this method to call the correct method in the visitor.
     */
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    public double getPercentage() {
        return percentage;
    }

    protected boolean equalsImpl(Object o) {
        if (!(o instanceof StylePercentageImpl)) {
            return false;
        }

        StylePercentageImpl other = (StylePercentageImpl) o;

        return percentage == other.percentage;
    }

    protected int hashCodeImpl() {
        int result = 0;
        long bits = Double.doubleToLongBits(getPercentage());
        result = 37 * result + (int) (bits ^ (bits >>> 32));
        return result;
    }

    public String getStandardCSS() {
        return convertDoubleToText(percentage) + "%";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 01-Dec-05	10514/1	ianw	VBM:2005112406 Fixed XDIMECP Title elemement

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 22-Aug-03	1176/1	adrian	VBM:2003081811 implemented hashcode in StyleValue classes

 ===========================================================================
*/
