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
import com.volantis.mcs.layouts.FormatType;

/**
 * Tests {@link NewNonGridFormatActionCommand}.
 */
public class NewNonGridFormatActionCommandTestCase
        extends FormatTypeBasedActionCommandTestAbstract {

    protected final static String ONE_BY_ONE_GRID =
            "<layout>" +
            "<canvasLayout>" +
            "<gridFormat rows='1' columns='2'>" +
            "<gridFormatColumns>" +
            "<gridFormatColumn/>" +
            "</gridFormatColumns>" +
            "<gridFormatRow>" +
            "<emptyFormat/>" +
            "<formFormat>" +
            "<emptyFormat/>" +
            "</formFormat>" +
            "</gridFormatRow>" +
            "</gridFormat>" +
            "</canvasLayout>" +
            "</layout>";

    protected final static String ONE_BY_ONE_GRID_RUN =
            "<layout>" +
            "<canvasLayout>" +
            "<gridFormat rows='1' columns='2'>" +
            "<gridFormatColumns>" +
            "<gridFormatColumn/>" +
            "</gridFormatColumns>" +
            "<gridFormatRow>" +
            "<paneFormat/>" +
            "<formFormat>" +
            "<emptyFormat/>" +
            "</formFormat>" +
            "</gridFormatRow>" +
            "</gridFormat>" +
            "</canvasLayout>" +
            "</layout>";

    protected final static String GRID_FIRST_CELL_PATH =
            "/lpdm:layout/lpdm:canvasLayout[1]/" +
            "lpdm:gridFormat[1]/lpdm:gridFormatRow[1]/lpdm:emptyFormat";

    protected final static String GRID_SECOND_CELL_PATH =
            "/lpdm:layout/lpdm:canvasLayout[1]/" +
            "lpdm:gridFormat[1]/lpdm:gridFormatRow[1]/lpdm:formFormat";

    protected final static String LAYOUT_PATH =
            "/lpdm:layout";

    protected final static String DEVICE_LAYOUT_PATH =
            "/lpdm:layout/lpdm:canvasLayout[1]";


    /**
     * Check that enable is false when a device layout is selected.
     */
    public void testEnableDeviceLayoutSelection() throws Exception {
        ODOMElement document = createDocument(getBaseDocument());
        ODOMSelectionManager selManager = createSelectionManager(document);
        TestData data = createTestData(
                createCommand(getOKFormatType(), selManager));

        setDocument(data, document);
        setSelectionManager(data, selManager);
        setSelections(data, new String[]{DEVICE_LAYOUT_PATH});

        doTestEnable(data, "testEnableDeviceLayoutSelection", false);

        // Make sure no changes have been made to the document
        assertDocumentsEqual(data, getBaseDocument());
    }

    /**
     * Check that enable is false when a layout and an empty format are
     * selected.
     */
    public void testEnableLayoutSelection() throws Exception {
        ODOMElement document = createDocument(getBaseDocument());
        ODOMSelectionManager selManager = createSelectionManager(document);
        TestData data = createTestData(
                createCommand(getOKFormatType(), selManager));

        setDocument(data, document);
        setSelectionManager(data, selManager);
        setSelections(data, new String[]{GRID_FIRST_CELL_PATH,
                                         DEVICE_LAYOUT_PATH});

        doTestEnable(data, "testEnableLayoutSelection", false);

        // Make sure no changes have been made to the document
        assertDocumentsEqual(data, getBaseDocument());
    }

    /**
     * Checks that enable is false when a multiple selection exists.
     */
    public void testEnableMultiSelect() throws Exception {
        ODOMElement document = createDocument(getBaseDocument());
        ODOMSelectionManager selManager = createSelectionManager(document);
        TestData data = createTestData(
                createCommand(getOKFormatType(), selManager));

        setDocument(data, document);
        setSelectionManager(data, selManager);
        setSelections(data, getBaseMultiSelection());

        doTestEnable(data, "testEnableMultiSelect", false);

        // Make sure no changes have been made to the document
        assertDocumentsEqual(data, getBaseDocument());
    }

    /**
     * Checks that enable is false when a constraint violation is found.
     */
    public void testEnableConstraintViolation() throws Exception {
        ODOMElement document = createDocument(getBaseDocument());
        ODOMSelectionManager selManager = createSelectionManager(document);
        TestData data = createTestData(
                createCommand(getViolationFormatType(), selManager));

        setDocument(data, document);
        setSelectionManager(data, selManager);
        setSelections(data, getBaseSingleSelection());

        doTestEnable(data, "testEnableConstraintViolation", false);

        // Make sure no changes have been made to the document
        assertDocumentsEqual(data, getBaseDocument());
    }

    /**
     * Checks that enable is true when no constraints are violated and there
     * is just a single empty selection.
     */
    public void testEnableOK() throws Exception {
        ODOMElement document = createDocument(getBaseDocument());
        ODOMSelectionManager selManager = createSelectionManager(document);
        TestData data = createTestData(
                createCommand(getOKFormatType(), selManager));

        setDocument(data, document);
        setSelectionManager(data, selManager);
        setSelections(data, getBaseSingleSelection());

        doTestEnable(data, "testEnableOK", true);

        // Make sure no changes have been made to the document
        assertDocumentsEqual(data, getBaseDocument());
    }

    /**
     * Tests that run does the required modification
     */
    public void testRunOK() throws Exception {
        ODOMElement document = createDocument(getBaseDocument());
        ODOMSelectionManager selManager = createSelectionManager(document);
        TestData data = createTestData(
                createCommand(getOKFormatType(), selManager));

        setDocument(data, document);
        setSelectionManager(data, selManager);
        setSelections(data, getBaseSingleSelection());

        doTestRun(data, getModifiedDocument());
    }

    // javadoc inherited
    protected FormatTypeBasedActionCommand createCommand(
            FormatType formatType, ODOMSelectionManager selectionManager) {
        return new NewNonGridFormatActionCommand(formatType, selectionManager);
    }

    /**
     * Returns the base document used for these tests.
     *
     * @return the base document used for the tests
     */
    protected String getBaseDocument() {
        return ONE_BY_ONE_GRID;
    }

    /**
     * Returns an appropriate single selection appropriate to the base
     * document.
     *
     * @return an appropriate single selection for the base document
     */
    protected String[] getBaseSingleSelection() {
        return new String[]{GRID_FIRST_CELL_PATH};
    }

    /**
     * Returns an appropriate multiple selection appropriate to the base
     * document.
     *
     * @return an appropriate multiple selection for the base document
     */
    protected String[] getBaseMultiSelection() {
        return new String[]{GRID_FIRST_CELL_PATH, GRID_SECOND_CELL_PATH};
    }

    /**
     * Returns the document used for successful run tests derived from the
     * base document.
     *
     * @return the modified base document expected after run
     */
    protected String getModifiedDocument() {
        return ONE_BY_ONE_GRID_RUN;
    }

    /**
     * Returns a format type that will generate a violation against the base
     * document, base single selection.
     *
     * @return a format type that generates a constraint violation
     */
    protected FormatType getViolationFormatType() {
        return FormatType.DISSECTING_PANE;
    }

    /**
     * Returns a format type that will allow the action to be run on the
     * base document, base single selection.
     *
     * @return a format type that allows the command to be run
     */
    protected FormatType getOKFormatType() {
        return FormatType.PANE;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 23-Jan-04	2727/1	philws	VBM:2004012301 Fix clipboard management

 21-Jan-04	2635/1	philws	VBM:2003121513 Implement the New Grid and non-Grid Action Commands

 ===========================================================================
*/
