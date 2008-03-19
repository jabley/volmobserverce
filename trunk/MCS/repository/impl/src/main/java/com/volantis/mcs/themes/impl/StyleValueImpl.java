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
 * $Header: /src/voyager/com/volantis/mcs/themes/StyleValue.java,v 1.9 2002/07/30 09:28:24 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Apr-02    Doug            VBM:2002040803 - Created as part of
 *                              implementation of new internal themes structure
 * 27-Apr-02    Doug            VBM:2002040803 - Added the VALUE_NOT_SET 
 *                              constant
 * 28-Apr-02    Allan           VBM:2002042404 - Added equals().
 * 29-Apr-02    Doug            VBM:2002040803 - Fixed bug with the 
 *                              PRIORITY_MAX constant.
 * 29-Apr-02    Doug            VBM:2002040803 - Ensured default priority is
 *                              normal and not important.
 * 06-Jun-02    Mat             VBM:2002060601 - Added STYLE_INVALID constant
 *                              to indicate an invalid style value.
 * 28-Jun-02    Paul            VBM:2002051302 - Made the toString value more
 *                              meaningful.
 * 19-Jul-02    Doug            VBM:2002071907 - Added a public clone() method.
 * 29-Jul-2002  Sumit           VBM:2002072906 - Added VALUE_TIME for StyleTime
 * 03-Jun-03    Allan           VBM:2003060301 - UndeclaredThrowableException 
 *                              moved to Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.model.jibx.JiBXBase;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.themes.StyleValue;

/**
 */
public abstract class StyleValueImpl 
        extends JiBXBase implements StyleValue {

    /**
     * The cached hash code.
     */
    private transient int cachedHashCode;


    /**
     * Create a new <code>StyleValueImpl</code>.
     */
    public StyleValueImpl() {
    }

    /**
     * Initialise.
     *
     * @param location The source location of the object, may be null.
     */
    StyleValueImpl(SourceLocation location) {
        super(location);
    }

    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        return equalsImpl(o);
    }

    protected abstract boolean equalsImpl(Object o);

    public int hashCode() {
        if (cachedHashCode == 0) {
            cachedHashCode = hashCodeImpl();
            if (cachedHashCode == 0) {
                cachedHashCode = 1;
            }
        }

        return cachedHashCode;
    }

    protected abstract int hashCodeImpl();

    public int getStandardCost() {
        return getStandardCSS().length();
    }

    // Javadoc inherited from super class.
    public String toString() {
        return getStandardCSS();
    }

    /**
     * All style values are immutable so there is no need to copy them.
     *
     * @return No need to copy.
     */
    public final Object copy() {
        return this;
    }

    /**
     * Display a double without .0 if necessary
     *
     * todo Merge with SerializeStyleValues
     */
    protected final static String convertDoubleToText(double value) {
        int intValue = (int) value;
        String text;
        if (intValue == value) {
            text = Integer.toString(intValue, 10);
        } else {
            text = Double.toString(value);
        }
        return text;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/21	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/9	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/17	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/7	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 29-Nov-05	10505/13	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (5)

 18-Nov-05	10347/5	pduffin	VBM:2005111405 Stopped copying style values in order to change whether they were explicitly specified or not

 29-Nov-05	10505/9	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 18-Nov-05	10347/3	pduffin	VBM:2005111405 Corrected issue with styling

 29-Nov-05	10505/5	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 16-Nov-05	10315/3	pduffin	VBM:2005111410 Added support for copying model objects

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/3	pduffin	VBM:2005111410 Added support for copying model objects

 14-Nov-05	10287/2	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.
 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Jun-05	7997/3	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 15-Feb-05	6969/1	geoff	VBM:2005021403 R821: Branding using Projects: Prerequisites: Push project into CSS StyleValues

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 11-Nov-04	6158/1	adrianj	VBM:2004110108 Added FontSizeComputer

 11-Nov-04	6138/1	adrianj	VBM:2004110810 Added FontSizeComputer

 01-Nov-04	5992/1	adrianj	VBM:2004101403 Moved generation of computed style values

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 22-Aug-03	1176/2	adrian	VBM:2003081811 implemented hashcode in StyleValue classes

 ===========================================================================
*/
