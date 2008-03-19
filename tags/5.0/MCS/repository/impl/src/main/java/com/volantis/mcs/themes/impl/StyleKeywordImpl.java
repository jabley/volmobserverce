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
 * $Header: /src/voyager/com/volantis/mcs/themes/StyleKeyword.java,v 1.3 2002/06/29 01:04:52 pduffin Exp $
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
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleValueVisitor;

/**
 */
public final class StyleKeywordImpl
        extends StyleValueImpl implements StyleKeyword {

    /**
     * A string representation of the keyword.
     */
    private String name;

    /**
     * Package private constructor for use by JiBX.
     */
    StyleKeywordImpl() {
    }

    /**
     * @param identifier
     * @deprecated Use generated keyword.
     */ 
    public StyleKeywordImpl(String identifier) {
        // Keywords should not be case sensitive.
        this.name = identifier.toLowerCase();
    }

    public boolean isInternal() {
        return name.startsWith("-internal-");
    }

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        return StyleValueType.KEYWORD;
    }

    /**
     * Override this method to call the correct method in the visitor.
     */
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    protected boolean equalsImpl(Object o) {
        return false;
    }

    protected int hashCodeImpl() {
        return System.identityHashCode(this);
    }

    public String getStandardCSS() {
        return name;
    }

    public String getName() {
        return name;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 31-Oct-05	9992/3	emma	VBM:2005101811 Adding new style property validation

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 22-Aug-03	1176/1	adrian	VBM:2003081811 implemented hashcode in StyleValue classes

 ===========================================================================
*/
