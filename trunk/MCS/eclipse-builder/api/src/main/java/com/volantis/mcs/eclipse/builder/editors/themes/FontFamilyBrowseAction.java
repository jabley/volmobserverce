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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.builder.editors.themes;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.SelectionDialog;
import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import com.volantis.mcs.eclipse.controls.FontFamilySelectionDialog;

/**
 * This class provides the browse action for Font Family Lists as used by
 * font-family.
 */

public class FontFamilyBrowseAction extends GenericListBrowsActionAbstract {

    public String doBrowse(String value,
                           Composite parent,
                           EditorContext context) {

        String result = value;
                FontFamilySelectionDialog fontFamilySelectionDialog =
                new  FontFamilySelectionDialog(parent.getShell());

        fontFamilySelectionDialog.setSelection(convertValueToArray(value));
        if (fontFamilySelectionDialog.open() == SelectionDialog.OK) {
            result = convertArrayToValue(fontFamilySelectionDialog.getFonts(),
                    ", ");
        }
        return result;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10608/1	ianw	VBM:2005120206 Added browse actions for font-family and mcs-chart-forground-color

 ===========================================================================
*/
