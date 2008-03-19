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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.pipeline;

import com.volantis.mcs.runtime.configuration.CacheOperationConfiguration;
import com.volantis.xml.pipeline.sax.cache.CacheProcessConfiguration;
import com.volantis.xml.pipeline.sax.drivers.ConnectionConfiguration;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfiguration;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDriverConfiguration;
import com.volantis.xml.pipeline.sax.operations.transform.TransformConfiguration;

import java.util.Map;
import java.util.HashMap;

/**
 * Holds pipeline configuration information.
 */
public class PipelineConfiguration {

    /**
     * Store extension configurations to be stored here
     */
    private final Map extensions = new HashMap();

    /**
     * Store the Web Services Driver configuration here so that the pipeline
     * may register a process using this configuration.
     */
    private WSDriverConfiguration wsDriverConfiguration = null;

    /**
     * Store the Web Driver configuration here so that the pipeline
     * may register a process using this configuration.
     */
    private WebDriverConfiguration webDriverConfiguration = null;

    /**
     * Store the transform configuration here so that the pipeline may register
     * a process using this configuration. (The transform configuration
     * determines whether xsl or xsltc is used.)
     */
    private TransformConfiguration transformConfiguration = null;

    /**
     * Store the general connection configuration here so that the pipeline may
     * register processes using this configuration.
     */
    private ConnectionConfiguration connectionConfiguration = null;

    /**
     * Configuration for the <pipeline-configuration>/<markup-extensions>
     * element.
     */
    private MarkupExtensionsConfiguration markupExtensionsConfiguration;

    /**
     * The directory that the pipeline debug out put will be written to.
     */
    private String debugOutputDirectory;

    /**
     * The cache process configuration.
     */
    private CacheProcessConfiguration cacheProcessConfiguration;

    /**
     * The cache operation configuration.
     */
    private CacheOperationConfiguration cacheOperationConfiguration;

    /**
     * Sets the cache operation configuration.
     *
     * @param cacheOperationConfiguration the new value
     */
    public void setCacheOperationConfiguration(
        final CacheOperationConfiguration cacheOperationConfiguration) {

        this.cacheOperationConfiguration = cacheOperationConfiguration;
    }

    /**
     * Returns the cache operation configuration.
     */
    public CacheOperationConfiguration getCacheOperationConfiguration() {
        return cacheOperationConfiguration;
    }

    // javadoc unnecessary
    public ConnectionConfiguration getConnectionConfiguration() {
        return connectionConfiguration;
    }

    // javadoc unnecessary
    public void setConnectionConfiguration(
            ConnectionConfiguration connectionConfiguration) {
        this.connectionConfiguration = connectionConfiguration;
    }

    /**
     * Add an extension configuration. The object will be keyed by its class
     *
     * @param value the configuration object
     */
    public void setExtensionConfiguration(Object value) {
        extensions.put(value.getClass(), value);
    }

    /**
     * Get the extension configuration object.
     *
     * @param clazz the class of the extension configuration object to return
     * @return the specified extension configuration object or null if it does
     * not exist.
     */
    public Object getExtensionConfiguration(Class clazz) {
        return extensions.get(clazz);
    }

    /**
     * Return the WSDriverConfiguration object.
     * @return the WSDriverConfiguration configuration object.
     */
    public WSDriverConfiguration getWsDriverConfiguration() {
        return wsDriverConfiguration;
    }

    /**
     * Set the WSDriverConfiguration object.
     * @param wsDriverConfiguration the <code>WSDriverConfiguration</code>
     * object
     */
    public void setWsDriverConfiguration(
                WSDriverConfiguration wsDriverConfiguration) {
        this.wsDriverConfiguration = wsDriverConfiguration;
    }

    /**
     * Return the WebDriverConfiguration object.
     * @return the WebDriverConfiguration configuration object.
     */
    public WebDriverConfiguration getWebDriverConfiguration() {
        return webDriverConfiguration;
    }

    /**
     * Set the WebDriverConfiguration object.
     * @param webDriverConfiguration the <code>WebDriverConfiguration</code>
     * object
     */
    public void setWebDriverConfiguration(
                WebDriverConfiguration webDriverConfiguration) {
        this.webDriverConfiguration = webDriverConfiguration;
    }

    /**
     * Return the TransformConfiguration object
     * @return TransformConfiguration
     */
    public TransformConfiguration getTransformConfiguration() {
        return transformConfiguration;
    }

    /**
     * Set the TransformConfiguration object
     * @param configuration the TransformConfiguration object
     */
    public void setTransformConfiguration(
                TransformConfiguration configuration) {
        transformConfiguration = configuration;
    }

    /**
     * Sets the directory that the pipeline debug output will be written to.
     *
     * @param debugOutputDirectory the directory prefix that will be used for
     * writing of pipeline debug output.
     */
    public void setDebugOutputDirectory(String debugOutputDirectory) {
        // Note we do not append a "/" as this is a prefix. See AN040.
        this.debugOutputDirectory = debugOutputDirectory;
    }

    // javadoc unnecessary
    public MarkupExtensionsConfiguration getMarkupExtensionsConfiguration() {
        return markupExtensionsConfiguration;
    }

    // javadoc unnecessary
    public void setMarkupExtensionsConfiguration(
                MarkupExtensionsConfiguration markupExtensionsCongiguration) {
        this.markupExtensionsConfiguration = markupExtensionsCongiguration;
    }

    /**
     * Returns the directory that the pipeline debug output will be written to.
     * @return the directory that the pipeline debug output will be written to.
     */
    public String getDebugOutputDirectory() {
        return debugOutputDirectory;
    }

    /**
     * Return the CacheProcessConfiguration for this PipelineConfiguration.
     *
     * @return a CacheProcessConfiguration
     */
    public CacheProcessConfiguration getCacheProcessConfiguration() {
        return cacheProcessConfiguration;
    }

    /**
     * Set the CacheProcessConfiguration for this PipelineConfiguration.
     *
     * @param cacheProcessConfiguration
     */
    public void setCacheProcessConfiguration(CacheProcessConfiguration cacheProcessConfiguration) {
        this.cacheProcessConfiguration = cacheProcessConfiguration;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	9724/1	philws	VBM:2005092810 Port forward of the generic pipeline connection timeout functionality

 04-Oct-05	9679/1	philws	VBM:2005092810 Provide a connection timeout mechanism and configuration for pipeline operations

 10-May-05	8135/1	matthew	VBM:2005050905 force debugOutputDirectory to end in File.seperator

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 ===========================================================================
*/
