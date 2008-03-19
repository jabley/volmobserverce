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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/StyleComponentURIRenderer.java,v 1.4 2002/08/08 17:14:09 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Allan           VBM:2002042404: The renderer for 
 *                              ComponentURI type StyleValue objects.
 * 28-Jun-02    Paul            VBM:2002051302 - Made a singleton.
 * 08-Aug-02    Allan           VBM:2002073101 - writeMarinerComponentURL()
 *                              changed to stop writing the uri inside a
 *                              mariner-component-url() string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleComponentURI;

import java.io.IOException;
import java.io.Writer;

/**
 * Render a style value representing a component uri.
 */
public class StyleComponentURIRenderer {

  /**
   * The copyright statement.
   */
  private static String mark = "(c) Volantis Systems Ltd 2002.";

  /**
   * The reference to the single allowable instance of this class.
   */
  private static StyleComponentURIRenderer singleton;

  // Initialise the static fields.
  static {
    // Always initialise to prevent a synchronization problem if we do it
    // lazily.
    singleton = new StyleComponentURIRenderer ();
  }

  /**
   * Get the single allowable instance of this class.
   * @return The single allowable instance of this class.
   */
  public static StyleComponentURIRenderer getSingleton () {
    return singleton;
  }

  /**
   * Protect the constructor to prevent any other instances being created.
   */
  protected StyleComponentURIRenderer () {
  }

  /**
   * Write out the value of a Mariner Component URL. Note that this
   * kind of url is not valid css until a valid url is established
   * at run time. Before runtime, the value of a mariner component
   * url is merely the name of the component.
   * @param context The RendererContext to obtain the Writer to write to.
   * @param uri The value of the mariner component uri (i.e. the component
   * name.
   */
  protected void writeMarinerComponentURL (RendererContext context,
                                           String uri)
    throws IOException {

    Writer writer = context.getWriter ();

    writer.write (uri);
  }

  public void render (StyleComponentURI value, RendererContext context)
    throws IOException {

    String uri = value.getExpressionAsString ();

    writeMarinerComponentURL (context, uri);
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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
