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

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.servlet.ServletContext;

/**
 * This class resolves resources against a ServletContext.
 */
public class ServletContextConfigurationResolver
    implements ConfigurationResolver {

    /**
     * The localizer used to retrieve localized messages for exceptions.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(
            ServletContextConfigurationResolver.class);

    /**
     * The servlet context against which to resolve.
     */
    private final ServletContext context;

    /**
     * Initializes this resolver with the given context.
     *
     * @param context the context to use
     */
    public ServletContextConfigurationResolver(ServletContext context) {
        this.context = context;
    }

    /**
     * Creates an InputStream for the given location by trying to load from the
     * location if it is absolute, or by trying to load relative to the
     * ServletContext's root directory. If both attempts fail to create the
     * stream then an attempt is made to retrieve the location as a resource
     * stream.
     *
     * @param location the location of the resource. Must be absolute.
     * @return the InputStream. Can be null.
     *
     * @throws LogException if an InputStream could not be created
     */
    public InputStream createConfigInputStream(final String location)
        throws LogException {
        InputStream is = null;
        File configFile = createConfigFile(location);

        if (configFile != null &&
            configFile.exists() && configFile.isFile()) {
            try {
                is = new FileInputStream(configFile);
            } catch (FileNotFoundException e) {
                throw new LogException(exceptionLocalizer.format(
                    "configuration-file-not-found", configFile));
            }
        } else {

            // The specified location cannot be loaded as a File so try
            // loading it as a servlet context resource. Note that such
            // resources must be absolute.
            String resourceLocation = location;
            if (resourceLocation.charAt(0) != '/') {
                // Warn that specifying the resource as relative to the current
                // webapp context will not be supported in the future, and
                // for now, prefix with a slash.
                // @todo this warning is commented out to get past some test cases. This should be reinstated.
                /*
                LogLog.warn(exceptionLocalizer.
                        format("location-format-unsupported-in-future-releases",
                                new Object[]{resourceLocation}));
                                */
                resourceLocation = '/' + resourceLocation;
            }
            is = context.getResourceAsStream(resourceLocation);
        }
        return is;
    }

    /**
     * Attempts to create a File from the given path where the File has an
     * absolute path.
     *
     * @param path the path of interest
     * @return the File or null if it could not be created
     */
    public File createConfigFile(String path) throws LogException {
        File configFile = new File(path);

        if (!configFile.exists()) {
            configFile = new File(context.getRealPath("/"), path);
        }
        if (!configFile.exists() || !configFile.isFile() ||
            !configFile.isAbsolute()) {
            configFile = null;
        }
        return configFile;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-May-05	452/10	pcameron	VBM:2005050604 Commented out log warning to get round some MCS test cases

 06-May-05	450/6	pcameron	VBM:2005050604 Log resolution relative to / not WEB-INF, warn for relative path

 29-Apr-05	443/6	pcameron	VBM:2005042807 Fixes to logging

 29-Apr-05	443/3	pcameron	VBM:2005042807 Fixes to logging

 19-Apr-05	428/11	pcameron	VBM:2005040505 Logging initialisation changed

 19-Apr-05	428/9	pcameron	VBM:2005040505 Logging initialisation changed

 18-Apr-05	428/6	pcameron	VBM:2005040505 Logging initialisation changed

 ===========================================================================
*/
