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
package com.volantis.mcs.runtime.pipeline.extensions;

import com.volantis.mcs.integration.PluggableAssetTranscoder;
import com.volantis.mcs.runtime.configuration.MCSConfiguration;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.synergetics.cornerstone.utilities.extensions.ExtensionFactoryLoader;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;

/**
 * Extension point for the pipeline.
 */
public abstract class PipelineConfigurationExtensionFactory {

    private static final String PROF_EXTENSION_CLASS =
        "com.volantis.mcs.professional.configuration.extensions.ProfessionalConfigurationExtensions";

    /**
     * The factory to return
     */
    private static PipelineConfigurationExtension defaultFactory;

    /**
     * Lock for mutual exclusion
     */
    private static final Object MUTEX = new Object();

    /**
     * Initialize the factory with an instance of the specified factory you
     * can call this as often as you wish. It will only use the first value
     * that was passed to it.
     *
     * @param classname the class name of the PipelineConfigurationExtension
     * @param classloader the classloader to use when loading the extension
     * factory. If null then the context classloader will be used.
     */
    private static void initialize(String classname, ClassLoader classloader) {
        synchronized(MUTEX) {
            if (defaultFactory == null) {
                if (classname != null) {
                    defaultFactory = (PipelineConfigurationExtension)
                            ExtensionFactoryLoader.createExtensionFactory(
                                    classname,classloader);
                }
                if (defaultFactory == null) {
                    defaultFactory = new NopPipelineConfigurationExtension();
                }
            }
        }
    }

    /**
     * Get the default instance of the pipeline extension.
     * The factory should be initalized before use otherwise it will self
     * initialize using the context classloader and a noop Pipeline extension.
     *
     * @return the default instance of the pipeline extension factory.
     */
    public static PipelineConfigurationExtension getDefaultInstance() {
        synchronized(MUTEX) {
            if (defaultFactory == null) {
                initialize(PROF_EXTENSION_CLASS, null);
            } else {

            }
        }
        return defaultFactory;
    }

    /**
     * A simple no operation extention
     */
    private static class NopPipelineConfigurationExtension
            implements PipelineConfigurationExtension {

        // Javadoc inherited
        public void extendPipelineConfiguration(
                XMLPipelineConfiguration config,
                MarinerConfiguration marinerConfiguration,
                MCSConfiguration mcsConfiguration,
                PluggableAssetTranscoder assetTranscoder) {
            // do nothing
        }

        // Javadoc inherited
        public void extendDataSourceResolution(
                MarinerConfiguration marinerConfiguration,
                MCSConfiguration mcsConfiguration) {
            // do nothing
        }

        // Javadoc inherited
        public void extendReferenceDataSourceResolution(
                MCSConfiguration mcsConfiguration) {
            // do nothing
        }
    }

}
