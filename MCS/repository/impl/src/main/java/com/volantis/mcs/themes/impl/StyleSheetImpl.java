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
package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.model.ThemeModel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.path.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link com.volantis.mcs.themes.StyleSheet}.
 */
public class StyleSheetImpl implements StyleSheet {

    /**
     * The list of rules that make up the style sheet.
     */
    private List rules;

    public StyleSheetImpl() {
        this.rules = new ArrayList();
    }

    public void validate(ValidationContext context) {
        Step ruleStep = context.pushPropertyStep(ThemeModel.RULES);
        for (int i = 0; i < rules.size(); i++) {
            Rule rule = (Rule) rules.get(i);
            Step indexStep = context.pushIndexedStep(i);
            rule.validate(context);
            context.popStep(indexStep);
        }
        context.popStep(ruleStep);
    }

    // Javadoc inherited.
    public List getRules() {
        return rules;
    }

    // Javadoc inherited.
    public void addRule(Rule rule) {
        rules.add(rule);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
