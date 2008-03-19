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
package com.volantis.mcs.eclipse.ab.actions.layout;

import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilterConfiguration;
import com.volantis.mcs.eclipse.ab.actions.ODOMActionCommandTestAbstract;

/**
 * Tests the Swap Action Command
 */
public class SwapActionCommandTestCase extends ODOMActionCommandTestAbstract {

    /**
     * Constant representing a layout before a swap.
     */
    private final static String DUAL_PANE_LAYOUT_BEFORE_SWAP =
            "<layout>" +
            "<canvasLayout>" +
            "<gridFormat rows=\"1\" columns=\"2\">" +
            "<gridFormatColumns>" +
            "<gridFormatColumn />" +
            "<gridFormatColumn />" +
            "</gridFormatColumns>" +
            "<gridFormatRow>" +
            "<paneFormat name=\"leftPane\" />" +
            "<paneFormat name=\"rightPane\" />" +
            "</gridFormatRow>" +
            "</gridFormat>" +
            "</canvasLayout>" +
            "</layout>";

    /**
     * Constant representing a layout after a swap.
     */
    private final static String DUAL_PANE_LAYOUT_AFTER_SWAP =
            "<layout>" +
            "<canvasLayout>" +
            "<gridFormat rows=\"1\" columns=\"2\">" +
            "<gridFormatColumns>" +
            "<gridFormatColumn />" +
            "<gridFormatColumn />" +
            "</gridFormatColumns>" +
            "<gridFormatRow>" +
            "<paneFormat name=\"rightPane\" />" +
            "<paneFormat name=\"leftPane\" />" +
            "</gridFormatRow>" +
            "</gridFormat>" +
            "</canvasLayout>" +
            "</layout>";

    /**
     * Constant representing the XPath to the Grid in the pre-swap layout.
     */
    protected final static String GRID_PATH =
            "/lpdm:layout/lpdm:canvasLayout/" +
            "lpdm:gridFormat";

    /**
     * Constant representing the XPath to the first pane in the pre-swap
     * layout.
     */
    protected final static String GRID_FIRST_CELL_PATH =
            "/lpdm:layout/lpdm:canvasLayout/" +
            "lpdm:gridFormat/lpdm:gridFormatRow/lpdm:paneFormat[1]";

    /**
     * Constant representing the XPath to the second pane in the pre-swap 
     * layout.
     */
    protected final static String GRID_SECOND_CELL_PATH =
            "/lpdm:layout/lpdm:canvasLayout/" +
            "lpdm:gridFormat/lpdm:gridFormatRow/lpdm:paneFormat[2]";


    /**
     * Utility method to create an ODOMSelectionManager
     * @param observe the element to observe
     * @return an ODOMSelectionManager
     */
    protected ODOMSelectionManager createSelectionManager(ODOMElement observe) {
        ODOMSelectionFilter filter = new ODOMSelectionFilter(null, null,
                new ODOMSelectionFilterConfiguration(true, false));
        filter.include(observe);
        return new ODOMSelectionManager(filter);
    }

    /**
     * Test the swap action command. This creates a document performs the swap
     * action on the two elements in it then tests to ensure the two elements
     * have excahnged positions
     * @throws Exception
     */
    public void testRun() throws Exception {
        ODOMElement document = createDocument(DUAL_PANE_LAYOUT_BEFORE_SWAP);
        ODOMSelectionManager selManager = createSelectionManager(document);
        TestData data = createTestData(new SwapActionCommand(selManager));
        setDocument(data, document);
        setSelectionManager(data, selManager);
        setSelections(data, new String[]{GRID_FIRST_CELL_PATH,
                                         GRID_SECOND_CELL_PATH});
        // perform the test.
        doTestRun(data, DUAL_PANE_LAYOUT_AFTER_SWAP);
    }

    /**
     * Test that swap is not enabled when the grid and a containing pane
     * are selected.
     * @throws Exception
     */
    public void testEnableFailure() throws Exception {
        ODOMElement document = createDocument(DUAL_PANE_LAYOUT_BEFORE_SWAP);
        ODOMSelectionManager selManager = createSelectionManager(document);
        TestData data = createTestData(new SwapActionCommand(selManager));
        setDocument(data, document);
        setSelectionManager(data, selManager);
        setSelections(data, new String[]{GRID_PATH, GRID_FIRST_CELL_PATH});
        // perform the test.
        doTestEnable(data, "test swap enable failure", false);
    }

    /**
     * Test that swap is enabled when the two panes are selected.
     * @throws Exception
     */
    public void testEnableSuccess() throws Exception {
        ODOMElement document = createDocument(DUAL_PANE_LAYOUT_BEFORE_SWAP);
        ODOMSelectionManager selManager = createSelectionManager(document);
        TestData data = createTestData(new SwapActionCommand(selManager));
        setDocument(data, document);
        setSelectionManager(data, selManager);
        setSelections(data, new String[]{GRID_SECOND_CELL_PATH,
                                         GRID_FIRST_CELL_PATH});
        // perform the test.
        doTestEnable(data, "test swap enable success", true);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Sep-04	5369/1	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 15-Jul-04	4886/1	allan	VBM:2004052812 Tidied some more and added basic enable tests.

 14-Jul-04	4833/1	tom	VBM:2004052812 Added Swap Functionality

 ===========================================================================
*/
