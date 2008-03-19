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
 * $Header: /src/voyager/com/volantis/mcs/dom/output/AbstractCharacterEncoder.java,v 1.1 2002/05/23 09:49:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-May-02    Paul            VBM:2002042202 - Created to make it easy to
 *                              write CharacterEncoders.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.output;

import java.io.IOException;
import java.io.Writer;

/**
 * Abstract class for encoding characters. Subclasses only need to implement
 * the {@link #encode (int, Writer)} method. The other methods may be
 * overridden for performance reasons.
 */
public abstract class AbstractCharacterEncoder
  implements CharacterEncoder {

  /**
   * Implemented in terms of the {@link #encode (char [], int, int, Writer)}.
   */
  public void encode (char [] buffer, Writer out)
    throws IOException {

    encode (buffer, 0, buffer.length, out);
  }

  /**
   * Implemented in terms of the {@link #encode (int, Writer)}.
   */
  public void encode (char [] buffer, int offset, int length,
                       Writer out)
    throws IOException {

    if (length > 0) {
        int end = offset + length - 1;
        for (int i = offset; i <= end; i++) {
          encode (buffer [i], out);
        }
    }
  }

  /**
   * Implemented in terms of the {@link #encode (String, int, int, Writer)}.
   */
  public void encode (String string, Writer out)
    throws IOException {

    encode (string, 0, string.length (), out);
  }

  /**
   * Implemented in terms of the {@link #encode (int, Writer)}.
   */
  public void encode (String string, int offset, int length, Writer out)
    throws IOException {
    
    if( length > 0 ) {
        int end = offset + length - 1;
        for (int i = offset; i <= end; i++) {
          encode (string.charAt (i), out);
        }
    }
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Feb-04	2974/2	steve	VBM:2004020608 SGML Quote handling

 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling

 ===========================================================================
*/
