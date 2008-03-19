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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/StyleColorRenderer.java,v 1.5 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Allan           VBM:2002042404 - The renderer for style colour
 *                               values.
 * 22-May-02    Doug            VBM:2002051701 - Modified the renderName method
 *                              so that the KeyordMapper is obtained via a
 *                              KeywordMapperFactory object.
 * 28-Jun-02    Paul            VBM:2002051302 - Made a singleton.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleColorPercentages;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleSyntaxes;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.css.version.CSSVersion;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Render a style value representing a color.
 */
public class StyleColorRenderer {

  private static char [] hexDigits = new char [] {
    '0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
  };

  /**
   * The reference to the single allowable instance of this class.
   */
  private static StyleColorRenderer singleton;

  // Initialise the static fields.
  static {
    // Always initialise to prevent a synchronization problem if we do it
    // lazily.
    singleton = new StyleColorRenderer ();
  }

  /**
   * Get the single allowable instance of this class.
   * @return The single allowable instance of this class.
   */
  public static StyleColorRenderer getSingleton () {
    return singleton;
  }

  /**
   * Protect the constructor to prevent any other instances being created.
   */
  protected StyleColorRenderer () {
  }

    /**
     * Render a StyleColor as a name.
     *
     * @param value   the StyleColor to render
     * @param context the RendererContext within which to render the StyleColor
     */
    public void renderName(StyleColorName value, RendererContext context)
            throws IOException {

        // The orange color is probably not supported on most devices so turn
        // it back into its rgb value.
        if (value == StyleColorNames.ORANGE) {
            renderRGB(value.getRGB(), context);
        } else {
            Writer writer = context.getWriter();
            writer.write(value.getName());
        }
    }

  /**
   * Write a part of a hex value.
   */
  protected void writeNibble(int nibble, Writer writer)
    throws IOException {

    char c = hexDigits [nibble];
    writer.write(c);
  }

    /**
     * Render a StyleColor as a rgb value in hex notation.
     *
     * @param value   the StyleColor to render
     * @param context the RendererContext within which to render the
     *                StyleColor
     */
    public void renderRGB(StyleColorRGB value, RendererContext context)
            throws IOException {

        Writer writer = context.getWriter();

        int rgb = value.getRGB();
        int r0 = (rgb & 0xf00000) >> 20;
        int r1 = (rgb & 0x0f0000) >> 16;
        int g0 = (rgb & 0x00f000) >> 12;
        int g1 = (rgb & 0x000f00) >> 8;
        int b0 = (rgb & 0x0000f0) >> 4;
        int b1 = rgb & 0x00000f;

        writer.write('#');
        final CSSVersion cssVersion = context.getCSSVersion();
        // Should default to supporting color triplets if no cssVersion is set.
        final boolean supportsCSSColorTriplets = cssVersion == null ||
                cssVersion.supportsSyntax(StyleSyntaxes.COLOR_TRIPLETS);

        if (supportsCSSColorTriplets && r0 == r1 && g0 == g1 && b0 == b1) {
            writeTriplet(writer, r0, g0, b0);
        } else {
            writeNibble(r0, writer);
            writeNibble(r1, writer);
            writeNibble(g0, writer);
            writeNibble(g1, writer);
            writeNibble(b0, writer);
            writeNibble(b1, writer);
        }
    }

  /**
   * Write an rgb value as three hexadecimal digits.
   * @param writer The Writer to use.
   * @param r The red component.
   * @param g The green component.
   * @param b The blue component.
   */
  protected void writeTriplet (Writer writer, int r, int g, int b)
    throws IOException {

    writeNibble (r, writer);
    writeNibble (g, writer);
    writeNibble (b, writer);
  }

  /**
   * Render a StyleColor as an rgb percentage.
   * @param value the StyleColor to render
   * @param context the RendererContext within which to render the StyleColor
   */
  public void renderPercentages (StyleColorPercentages value,
                                 RendererContext context)
    throws IOException {

    PrintWriter writer = context.getPrintWriter();

    int red = (int) value.getRed();
    int green = (int) value.getGreen();
    int blue = (int) value.getBlue();

    writer.write("rgb(");
    writer.print(red);
    writer.write("%,");
    writer.print(green);
    writer.write("%,");
    writer.print(blue);
    writer.write("%)");
  }
}

/*
 * Local variables:
 * c-basic-offset: 2
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10816/1	pduffin	VBM:2005121401 Porting forward changes from MCS 3.5

 14-Dec-05	10818/1	pduffin	VBM:2005121401 Added color orange, refactored NamedColor and StyleColorName to remove duplication of data

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
