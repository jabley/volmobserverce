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
package com.volantis.mcs.build.themes;

/**
 * Implementations of this interface supply information about which versions
 * of CSS are supported for this object.
 */
public interface ThemeVersionInfo {

    /**
     * Returns true if this object is supported in CSS1.
     */
    boolean isSupportedInCSS1();

    /**
     * Returns true if this object is supported in CSS2.
     */
    boolean isSupportedInCSS2();

    /**
     * Returns true if this object is supported in CSSMobile.
     */
    boolean isSupportedInCSSMobile();

    /**
     * Returns true if this object is supported in CSSWAP.
     */
    boolean isSupportedInCSSWAP();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
