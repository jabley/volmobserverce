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
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.config.PropertySetter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Element;

/**
 * This class is a DOMConfigurator that deals with relative paths specified in
 * the log4j config file, by resolving them with respect to the location of the
 * log4j configuration file.
 */
public class VolantisDOMConfigurator extends DOMConfigurator {

    /**
     * Used to retrieve localized messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(
            VolantisDOMConfigurator.class);

    /**
     * The absolute path to the log4j config file, if any. This is used to
     * resolve any relative paths specified in the log4j configuration files.
     * It is null until doConfigure has been called.
     */
    private String pathToLogFile;

    /**
     * Flag indicating if this configurator was successfully configured.
     */
    private boolean isConfigured;


    /**
     * Configure log4j by reading in a log4j.dtd compliant XML configuration
     * file.
     *
     * @param resolver        used to resolve the location of the log4j config
     *                        file
     * @param log4jConfigFile location of the log4j configuration file
     * @param repository      The hierarchy to operate on
     */
    public void doConfigure(ConfigurationResolver resolver,
                            String log4jConfigFile,
                            LoggerRepository repository) {
        isConfigured = false;
        InputStream is = null;

        try {
            // Try loading the file as a filesystem file.
            File f = resolver.createConfigFile(log4jConfigFile);

            if (f != null) {
                pathToLogFile = f.getParent();
                LogLog.debug("The path to the log4j configuration file is: " +
                             pathToLogFile);
                try {
                    is = new FileInputStream(f);
                } catch (IOException e) {
                    LogLog.error(exceptionLocalizer.format(
                        "config-file-cannot-be-opened", e));
                }
            } else {
                // Try loading the file as a resource.
                is = resolver.createConfigInputStream(log4jConfigFile);
            }

            if (is != null) {
                super.doConfigure(is, repository);
                isConfigured = true;
            }

        } catch (LogException e) {
            LogLog.error(exceptionLocalizer.format("exception-thrown", e));
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (java.io.IOException e) {
                    LogLog.error(exceptionLocalizer.format(
                        "unable-to-close-inputstream"), e);
                }
            }
        }
    }

    /**
     * This is overridden to resolve any filenames specified in the
     * configuration file with respect to the location of the configuration
     * file.
     *
     * @note rest of javadoc inherited
     */
    protected void setParameter(Element elem, PropertySetter propSetter) {
        String name = subst(elem.getAttribute("name"));
        String value = elem.getAttribute("value");
        if ("File".equals(name)) {
            value = resolveLogFileName(value);
        } else {
            value = OptionConverter.convertSpecialChars(value);
        }
        value = subst(value);
        propSetter.setProperty(name, value);
    }

    /**
     * Will ensure that relative paths to log files are resolved relative to
     * the log4j configuration file.
     *
     * @param path given in the configuration file
     * @return an absolute path to the log file or null if the path supplied
     *         was null
     */
    private String resolveLogFileName(String path) {
        String resolvedPath = null;

        if (path != null) {
            File f = new File(path);

            if (!f.isAbsolute()) {
                if (pathToLogFile != null) {
                    f = new File(pathToLogFile, path);
                } else {
                    // invalid paths are handled in super class, so just warn
                    LogLog.warn(exceptionLocalizer.format(
                        "unable-to-resolve-relative-path-to-log-file",
                        new Object[]{path}));
                }
            }
            LogLog.debug("Resolved appender path: " + path + " to path: " +
                         f.getAbsolutePath());
            resolvedPath = f.getAbsolutePath();
        }

        return resolvedPath;
    }

    /**
     * Indicates whether the logger has been configured.
     *
     * @return true if the logger has been successfully configured; false
     *         otherwise.
     */
    public boolean isConfigured() {
        return isConfigured;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-May-05	466/5	matthew	VBM:2005050609 correct log statement

 19-May-05	466/3	matthew	VBM:2005050609 correct log statement

 19-May-05	466/1	matthew	VBM:2005050609 Allow log file location to be specified on windows machines

 04-May-05	435/9	pcameron	VBM:2005040505 Fixed localisation of logging messages

 03-May-05	435/5	pcameron	VBM:2005040505 Fixes to logging

 28-Apr-05	435/3	pcameron	VBM:2005040505 Fixes to logging

 26-Apr-05	435/1	pcameron	VBM:2005040505 Logging initialisation changed

 19-Apr-05	428/25	pcameron	VBM:2005040505 Logging initialisation changed

 18-Apr-05	428/22	pcameron	VBM:2005040505 Logging initialisation changed

 17-Mar-05	411/15	emma	VBM:2005020303 Modifications after review

 17-Mar-05	411/13	emma	VBM:2005020303 Modifications after review

 17-Mar-05	411/11	emma	VBM:2005020303 Allow log files to be specified as relative to the log4j config file

 17-Mar-05	411/9	emma	VBM:2005020303 Allow log files to be specified as relative to the log4j config file

 ===========================================================================
*/
