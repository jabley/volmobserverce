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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.properties.StyleProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Base for all classes that test classes derived from
 * {@link AbstractCounterParser}.
 */
public abstract class CounterParserTestCaseAbstract
        extends ParsingPropertiesMockTestCaseAbstract {

    private final StyleProperty property;
    private final String propertyName;

    public CounterParserTestCaseAbstract(StyleProperty property) {
        this.property = property;
        this.propertyName = property.getName();
    }

    /**
     * Get the initial value for the integer component part of the parser.
     *
     * @return The integer value.
     */
    protected abstract int getIntegerInitialValue();

    /**
     * Test that none works.
     */
    public void testNone() throws Exception {

        expectSetProperty(property, StyleKeywords.NONE);

        parseDeclarations(propertyName + ": none");
    }

    /**
     * Test that a single counter with an integer works.
     */
    public void testSingleWithInteger() throws Exception {

        List list = new ArrayList();
        list.add(getCounter("alpha", 1));
        StyleValue counters = STYLE_VALUE_FACTORY.getList(list);

        expectSetProperty(property, counters);

        parseDeclarations(propertyName + ": alpha 1");
    }

    /**
     * Test that a single counter with no integer works.
     */
    public void testSingleNoInteger() throws Exception {

        List list = new ArrayList();
        list.add(getCounter("alpha", getIntegerInitialValue()));
        StyleValue counters = STYLE_VALUE_FACTORY.getList(list);

        expectSetProperty(property, counters);

        parseDeclarations(propertyName + ": alpha");
    }

    /**
     * Test that multiple counters with integers work.
     */
    public void testMultipleWithIntegers() throws Exception {

        List list = new ArrayList();
        list.add(getCounter("alpha", 1));
        list.add(getCounter("beta", 2));
        StyleValue counters = STYLE_VALUE_FACTORY.getList(list);

        expectSetProperty(property, counters);

        parseDeclarations(propertyName + ": alpha 1 beta 2");
    }

    /**
     * Test that multiple counters without integers work.
     */
    public void testMultipleNoIntegers() throws Exception {

        List list = new ArrayList();
        list.add(getCounter("alpha", getIntegerInitialValue()));
        list.add(getCounter("beta", getIntegerInitialValue()));
        StyleValue counters = STYLE_VALUE_FACTORY.getList(list);

        expectSetProperty(property, counters);

        parseDeclarations(propertyName + ": alpha beta");
    }

    /**
     * Test that a mixture of counters with and without integers work.
     */
    public void testMixture() throws Exception {

        List list = new ArrayList();
        list.add(getCounter("alpha", getIntegerInitialValue()));
        list.add(getCounter("beta", 2));
        list.add(getCounter("gamma", getIntegerInitialValue()));
        StyleValue counters = STYLE_VALUE_FACTORY.getList(list);

        expectSetProperty(property, counters);

        parseDeclarations(propertyName + ": alpha beta 2 gamma");
    }

    private StylePair getCounter(String identifier, int integer) {
        StyleIdentifier identifierValue = STYLE_VALUE_FACTORY.getIdentifier(
                null, identifier);
        StyleInteger integerValue = STYLE_VALUE_FACTORY.getInteger(null, integer);
        return STYLE_VALUE_FACTORY.getPair(identifierValue, integerValue);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
