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
package com.volantis.mcs.eclipse.controls;

import java.util.Iterator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

/**
 * A test for the ListValueBuilder.
 */
public class ListValueBuilderTest extends ControlsTestAbstract {

    /**
     * Creates a new ListValueBuilder test.
     * @param title the title of the test
     */
    public ListValueBuilderTest(String title) {
        super(title);
    }

    /**
     * Creates the ListValueBuilder control which uses a TimeSelectionDialog to
     * provide new values for the list.
     */
    public void createControl() {
        TimeSelectionDialog selectionDialog =
                new TimeSelectionDialog(getShell(), null);
        selectionDialog = null;
        // The default ListValueBuilder implementation handles Strings.
        final ListValueBuilder listVB = new ListValueBuilder(getShell(),
                true, // ordered list
                selectionDialog);
        listVB.setItems(new String[]{"peter",
                                     "philip",
                                     "ainsley",
                                     "jean-paul",
                                     "francois",
                                     "smokey",
                                     "tiger"});

        listVB.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                Object[] items = listVB.getItems();
                if (items != null && items.length > 0) {
                    System.out.println("MODIFY event: selection is:");
                    for (int i = 0; i < items.length; i++) {
                        System.out.println("item is " + (String) items[i]);
                    }
                } else {
                    System.out.println("MODIFY event: no selections.");
                }
            }
        });

        listVB.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection =
                        (IStructuredSelection) listVB.getSelection();
                Iterator it = selection.iterator();
                while (it.hasNext()) {
                    System.out.println("SELECTION_CHANGED: " + it.next());
                }
            }
        });

        listVB.setSelection(new StructuredSelection(
                new Object[]{"francois", "jean-paul", "peter"}));

    }

    // javadoc inherited
    public String getSuccessCriteria() {
        return "";
    }

    // javadoc inherited
    public static void main(String[] args) {
        new ListValueBuilderTest("ListValueBuilder").display();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-May-04	4172/2	pcameron	VBM:2004032305 Added SecondaryPatternsSection and refactored ListValueBuilder

 21-Apr-04	3968/3	pcameron	VBM:2004032402 setSelection delegates right to tableviewer

 21-Apr-04	3968/1	pcameron	VBM:2004032402 ListValueBuilder now implements ISelectionProvider

 18-Mar-04	3416/7	pcameron	VBM:2004022309 Added ListBuilder and ListPolicyValueModifier with tests

 ===========================================================================
*/
