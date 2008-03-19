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
package com.volantis.mcs.dom2theme.impl.generator.rule.builder;

import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.ThemeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to help us build style sheet rules.
 *
 * @mock.generate
 */
public class RuleBuilder {

    /**
     * The rule being built.
     */
    private Rule rule;

    /**
     * Initialise.
     */
    public RuleBuilder() {

        rule = ThemeFactory.getDefaultInstance().createRule();
    }

    /**
     * Add a simple selector sequence to the the rule being built.
     *
     * @param builder the sequence builder which has a sequence.
     */
    public void addSequence(SimpleSelectorSequenceBuilder builder) {

        List selectors = rule.getSelectors();
        if (selectors == null) {
            selectors = new ArrayList();
            rule.setSelectors(selectors);
        }

        selectors.add(builder.getSequence());
    }

    /**
     * Set the properties associated with the rule being built.
     *
     * @param properties the properties to set on the rule.
     */
    public void setProperties(StyleProperties properties) {

        rule.setProperties(properties);
    }

    /**
     * Returns the created rule.
     *
     * @return the created rule.
     */
    public Rule getRule() {

        return rule;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	8668/16	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
