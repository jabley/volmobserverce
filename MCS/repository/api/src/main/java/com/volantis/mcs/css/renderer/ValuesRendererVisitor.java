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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/ValuesRendererVisitor.java,v 1.3 2002/07/30 09:28:24 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Apr-02    Allan           VBM:2002042404 - Created. A the base class for
 *                              value renderers. If you want to render a value
 *                              from a style property then use this.
 * 28-Jun-02    Paul            VBM:2002051302 - Turned into an adapter which
 *                              visits style values and gets a ValuesRenderer
 *                              class to actually do the rendering.
 * 23-Jul-02    Ian             VBM:2002071802 - Add support for 
 *                              StyleInvalid.
 * 29-Jul-2002  Sumit           VBM:2002072906 - Support for StyleTime
 * 03-Jun-03    Allan           VBM:2003060301 - UndeclaredThrowableException 
 *                              moved to Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.CustomStyleValue;
import com.volantis.mcs.themes.StyleAngle;
import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleColorPercentages;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleFraction;
import com.volantis.mcs.themes.StyleFrequency;
import com.volantis.mcs.themes.StyleFunctionCall;
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
import com.volantis.mcs.themes.StyleUserAgentDependent;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueVisitor;
import com.volantis.mcs.themes.StyleTranscodableURI;
import com.volantis.synergetics.UndeclaredThrowableException;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * This class adapts the ValuesRenderer interface to the StyleValueVisitor
 * interface.
 */
public class ValuesRendererVisitor
  implements StyleValueVisitor {

  /**
   * The singleton instance of this class.
   */
  protected static ValuesRendererVisitor singleton
    = new ValuesRendererVisitor();

  /**
   * Get the singleton instance of this ValuesRendererVisitor.
   * @return the singleton ValuesRendererVisitor
   */
  public static ValuesRendererVisitor getSingleton() {
    return singleton;
  }

  /**
   * The protected constructor for this class.
   */
  protected ValuesRendererVisitor() {
  }

  /**
   * Render the specified StyleAngle object.
   * @param value The StyleAngle object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws UndeclaredThrowableException If an error occurred writing the
   * object.
   */
  public void visit(StyleAngle value, Object context) {

    RendererContext renderContext = (RendererContext) context;
    ValuesRenderer renderers = renderContext.getValuesRenderer ();
    try {
      renderers.render (value, renderContext);
    }
    catch(IOException e) {
      throw new UndeclaredThrowableException(e);
    }
  }

    public void visit(StyleColorName value, Object object) {

        RendererContext context = (RendererContext) object;
        ValuesRenderer renderers = context.getValuesRenderer();
        try {
            renderers.render(value, context);
        } catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    public void visit(StyleColorPercentages value, Object object) {

        RendererContext context = (RendererContext) object;
        ValuesRenderer renderers = context.getValuesRenderer();
        try {
            renderers.render(value, context);
        } catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    public void visit(StyleColorRGB value, Object object) {

        RendererContext context = (RendererContext) object;
        ValuesRenderer renderers = context.getValuesRenderer();
        try {
            renderers.render(value, context);
        } catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Render the specified StyleComponentURI object.
     *
     * @param value   The StyleComponentURI object to render.
     * @param context The RenderContext within which the object will be
     *                rendered.
     * @throws UndeclaredThrowableException If an error occurred writing the
     *          object.
     */
    public void visit(StyleComponentURI value, Object context) {

        RendererContext renderContext = (RendererContext) context;
        ValuesRenderer renderers = renderContext.getValuesRenderer();
        try {
            renderers.render(value, renderContext);
        }
        catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Render the specified StyleTranscodableURI object.
     *
     * @param value   The StyleTranscodableURI object to render.
     * @param context The RenderContext within which the object will be
     *                rendered.
     * @throws UndeclaredThrowableException If an error occurred writing the
     * object.
     */
    public void visit(final StyleTranscodableURI value, final Object context) {

        final RendererContext renderContext = (RendererContext) context;
        final ValuesRenderer renderers = renderContext.getValuesRenderer();
        try {
            renderers.render(value, renderContext);
        }
        catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    // Javadoc inherited.
    public void visit(StyleFrequency value, Object context) {
        RendererContext renderContext = (RendererContext) context;
        ValuesRenderer renderers = renderContext.getValuesRenderer();
        try {
            renderers.render(value, renderContext);
        } catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }


    public void visit(StyleFunctionCall value, Object context) {
        RendererContext renderContext = (RendererContext) context;
        Writer writer = renderContext.getWriter();
        try {
            writer.write(value.getName());
            writer.write("(");
            List arguments = value.getArguments();
            for (int i = 0; i < arguments.size(); i++) {
                StyleValue argument = (StyleValue) arguments.get(i);
                if (i > 0) {
                    writer.write(",");
                }
                writer.write(argument.getStandardCSS());
            }
            writer.write(")");
        } catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Render the specified StyleComponentURI object.
     *
     * @param value   The StyleComponentURI object to render.
     * @param context The RenderContext within which the object will be
     *                rendered.
     * @throws UndeclaredThrowableException If an error occurred writing the
     *          object.
     */
    public void visit(StyleIdentifier value, Object context) {

        RendererContext renderContext = (RendererContext) context;
        ValuesRenderer renderers = renderContext.getValuesRenderer ();
        try {
          renderers.render (value, renderContext);
        }
        catch(IOException e) {
          throw new UndeclaredThrowableException(e);
        }
    }

    /**
   * Render the specified StyleInherit object.
   * @param value The StyleInherit object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws UndeclaredThrowableException If an error occurred writing the
   * object.
   */
  public void visit(StyleInherit value, Object context) {

    RendererContext renderContext = (RendererContext) context;
    ValuesRenderer renderers = renderContext.getValuesRenderer ();
    try {
      renderers.render (value, renderContext);
    }
    catch(IOException e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  /**
   * Render the specified StyleInteger object.
   * @param value The StyleInteger object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws UndeclaredThrowableException If an error occurred writing the
   * object.
   */
  public void visit(StyleInteger value, Object context) {

    RendererContext renderContext = (RendererContext) context;
    ValuesRenderer renderers = renderContext.getValuesRenderer ();
    try {
      renderers.render (value, renderContext);
    }
    catch(IOException e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  /**
   * Render the specified StyleInvalid object.
   * @param value The StyleInvalid object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws UndeclaredThrowableException If an error occurred writing the
   * object.
   */
  public void visit(StyleInvalid value, Object context) {

    RendererContext renderContext = (RendererContext) context;
    ValuesRenderer renderers = renderContext.getValuesRenderer ();
    try {
      renderers.render (value, renderContext);
    }
    catch(IOException e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  /**
   * Render the specified StyleLength object.
   * @param value The StyleLength object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws UndeclaredThrowableException If an error occurred writing the
   * object.
   */
  public void visit(StyleLength value, Object context) {

    RendererContext renderContext = (RendererContext) context;
    ValuesRenderer renderers = renderContext.getValuesRenderer ();
    try {
      renderers.render (value, renderContext);
    }
    catch(IOException e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  /**
   * Render the specified StyleKeyword object.
   * @param value The StyleKeyword object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws UndeclaredThrowableException If an error occurred writing the
   * object.
   */
  public void visit(StyleKeyword value, Object context) {

    RendererContext renderContext = (RendererContext) context;
    ValuesRenderer renderers = renderContext.getValuesRenderer ();
    try {
      renderers.render (value, renderContext);
    }
    catch(IOException e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  /**
   * Render the specified StyleList object.
   * @param value The StyleList object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws UndeclaredThrowableException If an error occurred writing the
   * object.
   */
  public void visit(StyleList value, Object context) {

    RendererContext renderContext = (RendererContext) context;
    ValuesRenderer renderers = renderContext.getValuesRenderer ();
    try {
      renderers.render (value, renderContext);
    }
    catch(IOException e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  /**
   * Render the specified StyleNumber object.
   * @param value The StyleNumber object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws UndeclaredThrowableException If an error occurred writing the
   * object.
   */
  public void visit(StyleNumber value, Object context) {

    RendererContext renderContext = (RendererContext) context;
    ValuesRenderer renderers = renderContext.getValuesRenderer ();
    try {
      renderers.render (value, renderContext);
    }
    catch(IOException e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  /**
   * Render the specified StylePair object.
   * @param value The StylePair object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws UndeclaredThrowableException If an error occurred writing the
   * object.
   */
  public void visit(StylePair value, Object context) {

    RendererContext renderContext = (RendererContext) context;
    ValuesRenderer renderers = renderContext.getValuesRenderer ();
    try {
      renderers.render (value, renderContext);
    }
    catch(IOException e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  /**
   * Render the specified StylePercentage object.
   * @param value The StylePercentage object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws UndeclaredThrowableException If an error occurred writing the
   * object.
   */
  public void visit(StylePercentage value, Object context) {

    RendererContext renderContext = (RendererContext) context;
    ValuesRenderer renderers = renderContext.getValuesRenderer ();
    try {
      renderers.render (value, renderContext);
    }
    catch(IOException e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  /**
   * Render the specified StyleString object.
   * @param value The StyleString object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws UndeclaredThrowableException If an error occurred writing the
   * object.
   */
  public void visit(StyleString value, Object context) {

    RendererContext renderContext = (RendererContext) context;
    ValuesRenderer renderers = renderContext.getValuesRenderer ();
    try {
      renderers.render (value, renderContext);
    }
    catch(IOException e) {
      throw new UndeclaredThrowableException(e);
    }
  }

  /**
   * Render the specified StyleURI object.
   * @param value The StyleURI object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws UndeclaredThrowableException If an error occurred writing the
   * object.
   */
  public void visit(StyleURI value, Object context) {

    RendererContext renderContext = (RendererContext) context;
    ValuesRenderer renderers = renderContext.getValuesRenderer ();
    try {
      renderers.render (value, renderContext);
    }
    catch(IOException e) {
      throw new UndeclaredThrowableException(e);
    }
  }

    public void visit(StyleUserAgentDependent value, Object object) {
        throw new IllegalStateException(
                "User agent dependent values should never be output");
    }

    /**
   * Render the specified StyleTime object.
   * @param value The StyleTime object to render.
   * @param context The RenderContext within which the object will be
   * rendered.
   * @throws UndeclaredThrowableException If an error occurred writing the
   * object.
   */
  public void visit(StyleTime value, Object context) {

    RendererContext renderContext = (RendererContext) context;
    ValuesRenderer renderers = renderContext.getValuesRenderer ();
    try {
      renderers.render (value, renderContext);
    }
    catch(IOException e) {
      throw new UndeclaredThrowableException(e);
    }
  }

    public void visit(CustomStyleValue value, Object object) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public void visit(StyleFraction value, Object context) {
        RendererContext renderContext = (RendererContext) context;
        ValuesRenderer renderers = renderContext.getValuesRenderer ();
        try {
          renderers.render (value, renderContext);
        }
        catch(IOException e) {
          throw new UndeclaredThrowableException(e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 27-Sep-05	9487/4	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
