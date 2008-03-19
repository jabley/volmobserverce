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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

import com.volantis.synergetics.log.ConfigurationResolver;
import org.xml.sax.InputSource;

import java.io.File;

/**
 * This interface enables certain methods available in
 * javax.servlet.ServletContext to be abstracted from the runtime environment.
 * 
 * @mock.generate
 */
public interface ConfigContext {

    /**
     * Resolves the location specified for the main mcs configuration file and
     * return the corresponding InputSource.
     *
     * @return the InputSource for the main mcs configuration file
     * @throws ConfigurationException if unable to get an InputSource for the
     *                                main mcs configuration file
     */
    InputSource getMainConfigInputSource() throws ConfigurationException;


    /**
     * Resolves a path relative to the location of the main configuration file.
     * If the specified path is absolute it returns the File object that
     * corresponds to that path.
     *
     * @param path the path to resolve
     * @param mustExist true if the file is expected to be exist
     * @return the resolved File object or null if the file doesn't exist, but
     * it is expected to exist.
     * @throws ConfigurationException if a relative path was supplied and the
     * configuration file path against which to resolve it is null
     */
    public File getConfigRelativeFile(String path, boolean mustExist)
        throws ConfigurationException;

    /**
     * Gets the location of the log4j configuration file. This may be an
     * absolute or relative path to a file on the filesystem or to a resource
     * loadable with a ClassLoader.
     *
     * @return the location of the configuration file, or null if none was
     * specified
     */
    String getLog4jLocation();

    /**
     * Gets a ConfigurationResolver instance which can be used to resolve
     * references to configuration resources or files against the servlet
     * context.
     *
     * @param paramName the name of the resolver
     * @return the resolver
     */
    ConfigurationResolver getConfigurationResolver(String paramName);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-May-05	7759/11	pcameron	VBM:2005040505 Fixes to logging

 26-Apr-05	7759/9	pcameron	VBM:2005040505 Logging initialisation changed

 19-Apr-05	7665/9	pcameron	VBM:2005040505 Logging initialisation changed

 17-Mar-05	7401/1	emma	VBM:2005020303 Allow log files to be specified as relative to the log4j config file

 11-Mar-05	6842/4	emma	VBM:2005020302 Making file references in config files relative to those files

 21-Feb-05	6986/1	emma	VBM:2005021411 Changes merged from MCS3.3

 18-Feb-05	6974/1	emma	VBM:2005021411 Making the device repository and xml policies locations relative to mcs-config.xml

 08-Dec-04	6416/7	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/5	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 06-Dec-04	5800/1	ianw	VBM:2004090605 New Build system

 ===========================================================================
*/
