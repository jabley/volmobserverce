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
package com.volantis.styling.impl.counter;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link Counter}.
 *
 * todo: test some failure conditions.
 */
public class CounterTestCase extends TestCaseAbstract {

    /**
     * Test that an explicit reset works.
     */
    public void testReset() {

        Counter counter = new Counter("name");
        {
            counter.reset(0);
            assertEquals(0, counter.value());
        }
        assertEquals(true, counter.endElement());
    }

    /**
     * Test that an increment works - this does an implicit reset.
     */
    public void testIncrement() {

        Counter counter = new Counter("name");
        {
            counter.increment(1);
            assertEquals(1, counter.value());
        }
        assertEquals(true, counter.endElement());
    }

    /**
     * Test that an explicit reset and an increment works.
     */
    public void testResetIncrement() {

        Counter counter = new Counter("name");
        {
            counter.reset(0);
            assertEquals(0, counter.value());
            counter.increment(1);
            assertEquals(1, counter.value());
        }
        assertEquals(true, counter.endElement());
    }

    /**
     * Test a simple nested scenario - an element with a reset and
     * increment containing another element with a reset and increment.
     */
    public void testNestedSimple() {

        Counter counter = new Counter("name");
        {
            counter.reset(0);
            assertEquals(0, counter.value());
            counter.increment(1);
            assertEquals(1, counter.value());
            counter.startElement();
            {
                counter.reset(10);
                assertEquals(10, counter.value());
                counter.increment(2);
                assertEquals(12, counter.value());
            }
            assertEquals(false, counter.endElement());
            assertEquals(1, counter.value());
        }
        assertEquals(true, counter.endElement());
    }

    /**
     * Test a complex nested scenario - combinations of implicit and explicit
     * resets with varuious increments and elements with no counters at all.
     */
    public void testNestedComplex() {

        Counter counter = new Counter("name");
        {
            counter.reset(0);
            assertEquals(0, counter.value());
            counter.increment(1);
            assertEquals(1, counter.value());
            counter.startElement();
            {
                assertEquals(1, counter.value());
                counter.startElement();
                {
                    counter.reset(10);
                    assertEquals(10, counter.value());
                    counter.increment(2);
                    assertEquals(12, counter.value());
                }
                assertEquals(false, counter.endElement());
                assertEquals(1, counter.value());
                counter.increment(2);
                assertEquals(3, counter.value());
                counter.startElement();
                {
                    counter.reset(20);
                    assertEquals(20, counter.value());
                    counter.increment(2);
                    assertEquals(22, counter.value());
                }
                assertEquals(false, counter.endElement());
                assertEquals(3, counter.value());
            }
            assertEquals(false, counter.endElement());
            assertEquals(3, counter.value());
        }
        assertEquals(true, counter.endElement());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9578/1	adrianj	VBM:2005092102 Integrate counters into styling engine

 29-Jul-05	9114/1	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 ===========================================================================
*/
