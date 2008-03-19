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
 * $Header: /src/voyager/com/volantis/mcs/themes/StyleList.java,v 1.4 2002/07/19 14:18:25 doug Exp $
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
 * 19-Jul-02    Doug            VBM:2002071907 - Added public clone() method.
 * 03-Jun-03    Allan           VBM:2003060301 - UndeclaredThrowableException 
 *                              moved to Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;
import com.volantis.mcs.themes.StyleValue;

import java.util.Collections;
import java.util.List;

/**
 */
public final class StyleListImpl
        extends StyleValueImpl implements StyleList {

    /**
     * Indicates whether the items have to be unique within the list.
     *
     * <p>This is used by JiBX.</p>
     */
    private boolean unique;

    /**
     * The list value.
     */
    private List list;

    /**
     * The unmodifiable list.
     */
    private transient List unmodifiableList;

    /**
     * Package private constructor for use by JiBX.
     */
    StyleListImpl() {
    }

    /**
     * Create a new <code>StyleList</code>.
     *
     * @param list The list value.
     */
    public StyleListImpl(List list) {
        this(list, false);
    }

    public StyleListImpl(List list, boolean unique) {

        // Validate the argument.
        if (list == null) {
            throw new IllegalArgumentException("list must not be null");
        }

        this.list = list;
        this.unmodifiableList = Collections.unmodifiableList(list);
        this.unique = unique;
    }

    public boolean isUnique() {
        return unique;
    }

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        return StyleValueType.LIST;
    }

    /**
     * Override this method to call the correct method in the visitor.
     */
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    public List getList() {
        return unmodifiableList;
    }

    protected boolean equalsImpl(Object o) {
        if (!(o instanceof StyleListImpl)) {
            return false;
        }

        StyleListImpl other = (StyleListImpl) o;

        return list.equals(other.list);
    }

    protected int hashCodeImpl() {
        return (unique ? 37 : 17) + list.hashCode();
    }

    public String getStandardCSS() {
        return getStandardCSS(" ");
    }

    public int getStandardCost() {
        int cost = 0;
        for (int i = 0; i < list.size(); i++) {
            StyleValue value = (StyleValue) list.get(i);
            if (i != 0) {
                cost += 1; // Separator, either ' ', or ','.
            }
            cost += value.getStandardCost();
        }

        return cost; 
    }

    public String getStandardCSS(String separator) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            StyleValue styleValue = (StyleValue) list.get(i);
            if (i != 0) {
                buffer.append(separator);
            }
            buffer.append(styleValue);
        }
        return buffer.toString();
    }

    /**
     * Visit all the children.
     *
     * @param visitor The visitor to invoke for each of the children.
     */
    public void visitChildren(StyleValueVisitor visitor) {
        for (int i = 0; i < list.size(); i++) {
            StyleValue value = (StyleValue) list.get(i);
            value.visit(visitor, null);
        }
    }

    /**
     * Invoked by JiBX after it has marshalled the value.
     */
    void jibxPostSet() {
        this.unmodifiableList = Collections.unmodifiableList(list);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10610/1	ianw	VBM:2005120206 Added browse actions for font-family and mcs-chart-forground-color

 06-Dec-05	10608/1	ianw	VBM:2005120206 Added browse actions for font-family and mcs-chart-forground-color

 29-Nov-05	10505/9	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/5	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 20-Sep-05	9513/1	adrianj	VBM:2005091408 Represent style values as elements rather than attributes

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 12-Sep-05	9372/1	ianw	VBM:2005082221 Allow only one instance of MarinerPageContext for a page

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 22-Aug-03	1176/1	adrian	VBM:2003081811 implemented hashcode in StyleValue classes

 ===========================================================================
*/
