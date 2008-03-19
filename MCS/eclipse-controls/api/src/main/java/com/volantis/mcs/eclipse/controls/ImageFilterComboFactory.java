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

import org.eclipse.swt.widgets.Composite;

/**
 * Factory to create a Filter combo for image component images
 */
public class ImageFilterComboFactory implements FilterComboFactory {
    
    /**
     * The name of the element that this filtered combo holds extentions for.
     * The properties for the element will be read from 
     * ControlsMessages.properties to populate the combo once it has been 
     * created.
     */
    private static String ELEMENT_NAME = "imageComponent"; //$NON-NLS-1$
    
    /**
     * Create the FilterCombo for image components.
     * @return a FilterCombo populated for image components
     */
    public FilterCombo getFilterCombo( Composite container) {
        return new FilterCombo( container, ELEMENT_NAME );
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 03-Nov-03	1661/4	steve	VBM:2003102410 javadoc and merge fixes

 31-Oct-03	1661/2	steve	VBM:2003102410 Moved messages to ControlsMessages and Factory should not be a singleton

 ===========================================================================
*/
