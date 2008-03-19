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

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.PresentableItem;
import com.volantis.mcs.objects.PropertyValueLookUp;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

import java.util.List;

/**
 * This is a manual test for the ComboViewer. Run the test
 * and follow the instructions which describe the successful
 * test criteria.
 */
public class ComboViewerTest extends ControlsTestAbstract {

    /**
     * Construct a ComboViewerTest.
     * @param title the title of the test
     */
    public ComboViewerTest(String title) {
        super(title);
    }

    /**
     * Adds the ComboViewer widget to the shell.
     */
    public void createControl() {
        final String elementName = "imageComponent";
        List elements = PropertyValueLookUp.getDependentElements(elementName);
        PresentableItem [] items = new PresentableItem[elements.size()];
          for (int i = 0; i < elements.size(); i++) {
              String assetName = (String) elements.get(i);
              String resourceName =
                      EclipseCommonMessages.getString("PolicyName." + 
                      assetName);
              items[i] = new PresentableItem(assetName, resourceName);
          }
         final ComboViewer comboViewer =
                new ComboViewer(getShell(), SWT.SIMPLE, items);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        comboViewer.setLayoutData(gridData);
        comboViewer.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                System.out.println("You chose " + comboViewer.getValue());
            }
        });
    }

    /**
     * The description of the tests to carry out for the ComboViewer
     * and what the expected results are for success.
     * @return success criteria
     */
    public String getSuccessCriteria() {
        String msg = "This is a test of the ComboViewer.\n" +
                "The ComboViewer must appear and function correctly\n" +
                "for the test to pass. Choose items from the\n" +
                "Combo control and you should see text printed out.\n" +
                "The text should be the following for each selected " +
                "item:\n" +
                "choosing deviceImageAsset prints  Device Specific Image,\n" +
                "convertibleImageAsset prints Convertible Image,\n" +
                "genericImageAsset prints Generic Image\n\n";


        return msg;
    }

    /**
     * The main method must be implemented by the test class writer.
     * @param args the ComboViewer does not require input arguments.
     */
    public static void main(String[] args) {
        new ComboViewerTest("ComboViewer Test").display();
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 05-Jan-04	2393/3	pcameron	VBM:2004010204 ComboViewer now also uses ModifyListeners

 14-Dec-03	2208/3	allan	VBM:2003121201 Move PresentableItem to eclipse.common

 13-Dec-03	2208/1	allan	VBM:2003121201 Use PresentableItems for presenting attribute values.

 09-Dec-03	2170/1	pcameron	VBM:2003102103 Added AssetTypeSection

 ===========================================================================
*/
