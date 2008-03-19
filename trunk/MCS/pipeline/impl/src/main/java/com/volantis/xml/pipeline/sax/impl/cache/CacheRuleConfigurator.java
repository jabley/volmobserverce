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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.impl.cache;

import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.impl.dynamic.SingleNamespaceConfigurator;
import com.volantis.xml.pipeline.sax.cache.CacheRule;
import com.volantis.xml.pipeline.sax.cache.CacheInfoRule;
import com.volantis.xml.pipeline.sax.cache.CacheKeyRule;
import com.volantis.xml.pipeline.sax.cache.CacheBodyRule;
import com.volantis.xml.pipeline.sax.cache.CacheControlRule;

/**
 * Configures rules for cache.
 */
public class CacheRuleConfigurator
        extends SingleNamespaceConfigurator {

    /**
     * The default instance.
     */
    private static final DynamicRuleConfigurator DEFAULT_INSTANCE =
            new CacheRuleConfigurator();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static DynamicRuleConfigurator getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private static final DynamicElementRule CACHE_RULE = new CacheRule();
    private static final DynamicElementRule CACHE_INFO_RULE = new CacheInfoRule();
    private static final DynamicElementRule CACHE_KEY_RULE = new CacheKeyRule();
    private static final DynamicElementRule CACHE_BODY_RULE = new CacheBodyRule();
    private static final DynamicElementRule CACHE_CONTROL_RULE = new CacheControlRule();

    public CacheRuleConfigurator() {
        super(Namespace.PIPELINE.getURI());
    }

    protected void configure(NamespaceRuleSet ruleSet) {

        // add the rule for the cache element.
        ruleSet.addRule("cache", CACHE_RULE);

        // add the rule for the cacheInfo element.
        ruleSet.addRule("cacheInfo", CACHE_INFO_RULE);

        // add the rule for the cacheKey element.
        ruleSet.addRule("cacheKey", CACHE_KEY_RULE);

        // add the rule for the cacheBody element.
        ruleSet.addRule("cacheBody", CACHE_BODY_RULE);

        // add the rule for the cacheControl element.
        ruleSet.addRule("cacheControl", CACHE_CONTROL_RULE);
    }
}
