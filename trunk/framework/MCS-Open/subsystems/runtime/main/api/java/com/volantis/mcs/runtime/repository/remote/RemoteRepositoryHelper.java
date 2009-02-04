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
 * $Header: /src/voyager/com/volantis/mcs/runtime/repository/remote/RemoteRepositoryHelper.java,v 1.5 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Mat             VBM:2002040814 - Created
 * 29-May-02    Paul            VBM:2002050301 - Rewrote the isRemoteName
 *                              method to generate less garbage and added an
 *                              isRemoteIdentity method.
 * 04-Nov-02    Mat             VBM:2002110401 - Added to check for a null
 *                              identity in isRemoteIdentity.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.repository.remote;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 *
 * @author  mat
 */
public class RemoteRepositoryHelper {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(RemoteRepositoryHelper.class);

    /**
     * Is this a remote name or not?
     */
    public static boolean isRemoteName(String name) {
        if(logger.isDebugEnabled()){
            logger.debug("Remote checking " + name);
        }
        
        int offset = 0;
        if(name.startsWith("{")) {
            offset++;
        }

        // We cannot convert the name to lower case as we must avoid creating
        // any unnecessary objects. Instead we explicitly test the characters
        // that we are interested in.

        // First we test the length of the string to make sure it is long
        // enough, this will probably not catch many local names but it is
        // a start.
        if (name.length () < "http://".length ()) {
          return false;
        }

        // Finally we check the characters one by one. We start with the
        // character which can only legally be used in a remote name as that
        // will filter out all legal local names. The other tests are made
        // to maintain the old behaviour.
        return (name.charAt (offset + 4) == ':'
                && name.charAt (offset + 5) == '/'
                && name.charAt (offset + 6) == '/'
                && Character.toLowerCase (name.charAt (offset)) == 'h'
                && Character.toLowerCase (name.charAt (offset + 1)) == 't'
                && Character.toLowerCase (name.charAt (offset + 2)) == 't'
                && Character.toLowerCase (name.charAt (offset + 3)) == 'p');
    }

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
