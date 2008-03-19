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
 * Copyright Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.extensions;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.utilities.extensions.ExtensionFactoryLoader;
import com.volantis.synergetics.log.LogDispatcher;
import our.apache.commons.digester.Digester;

/**
 * Extension point for the pipeline.
 */
public abstract class ConfigurationRuleSetExtensionFactory {

    /**
     * The factory to return
     */
    private static ConfigurationRuleSetExtension defaultFactory;

    /**
     * Lock for mutual exclusion
     */
    private static final Object MUTEX = new Object();

    /**
     * Initialize the factory with an instance of the specified factory you
     * can call this as often as you wish. It will only use the first value
     * that was passed to it.
     *
     * @param classname the class name of the PipelineExtensionFactory
     * @param classloader the classloader to use when loading the extension
     * factory. If null then the context classloader will be used.
     */
    private static void initialize(String classname, ClassLoader classloader) {
        synchronized(MUTEX) {
            if (defaultFactory == null) {
                if (classname != null) {
                    defaultFactory = (ConfigurationRuleSetExtension)
                            ExtensionFactoryLoader.createExtensionFactory(
                                    classname,classloader);
                }
                if (defaultFactory == null) {
                    defaultFactory = new NopPipelineExtensionFactory();
                }
            }
        }
    }

    /**
     * Get the default instance of the pipeline extension factory.
     * The factory should be initalized before use otherwise it will self
     * initialize using the context classloader and a nop Pipeline extension
     * factory.
     *
     * @return the default instance of the pipeline extension factory.
     */
    public static ConfigurationRuleSetExtension getDefaultInstance() {
        synchronized(MUTEX) {
            if (defaultFactory == null) {
                initialize(ExtensionFactoryLoader.EXTENSION_CLASS, null);
            }
        }
        return defaultFactory;
    }


    /**
     * A simple no operation extention factory
     */
    private static class NopPipelineExtensionFactory
            implements ConfigurationRuleSetExtension {

        // Javadoc inherited
        public void extendProcessRuleSet(String prefix, Digester digester) {
            // do nothing
        }
    }
}
