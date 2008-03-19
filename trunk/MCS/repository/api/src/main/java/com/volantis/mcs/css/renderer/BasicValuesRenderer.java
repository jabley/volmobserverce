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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/BasicValuesRenderer.java,v 1.4 2003/01/02 09:18:19 payal Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jun-02    Paul            VBM:2002051302 - Created to be the base class
 *                              for specific versions of the values renderer.
 * 23-Jul-02    Ian             VBM:2002071802 - Add support for StyleInvalid.
 * 30-Jul-02    Sumit           VBM:2002072906 - Added support for StyleTime
 * 02-Jan-03    Payal           VBM:2002103102 - Modified BasicValuesRenderer()
 *                              to get the singleton instance of
 *                              StyleTimeRenderer.Modified render() to call 
 *                              render() on StyleTimeRenderer to render a 
 *                              StyleTime .
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
 * This is the basic ValuesRenderer which renders the values using the
 * Mariner CSS format which extends CSS2 by adding a ComponentURI type which
 * is similar to the URI type but refers to a component instead of a physical
 * asset.
 */
public class BasicValuesRenderer
  implements ValuesRenderer {

  /**
   * The reference to the single allowable instance of this class.
   */
  private static BasicValuesRenderer singleton;

    // Initialise the static fields.
  static {
    // Always initialise to prevent a synchronization problem if we do it
    // lazily.
    singleton = new BasicValuesRenderer ();
  }

  /**
   * Get the single allowable instance of this class.
   * @return The single allowable instance of this class.
   */
  public static ValuesRenderer getSingleton () {
    return singleton;
  }

  /**
   * Renderer used to render StyleAngle objects.
   */
  protected StyleAngleRenderer styleAngleRenderer;

  /**
   * Renderer used to render StyleColor objects.
   */
  protected StyleColorRenderer styleColorRenderer;

  /**
   * Renderer used to render StyleComponentURI objects.
   */
  protected StyleComponentURIRenderer styleComponentURIRenderer;

  /**
   * Renderer used to render StyleTranscodableURI objects.
   */
  protected StyleTranscodableURIRenderer styleTranscodableURIRenderer;

  /**
   * Renderer used to render StyleFraction objects.
   */
  protected StyleFractionRenderer styleFractionRenderer;

  /**
   * Renderer used to render StyleFrequency objects.
   */
  protected StyleFrequencyRenderer styleFrequencyRenderer;

  /**
   * Renderer used to render StyleInherit objects.
   */
  protected StyleIdentifierRenderer styleIdentifierRenderer;

  /**
   * Renderer used to render StyleInherit objects.
   */
  protected StyleInheritRenderer styleInheritRenderer;

  /**
   * Renderer used to render StyleInteger objects.
   */
  protected StyleIntegerRenderer styleIntegerRenderer;

  /**
   * Renderer used to render StyleInvalid objects.
   */
  protected StyleInvalidRenderer styleInvalidRenderer;

  /**
   * Renderer used to render StyleList objects.
   */
  protected StyleKeywordRenderer styleKeywordRenderer;

  /**
   * Renderer used to render StyleLength objects.
   */
  protected StyleLengthRenderer styleLengthRenderer;

  /**
   * Renderer used to render StyleList objects.
   */
  protected StyleListRenderer styleListRenderer;

  /**
   * Renderer used to render StyleTime objects.
   */
  protected StyleTimeRenderer styleTimeRenderer;
 
  /**
   * Renderer used to render StyleNumber objects.
   */
  protected StyleNumberRenderer styleNumberRenderer;

  /**
   * Renderer used to render StylePair objects.
   */
  protected StylePairRenderer stylePairRenderer;

  /**
   * Renderer used to render StylePercentage objects.
   */
  protected StylePercentageRenderer stylePercentageRenderer;

  /**
   * Renderer used to render StyleString objects.
   */
  protected StyleStringRenderer styleStringRenderer;

  /**
   * Renderer used to render StyleURI objects.
   */
  protected StyleURIRenderer styleURIRenderer;

  protected BasicValuesRenderer () {
      // todo: no need for these to be singletons especially given this
      // class is anyway.
    styleAngleRenderer = StyleAngleRenderer.getSingleton ();
    styleColorRenderer = StyleColorRenderer.getSingleton ();
    styleComponentURIRenderer = StyleComponentURIRenderer.getSingleton ();
    styleFractionRenderer = StyleFractionRenderer.getSingleton();
    styleFrequencyRenderer = StyleFrequencyRenderer.getSingleton();
    styleIdentifierRenderer = StyleIdentifierRenderer.getSingleton();
    styleInheritRenderer = StyleInheritRenderer.getSingleton ();
    styleIntegerRenderer = StyleIntegerRenderer.getSingleton ();
    styleInvalidRenderer = StyleInvalidRenderer.getSingleton ();
    styleKeywordRenderer = StyleKeywordRenderer.getSingleton ();
    styleLengthRenderer = StyleLengthRenderer.getSingleton ();
    styleListRenderer = new StyleListRenderer();
    styleTimeRenderer = StyleTimeRenderer.getSingleton();
    styleNumberRenderer = StyleNumberRenderer.getSingleton ();
    stylePairRenderer = StylePairRenderer.getSingleton ();
    stylePercentageRenderer = StylePercentageRenderer.getSingleton ();
    styleStringRenderer = StyleStringRenderer.getSingleton ();
    styleURIRenderer = StyleURIRenderer.getSingleton ();
  }

  // Javadoc inherited from super class.
  public void render (StyleAngle value, RendererContext context)
    throws IOException {

    styleAngleRenderer.render (value, context);
  }

    // Javadoc inherited.
    public void render(StyleColorName value, RendererContext context)
            throws IOException {

        styleColorRenderer.renderName(value, context);
    }

    // Javadoc inherited.
    public void render(StyleColorPercentages value, RendererContext context)
            throws IOException {

        styleColorRenderer.renderPercentages(value, context);
    }

    // Javadoc inherited.
    public void render(StyleColorRGB value, RendererContext context) 
            throws IOException {

        styleColorRenderer.renderRGB(value, context);
    }

    // Javadoc inherited from super class.
    public void render(StyleComponentURI value, RendererContext context)
            throws IOException {

        styleComponentURIRenderer.render(value, context);
    }

    // Javadoc inherited from super class.
    public void render (StyleTranscodableURI value, RendererContext context)
            throws IOException {
        styleTranscodableURIRenderer.render(value, context);
    }

    // Javadoc inherited.
    public void render(StyleFraction value, RendererContext context)
            throws IOException {
        styleFractionRenderer.render(value, context);
    }

    // Javadoc inherited.
    public void render(StyleFrequency value, RendererContext context) throws IOException {
        styleFrequencyRenderer.render(value, context);
    }

    // Javadoc inherited.
    public void render(StyleIdentifier value, RendererContext context) throws IOException {
         styleIdentifierRenderer.render(value, context);
    }

    // Javadoc inherited from super class.
  public void render (StyleInherit value, RendererContext context)
    throws IOException {

    styleInheritRenderer.render (value, context);
  }

  // Javadoc inherited from super class.
  public void render (StyleInteger value, RendererContext context)
    throws IOException {

    styleIntegerRenderer.render (value, context);
  }

  // Javadoc inherited from super class.
  public void render (StyleInvalid value, RendererContext context)
    throws IOException {

    styleInvalidRenderer.render (value, context);
  }

  // Javadoc inherited from super class.
  public void render (StyleKeyword value, RendererContext context)
    throws IOException {

    styleKeywordRenderer.render (value, context);
  }

  // Javadoc inherited from super class.
  public void render (StyleLength value, RendererContext context)
    throws IOException {

    styleLengthRenderer.render (value, context);
  }

  // Javadoc inherited from super class.
  public void render (StyleList value, RendererContext context)
    throws IOException {

    styleListRenderer.render (value, context);
  }

  // Javadoc inherited from super class.
  public void render (StyleNumber value, RendererContext context)
    throws IOException {

    styleNumberRenderer.render (value, context);
  }

  // Javadoc inherited from super class.
  public void render (StylePair value, RendererContext context)
    throws IOException {

    stylePairRenderer.render (value, context);
  }

  // Javadoc inherited from super class.
  public void render (StylePercentage value, RendererContext context)
    throws IOException {

    stylePercentageRenderer.render (value, context);
  }

  // Javadoc inherited from super class.
  public void render (StyleString value, RendererContext context)
    throws IOException {

    styleStringRenderer.render (value, context);
  }

  // Javadoc inherited from super class.
  public void render (StyleURI value, RendererContext context)
    throws IOException {

    styleURIRenderer.render (value, context);
  }
  
  /** Render the specified StyleTime object.
   * @param value The StyleTime object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws IOException If an error occurred writing the object.
   */
  public void render(StyleTime value, RendererContext context) 
    throws IOException {
    styleTimeRenderer.render(value, context);
  }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 29-Jul-05	9114/2	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
