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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/StyleInvalidRenderer.java,v 1.4 2002/08/06 14:09:17 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-Jul-02    Ian             VBM:2002071802 - Created.
 * 02-Aug-02    Byron           VBM:2002073003 - Modified render() method 
 *                              to handle invalid parsing slightly more 
 *                              gracefully and removed system.out.println line.
 * 06-Aug-02    Allan           VBM:2002080102 - Changed Logger back to
 *                              Category.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleInvalid;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Render a style value representing string.
 */
public class StyleInvalidRenderer {

    
  /**
   * The copyright statement.
   */
  private static String mark = "(c) Volantis Systems Ltd 2002.";

  /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(StyleInvalidRenderer.class);
  
  /**
   * The reference to the single allowable instance of this class.
   */
  private static StyleInvalidRenderer singleton;

  // Initialise the static fields.
  static {
    // Always initialise to prevent a synchronization problem if we do it
    // lazily.
    singleton = new StyleInvalidRenderer ();
  }

  /**
   * Get the single allowable instance of this class.
   * @return The single allowable instance of this class.
   */
  public static StyleInvalidRenderer getSingleton () {
    return singleton;
  }

  /**
   * Protect the constructor to prevent any other instances being created.
   */
  protected StyleInvalidRenderer () {
  }

  /**
   * Render a StyleBitSet.
   * @param value the StyleBitSet to render
   * @param context the RendererContext within which to render the StyleBitSet
   */
  public void render(StyleInvalid value, RendererContext context)
    throws IOException {

    PrintWriter writer = context.getPrintWriter();
    if( value == null || value.getValue() == null ) {
        logger.error("style-invalid-null");
        return;
    }
    String string = value.getValue();
    writer.print(string);
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

 16-Sep-05	9512/1	pduffin	VBM:2005091408 Added support for invalid style values

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
