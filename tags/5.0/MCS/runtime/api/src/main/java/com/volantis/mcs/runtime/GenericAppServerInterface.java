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
 * $Header: /src/voyager/com/volantis/mcs/runtime/GenericAppServerInterface.java,v 1.3 2003/03/20 15:15:33 sumit Exp $
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
 * 23-Aug-01    Mat             VBM: 2001082303 - Get volantisBean from the
 *                              pageContext
 * 07-Nov-01    Mat             VBM:2001110701 - Add ability to get the
 *                              datasource from the application server
 * 21-Dec-01    Paul            VBM:2001121702 - Removed unused imports.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 09-Jan-02    Paul            VBM:2002010403 - Renamed from Tomcat31Interface
 *                              in the appserver package.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

import com.volantis.mcs.integration.AbstractAppServer;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import javax.sql.DataSource;

/**
 *
 * @author  mat
 * Methods to access information from Dynamo
 */
public class GenericAppServerInterface extends AbstractAppServer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(GenericAppServerInterface.class);

    /**
     * Create new <code>GenericAppServerInterface</code>.
     */
    public GenericAppServerInterface() {
        if (logger.isDebugEnabled()) {
            logger.debug("Generic Application Server Interface started");
        }
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

 28-Apr-05	7922/1	pduffin	VBM:2005042801 Removed User and UserFactory classes

 27-Apr-05	7896/1	pduffin	VBM:2005042709 Removing PolicyPreference and all related classes

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 21-Jun-04	4702/3	matthew	VBM:2004061402 rework PageTracking

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
