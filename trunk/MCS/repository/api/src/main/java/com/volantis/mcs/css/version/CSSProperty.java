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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.version;

import com.volantis.styling.properties.StyleProperty;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleValueType;

/**
 * Implementations of this interface define which MCS theme value types and
 * keywords are supported for a particular property in a particular version of
 * CSS.
 *
 * @mock.generate 
 */
public interface CSSProperty {

    StyleProperty getStyleProperty();

    /**
     * Returns true if the value type supplied, as defined by
     * {@link com.volantis.mcs.themes.StyleValue}, is supported for this
     * property in this version of CSS.
     * <p>
     * NOTE: this accepts the int enumeration value of value types from
     * {@link com.volantis.mcs.themes.StyleValue} because the proper Typesafe
     * Enum value from {@link com.volantis.mcs.themes.StyleValueType} is not
     * (currently) available in the runtime. In future it would be good it
     * it was to make this method typesafe.
     *
     * @param valueType the value type enumeration value.
     * @return true if the value type is supported.
     */
    boolean supportsValueType(StyleValueType valueType);

    /**
     * Returns true if the keyword supplied is supported, as defined by the
     * appropriate keyword enumeration class, for this property in this
     * version of CSS.
     *
     * @param keyword the keyword enumeration value.
     * @return true if the keyword is supported.
     */
    boolean supportsKeyword(StyleKeyword keyword);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 29-Nov-05	10370/2	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 18-Nov-05	10370/1	geoff	VBM:2005111405 interim commit

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
