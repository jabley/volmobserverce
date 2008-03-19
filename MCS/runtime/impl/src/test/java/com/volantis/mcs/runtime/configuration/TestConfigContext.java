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
package com.volantis.mcs.runtime.configuration;

import com.volantis.synergetics.log.ConfigurationResolver;
import com.volantis.synergetics.log.LogException;

import java.io.File;
import java.io.InputStream;

import org.xml.sax.InputSource;

/**
 * Test ConfigContext.
 */
public class TestConfigContext implements ConfigContext {

    // Javadoc inherited
    public InputSource getMainConfigInputSource()
        throws ConfigurationException {
        return null;
    }

    public File getConfigRelativeFile(String path, boolean mustExist)
            throws ConfigurationException {
        return null;
    }

    // Javadoc inherited
    public String getLog4jLocation() {
        return ".";
    }

    // Javadoc inherited
    public ConfigurationResolver getConfigurationResolver(String paramName) {
        return new ConfigurationResolver() {
            public InputStream createConfigInputStream(String s) throws LogException {
                return null;
            }

            public File createConfigFile(String s) throws LogException {
                return null;
            }
        };
    }
}

/*
 ===================================te ========================================
 Change History
 ===========================================================================
 $Log$

 09-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 ===========================================================================
*/
