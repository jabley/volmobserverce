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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.osgi;

/**
 * Contains constants used when creating an {@link OSGiManager}.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface OSGiManagerConstants {

    /**
     * The URI to the area in the file system that the OSGi framework
     * implementation uses for its own private data.
     */
    String FRAMEWORK_AREA = "volantis.osgi.framework.area";

    /**
     * A list of URIs for bundles that must be installed and started as part
     * of booting the OSGi framework.
     */
    String BOOT_BUNDLES = "volantis.osgi.boot.bundles";
}
