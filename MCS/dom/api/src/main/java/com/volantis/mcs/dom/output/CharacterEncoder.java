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
 * $Header: /src/voyager/com/volantis/mcs/dom/output/CharacterEncoder.java,v 1.1 2002/05/23 09:49:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-May-02    Paul            VBM:2002042202 - Created to allow the output
 *                              from the XMLOutputter to account for
 *                              differences in character encodings.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.output;

import java.io.IOException;
import java.io.Writer;

/**
 * This interface defines methods which should be implemented by classes which
 * want to control how characters are encoded in the output.
 */
public interface CharacterEncoder {

  /**
   * Encode the contents of the buffer and write the results to the writer.
   * @param buffer The buffer to convert.
   * @throws IOException If there was a problem with the Writer.
   */
  public void encode (char [] buffer, Writer out)
    throws IOException;

  /**
   * Encode the contents of the buffer and write the results to the writer.
   * @param buffer The buffer to convert.
   * @param offset The index of the first character within the buffer to
   * convert.
   * @param length The length of the section of the buffer to convert.
   * @throws IOException If there was a problem with the Writer.
   */
  public void encode (char [] buffer, int offset, int length,
                      Writer out)
    throws IOException;

  /**
   * Encode the contents of the string and write the results to the writer.
   * @param string The string to convert.
   * @throws IOException If there was a problem with the Writer.
   */
  public void encode (String string, Writer out)
    throws IOException;

  /**
   * Encode the contents of the string and write the results to the writer.
   * @param string The string to convert.
   * @param offset The index of the first character within the string to
   * convert.
   * @param length The length of the section of the string to convert.
   * @throws IOException If there was a problem with the Writer.
   */
  public void encode (String string, int offset, int length, Writer out)
    throws IOException;

  /**
   * Encode the character and write the results to the writer.
   * @param c The character to convert is contained in the 16 low-order
   * bits of this integer value; the 16 high-order bits are ignored.
   * @throws IOException If there was a problem with the Writer.
   */
  public void encode (int c, Writer out)
    throws IOException;
}

/*
 * Local variables:
 * c-basic-offset: 2
 * end:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
