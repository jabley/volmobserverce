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
package com.volantis.xml.pipeline.sax.convert;

import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.synergetics.url.URLPrefixRewriteManager;

/**
 * Configuration for URLRewriteProcess.
 *
 * There are two aspects to configuring a URLRewriteProcess: a) the
 * URLPrefixRewriteManager and b) the ConverterConfiguration.
 *
 * The URLPrefixRewriteManager contains the url prefixes of urls that should
 * be rewritten and the rules that define how to rewrite them.
 *
 * The ConverterConfiguration defines which elements contain urls that should
 * be rewritten and the attributes of these elements that contain the url
 * value.
 */
public class URLRewriteProcessConfiguration implements Configuration {

    /**
     * The ConverterConfiguration associated with this
     * URLRewriteProcessConfiguration.
     */
    private ConverterConfiguration converterConfiguration =
            new ConverterConfiguration();

    /**
     * The URLPrefixRewriteManager associated with this
     * URLRewriteProcessConfiguration.
     */
    private URLPrefixRewriteManager urlPrefixRewriteManager =
            new URLPrefixRewriteManager();

    /**
     * Process mode determines set of tuples used in rewriting
     */
    private String processMode = ConverterConfiguration.DEFAULT_MODE;

    /**
     * Get the ConverterConfiguration associated with this
     * URLRewriteProcessConfiguration.
     * @return the ConverterConfiguration
     */
    public ConverterConfiguration getConverterConfiguration() {
        return converterConfiguration;
    }

    /**
     * Get the URLPrefixRewriteManager associated with this
     * URLRewriteProcessConfiguration.
     * @return the URLPrefixRewriteManager.
     */
    public URLPrefixRewriteManager getURLPrefixRewriteManager() {
        return urlPrefixRewriteManager;
    }

    /**
     *  Set mode in which process will be working.
     */
    public void setProcessMode(String mode){
        this.processMode = mode;        
    }

    /**
     * Get current process mode.
     */
    public String getProcessMode(){
        return this.processMode;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 26-May-04	708/1	allan	VBM:2004052102 Provide a URL rewriting process.

 ===========================================================================
*/
