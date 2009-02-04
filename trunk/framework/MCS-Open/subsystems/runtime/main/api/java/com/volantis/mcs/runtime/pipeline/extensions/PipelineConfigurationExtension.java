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
import com.volantis.synergetics.cornerstone.utilities.extensions.Extension;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;

/**
 * Implement this interface
 */
public interface PipelineConfigurationExtension extends Extension {

    /**
     * Extend the pipeline configuration.
     *
     * @param config the configuration object to modify
     * @param marinerConfiguration the mariner configuration
     * @param mcsConfiguration the mcs configuration
     * @param assetTranscoder the assetTranscoder
     */
    public void extendPipelineConfiguration(
            XMLPipelineConfiguration config,
            MarinerConfiguration marinerConfiguration,
            MCSConfiguration mcsConfiguration,
            PluggableAssetTranscoder assetTranscoder);

    /**
     * Allow extensions to resolve data sources.
     *
     * @param marinerConfig the mariner configuration
     * @param mcsConfiguration the mcs configuration
     */
    public void extendDataSourceResolution(MarinerConfiguration marinerConfig,
                                           MCSConfiguration mcsConfiguration);

    /**
     * Allow extensions to resolve relative data sources.
     *
     * @param mcsConfiguration the mcs configuration
     */
    public void extendReferenceDataSourceResolution(
            MCSConfiguration mcsConfiguration);

}
