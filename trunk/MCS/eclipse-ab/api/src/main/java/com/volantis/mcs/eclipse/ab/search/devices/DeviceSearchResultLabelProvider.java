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
package com.volantis.mcs.eclipse.ab.search.devices;

import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * @todo javadoc this class
 */
public class DeviceSearchResultLabelProvider extends WorkbenchLabelProvider {
    /**
     * Override decorateText to add the device name to the input text if
     * the input element is a DeviceSearchMatch.
     * @param input the String to decorate
     * @param element the element associated with the text. It is expected
     * that this will be an IContainer in which case no decoration is required
     * or a DeviceSearchMatch in which case the input will be decorated with
     * the device name.
     * @return the label text associated with element
     */
    protected String decorateText(String input, Object element) {
        if(element instanceof DeviceSearchMatch) {
            // todo fix the formatting and garbage creation
            input = input + " - " + ((DeviceSearchMatch)element).getDeviceName();
        }
		return input;
	}
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Oct-04	5557/1	allan	VBM:2004070608 Device search

 ===========================================================================
*/
