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

import com.volantis.mcs.runtime.configuration.StyleSheetPageLevelGenerationConfiguration;


/**
 * Adds digester rules for the style-sheets tag.
 */
public class StyleSheetPageLevelGenerationRuleSet extends PrefixRuleSet {


    public StyleSheetPageLevelGenerationRuleSet(String prefix) {
        this.prefix = prefix;
    }

    // JavaDoc inherited
    public void addRuleInstances(Digester digester) {

        // <style-sheets>/<external-generation>
        final String pattern = prefix;
        digester.addObjectCreate(pattern,
                StyleSheetPageLevelGenerationConfiguration.class);
        digester.addSetNext(pattern, "setPageLevelCacheConfiguration");
        digester.addSetProperties(pattern,
                new String[] {"max-age"},
                new String[] {"maxAge"});
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 ===========================================================================
*/
