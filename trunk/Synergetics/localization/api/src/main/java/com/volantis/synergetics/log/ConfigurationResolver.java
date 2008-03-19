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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.log;

import java.io.File;
import java.io.InputStream;

public interface ConfigurationResolver {

    /**
     * Creates and returns an InputStream representing the supplied location.
     * Tries to interpret the location of the configuration file supplied as:
     *
     * <ul>
     *
     * <li>absolute resource ID</li>
     *
     * <li>resource ID relative to the main config file</li>
     *
     * <li>absolute path</li>
     *
     * <li>path relative to the main config file</li>
     *
     * <li>resource ID relative to webapp context</li>
     *
     * </ul>
     *
     * @param location of a configuration file as either resource ID or path
     * @return InputStream or null if the location does not map to a valid
     *         resource ID or path
     */
    InputStream createConfigInputStream(String location) throws LogException;

    /**
     * Creates and returns a file if the path supplied is either absolute or
     * valid relative to the absolute location of the main configuration file.
     * Will throw an exception if a relative path has been supplied, and the
     * absolute path to the main config file is not known (e.g. if it was
     * specified using a resource ID which could not be resolved into an
     * absolute path).
     *
     * @param path the path to validate
     * @return the file that the path corresponds to, or null if no such file
     *         exists
     */
    File createConfigFile(String path) throws LogException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Mar-05	411/4	emma	VBM:2005020303 Allow log files to be specified as relative to the log4j config file

 ===========================================================================
*/
