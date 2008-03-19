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
 * $Header: /src/voyager/com/volantis/mcs/themes/StylePair.java,v 1.5 2002/08/08 13:57:43 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Apr-02    Doug            VBM:2002040803 - Created as part of
 *                              implementation of new internal themes structure
 * 28-Apr-02    Allan           VBM:2002042404 - Added equals().
 * 28-Jun-02    Paul            VBM:2002051302 - Made the toString value more
 *                              meaningful.
 * 19-Jul-02    Doug            VBM:2002071907 - Added public clone() method
 *                              and fixed null pointer exception in equals().
 * 06-Aug-02    Doug            VBM:2002072408 - Fixed bug in equals method.
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl;

import com.volantis.synergetics.ObjectHelper;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;

/**
 * This class represents a pair of values of a style property.
 */
public final class StylePairImpl
        extends StyleValueImpl implements StylePair {

    /**
     * The first value.
     */
    private StyleValue first;

    /**
     * The second value.
     */
    private StyleValue second;

    /**
     * Package private constructor for use by JiBX.
     */
    StylePairImpl() {
    }

    /**
     * Initialise.
     *
     * @param first  The first value.
     * @param second The second value.
     */
    public StylePairImpl(
            StyleValue first,
            StyleValue second) {
        this(first, first, second);
    }

    /**
     * Initialise.
     *
     * @param first  The first value.
     * @param second The second value.
     */
    public StylePairImpl(SourceLocation location,
                     StyleValue first, StyleValue second) {
        super(location);

        this.first = first;
        this.second = second;
    }

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        return StyleValueType.PAIR;
    }

    /**
     * Override this method to call the correct method in the visitor.
     */
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    public StyleValue getFirst() {
        return first;
    }

    public StyleValue getSecond() {
        return second;
    }

    /**
     * This method is called by JiBX to figure out if the second part of the
     * pair is present. This is required to allow the marshalling to figure out
     * that it should not write an empty <second></second> if the second part of
     * the pair is not present.
     * <p>
     * ********* DO NOT REMOVE THIS METHOD - IT IS USED BY JIBX *************
     *
     * @return true if the pair contains the optional second part.
     */
    boolean jibxHasSecond() {
        return second != null;
    }

    protected boolean equalsImpl(Object o) {
        if (!(o instanceof StylePairImpl)) {
            return false;
        }

        StylePairImpl other = (StylePairImpl) o;

        return ObjectHelper.equals(first, other.first) &&
                ObjectHelper.equals(second, other.second);
    }

    protected int hashCodeImpl() {
        int result = 0;
        result = 37 * result + first.hashCode();
        if (second != null) {
            result = 37 * result + second.hashCode();
        }
        return result;
    }

    public String getStandardCSS() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(first);
        if (second != null) {
            buffer.append(" ");
            buffer.append(second);
        }
        return buffer.toString();
    }

    public int getStandardCost() {
        int cost = first.getStandardCost();
        if (second != null) {
            cost += second.getStandardCost();
        }
        return cost;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/9	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/5	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 31-Oct-05	9992/3	emma	VBM:2005101811 Adding new style property validation

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 27-Oct-05	10007/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9992/3	emma	VBM:2005101811 Adding new style property validation

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 15-Jul-04	4869/1	geoff	VBM:2004062303 Implementation of theme style options: HTMLVersion 3.2 Family

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 22-Aug-03	1176/1	adrian	VBM:2003081811 implemented hashcode in StyleValue classes

 ===========================================================================
*/
