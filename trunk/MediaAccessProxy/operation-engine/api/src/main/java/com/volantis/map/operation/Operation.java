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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Plugins to the operation engine should implement this interface.
 * 
 * @mock.generate
 */
public interface Operation {

    /**
     * This method should return {@link Result#SUCCESS} if the operation was
     * successfull, {@link Result#UNSUPPORTED} if the plugin cannot perform
     * the specified operation.
     *
     * @param descriptor
     * @param request
     * @param response
     * @return Result#SUCCESS if the operation succeeded, Result#UNSUPPORTED if
     * the operation is not supported.
     * @throws Exception if an error occurs when attempting to perform the
     * operation
     */
    public Result execute(ResourceDescriptor descriptor,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception;

}
