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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.shared;

import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.PrintStream;

public class SharedContentUsagesImplTestCase
    extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public SharedContentUsagesImplTestCase(String name) {
        super(name);
    }

    protected SharedContentUsagesImpl createSimple() {
        SharedContentUsagesImpl usages = new SharedContentUsagesImpl(6);
        usages.addSharedContentUsage(2);
        usages.addSharedContentUsage(4);
        return usages;
    }

    protected void checkSimple(SharedContentUsagesImpl usages)
        throws Exception {

        assertTrue("Entry number 0 set", !usages.isSharedContentUsed(0));
        assertTrue("Entry number 1 set", !usages.isSharedContentUsed(1));
        assertTrue("Entry number 2 not set", usages.isSharedContentUsed(2));
        assertTrue("Entry number 3 set", !usages.isSharedContentUsed(3));
        assertTrue("Entry number 4 not set", usages.isSharedContentUsed(4));
        assertTrue("Entry number 5 set", !usages.isSharedContentUsed(5));
    }

    public void testInitialise()
        throws Exception {

        SharedContentUsagesImpl usages = createSimple();
        checkSimple(usages);
    }

    public void testErrorChecking()
        throws Exception {

        SharedContentUsagesImpl usages = new SharedContentUsagesImpl(6);
        boolean exceptionThrown;

        exceptionThrown = false;
        try {
            usages.addSharedContentUsage(9);
        } catch (ArrayIndexOutOfBoundsException e) {
            exceptionThrown = true;
        }
        assertTrue("No maximum range check", exceptionThrown);

        exceptionThrown = false;
        try {
            usages.addSharedContentUsage(-4);
        } catch (ArrayIndexOutOfBoundsException e) {
            exceptionThrown = true;
        }
        assertTrue("No minimum range check", exceptionThrown);

        exceptionThrown = false;
        try {
            usages.addSharedContentUsage(6);
        } catch (ArrayIndexOutOfBoundsException e) {
            exceptionThrown = true;
        }
        assertTrue("No border line range check", exceptionThrown);
    }

    public void testChangeSetUndo()
        throws Exception {

        // Create a simple object and check its state.
        SharedContentUsagesImpl usages = createSimple();
        checkSimple(usages);

        // Create and push a change set to record changes.
        SharedContentUsages.ChangeSet changes = usages.createChangeSet();
        SharedContentUsages.ChangeSet oldChangeSet;
        oldChangeSet = usages.pushChangeSet(changes);

        // Change the table.
        usages.addSharedContentUsage(3);
        assertTrue("Entry number 3 not set", usages.isSharedContentUsed(3));

        // Pop the change set and undo any changes.
        usages.popChangeSet(oldChangeSet, true);

        // Test that the object's state is as it was before we made the
        // changes.
        checkSimple(usages);
    }

    // todo: Add some tests to make sure that the change sets are updated properly.

    public static void print(PrintStream out,
                             SharedContentUsages usages) {
        int count = usages.getCount();
        for (int i = 0; i < count; i += 1) {
            out.println("Entry " + i + " is"
                        + (usages.isSharedContentUsed(i) ? "" : " not")
                        + " used");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
