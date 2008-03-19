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

package com.volantis.styling.integration.engine;

import com.volantis.mcs.themes.StyleInherit;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.StyleValuesMock;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.impl.engine.Inheriter;
import com.volantis.styling.impl.engine.InheriterImpl;
import com.volantis.styling.properties.PropertiesTestHelper;
import com.volantis.styling.properties.StylePropertyDefinitionsMock;
import com.volantis.styling.properties.StylePropertyMock;
import com.volantis.styling.values.ImmutablePropertyValuesMock;
import com.volantis.styling.values.InitialValueFinderMock;
import com.volantis.styling.values.MutablePropertyValuesMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class InheriterTestCase
        extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();

    private MutablePropertyValuesMock valuesMock;
    private StyleValuesMock inheritableValuesMock;
    private InitialValueFinderMock initialValueFinderMock;
    private StylePropertyDefinitionsMock definitionsMock;
    private ExpectationBuilder expectations;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        expectations = mockFactory.createUnorderedBuilder();

        valuesMock = new MutablePropertyValuesMock(
                "valuesMock", expectations);
        inheritableValuesMock =
                new StyleValuesMock("inheritableValuesMock", expectations);

        initialValueFinderMock = new InitialValueFinderMock(
                "initialValueFinderMock", expectations);

        definitionsMock =
                new StylePropertyDefinitionsMock(
                        "definitionsMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        valuesMock.expects.getStylePropertyDefinitions()
                .returns(definitionsMock).any();
    }

    public void test() {

    }

    /**
     * Test that a property that is automatically inherited and which has not
     * been specified is correctly inherited from the parent values.
     */
    public void testImplicitlyInherited() {

        // The implicitly inherited property.
        final StylePropertyMock propertyMock =
                PropertiesTestHelper.createStylePropertyMock(
                        expectations, "propertyMock", true, false);
        final StyleValue outputValue = StyleKeywords.ACCEPT;

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        PropertiesTestHelper.addExpectations(
                expectations, definitionsMock,
                new StylePropertyMock[]{
                    propertyMock,
                });

        expectations.add(new OrderedExpectations() {
            public void add() {

                // Test that implicit inheritance works:
                // -------------------------------------

                // The implicitly inherited property has already been set up to
                // indicate that it automatically inherits.

                // No value was specified.
                valuesMock.expects.getSpecifiedValue(propertyMock)
                        .returns(null);

                // Get the computed value from the inheritable properties.
                inheritableValuesMock.expects
                        .getStyleValue(propertyMock)
                        .returns(outputValue);

                // Set the computed value.
                valuesMock.expects.setComputedValue(
                        propertyMock, outputValue);

            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Inheriter inheriter = new InheriterImpl(
                initialValueFinderMock);
        inheriter.inherit(valuesMock, inheritableValuesMock);
    }

    /**
     * Test that a property that can be computed is.
     */
    public void testComputed() {

        // The implicitly inherited property.
        final StylePropertyMock propertyMock =
                PropertiesTestHelper.createStylePropertyMock(
                        expectations, "propertyMock", false, true);
        final StyleValue outputValue = StyleKeywords.RIGHT;

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        PropertiesTestHelper.addExpectations(
                expectations, definitionsMock,
                new StylePropertyMock[]{
                    propertyMock,
                });

        expectations.add(new OrderedExpectations() {
            public void add() {

                // Test that computed value works:
                // -------------------------------------

                // The implicitly inherited property has already been set up to
                // indicate that it automatically inherits.

                // No value was specified.
                valuesMock.expects.getSpecifiedValue(propertyMock)
                        .returns(null);

                // Get the initial value from the retriever.
                initialValueFinderMock.expects
                        .getInitialValue(valuesMock,
                                propertyMock.getStandardDetails())
                        .returns(outputValue);

                // Set the computed value.
                valuesMock.expects.setComputedValue(
                        propertyMock, outputValue);

            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Inheriter inheriter = new InheriterImpl(
                initialValueFinderMock);
        inheriter.inherit(valuesMock, inheritableValuesMock);
    }

    /**
     * Test that a property that specifies that it should be inherited picks
     * up the correct value from its parent.
     */
    public void testExplicitlyInherited() {

        // The explicitly inherited property.
        final StylePropertyMock propertyMock =
                PropertiesTestHelper.createStylePropertyMock(
                        expectations, "propertyMock");
        final StyleValue outputValue = StyleKeywords.ACCEPT;
        final StyleInherit inheritValue = STYLE_VALUE_FACTORY.getInherit();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        PropertiesTestHelper.addExpectations(
                expectations, definitionsMock,
                new StylePropertyMock[]{
                    propertyMock,
                });

        expectations.add(new OrderedExpectations() {
            public void add() {

                // Test that explicit inheritance works:
                // -------------------------------------

                // The explicitly inherited property has already been set up to
                // indicate that it automatically inherits.

                // No value was specified.
                valuesMock.expects.getSpecifiedValue(propertyMock)
                        .returns(inheritValue);

                // Get the computed value from the inheritable properties.
                inheritableValuesMock.expects
                        .getStyleValue(propertyMock)
                        .returns(outputValue);

                // Set the computed values.
                valuesMock.expects.setComputedValue(
                        propertyMock, outputValue);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Inheriter inheriter = new InheriterImpl(
                initialValueFinderMock);
        inheriter.inherit(valuesMock, inheritableValuesMock);
    }

    /**
     * Test that a property that has not been set and which is not
     * automatically inherited picks up the initial value properly.
     */
    public void testInitialValueWorks() {

        // The initialised property.
        final StylePropertyMock initialisedPropertyMock =
                PropertiesTestHelper.createStylePropertyMock(
                        expectations, "initialisedPropertyMock", false, false);
        final StyleValue initialisedValue = StyleKeywords.ARMENIAN;

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        PropertiesTestHelper.addExpectations(
                expectations, definitionsMock,
                new StylePropertyMock[]{
                    initialisedPropertyMock
                });

        expectations.add(new OrderedExpectations() {
            public void add() {

                // Test that initial values work.
                // --------------------------------------

                // The initialised property has already been set up to indicate
                // that it does not automatically inherit.

                // No value was specified.
                valuesMock.expects.getSpecifiedValue(initialisedPropertyMock)
                        .returns(null);

                // Get the initial value from the retriever.
                initialValueFinderMock.expects
                        .getInitialValue(valuesMock,
                                initialisedPropertyMock.getStandardDetails())
                        .returns(initialisedValue);

                // Set the computed value.
                valuesMock.expects.setComputedValue(
                        initialisedPropertyMock, initialisedValue);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Inheriter inheriter = new InheriterImpl(
                initialValueFinderMock);
        inheriter.inherit(valuesMock, inheritableValuesMock);
    }

    /**
     * Test that whan a value is specified that it is used to compute the
     * initial value.
     */
    public void testNormalValueWorks() {

        // The specified property.
        final StylePropertyMock specifiedPropertyMock =
                PropertiesTestHelper.createStylePropertyMock(
                        expectations, "specifiedPropertyMock", false, false);

        // The value that was specified.
        final StyleValue specifiedValueMock = StyleKeywords.BASELINE;

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        PropertiesTestHelper.addExpectations(
                expectations, definitionsMock,
                new StylePropertyMock[]{
                    specifiedPropertyMock
                });

        expectations.add(new OrderedExpectations() {
            public void add() {

                // Test that initial values work.
                // --------------------------------------

                // The specified property has already been set up to indicate
                // that it does not automatically inherit.

                // No value was specified.
                valuesMock.expects.getSpecifiedValue(specifiedPropertyMock)
                        .returns(specifiedValueMock);

                // Set the computed value.
                valuesMock.expects.setComputedValue(
                        specifiedPropertyMock,
                        specifiedValueMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Inheriter inheriter = new InheriterImpl(
                initialValueFinderMock);
        inheriter.inherit(valuesMock, inheritableValuesMock);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/11	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/9	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Nov-05	10347/7	pduffin	VBM:2005111405 Made session context create its contents lazily and optimised PseudoStylePath

 18-Nov-05	10347/5	pduffin	VBM:2005111405 Corrected issue with styling

 18-Nov-05	10347/3	pduffin	VBM:2005111405 Removed some unnecessary usages of setSpecifiedValue

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
