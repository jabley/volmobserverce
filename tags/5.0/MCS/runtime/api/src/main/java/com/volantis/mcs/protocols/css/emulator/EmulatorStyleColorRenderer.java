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
 * $Header: /src/voyager/com/volantis/mcs/protocols/css/emulator/EmulatorStyleColorRenderer.java,v 1.2 2002/07/08 09:04:19 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Adrian          VBM:2002040808 - The emulator renderer for
 *                              style colour values.
 * 03-May-02    Adrian          VBM:2002040808 - Modified renderPercentages and
 *                              renderRGB to only write values in the format
 *                              #RRGGBB
 * 06-Jun-02    Adrian          VBM:2002040808 - Get KeywordMapper from the
 *                              KeywordMapperFactory in the RenderContext
 * 28-Jun-02    Paul            VBM:2002051302 - Moved from css.emulator and
 *                              changed to extend StyleColorRenderer.
 * 08-Jul-02    Adrian          VBM:2002070405 - changed method renderTriplet
 *                              to writeTriplet to correctly override the
 *                              superclass method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css.emulator;

import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.css.renderer.StyleColorRenderer;
import com.volantis.mcs.themes.StyleColorPercentages;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Render a style value representing a color.
 */
public final class EmulatorStyleColorRenderer
        extends StyleColorRenderer {

    /**
     * The reference to the single allowable instance of this class.
     */
    private static final StyleColorRenderer singleton;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new EmulatorStyleColorRenderer();
    }

    /**
     * Get the single allowable instance of this class.
     *
     * @return The single allowable instance of this class.
     */
    public static StyleColorRenderer getSingleton() {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    private EmulatorStyleColorRenderer() {
    }

    /**
     * Used to perform calculation of RGB color from percentage RGB colors.
     */
    private static final double dbl255 = 2.55;

    /**
     * Override this method to render the rgb value as six digits instead of
     * three.
     */
    protected void writeTriplet(Writer writer, int r, int g, int b)
            throws IOException {

        writeNibble(r, writer);
        writeNibble(r, writer);
        writeNibble(g, writer);
        writeNibble(g, writer);
        writeNibble(b, writer);
        writeNibble(b, writer);
    }

    /**
     * Render a StyleColor percentage as an RGB value
     *
     * @param value   the StyleColor to render
     * @param context the RendererContext within which to render the StyleColor
     */
    public void renderPercentages(
            StyleColorPercentages value,
            RendererContext context)
            throws IOException {

        PrintWriter writer = context.getPrintWriter();

        double red = value.getRed() * dbl255;
        double green = value.getGreen() * dbl255;
        double blue = value.getBlue() * dbl255;

        int redInt = round(red);
        int greenInt = round(green);
        int blueInt = round(blue);

        int r0 = (redInt & 0x0000f0) >> 4;
        int r1 = redInt & 0x00000f;

        int g0 = (greenInt & 0x0000f0) >> 4;
        int g1 = greenInt & 0x00000f;

        int b0 = (blueInt & 0x0000f0) >> 4;
        int b1 = blueInt & 0x00000f;

        writer.write('#');
        writeNibble(r0, writer);
        writeNibble(r1, writer);
        writeNibble(g0, writer);
        writeNibble(g1, writer);
        writeNibble(b0, writer);
        writeNibble(b1, writer);
    }

    /**
     * Round the double value to the "nearest neighbour" int
     *
     * @param dbl the double to round.
     * @return the int value
     */
    private int round(double dbl) {
        int i = (int) dbl;
        double d = dbl - i;
        if (d > 0.5) {
            i = i + 1;
        }
        return i;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 ===========================================================================
*/
