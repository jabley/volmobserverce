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
 * $Header: /src/voyager/com/volantis/mcs/utilities/VolantisEnvironment.java,v 1.49 2003/03/20 15:15:34 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Jul-01    Paul            VBM:2001062810 - Added this header and removed
 *                              references to javax.servlet classes.
 * 02-Aug-01    Allan           VBM:2001072604 - Added commentsEnabled property
 *                              and associated get/set methods.
 * 10-Sep-01    Allan           VBM:2001083118 - Removed LogAgent 
 *                              initialization.
 * 05-Dec-01    Allan           VBM:2001120501 - Added check to establishLog()
 *                              to ensure that no attempt is made to start
 *                              the log thread if it is already alive.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging and
 *                              removed code relating to our own logging
 *                              mechanism.
 * 09-Jan-02    Paul            VBM:2002010403 - Removed UPDATE_TABLE,
 *                              KEY_SEPARATOR and getLogThread.
 * 11-Jan-02    Paul            VBM:2002010403 - Made revision an int.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 11-Mar-03    Geoff           VBM:2002112102 - removed unused log() methods
 *                              and related consts, and comments to indicate
 *                              how it relates to the new DebugConfiguration.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.utilities;

/**
 * The Volantis environment class provides common services to applications with
 * out the need to create and pass class instances. A good example of the kind
 * of service provided by this class is logging of information and error
 * messages. Classes need to do this in a consistent way without worrying
 * about how log records are written. The environment class provides shared
 * services across all executing instances of Volantis code.
 * <p> 
 * @todo This class is old and crufty. What little functionality it has should
 * probably be refactored away to better homes... 
 */
public class VolantisEnvironment {

    private static boolean commentsEnabled = false;
    private static boolean logPageOutput = false;

    // @todo logPageOutput and commentsEnabled
    // mostly just shadow the values in DebugConfiguration. 
    // This should probably be cleaned up...
    
    public static boolean logPageOutput() {
        return logPageOutput;
    }

    public static void setLogPageOutput(boolean b) {
        logPageOutput = b;
    }

    public static boolean commentsEnabled() {
        return commentsEnabled;
    }

    public static void setCommentsEnabled(boolean b) {
        commentsEnabled = b;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Apr-05	7946/1	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 28-Apr-05	7908/1	pduffin	VBM:2005042712 Removing Revision object, added UNKNOWN_REVISION constant to JDBCAccessorHelper. This will be removed when revisions are removed from the database tables

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 25-Jun-03	540/1	geoff	VBM:2003061709 remove mariner config debug enabled attribute

 ===========================================================================
*/
