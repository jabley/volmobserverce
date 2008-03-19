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
 * $Header: /src/voyager/com/volantis/mcs/protocols/css/CssCandidate.java,v 1.2 2002/12/02 15:43:22 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Oct-02    Adrian          VBM:2002100404 - Created this interface for
 *                              the classes which represent objects which may
 *                              become css links / inline css / css imports
 * 29-Nov-02    Phil W-S        VBM:2002112901 - Updated writeCss to take the
 *                              protocol instance for which the output is to
 *                              be generated.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.VolantisProtocol;

import java.io.IOException;

/**
 * This interface defines those methods common to the set of classes which
 * encapsulate information that may be used to create css links, inline css,
 * or css import statements.
 */
public interface CssCandidate {

    /**
   * Render the encapsulated information to the specified buffer either as a
   * css link, inline css, or as a css import statement.
   *
   * @param protocol the protocol for which the CSS is to be written
   * @param buffer the output buffer to which the CSS is to be added
   */
  public void writeCss(VolantisProtocol protocol, OutputBuffer buffer)
            throws IOException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Oct-04	6010/1	claire	VBM:2004102701 mergevbm: External stylesheet handling with different projects in portals

 28-Oct-04	5995/1	claire	VBM:2004102701 External stylesheet handling with different projects in portals

 ===========================================================================
*/
