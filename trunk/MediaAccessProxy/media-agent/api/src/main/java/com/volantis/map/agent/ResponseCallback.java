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
package com.volantis.map.agent;

import com.volantis.map.common.param.Parameters;

/**
 * The callback that will be invoked by the Media Conversion Program when the
 * requested information becomes available. Note that this callback will only
 * ever be executed by the thread that calls into the
 * {@link MediaAgent#waitForComplete()} method. As such it will be thread safe.
 * 
 * @mock.generate
 */
public interface ResponseCallback {
    /**
     * This method will be called when the Media Conversion Program has the
     * requested information and the caller has entered the
     * {@link MediaAgent#waitForComplete()} method.
     *
     * @param params the parameters asked for. This MAY contain more parameters
     * then where requested. It will always contain a value for the generated
     * short URL. The key for this value is
     * {@link MediaAgent.OUTPUT_URL_PARAMETER_NAME}.
     *
     * @throws Exception if any problem is encountered.
     */
    public void execute(Parameters params) throws Exception;
}
