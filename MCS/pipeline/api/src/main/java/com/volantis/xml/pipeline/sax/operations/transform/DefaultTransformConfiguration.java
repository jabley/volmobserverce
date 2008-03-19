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
package com.volantis.xml.pipeline.sax.operations.transform;

import com.volantis.synergetics.cache.GenericCache;
import com.volantis.synergetics.cache.GenericCacheFactory;
import com.volantis.xml.pipeline.sax.operations.transform.TransformConfiguration;

/**
 * This class stores the configuration information for transforms,
 */
public class DefaultTransformConfiguration implements TransformConfiguration {

    /**
     * A boolean storing whether or not we can compile transforms.
     */
    private boolean required;

    /**
     * A boolean storing whether or not transforms are cached.
     */
    private boolean cacheRequired;

    /**
     * The cache for transformations.
     */
    private GenericCache cache;

    // Javadoc inherited.
    public void setTemplateCompilationRequired(boolean required) {
        this.required = required;
    }

    // Javadoc inherited.
    public boolean isTemplateCompilationRequired() {
        return required;
    }

    // Javadoc inherited.
    public void setTemplateCacheRequired(boolean required) {
        cacheRequired = required;
    }

    // Javadoc inherited.
    public boolean isTemplateCacheRequired() {
        return cacheRequired;
    }

    /**
     * Get the cache that the transform operation should use to cache templates.
     * @return The cache to use or null if the transform operation should not
     * cache the templates.
     * @see #setTemplateCacheRequired
     * */
    public synchronized Object getTemplateCache() {
        // If cache already exists we don't want to kill it!
        if (cacheRequired && cache == null) {
            cache = GenericCacheFactory.createCache(null, -1, -1);
        }
        return cache;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 26-Jan-04	551/1	claire	VBM:2004012204 Implementing caching for transforms

 23-Jan-04	545/3	claire	VBM:2004012202 Updated TransformConfiguration and related implementations and testcases

 22-Jan-04	545/1	claire	VBM:2004012202 transform configuration to support new template cache property

 07-Aug-03	268/4	chrisw	VBM:2003072905 Public API changed for transform configuration

 ===========================================================================
*/
