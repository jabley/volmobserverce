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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2theme.impl.generator;

import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.dom2theme.impl.generator.rule.RuleExtractor;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultDOMThemeGenerator implements DOMThemeGenerator {

    private List ruleExtractorList = new ArrayList();

    private final StyleSheetFactory styleSheetFactory;

    public DefaultDOMThemeGenerator() {
        this.styleSheetFactory = StyleSheetFactory.getDefaultInstance();
    }

    public void addRuleExtractor(RuleExtractor ruleExtractor) {

        ruleExtractorList.add(ruleExtractor);
    }

    public StyleSheet generateStyleSheetFor(OutputStyledElementList elementList) {

        StyleSheet styleSheet = styleSheetFactory.createStyleSheet();

        Iterator ruleExtractors = ruleExtractorList.iterator();
        while (ruleExtractors.hasNext()) {
            RuleExtractor extractor = (RuleExtractor) ruleExtractors.next();
            styleSheet = extractor.extractRules(elementList, styleSheet);
        }

        return styleSheet;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 18-Jul-05	8668/12	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
