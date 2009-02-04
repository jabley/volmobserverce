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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/StyleSheetExternalGenerationRuleSet.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; adds digester rules
 *                              for the style-sheets tag.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.StyleSheetExternalGenerationConfiguration;
import com.volantis.mcs.runtime.configuration.xml.PrefixRuleSet;

/**
 * Adds digester rules for the style-sheets tag.
 */
public class StyleSheetExternalGenerationRuleSet extends PrefixRuleSet {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public StyleSheetExternalGenerationRuleSet(String prefix) {
        this.prefix = prefix;
    }

    // JavaDoc inherited
    public void addRuleInstances(Digester digester) {

        // <style-sheets>/<external-generation>
        final String pattern = prefix;
        digester.addObjectCreate(pattern,
                StyleSheetExternalGenerationConfiguration.class);
        digester.addSetNext(pattern, "setExternalCacheConfiguration");
        digester.addSetProperties(pattern,
                new String[] {"cache", "flush-on-restart", "base-directory",
                              "base-url"},
                new String[] {"cache", "flushOnRestart", "baseDirectory",
                              "baseUrl"});
        digester.addSetProperties(pattern, "session", "cssSessionType");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jun-04	4726/6	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 24-Jun-04	4726/3	claire	VBM:2004060803 Implementation of internal style sheet caching

 09-Mar-04	2867/3	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 ===========================================================================
*/
