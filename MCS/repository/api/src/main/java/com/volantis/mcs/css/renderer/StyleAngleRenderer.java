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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/StyleAngleRenderer.java,v 1.3 2002/06/29 01:04:51 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Allan           VBM:2002042404 - Created. The renderer for
 *                              style angle values. 
 * 22-May-02    Doug            VBM:2002051701 - Modified the render method
 *                              so that the KeyordMapper is obtained via a 
 *                              KeywordMapperFactory object.
 * 28-Jun-02    Paul            VBM:2002051302 - Made a singleton.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleAngle;
import com.volantis.mcs.themes.values.AngleUnit;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Render a style value representing an angle.
 */
public class StyleAngleRenderer {

    /**
     * The reference to the single allowable instance of this class.
     */
    private static StyleAngleRenderer singleton;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new StyleAngleRenderer();
    }

    /**
     * Get the single allowable instance of this class.
     *
     * @return The single allowable instance of this class.
     */
    public static StyleAngleRenderer getSingleton() {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    protected StyleAngleRenderer() {
    }

    /**
     * Render a StyleAngle.
     *
     * @param value   the StyleAngle to render
     * @param context the RendererContext within which to render the StyleAngle
     */
    public void render(StyleAngle value, RendererContext context)
            throws IOException {

        PrintWriter writer = context.getPrintWriter();

        double number = value.getNumber();
        AngleUnit unit = value.getUnit();

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
