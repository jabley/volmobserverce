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
import com.volantis.mcs.runtime.configuration.MarkupPluginConfiguration;

/**
 * Define the RuleSet for markup plugin.
 */
public class MarkupPluginRuleSet
        extends IntegrationPluginRuleSet {
    
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    public MarkupPluginRuleSet(String prefix) {
        super(prefix, "markup-plugin",
              MarkupPluginConfiguration.class,
              "addMarkupPlugin",
              new String [] {"scope"},
              new String [] {"scope"});
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/2	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Jun-04	4619/1	ianw	VBM:2004060111 Fixed MPS Configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 09-Jul-03	761/1	adrian	VBM:2003070801 Added configuration support for mcs plugins

 ===========================================================================
*/
