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

import com.volantis.mcs.themes.StyleSheetFactory;

/**
 * A factory for creating objects which can build style sheet rules and
 * dependent objects.
 * <p>
 * The dependent objects for a rule may be created by creating one or more
 * builders for simple selector sequences, and adding them to a rule.
 * 
 * @mock.generate
 */
public class RuleBuilderFactory {

    /**
     * Create a builder for a style sheet rule.
     *
     * @return the created builder.
     */
    public RuleBuilder createRuleBuilder() {
        return new RuleBuilder();
    }

    /**
     * Create a builder for a style sheet simple selector sequence.
     *
     * @return the created builder.
     */
    public SimpleSelectorSequenceBuilder createSequenceBuilder() {
        return new SimpleSelectorSequenceBuilder(
                StyleSheetFactory.getDefaultInstance());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 18-Jul-05	8668/1	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
