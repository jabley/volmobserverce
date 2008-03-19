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

package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.StyleSheetsConfig;

/**
 * Adds digester rules for the style-sheets element and it's sub elements.
 */
public class StyleSheetsRuleSet extends PrefixRuleSet {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Construct an instance of this class, using the prefix provided.
     *
     * @param prefix the prefix to add the rules to the digester under.
     */
    public StyleSheetsRuleSet(String prefix) {
        this.prefix = prefix;
    }

    // JavaDoc inherited
    public void addRuleInstances(Digester digester) {

        // <style-sheets>
        String pattern = prefix + "/style-sheets";
        digester.addObjectCreate(pattern, StyleSheetsConfig.class);
        digester.addSetNext(pattern, "setStylesheetConfiguration");

        // <style-sheets>/<external-generation>
        String externalPattern = pattern + "/external-generation";
        StyleSheetExternalGenerationRuleSet stylesheetSet =
                new StyleSheetExternalGenerationRuleSet(externalPattern);
        stylesheetSet.addRuleInstances(digester);

        // <style-sheets>/<page-level-generation>
        String pageLevelPattern = pattern + "/page-level-generation";
        StyleSheetPageLevelGenerationRuleSet pageLevelSet =
                new StyleSheetPageLevelGenerationRuleSet(pageLevelPattern);
        pageLevelSet.addRuleInstances(digester);        
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jun-04	4726/3	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 ===========================================================================
*/
