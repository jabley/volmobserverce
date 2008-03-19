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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.mcs.themes.StyleColor;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.StyleComponentURI;

/**
 * <p/>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface FormatAttributeToStyleValueConverter {

    StyleValueFactory getFactory();

    /**
     * Convert colour value expressed as a string to a StyleColor.
     *
     * @param colour The colour, may be null.
     * @return The style color.
     */
    StyleColor getColorValue(String colour);

    /**
     * Convert the given dimension string to a StyleValue. If the string
     * corresponds to a value of zero, null will be returned. If is not set, it
     * will be set to a StylePercentage of 100%, otherwise it will be converted
     * to the correct StyleValue.
     *
     * @param dimensionString       to convert to a StyleValue
     * @param unitString            the units of the dimensionString
     * @return StyleValue which represents the given dimension string.
     * May be null.
     */
    StyleValue getDimensionValue(String dimensionString,
            String unitString);

    /**
     * Convert the given magnitudeString to a StyleValue. If the string
     * corresponds to a value of zero, it will either be treated as a
     * significant value that should be converted to a StyleValue, or as a
     * default value in which case null will be returned.
     *
     * @param magnitudeString   to convert to a StyleValue
     * @param zeroIsSignificant if true, string which correspond to zero should
     * be treated as a significant value and converted to a StyleValue and
     * returned. If false, null will be returned if the given string represents
     * zero
     * @return StyleValue which represents the given magnitudeString.
     * May be null.
     */
    StyleValue getLengthValue(String magnitudeString, boolean zeroIsSignificant);

    StyleValue getPairValue(StyleValue value);

    StyleComponentURI getComponentURI(String backgroundComponentName);

    /**
     * Translate the horizontal alignment represented as a string into
     * a StyleValue.
     *
     * @param align
     * @return valid horizontal alignment StyleValue or null
     */
    StyleValue getHorizontalAlign(String align);

    /**
     * Translate the vertical alignment represented as a string into
     * a StyleValue.
     *
     * @param align
     * @return valid vertical alignment StyleValue or null
     */
    StyleValue getVerticalAlign(String align);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/3	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 09-Nov-05	10201/1	emma	VBM:2005102606 Fixing various styling bugs

 09-Nov-05	10221/1	emma	VBM:2005102606 Forward port: fixing various styling bugs

 09-Nov-05	10201/1	emma	VBM:2005102606 Fixing various styling bugs

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
