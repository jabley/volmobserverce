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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoInfo;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMemento;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMementoOriginator;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mocks.MockFile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;

import java.util.Arrays;
import java.util.List;

/**
 * Test case for ODOMEditorContext.
 */
public class ODOMEditorContextTestCase extends TestCaseAbstract {
    private static final String POLICY_FILE =
            "com/volantis/mcs/eclipse/ab/editors/dom/imageComponent.NOHIST.vic";


    public static final UndoRedoMementoOriginator NULL_UR_MEMENTO_ORIGINATOR =
            new UndoRedoMementoOriginator() {
                public UndoRedoMemento takeSnapshot() {
                    return UndoRedoMemento.NULLOBJ;
                }

                public void restoreSnapshot(UndoRedoInfo undoRedoInfo) {
                }
            };

    /**
     * This tests the contructor and that it calls
     * configurePropertiesForRootElement().
     */
    public void testConstructor() throws Exception {

        ODOMEditorContextManager manager = new ODOMEditorContextManager(
                new ODOMEditorContextCreator() {
            public ODOMEditorContext create() throws Exception {
                return new ODOMEditorContext(new MockFile(POLICY_FILE),
                        NULL_UR_MEMENTO_ORIGINATOR,
                                  createRootElement(), null);
            }
        });

        manager.performOperation(new ODOMEditorContextOperation() {
            public void perform(ODOMEditorContext context) {
                // no need to do anything here.
            }
        });
    }

    /**
     * Test createRootElement().
     */
    public void testCreateRootElement() throws Exception {

        ODOMEditorContextManager manager = new ODOMEditorContextManager(
                new ODOMEditorContextCreator() {
            public ODOMEditorContext create() throws Exception {
                return new ODOMEditorContext(createResource(),
                        NULL_UR_MEMENTO_ORIGINATOR,
                        createRootElement(),
                        null);
            }
        });

        manager.performOperation(new ODOMEditorContextOperation() {
            public void perform(ODOMEditorContext context) {
                ODOMElement element = context.getRootElement();

                assertNotNull("Root element is null.", element);

                assertEquals("Incorrect root element name.",
                        "imageComponent", element.getName());
            }
        });
    }

    /**
     * Test resourceExists().
     */
    public void testResourceExists() throws Exception {

        ODOMEditorContextManager manager = new ODOMEditorContextManager(
                new ODOMEditorContextCreator() {
            public ODOMEditorContext create() throws Exception {
                return new ODOMEditorContext(createResource(),
                        NULL_UR_MEMENTO_ORIGINATOR,
                        createRootElement(),
                        null);
            }
        });

        manager.performOperation(new ODOMEditorContextOperation() {
            public void perform(ODOMEditorContext context) {
                assertTrue("Expected resourceExists() to return true.",
                        context.resourceExists());
            }
        });
    }

    /**
     * Test findProblemMarkers.
     */
    public void testFindProblemMarkers() throws Exception {

        final IResource resource = createResource();

        ODOMEditorContextManager manager = new ODOMEditorContextManager(
                new ODOMEditorContextCreator() {
            public ODOMEditorContext create() throws Exception {
                return new ODOMEditorContext(resource,
                        NULL_UR_MEMENTO_ORIGINATOR,
                        createRootElement(),
                        null);
            }
        });

        manager.performOperation(new ODOMEditorContextOperation() {
            public void perform(ODOMEditorContext context) throws Exception {
                XPath root = new XPath("/");
                XPath aPath1 = new XPath("/aone");
                XPath aPath2 = new XPath("/aone/atwo");

                XPath bPath1 = new XPath("/bone");
                XPath bPath2 = new XPath("/bone/btwo");

                IMarker marker1 = resource.createMarker(IMarker.PROBLEM);
                marker1.setAttribute(XPath.class.getName(),
                        aPath2.getExternalForm());
                IMarker marker2 = resource.createMarker(IMarker.PROBLEM);
                marker2.setAttribute(XPath.class.getName(),
                        aPath1.getExternalForm());
                IMarker marker3 = resource.createMarker(IMarker.PROBLEM);
                marker3.setAttribute(XPath.class.getName(),
                        bPath2.getExternalForm());
                IMarker marker4 = resource.createMarker(IMarker.MESSAGE);
                marker4.setAttribute(XPath.class.getName(),
                        bPath1.getExternalForm());

                IMarker markers [];

                markers = context.findProblemMarkers(root);

                List markerList = Arrays.asList(markers);

                assertEquals("There should be 3 markers.", 3, markers.length);
                assertTrue("Expected to find /aone marker",
                        markerList.contains(marker1));
                assertTrue("Expected to find /aone/atwo marker",
                        markerList.contains(marker2));
                assertTrue("Expected to find /bone/btwo marker",
                        markerList.contains(marker3));

                markers = context.findProblemMarkers(aPath2);
                markerList = Arrays.asList(markers);

                assertEquals("There can be only 1 ... marker", 1, markers.length);
                assertTrue("Expected to find /aone/atwo marker",
                        markerList.contains(marker1));

                markers = context.findProblemMarkers(aPath1);
                markerList = Arrays.asList(markers);

                assertEquals("There should be 2 markers.", 2, markers.length);
                assertTrue("Expected to find /aone marker",
                        markerList.contains(marker1));
                assertTrue("Expected to find /aone/atwo marker",
                        markerList.contains(marker2));
            }
        });
    }

    /**
     * Tests the {@link com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext#addRootElement} method.
     * @throws Throwable if an error occurs.
     */
    public void testAddRootElement() throws Throwable {
        // create a root element
        final ODOMElement root = createRootElement();
        final IResource resource = createResource();

        ODOMEditorContextManager manager = new ODOMEditorContextManager(
                new ODOMEditorContextCreator() {
            public ODOMEditorContext create() throws Exception {
                return new ODOMEditorContext(resource,
                        NULL_UR_MEMENTO_ORIGINATOR,
                        root, null);
            }
        });

        manager.performOperation(new ODOMEditorContextOperation() {
            public void perform(ODOMEditorContext context) throws Exception {

                // check that the root element is the one that we are
                // interested in
                assertSame("rootElement is not as expected ",
                           root,
                           context.getRootElement());

                context.removeRootElement(root);

                // provide a new root element
                ODOMElement newRoot = createRootElement();
                // set the new root on the context
                context.addRootElement(newRoot, null);
                // check that the root element is the one that we are
                // interested in
                assertSame("new rootElement is not as expected ",
                           newRoot,
                           context.getRootElement());

                // Check that adding the same root element again does not
                // actually increase the number of root elements.

                // set the new root on the context
                context.addRootElement(newRoot, null);
                // check that the root element is the one that we are
                // interested in
                assertSame("new rootElement is not as expected ",
                           newRoot,
                           context.getRootElement());

                // Check that adding a new different root element does add the
                // root element.
                context.addRootElement(createRootElement(), null);
                try {
                    context.getRootElement();
                    fail("Expected an IllegalStateException because of " +
                            "multiple rootelements.");
                } catch (IllegalStateException e) {
                    // Success.
                }
            }
        });
    }

    /**
     * Factory method for creating a testable IResource instance
     * @return an IResource instance
     */
    private IResource createResource() {
        return new MockFile(POLICY_FILE);
    }

    /**
     * Factory method for creating a root ODOMElement instance
     * @return an ODOMElement instance
     * @throws Exception if an error occurs
     */
    private ODOMElement createRootElement()
                throws Exception  {
        IFile resource = (IFile)createResource();

        return ODOMEditorUtils.createRootElement(resource,
                                                 new LPDMODOMFactory());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Sep-04	5488/3	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 26-Aug-04	5294/1	geoff	VBM:2004082405 Reduce unnecessary background threads in testsuite

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 11-May-04	4272/1	allan	VBM:2004050503 Unique problem markers fix for device editor.

 05-May-04	4115/2	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 27-Apr-04	4016/5	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 27-Apr-04	4016/3	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 16-Apr-04	3743/4	doug	VBM:2004032101 Added a DeviceEditorContext class

 17-Feb-04	2988/3	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 08-Jan-04	2431/3	allan	VBM:2004010404 Fix validation and display update issues.

 07-Jan-04	2447/1	philws	VBM:2004010609 Fix test issues

 17-Dec-03	2219/1	doug	VBM:2003121502 Added dom validation to the eclipse editors

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 01-Dec-03	2067/5	allan	VBM:2003111911 Rework design making ODOMEditorContext immutable.

 01-Dec-03	2067/3	allan	VBM:2003111911 Rework design making ODOMEditorContext immutable.

 29-Nov-03	2067/1	allan	VBM:2003111911 Create ODOMEditorContext.

 ===========================================================================
*/
