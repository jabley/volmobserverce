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
package com.volantis.styling.integration.counter;

import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.impl.counter.CounterEngine;
import com.volantis.synergetics.testtools.TestCaseAbstract;


/**
 * An integration test case for {@link CounterEngine}.
 *
 * todo: extend to support pseudo elements if necessary
 */
public class CounterEngineTestCase extends TestCaseAbstract {

    /**
     * The class under test.
     */
    private CounterEngine engine;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        engine = new CounterEngine();
    }

    /**
     * Test an (implicit reset and) increment.
     */
    public void testImplicit() {

        final String name = "implicit";
        Styles increment =
                StylesBuilder.getStyles("counter-increment: " + name);

        engine.startElement(increment);
        {
            assertEquals(1, counter(name));
            engine.startElement(increment);
            {
                assertEquals(2, counter(name));
            }
            engine.endElement();
            assertEquals(2, counter(name));
        }
        engine.endElement();
    }

    /**
     * Test an explicit reset and increment.
     */
    public void testExplicit() {

        final String name = "explicit";
        Styles reset = StylesBuilder.getStyles("counter-reset: " + name);
        Styles increment = StylesBuilder.getStyles("counter-increment: " + name);

        engine.startElement(reset);
        {
            assertEquals(0, counter(name));
            engine.startElement(increment);
            {
                assertEquals(1, counter(name));
            }
            engine.endElement();
            assertEquals(1, counter(name));
        }
        engine.endElement();
    }

    /**
     * Test a counter going out of scope.
     */
    public void testOutOfScope() {
        final String name = "counter";
        Styles increment =
                StylesBuilder.getStyles("counter-increment: " + name);
        Styles none = StylesBuilder.getEmptyStyles();
        engine.startElement(none);
        {
            assertNull("Counter should be out of scope",
                    engine.getCounter(name, false));
            engine.startElement(increment);
            assertEquals(1, counter(name));
            engine.endElement();
        }
        // At this point the counter should be out of scope again.
        assertNull("Counter should be out of scope",
                engine.getCounter(name, false));
    }

    /**
     * Test a complex-ish nested scenario.
     */
    public void testNested() {

        final String name = "nested";
        Styles reset = StylesBuilder.getStyles("counter-reset: " + name);
        Styles increment =
                StylesBuilder.getStyles("counter-increment: " + name);
        Styles none = StylesBuilder.getEmptyStyles();
        Styles reset2 =
                StylesBuilder.getStyles("counter-reset: " + name + " 2");
        Styles increment2 =
                StylesBuilder.getStyles("counter-increment: " + name + " 2");

        engine.startElement(reset);
        {
            assertEquals(0, counter(name));
            engine.startElement(increment);
            {
                assertEquals(1, counter(name));
                engine.startElement(none);
                {
                    assertEquals(1, counter(name));
                    engine.startElement(reset2);
                    {
                        assertEquals(2, counter(name));
                        engine.startElement(increment2);
                        {
                            assertEquals(4, counter(name));
                            engine.startElement(none);
                            {
                                assertEquals(4, counter(name));
                                assertEquals(new int[] {1, 4},
                                        counterValues(name));
                            }
                            engine.endElement();
                            assertEquals(4, counter(name));
                        }
                        engine.endElement();
                        assertEquals(4, counter(name));
                    }
                    engine.endElement();
                    assertEquals(1, counter(name));
                }
                engine.endElement();
                assertEquals(1, counter(name));
            }
            engine.endElement();
            assertEquals(1, counter(name));
        }
        engine.endElement();
    }

    /**
     * Return the value of the named counter as an int.
     *
     * @param name the name of the counter.
     * @return the integer value of the counter.
     */
    private int counter(String name) {
        return engine.getCounter(name, false).intValue();
    }

    /**
     * Return all in-scope values of the named counter as an int[].
     *
     * @param name the name of the counter.
     * @return the integer value of the counter.
     */
    private int[] counterValues(String name) {
        return engine.getCounterValues(name, false);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9635/1	adrianj	VBM:2005092817 Counter functions for CSS

 28-Sep-05	9487/6	pduffin	VBM:2005091203 Resolved merge conflicts

 28-Sep-05	9487/4	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Sep-05	9578/2	adrianj	VBM:2005092102 Integrate counters into styling engine

 09-Aug-05	9195/1	emma	VBM:2005080510 Refactoring to create StyledDOMTester

 29-Jul-05	9114/1	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 ===========================================================================
*/
