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

import com.volantis.mcs.eclipse.ab.actions.ODOMActionCommand;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContextManager;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContextCreator;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContextOperation;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoInfo;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMemento;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMementoOriginator;
import com.volantis.testtools.mocks.MockFile;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Display;

public class PasteActionCommandTestCase extends
        LayoutActionCommandTestAbstract {

    public static final UndoRedoMementoOriginator NULL_UR_MEMENTO_ORIGINATOR =
            new UndoRedoMementoOriginator() {
                public UndoRedoMemento takeSnapshot() {
                    return UndoRedoMemento.NULLOBJ;
                }

                public void restoreSnapshot(UndoRedoInfo undoRedoInfo) {
                }
            };

    private static final String FILE =
            "com/volantis/mcs/eclipse/ab/editors/dom/imageComponent.NOHIST.vic";


    /**
     * Original DOM
     */
    private final static String SINGLE_PANE_RUN =
            "<layoutFormat>" +
            "<deviceLayoutCanvasFormat>" +
            "<paneFormat/>" +
            "</deviceLayoutCanvasFormat>" +
            "</layoutFormat>";

    /**
     * Expected result after cutting the paneFormat element
     */
    private final static String SINGLE_PANE_DELETED_RUN =
            "<layoutFormat>" +
            "<deviceLayoutCanvasFormat>" +
            "<emptyFormat/>" +
            "</deviceLayoutCanvasFormat>" +
            "</layoutFormat>";


    /**
     * Path to the element that will be "cut" from the DOM
     */
    private final static String SINGLE_PANE_PATH =
            "/lpdm:layoutFormat/lpdm:deviceLayoutCanvasFormat/" +
            "lpdm:paneFormat";

    /**
     * Path to the "empty" element
     */
    private final static String EMPTY_ELEMENT_PATH =
            "/lpdm:layoutFormat/lpdm:deviceLayoutCanvasFormat/" +
            "lpdm:emptyFormat";

    /**
     * Create an Editor context for this test. This method produces a
     * ODOMEditorContext that contains one DOM root. That DOM represents
     * the structure in the SINGLE_PANE_RUN constant.
     * @return
     * @throws Exception
     */
    protected ODOMEditorContext createEditorContext() throws Exception {
        ODOMEditorContext context = ODOMEditorContext.
                createODOMEditorContext(null, new MockFile(FILE),
                        NULL_UR_MEMENTO_ORIGINATOR);
        ODOMElement document = createDocument(SINGLE_PANE_RUN);
        context.removeRootElement(context.getRootElement());
        context.addRootElement(document, null);
        return context;
    }

    public void testDummy() {
    }


    /**
     * Test the "Paste" action. This is done by first cutting a known element
     * from the DOM and checking that that the result of the "cut" is correct.
     * Then the cut item is pasted back into the dom. The result of this
     * operation is also checked.
     * @throws Exception
     * Todod fix in new build
     */
    public void notestPasteActionCommand() throws Exception {

        ODOMEditorContextManager manager = new ODOMEditorContextManager(
                new ODOMEditorContextCreator() {
            public ODOMEditorContext create() throws Exception {
                return createEditorContext();
            }
        });

        manager.performOperation(new ODOMEditorContextOperation() {
            public void perform(ODOMEditorContext context) throws Exception {
                // perform a cut to populate the clipboard
                Clipboard clipboard = new Clipboard(Display.getDefault());
                TestData data = createTestData(new CutActionCommand(clipboard,
                        context.getODOMSelectionManager()));
                setDocument(data, context.getRootElement());
                setSelectionManager(data, context.getODOMSelectionManager());
                setSelections(data, new String[]{SINGLE_PANE_PATH});
                // ensure the cut action is applicable
                doTestEnable(data, "test cut enable", true);
                // perform the cut operation
                doTestRun(data, SINGLE_PANE_DELETED_RUN);

                // now perform a paste using the previously cut element
                ODOMActionCommand command = new PasteActionCommand(clipboard,
                        context);
                data = createTestData(command);
                setDocument(data, context.getRootElement());
                setSelectionManager(data, context.getODOMSelectionManager());
                setSelections(data, new String[]{EMPTY_ELEMENT_PATH});

                // ensure the paste operation is applicable
                // this should NOT fire any ODOMSelectionEvents but does fire one.
                // Therefore use the method that allows the number of events to be
                // specified.
                // @todo later Find the source of the extra ODOMSelectionEvent.
                doTestEnable(data, "doTestEnable", true, 1);
                // perform the paste. This should only fire one selection event but is
                // currently firing two. This is a minor problem.
                // @todo later Find the source of the extra ODOMSelectionEvent
                doTestRun(data, SINGLE_PANE_RUN, 2);
            }
        });
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

 26-Aug-04	5294/1	geoff	VBM:2004082405 Reduce unnecessary background threads in testsuite

 26-May-04	4470/5	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/3	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 ===========================================================================
*/
