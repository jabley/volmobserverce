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
package com.volantis.mcs.dom2theme.impl.generator.rule;

import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.Rule;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GroupRuleExtractor implements RuleExtractor {

    public StyleSheet extractRules(OutputStyledElementList elementList,
            StyleSheet styleSheet) {

        // ==================================================================
        // 3 - Group selectors with common properties.
        // ==================================================================

        Map propertiesToRule = new HashMap();
        Iterator rules = styleSheet.getRules().iterator();
        while (rules.hasNext()) {
            Rule rule = (Rule) rules.next();
            StyleProperties properties = rule.getProperties();
            // If the theme already has a rule with this properties ...
            Rule existingRule = (Rule) propertiesToRule.get(properties);
            if (existingRule != null) {
                // ... then we should add this rule's selectors to the existing
                // rule with those properties and throw this rule away.
                existingRule.getSelectors().addAll(rule.getSelectors());
                rules.remove();
            } else {
                // ,,, else we leave this rule alone, and add it into the
                // mapping we use to check for existance.
                propertiesToRule.put(properties, rule);
            }
        }

//        // Move all the grouped rules to the end of the theme for compatibility
//        // with the integration tests in AN004 (and clarity ;-).
//        // First suck out the grouped rules into a separate list.
//        List groupedRules = new ArrayList();
//        rules = styleSheet.getRules().iterator();
//        while (rules.hasNext()) {
//            Rule rule = (Rule) rules.next();
//            if (rule.getSelectors().size() > 1) {
//                rules.remove();
//                groupedRules.add(rule);
//            }
//        }
//        // Then add them back to the theme at the end.
//        rules = groupedRules.iterator();
//        while (rules.hasNext()) {
//            Rule rule = (Rule) rules.next();
//            styleSheet.addRule(rule);
//        }

        return styleSheet;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	8668/11	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
