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
 * $Header: /src/voyager/com/volantis/mcs/atg/dynamo/Dynamo511Interface.java,v 1.1 2002/01/09 13:44:48 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 *
 * Created on 04 June 2001, 16:57
 *
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-Aug-01    Mat             VBM:2001082303 - Get volantisBean from the 
 *                              pageContext
 * 07-Nov-01    Mat             VBM:2001110701 - Add ability to get the 
 *                              datasource from the application server
 * 21-Dec-01    Paul            VBM:2001121702 - Removed unused imports.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 09-Jan-02    Paul            VBM:2002010403 - Moved from appserver package.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.atg.dynamo;

import com.volantis.mcs.integration.AbstractAppServer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 *
 * @author  mat
 * Methods to access information from Dynamo
 */
public class Dynamo511Interface extends AbstractAppServer {
    
    /**
     * Used for logging
     */
    private static LogDispatcher logger =
            LocalizationFactory.createLogger(Dynamo511Interface.class);

    
    /** Creates new dynamo511 */
    public Dynamo511Interface() {
        if (logger.isDebugEnabled()) {
            logger.debug ("Dynamo5.1.1 Application Server Interface started");
        }


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

 28-Apr-05	7922/1	pduffin	VBM:2005042801 Removed User and UserFactory classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 21-Jun-04	4702/3	matthew	VBM:2004061402 rework PageTracking

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 27-Apr-04	3843/2	ianw	VBM:2004041408 Port forward ATG 5.6.1 integration

 ===========================================================================
*/
