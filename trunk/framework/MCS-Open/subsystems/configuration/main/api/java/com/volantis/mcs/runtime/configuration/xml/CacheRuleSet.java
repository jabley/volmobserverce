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

package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.CacheConfiguration;

/**
 * Define the RuleSet for the Cache.
 */
public class CacheRuleSet extends PrefixRuleSet {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * The pattern to look for when matching a cache in addition to the prefix
     */
    private String patternToMatch;

    /**
     * Initialise a new instance of this rule set.  This rule set matches
     * cache elements (in terms of attributes) but the exact pattern to use
     * in the matching is specified as the second parameter.  This allows this
     * rule set to be used for the multiple places in which the cache element
     * can appear in the configuration file.
     *
     * @param prefix         The prefix to use before all patterns
     * @param patternToMatch The pattern to match.  An example of this is
     *                       "/cache" to just match a cache element.
     */
    public CacheRuleSet(String prefix, String patternToMatch) {
        this.prefix = prefix;
        this.patternToMatch = patternToMatch;
    }

    // javadoc inherited
    public void addRuleInstances(Digester digester) {
        // <caching-operation/cache>
        final String pattern = prefix + patternToMatch;

        digester.addObjectCreate(pattern, CacheConfiguration.class);
        digester.addSetNext(pattern, "addCache");

        digester.addSetProperties(pattern,
                new String[] {"name", "strategy", "max-entries", "max-age"},
                new String[] {"name", "strategy", "maxEntries", "maxAge"});
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jun-04	4726/3	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 12-Jun-03	316/2	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
