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

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.SWT;

/**
 *
 */
public class ItemToolTipperTest extends ControlsTestAbstract {

    /**
     * Construct a new TableItemTooltipperTest with the given title.
     */
    public ItemToolTipperTest(String title) {
        super(title);
    }

    public void createControl() {
        Table table = new Table(getShell(), SWT.NONE);

        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        String itemText [] = { "item1", "item2" };

        TableItemContainer ic = new TableItemContainer(table);
        ItemToolTipper tipper = new ItemToolTipper(ic);
        TableItem items [] = new TableItem[itemText.length];
        for(int i=0; i<itemText.length; i++) {
            items[i] = new TableItem(table, SWT.NONE, i);
            items[i].setText(itemText[i]);
            tipper.setToolTipText(items[i], "tt for item" + i);
        }

    }

    public String getSuccessCriteria() {
        return null;
    }


    /**
     * The main method must be implemented by the test class writer.
     * @param args Unused.
     */
    public static void main(String[] args) {
        new ItemToolTipperTest("TableItemTooltipper Test").display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-03	2123/3	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
