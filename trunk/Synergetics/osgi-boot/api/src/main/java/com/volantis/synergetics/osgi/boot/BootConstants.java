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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.osgi.boot;

/**
 * Set of constants needed to boot up an OSGi framework.
 *
 * <p>Constants specified within here may be specified outside the
 * framework. Some in the servlet context init parameters, others both there
 * and also in the boot properties file.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface BootConstants {

    /**
     * The class path containing all the classes necessary for booting up
     * OSGi.
     */
    String BOOT_CLASSPATH = "volantis.osgi.boot.classpath";

    /**
     * See OSGi standard for details.
     */
    String BOOT_DELEGATION = "org.osgi.framework.bootdelegation";

    /**
     * See OSGi standard for details.
     */
    String SYSTEM_PACKAGES = "org.osgi.framework.system.packages";

    /**
     * The path to the area in the file system that OSGi uses for its data.
     */
    String OSGI_AREA = "volantis.osgi.area";

    /**
     * The path to the area of the file system that contains the context area
     * in which files can be stored.
     */
    String CONTEXT_AREA = "volantis.osgi.config.area";
}
