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
 * $Header: /src/voyager/com/volantis/mcs/application/ApplicationContextFactory.java,v 1.2 2003/02/11 12:50:18 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.application;

import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.repository.RepositoryException;
import java.util.Map;

/**
 * This interface is used to describe the the requirements
 * for a class that can be refistered with Mariner to
 * provide an application specific context.
 */
public interface ApplicationContextFactory {
    
    /**
     * This method instanciates an application specific context.
     * @param marinerRequestContext The current request context.
     * @return ApplicationContext The application specific context.
     * @throws RepositoryException
     */
    public ApplicationContext createApplicationContext(
            MarinerRequestContext marinerRequestContext)
                    throws RepositoryException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 ===========================================================================
*/
