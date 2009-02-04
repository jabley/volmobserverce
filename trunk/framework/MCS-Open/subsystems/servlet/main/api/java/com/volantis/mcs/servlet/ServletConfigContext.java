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
package com.volantis.mcs.servlet;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.configuration.ConfigContext;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.xml.XMLConfigurationBuilder;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.ConfigurationResolver;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.log.LogException;
import com.volantis.synergetics.log.ServletContextConfigurationResolver;
import org.xml.sax.InputSource;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * The servlet specific implementation of a ConfigContext.
 */
public class ServletConfigContext implements ConfigContext {
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ServletConfigContext.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    ServletConfigContext.class);

    /**
     * The name of the parameter used to identify the mcs configuration file
     * in the webapp's web.xml file.
     */
    private static final String CONFIG_FILE_PARAM = "config.file";


    /**
     * The name of the system property used to identify the mcs configuration file.
     */
    private static final String SYSTEM_CONFIG_FILE_PARAM = "mcs.config.file";

    /**
     * Configuration file to use if none is supplied.
     */
    private static final String DEFAULT_CONFIG_FILE = "/WEB-INF/mcs-config.xml";

    /**
     * The name of the global web parameter for the log4j configuration file of
     * MCS.
     */
    private static final String LOG4J_FILE_PARAM = "mcs.log4j.config.file";

    /**
     * The absolute path to the mcs configuration file. This may be null even
     * after class creation, and will be null before getMainConfigInputSource
     * has been called.
     */
    private String absConfigPath = null;

    /**
     * The absolute resource id of the mcs configuration file. This may be null
     * even after class creation, and will be null before
     * getMainConfigInputSource has been called.
     */
    private String absConfigRID = null;

    /**
     * A map of ConfigurationResolvers keyed on the name of the parameter
     * for which resolution is required. Currently two resolvers are needed,
     * keyed against {@link #CONFIG_FILE_PARAM} and {@link #LOG4J_FILE_PARAM}.
     */
    private final Map paramNameToResolversMap = new HashMap(2, 1);

    /**
     * The ServletContext to whom we delegate.
     */
    private final ServletContext servletContext;

    /**
     * The location of the mcs configuration file used (this is either an
     * absolute path or resource id). Will be initialised after class creation.
     */
    private String configLocation;


    /**
     * Instantiate the class with our delegate ServletContext
     * @param servletContext The ServletContext
     */
    public ServletConfigContext(ServletContext servletContext) {
        this.servletContext = servletContext;

        String loc = System.getProperty(SYSTEM_CONFIG_FILE_PARAM);
        if(loc == null){
            loc = servletContext.getInitParameter(CONFIG_FILE_PARAM);
        }
        if (loc == null) {
            loc = DEFAULT_CONFIG_FILE;
            logger.warn("using-default-configuration");
        }
        logger.info("config-file-details", loc);

        this.configLocation = loc;

        initResolvers(servletContext);
    }

    /**
     * Initialises the map of resolvers.
     *
     * @param context the ServletContext used by the resolvers
     */
    private void initResolvers(ServletContext context) {
        paramNameToResolversMap.put(LOG4J_FILE_PARAM,
                new ServletContextConfigurationResolver(context));
        paramNameToResolversMap.put(CONFIG_FILE_PARAM,
                new SCCConfigurationResolver(context));
    }

    /**
     * Attempts to open a file relative to a root location relative to the
     * servlet context's view of the webapp root and return an input stream
     * containing the file's contents.
     *
     * @param root The root location in the webapp
     * @param location The location of the file relative to this location
     * @return An InputStream reading from the specified file, or null if none
     *         could be found.
     */
    private InputStream getInputRelativeToServletContext(String root,
                                                         String location) {
        InputStream is = null;
        String basePath = servletContext.getRealPath(root);
        if (basePath != null) {
            is = getFileInputStream(new File(basePath, location));
        }

        return is;
    }


    // javadoc inherited
    public InputSource getMainConfigInputSource()
            throws ConfigurationException {

        // Try to resolve the location to a file path
        File f = new File(configLocation);
        InputStream is = getFileInputStream(f);

        if (is == null) {
            // Try to resolve the location relative to the webapp root.
            is = getInputRelativeToServletContext("/", configLocation);
            String basePath = servletContext.getRealPath("/");
            
            // If this fails, attempt to resolve relative to the WEB-INF
            // directory, where the web.xml file should be located.
            if (is == null) {
                basePath = servletContext.getRealPath("/WEB-INF");
                is = getInputRelativeToServletContext("/WEB-INF", 
                        configLocation);
            }

            if (is != null) {
            	
            	File configFile = new File(basePath, configLocation);
            	try {
                    absConfigPath = configFile.getCanonicalFile().getParent(); 
            	} catch (IOException e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(e);
                    }
            	}
            } else {
                // Try to resolve the location to a resource if no file found

                // see if it's a RID relative to the current webapp context,
                if (configLocation.charAt(0) != '/' &&
                        !(new File(configLocation).isAbsolute())) {
                    // warn that specifying the config file as relative to the
                    // current webapp context will not be supported in the future
                    logger.warn("location-format-unsupported-in-future-releases",
                            configLocation);
                    is = servletContext.getResourceAsStream('/' + configLocation);

                    // If we've located the file successfully in this way,
                    // update the config location so that we can correctly
                    // generate the absolute resource ID
                    if (is != null) {
                        configLocation = '/' + configLocation;
                    }
                } else {
                    is = servletContext.getResourceAsStream(configLocation);
                }

                if (is != null) {
                    // NB: the separator should be / and not File.separator
                    // because it's a resource identifier.
                    absConfigRID = configLocation.substring(0,
                            configLocation.lastIndexOf('/') + 1);
                } else {
                    // If is is still null, the config file could not be found
                    throw new ConfigurationException(exceptionLocalizer.format(
                            "configuration-file-not-found", configLocation));
                }
            }
        } else {
            absConfigPath = f.getParent();
        }
        return new InputSource(is);
    }

    // javadoc inherited
    public InputSource getSchemaInputSource() throws ConfigurationException {
        InputStream is = createConfigInputStream(
                XMLConfigurationBuilder.SCHEMA_NAME);

        if (is == null) {
            throw new ConfigurationException(exceptionLocalizer.format(
                    "configuration-file-not-found",
                    XMLConfigurationBuilder.SCHEMA_NAME));
        }

        return new InputSource(is);
    }

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
    public InputStream createConfigInputStream(String location)
            throws ConfigurationException {
        SCCConfigurationResolver resolver = (SCCConfigurationResolver)
                paramNameToResolversMap.get(CONFIG_FILE_PARAM);
        try {
            return resolver.createConfigInputStream(location);
        } catch (LogException e) {
            throw new ConfigurationException(e.getLocalizedMessage(), e);
        }
    }

    // javadoc inherited
    public File getConfigRelativeFile(final String path,
                                      final boolean mustExist)
            throws ConfigurationException {
        final File file;
        final SCCConfigurationResolver resolver = (SCCConfigurationResolver)
                paramNameToResolversMap.get(CONFIG_FILE_PARAM);
        try {
            file = resolver.locateFileOrResource(path, false, mustExist);
        } catch (LogException e) {
            throw new ConfigurationException(e.getLocalizedMessage(), e);
        }

        return file;
    }

    // javadoc inherited
    public String getLog4jLocation() {
        String loc = System.getProperty(LOG4J_FILE_PARAM);
        if(loc == null) loc = servletContext.getInitParameter(LOG4J_FILE_PARAM);
        return loc;
    }

    /**
     * Utility method which returns a FileInputStream for the supplied file,
     * assuming that the file exists and is readable. Returns null otherwise.
     * Avoids code duplication.
     *
     * @param f the file for which to get a FileInputStream
     * @return a FileInputStream or null
     */
    private FileInputStream getFileInputStream(File f) {
        FileInputStream fis = null;
        if (f != null && f.exists() && f.canRead()) {
            try {
                fis = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                logger.info("location-not-file-path", f.getPath());
            }
        }
        return fis;
    }

    /**
     * Gets a configuration resolver.
     *
     * @param paramName the name of the resolver to get. Currently, paramName is
     * "config.file" or  "mcs.log4j.config.file". The former resolver resolves
     * against the location of the main MCS configuration file. The latter
     * resolves against the location of the log4j configuration file.
     * @return the resolver
     */
    public ConfigurationResolver getConfigurationResolver(String paramName) {
        return (ConfigurationResolver) paramNameToResolversMap.get(paramName);
    }


    /**
     * A ConfigurationResolver that resolves locations with respect to the
     * main configuration file.
     */
    private class SCCConfigurationResolver implements ConfigurationResolver {

        /**
         * The ServletContext used by this resolver.
         */
        private final ServletContext context;


        // javadoc inherited
        public SCCConfigurationResolver(ServletContext context) {
            this.context = context;
        }

        // javadoc inherited
        public InputStream createConfigInputStream(final String location)
                throws LogException {
            InputStream is = null;

            String validatedLocation = location;
            if (location.charAt(0) != '/' && absConfigRID != null) {
                validatedLocation = absConfigRID + location;
            }

            // attempt to load config file as 'validated' resource
            is = context.getResourceAsStream(validatedLocation);

            // If we cannot load as a resource... try as a file
            if (is == null) {
                File f = locateFileOrResource(location, true, false);
                is = getFileInputStream(f);

                if (is == null) {
                    // see if it's a RID relative to the current webapp context
                    String resourceLocation = location;
                    if (location.charAt(0) != '/') {
                        // NB: the separator should be / and not File.separator
                        // because it's a resource identifier.
                        resourceLocation = '/' + location;
                    }

                    is = context.getResourceAsStream(resourceLocation);
                }
            }

            return is;
        }

        /**
         * Attempts to resolve the path given against the location of the main
         * mcs configuration file.
         *
         * @param path to resolve and create file for
         * @param couldBeResource
         *             false if the path definitely refers to a file path
         *             true if it could be a resource identifier
         * @return a file at the resolved path
         * @throws LogException if a relative path was supplied and the path
         *                      against which to resolve it is null, and the
         *                      path is definitely a file path (as opposed to a
         *                      resource ID)
         */
        public File locateFileOrResource(String path,
                                         boolean couldBeResource,
                                         boolean mustExist)
                throws LogException {
            File configFile = null;
            if (path != null) {
            	
                configFile = new File(path);
                
                if (!configFile.isAbsolute()) {
                    if (absConfigPath != null) {
                        configFile = new File(absConfigPath, path);
                    } else if (!couldBeResource) {
                        throw new LogException(exceptionLocalizer.format(
                                "relative-paths-not-allowed", path));
                    }
                }
                if (!configFile.exists() && mustExist) {
                    configFile = null;
                }
            }
            return configFile;
        }

        //javadoc inherited
        public File createConfigFile(String path) throws LogException {
            File configFile = locateFileOrResource(path, false, true);

            return configFile;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-May-05	8117/1	emma	VBM:2005050610 Fixing bug - using linux style paths in config files caused init to fail on windows

 06-May-05	8075/1	emma	VBM:2005050519 Fixing file separator bug which caused volantis to fail to initialise on windows

 04-May-05	7759/27	pcameron	VBM:2005040505 Fixes to logging

 26-Apr-05	7759/25	pcameron	VBM:2005040505 Logging initialisation changed

 17-Mar-05	7401/2	emma	VBM:2005020303 Allow log files to be specified as relative to the log4j config file

 11-Mar-05	6842/6	emma	VBM:2005020302 Making file references in config files relative to those files

 21-Feb-05	6986/1	emma	VBM:2005021411 Changes merged from MCS3.3

 18-Feb-05	6974/3	emma	VBM:2005021411 Modifications after review

 18-Feb-05	6974/1	emma	VBM:2005021411 Making the device repository and xml policies locations relative to mcs-config.xml

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/1	ianw	VBM:2004090605 New Build system

 ===========================================================================
*/
