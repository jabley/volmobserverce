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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.operation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The operation engine. This performs all operations.
 * 
 * @mock.generate
 */
public interface OperationEngine {

    /**
     * Implementations of this method will be called when a request is made
     *
     * @param externalID the external ID
     * @param request the servlet request
     * @param response the servlet response
     * @throws ResourceDescriptorNotFoundException
     * @throws OperationNotFoundException
     * @throws Exception
     */
    public void processRequest(String externalID,
                          HttpServletRequest request,
                          HttpServletResponse response)
        throws ResourceDescriptorNotFoundException,
        OperationNotFoundException, Exception;

}
