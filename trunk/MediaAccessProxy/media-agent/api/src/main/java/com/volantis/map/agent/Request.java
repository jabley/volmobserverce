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

import java.util.Set;

/**
 * A request to the Media Conversion Program for a url that can be used
 * externally to access the transcoded asset for the current device.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 * 
 * @mock.generate
 */
public interface Request {

    /**
     * The type of the the resource being processed.
     * 
     * @return
     */
    public String getResourceType();

    /**
     * Return the source location of the asset. This is used by the MCP to
     * obtain the raw asset.
     *
     * @return the location of the raw asset
     */
    public String getSourceURL();

    /**
     * Return an immutable set of the transcoding parameters to pass to the
     * servlet. The must not be null.
     *
     * @return the immutable set of transcoding parameters
     */
    public Parameters getInputParams();

    /**
     * Return an immutable set of the response parameters wanted from the
     * Media Conversion Program. This can be used to request information such
     * as the the width and height of an image after transcoding. Note that the
     * Media Conversion Program MAY return more parameters then are asked for.
     *
     * @return the immutable set of properties that the caller needs.
     */
    public Set getOutputParams();
}
