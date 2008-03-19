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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.RenderedPageCacheConfiguration;
import our.apache.commons.digester.Digester;

/**
 * Adds digester rules for the page-cache element.
 */
public class RenderedPageCacheRuleSet extends PrefixRuleSet {
    /**
     * Construct an instance of this class, using the prefix provided.
     *
     * @param prefix the prefix to add the rules to the digester under.
     */
    public RenderedPageCacheRuleSet(String prefix) {
        this.prefix = prefix;
    }

    // Javadoc inherited
    public void addRuleInstances(Digester digester) {
        final String pattern = prefix + "/page-cache";

        digester.addObjectCreate(pattern,
                RenderedPageCacheConfiguration.class);
        digester.addSetNext(pattern, "setRenderedPageCacheConfig");
        
        digester.addSetProperties(
                pattern,
                new String[] {
                    "strategy", "max-entries", "timeout", "default-scope"},
                new String[] {
                    "strategy", "maxEntries", "timeout", "defaultScope"});
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	7762/2	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 11-Feb-05	6786/1	adrianj	VBM:2005012506 Added rendered page cache

 ===========================================================================
*/
