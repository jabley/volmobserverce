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
 * $Header: /src/voyager/com/volantis/testtools/server/SocketUtil.java,v 1.1 2002/11/08 14:27:08 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Oct-02    Steve           VBM:2002071604 A shorthand way to create 
 *                              BufferedReaders and PrintWriters associated 
 *                              with a Socket.
 *                              Taken from Core Servlets and JSP,
 *                              http://www.apl.jhu.edu/~hall/csajsp/.
 *                              1999 Marty Hall; may be freely used or adapted.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.apache.log4j.Category;

/** A shorthand way to create BufferedReaders and
 *  PrintWriters associated with a Socket.
 *  <P>
 *  Taken from Core Servlets and JSP,
 *  http://www.apl.jhu.edu/~hall/csajsp/.
 *  1999 Marty Hall; may be freely used or adapted.
 */
public class SocketUtil
{
    /** Copyright */
    private static String mark = "(c) Volantis Systems Ltd 2000.";
    
    /**
     * The log4j object to log to.
     */
    private static Category logger
    = Category.getInstance( "com.volantis.testtools.server.SocketUtil");
    
    /** Make a BufferedReader to get incoming data. */
    public static BufferedReader getReader(Socket s) throws IOException {
        return(new BufferedReader(
            new InputStreamReader(s.getInputStream())));
  }

  /** Make a PrintWriter to send outgoing data.
   *  This PrintWriter will automatically flush stream
   *  when println is called.
   */
  public static PrintWriter getWriter(Socket s) throws IOException {
        // 2nd argument of true means autoflush
        return(new PrintWriter(s.getOutputStream(), true));
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
