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
 * $Header: /src/voyager/com/volantis/mcs/protocols/ScriptHandler.java,v 1.3 2002/03/18 12:41:17 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jan-02    Paul            VBM:2001122105 - Created to allow protocols to
 *                              support multiple scripting languages.
 * 11-Mar-02    Paul            VBM:2001122105 - Improved the documentation.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

/**
 * This interface defines the methods which must be implemented by a class
 * which can manage a script language.
 * <p>
 * All implementations of this interface should be singletons so that there is
 * no need to manage instances of this class.
 * </p>
 */
public interface ScriptHandler {

    /**
   * Join two scripts together.
   * @param protocol The protocol used to generate the page.
   * @param script1 The first script.
   * @param script2 The second script.
   * @return A joining of the scripts together.
   */
  public String joinScripts (VolantisProtocol protocol,
                             String script1, String script2);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
