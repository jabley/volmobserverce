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
package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.ab.editors.dom.LPDMODOMFactory;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;

import java.util.List;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.jdom.Element;

/**
 * This is a manual test for the NodeSelectionDialog.
 */
public class NodeSelectionDialogTest extends ControlsTestAbstract {

    /**
     * Construct a NodeSelectionDialogTest.
     * @param title the title of the test
     */
    public NodeSelectionDialogTest(String title) {
        super(title);
    }

    /**
     * Creates the button and action to show the dialog, and creates the
     * providers for the dialog.
     */
    public void createControl() {
        Button button = new Button(getShell(), SWT.PUSH);
        button.setText("Press me for a node selection dialog");

        // Create a tree for the dialog.
        LPDMODOMFactory factory = new LPDMODOMFactory();

        // Create first subtree
        Element parent1 = factory.element("parent1");
        Element child1a = factory.element("child1a");
        Element child2a = factory.element("child2a");
        parent1.addContent(child2a);
        parent1.addContent(child1a);

        // Create second subtree
        Element parent2 = factory.element("parent2");
        Element child1b = factory.element("child1b");
        Element child2b = factory.element("child2b");
        parent2.addContent(child1b);
        parent2.addContent(child2b);

        // Create the root element and add content
        final ODOMElement rootElement = (ODOMElement) factory.element("root");
        rootElement.addContent(parent2);
        rootElement.addContent(parent1);

        // Create the content provider for the dialog.
        final class TestContentProvider
                implements ITreeContentProvider {

            // javadoc inherited
            public Object[] getChildren(Object parentElement) {
                ODOMElement parent = (ODOMElement) parentElement;
                List children = parent.getChildren();
                return children.toArray();
            }

            // javadoc inherited
            public Object getParent(Object element) {
                ODOMElement odomElement = (ODOMElement) element;
                return odomElement.getParent();
            }

            // javadoc inherited
            public boolean hasChildren(Object element) {
                ODOMElement odomElement = (ODOMElement) element;
                return odomElement.getChildren().size() > 0;
            }

            // javadoc inherited
            public void inputChanged(Viewer v, Object oldInput,
                                     Object newInput) {
            }

            // javadoc inherited
            public void dispose() {
            }

            // Return the tasks as an array of Objects
            public Object[] getElements(Object parent) {
                return rootElement.getChildren().toArray();
            }
        }

        // Create the label provider for the dialog which simply returns
        // the name of an ODOMElement prepended with a message.
        final class TestLabelProvider extends LabelProvider {

            /**
             * Return a null image.
             */
            // rest of javadoc inherited
            public Image getImage(Object element) {
                return null;
            }

            /**
             * Returns the name of the ODOMElement prepended with some text.
             * @param element the element from which to get the text
             * @return the text
             */
            public String getText(Object element) {
                ODOMElement odomElem = (ODOMElement) element;
                return new StringBuffer("Element is ").
                        append(odomElem.getName()).toString();
            }
        }

        // Creates a NodeSelectionDialog
        final NodeSelectionDialog dialog =
                new NodeSelectionDialog(button.getShell(),
                        "Select a node:",
                        rootElement,
                        new TestContentProvider(),
                        new TestLabelProvider());
        dialog.setTitle("This is my node selection tree dialog");

        // Add a listener to report the selections.
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                dialog.open();
                Object[] result = dialog.getResult();
                if (result == null) {
                    System.out.println("You made no selection.");
                } else {
                    ODOMElement node = (ODOMElement) result[0];
                    System.out.println("You chose: " + node.getName());
                }
            }
        });
    }

    /**
     * The description of the tests to carry out
     * and what the expected results are for success.
     * @return
     */
    public String getSuccessCriteria() {
        String msg = "";
        return msg;
    }

    /**
     * The main method must be implemented by the test class writer.
     * @param args not required
     */
    public static void main(String[] args) {
        new NodeSelectionDialogTest("NodeSelectionDialog Test").display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 30-Mar-04	3634/1	pcameron	VBM:2004032210 Added NodeSelectionDialog

 ===========================================================================
*/
