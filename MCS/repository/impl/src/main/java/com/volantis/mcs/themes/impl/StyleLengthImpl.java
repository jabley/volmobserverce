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
 * $Header: /src/voyager/com/volantis/mcs/themes/StyleLength.java,v 1.5 2003/03/19 09:52:58 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Apr-02    Doug            VBM:2002040803 - Created as part of
 *                              implementation of new internal themes structure
 * 27-Apr-02    Doug            VBM:2002040803 - Added the 
 *                              LengthUnit import statement.
 * 28-Apr-02    Allan           VBM:2002042404 - Added equals().
 * 28-Jun-02    Paul            VBM:2002051302 - Made the toString value more
 *                              meaningful.
 * 18-Mar-03    Byron           VBM:2003031105 - Added pixels method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleValueVisitor;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.model.validation.SourceLocation;

/**
 */
public final class StyleLengthImpl
        extends StyleValueImpl implements StyleLength {

    /**
     * The unit in which the length is stored.
     */
    private LengthUnit unit;

    /**
     * The number of unit.
     */
    private double number;

    /**
     * Package private constructor for use by JiBX.
     */
    StyleLengthImpl() {
    }

    /**
     * Initialise.
     *
     * @param number The number.
     * @param unit   The unit.
     */
    public StyleLengthImpl(double number, LengthUnit unit) {
        this(null, number, unit);
    }

    /**
     * Initialise.
     *
     * @param location The source location of the object, may be null.
     * @param number   The number.
     * @param unit     The unit.
     */
    public StyleLengthImpl(
            SourceLocation location, double number, LengthUnit unit) {
        super(location);

        this.number = number;

        // Validate the argument.
        if (unit == null) {
            throw new IllegalArgumentException("unit cannot be null");
        }
        this.unit = unit;
    }

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        return StyleValueType.LENGTH;
    }

    /**
     * Override this method to call the correct method in the visitor.
     */
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    public LengthUnit getUnit() {
        return unit;
    }

    public double getNumber() {
        return number;
    }


    public int pixels() {
        if (getUnit() != LengthUnit.PX) {
            throw new IllegalStateException("Pixels value should only be used " +
                    "for PIXELS unit type");
        }
        // @todo later not sure why the number is a double. Is this much
        // precision necessary? (it results in lossy casts like this...)
        return (int) Math.round(getNumber());
    }

    protected boolean equalsImpl(Object o) {
        if (!(o instanceof StyleLengthImpl)) {
            return false;
        }

        StyleLengthImpl other = (StyleLengthImpl) o;

        return number == other.number && (number == 0 || unit == other.unit);
    }

    protected int hashCodeImpl() {
        int result = 0;
        long number = Double.doubleToLongBits(getNumber());
        result = 37 * result + (int) (number ^ (number >>> 32));
        if (this.number != 0) {
            result = 37 * result + unit.hashCode();
        }
        return result;
    }

    // Javadoc inherited.
    public String getStandardCSS() {
        if (number == 0) {
            return "0";
        } else {
            return convertDoubleToText(number) + unit;
        }
    }

    public int getStandardCost() {
        if (number == 0) {
            return 1;
        } else {
            return getStandardCSS().length();
        }
    }

    public String getPixelsAsString() {
        if (unit != LengthUnit.PX) {
            return null;
        }
        return Integer.toString((int) number);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 27-Sep-05	9487/3	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 22-Aug-03	1176/1	adrian	VBM:2003081811 implemented hashcode in StyleValue classes

 ===========================================================================
*/
