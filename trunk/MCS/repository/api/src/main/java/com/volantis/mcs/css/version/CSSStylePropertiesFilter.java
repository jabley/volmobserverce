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

import com.volantis.mcs.themes.StyleProperties;

/**
 * Implementations of this interface filter MCS theme {@link StyleProperties}
 * instances, removing any data contained within which is not supported for a
 * particular version of CSS.
 */
public interface CSSStylePropertiesFilter {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2004. ";

    /**
     * Filter a collection of MCS theme style properties by removing all the
     * properties, value types and keywords that are not supported by a
     * particular version of CSS.
     *
     * @param input The input style properties, containing any data valid for
     *      MCS.
     * @return the output style properties, containing data valid for a
     *      particular version of CSS. May not be null.
     */
    StyleProperties filter(StyleProperties input);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
