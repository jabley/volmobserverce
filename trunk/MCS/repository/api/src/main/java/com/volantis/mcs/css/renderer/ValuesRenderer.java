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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/ValuesRenderer.java,v 1.3 2002/07/30 09:28:24 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jun-02    Paul            VBM:2002051302 - Created to define the methods
 *                              which need to be implemented by classes which
 *                              can render different style values.
 * 23-Jul-02    Ian             VBM:2002071802 - Added support for 
 *                              StyleInvalid.
 * 29-Jul-2002  Sumit           VBM:2002072906 - Support for StyleTime
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleAngle;
import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleColorPercentages;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleFraction;
import com.volantis.mcs.themes.StyleFrequency;
import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleInherit;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleInvalid;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StyleNumber;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleTime;
import com.volantis.mcs.themes.StyleURI;
import com.volantis.mcs.themes.StyleTranscodableURI;

import java.io.IOException;

/**
 * This interface defines methods for rendering all the different style value
 * specializations.
 */
public interface ValuesRenderer {

  /**
   * Render the specified StyleAngle object.
   * @param value The StyleAngle object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws IOException If an error occurred writing the object.
   */
  public void render (StyleAngle value, RendererContext context)
    throws IOException;

    /**
     * Render the specified StyleColorName object.
     *
     * @param value   The StyleColorName object to render.
     * @param context The RenderContext within which the object will be
     *                rendered.
     * @throws IOException If an error occurred writing the object.
     */
    public void render(StyleColorName value, RendererContext context)
            throws IOException;

    /**
     * Render the specified StyleColorPercentages object.
     *
     * @param value   The StyleColorPercentages object to render.
     * @param context The RenderContext within which the object will be
     *                rendered.
     * @throws IOException If an error occurred writing the object.
     */
    public void render(StyleColorPercentages value, RendererContext context)
            throws IOException;

    /**
     * Render the specified StyleColorRGB object.
     *
     * @param value   The StyleColorRGB object to render.
     * @param context The RenderContext within which the object will be
     *                rendered.
     * @throws IOException If an error occurred writing the object.
     */
    public void render(StyleColorRGB value, RendererContext context)
            throws IOException;

    /**
     * Render the specified StyleComponentURI object.
     *
     * @param value   The StyleComponentURI object to render.
     * @param context The RenderContext within which the object will be
     *                rendered.
     * @throws IOException If an error occurred writing the object.
     */
    public void render(StyleComponentURI value, RendererContext context)
        throws IOException;

    /**
     * Render the specified StyleTranscodableURI object.
     *
     * @param value   The StyleTranscodableURI object to render.
     * @param context The RenderContext within which the object will be
     *                rendered.
     * @throws IOException If an error occurred writing the object.
     */
    public void render(StyleTranscodableURI value, RendererContext context)
        throws IOException;

    /**
     * Render the specified StyleFrequency object.
     * @param value The StyleFrequency object to render.
     * @param context The RenderContext within which the object will be
     * rendered.
     * @throws IOException If an error occurred writing the object.
     */
    public void render (StyleFrequency value, RendererContext context)
            throws IOException;

    /**
     * Render the specified StyleInherit object.
     *
     * @param value   The StyleInherit object to render.
     * @param context The RenderContext within which the object will be
     *                rendered.
     * @throws IOException If an error occurred writing the object.
     */
    void render(StyleIdentifier value, RendererContext context)
            throws IOException;

  /**
   * Render the specified StyleInherit object.
   * @param value The StyleInherit object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws IOException If an error occurred writing the object.
   */
  public void render (StyleInherit value, RendererContext context)
    throws IOException;

  /**
   * Render the specified StyleInteger object.
   * @param value The StyleInteger object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws IOException If an error occurred writing the object.
   */
  public void render (StyleInteger value, RendererContext context)
    throws IOException;

  /**
   * Render the specified StyleInvalid object.
   * @param value The StyleInteger object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws IOException If an error occurred writing the object.
   */
  public void render (StyleInvalid value, RendererContext context)
    throws IOException;

  /**
   * Render the specified StyleKeyword object.
   * @param value The StyleKeyword object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws IOException If an error occurred writing the object.
   */
  public void render (StyleKeyword value, RendererContext context)
    throws IOException;

  /**
   * Render the specified StyleLength object.
   * @param value The StyleLength object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws IOException If an error occurred writing the object.
   */
  public void render (StyleLength value, RendererContext context)
    throws IOException;

  /**
   * Render the specified StyleList object.
   * @param value The StyleList object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws IOException If an error occurred writing the object.
   */
  public void render (StyleList value, RendererContext context)
    throws IOException;

  /**
   * Render the specified StyleNumber object.
   * @param value The StyleNumber object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws IOException If an error occurred writing the object.
   */
  public void render (StyleNumber value, RendererContext context)
    throws IOException;

  /**
   * Render the specified StylePair object.
   * @param value The StylePair object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws IOException If an error occurred writing the object.
   */
  public void render (StylePair value, RendererContext context)
    throws IOException;

  /**
   * Render the specified StylePercentage object.
   * @param value The StylePercentage object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws IOException If an error occurred writing the object.
   */
  public void render (StylePercentage value, RendererContext context)
    throws IOException;

  /**
   * Render the specified StyleString object.
   * @param value The StyleString object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws IOException If an error occurred writing the object.
   */
  public void render (StyleString value, RendererContext context)
    throws IOException;

  /**
   * Render the specified StyleURI object.
   * @param value The StyleURI object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws IOException If an error occurred writing the object.
   */
  public void render (StyleURI value, RendererContext context)
    throws IOException;

  /**
   * Render the specified StyleTime object.
   * @param value The StyleTime object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws IOException If an error occurred writing the object.
   */
  public void render (StyleTime value, RendererContext context)
    throws IOException;

    /**
     * Render the specified StyleFraction object.
     *
     * @param value   The StyleFraction object to render.
     * @param context The RenderContext within which the object will be
     *                rendered.
     * @throws IOException If an error occurred writing the object.
     */
    public void render(StyleFraction value, RendererContext context)
            throws IOException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
