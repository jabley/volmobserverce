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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.actions.layout;

import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Display;

/**
 * Tests for the "cut" action command
 */
public class CutActionCommandTestCase extends LayoutActionCommandTestAbstract {

    /**
     * The document we want to cut an element from. (The before)
     */
    private final static String SINGLE_PANE_RUN =
            "<layoutFormat>" +
            "<deviceLayoutCanvasFormat>" +
            "<paneFormat/>" +
            "</deviceLayoutCanvasFormat>" +
            "</layoutFormat>";

    /**
     * The document we expect to get after the cut has been performed. (The
     * after)
     */
    private final static String SINGLE_PANE_DELETED_RUN =
            "<layoutFormat>" +
            "<deviceLayoutCanvasFormat>" +
            "<emptyFormat/>" +
            "</deviceLayoutCanvasFormat>" +
            "</layoutFormat>";


    /**
     *  Path to the element we want to cut from the document
     */
    private final static String SINGLE_PANE_PATH =
            "/lpdm:layoutFormat/lpdm:deviceLayoutCanvasFormat/" +
            "lpdm:paneFormat";


    public void testDummy() {
    }


    /**
     * Test the cut action command. This creates a document performs the cut
     * action on it then tests to ensure the resulting ODOM has an empty element
     * at the cut location.
     * @throws Exception
     * Todo Fix in new build
     */
    public void notestCutActionCommand() throws Exception {
        ODOMElement document = createDocument(SINGLE_PANE_RUN);
        ODOMSelectionManager selManager = createSelectionManager(document);
        Clipboard clipboard = new Clipboard(Display.getDefault());
        TestData data = createTestData(
                new CutActionCommand(clipboard, selManager));

        setDocument(data, document);
        setSelectionManager(data, selManager);
        setSelections(data, new String[]{SINGLE_PANE_PATH});
        // perform the test.
        doTestRun(data, SINGLE_PANE_DELETED_RUN);


    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 26-May-04	4470/7	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/5	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 ===========================================================================
*/
