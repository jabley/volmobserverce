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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/StyleTimeRenderer.java,v 1.2 2003/01/02 09:18:19 payal Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description 
 * ---------    --------------- -----------------------------------------------
 * 02-Jan-03    Payal           VBM:2002103102 - Created.Renders a style value 
 *                              representing a time.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleTime;
import com.volantis.mcs.themes.values.TimeUnit;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Render a style value representing an time.
 */
public class StyleTimeRenderer {

    /**
     * The reference to the single allowable instance of this class.
     */
    private static StyleTimeRenderer singleton;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new StyleTimeRenderer ();
    }

    /**
     * Get the single allowable instance of this class.
     * @return The single allowable instance of this class.
     */
    public static StyleTimeRenderer getSingleton () {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    protected StyleTimeRenderer () {
    }

    /**
     * Render a StyleTime.
     * @param value the StyleTime to render
     * @param context the RendererContext within which to render the StyleTime
     */
    public void render(StyleTime value, RendererContext context)
        throws IOException {

        PrintWriter writer = context.getPrintWriter();

        double number = value.getNumber();
        TimeUnit unit = value.getUnit();

        // If rounding it to an int and back again does not change the
        // number then just write it out as an int.
        int intNumber = (int) number;
        if (intNumber == number) {
            writer.print(intNumber);
        } else {
            writer.print(number);
        }

        writer.write(unit.toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
