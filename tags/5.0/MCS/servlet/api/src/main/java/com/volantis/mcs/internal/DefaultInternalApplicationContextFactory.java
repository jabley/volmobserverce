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
 * $Header: /src/voyager/com/volantis/mcs/internal/DefaultInternalApplicationContextFactory.java,v 1.4 2003/02/18 13:59:01 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis.
 * 12-Feb-03    Phil W-S        VBM:2003021206 - Add setting packager on the
 *                              application context in
 *                              createApplicationContext.
 * 13-Feb-03    Byron           VBM:2003020305 - Removed
 *                              createApplicationContext and added
 *                              resolveDevice. Tidied imports.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.internal;

import com.volantis.mcs.application.DefaultApplicationContextFactory;
import com.volantis.mcs.application.DeviceReader;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.repository.RepositoryException;

/**
 * This is the default ApplicationContextFactory for the internal environment
 * most of the initialisation for this environment was taken from 
 * MarinerPageContext and that code replaced with calls to the resultant
 * ApplicationContext.
 */
public class DefaultInternalApplicationContextFactory
    extends DefaultApplicationContextFactory {

    /** Creates a new instance of DefaultApplicationContextFactory */
    public DefaultInternalApplicationContextFactory() {
        super();
    }
    
    // javadoc inherited
    protected InternalDevice resolveDevice(DeviceReader deviceReader,
                                   MarinerRequestContext requestContext)
            throws RepositoryException{

        InternalRequest internalRequest =
                ((MarinerInternalRequestContext)requestContext).getRequest();

        return deviceReader.getDevice(internalRequest.getDeviceName());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 13-Oct-03	1517/2	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 ===========================================================================
*/
