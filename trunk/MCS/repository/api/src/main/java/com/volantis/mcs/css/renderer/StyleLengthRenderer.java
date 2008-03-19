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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/StyleLengthRenderer.java,v 1.5 2003/03/19 09:52:58 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Allan           VBM:2002042404 - The renderer for style
 *                              length values.
 * 22-May-02    Doug            VBM:2002051701 - Modified the render method
 *                              so that the KeyordMapper is obtained via a
 *                              KeywordMapperFactory object.
 * 28-Jun-02    Paul            VBM:2002051302 - Made a singleton.
 * 17-Mar-03    Byron           VBM:2003031105 - Modified render method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.values.LengthUnit;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Render a style value representing an length.
 */
public class StyleLengthRenderer {

    /**
     * The reference to the single allowable instance of this class.
     */
    private static StyleLengthRenderer singleton;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new StyleLengthRenderer();
    }

    /**
     * Get the single allowable instance of this class.
     * @return The single allowable instance of this class.
     */
    public static StyleLengthRenderer getSingleton() {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    protected StyleLengthRenderer() {
    }

    /**
     * Render a StyleLength.
     * 
     * @param value   the StyleLength to render
     * @param context the RendererContext within which to render the
     *                StyleBitSet
     */
    public void render(StyleLength value, RendererContext context)
            throws IOException {

        PrintWriter writer = context.getPrintWriter();

        if (value != null) {
            double number = value.getNumber();
            if (number == 0) {
                writer.write("0");
            } else {
                LengthUnit unit = value.getUnit();

                // If rounding it to an int and back again does not change the
                // number then just write it out as an int. Also, always write
                // out pixels as a whole number.
                int intNumber = (int) Math.round(number);
                if (intNumber == number
                        || value.getUnit() == LengthUnit.PX) {
                    writer.print(intNumber);
                } else {
                    writer.print(number);
                }
                writer.write(unit.toString());
            }
        } else {
            writer.print("");
        }
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10585/1	pduffin	VBM:2005112407 Fixed pair rendering issue, valign="baseline" and also fixed string rendering as well

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
