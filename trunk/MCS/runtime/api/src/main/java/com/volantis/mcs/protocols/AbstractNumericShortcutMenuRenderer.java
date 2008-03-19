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
package com.volantis.mcs.protocols;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstance;
import com.volantis.mcs.protocols.wml.MenuRendererContext;

/**
 * A class to factor out common code from the various 
 * NumericShortcutMenuRenderers. 
 * <p>
 * I'm not sure if the name of this class is correct as it doesn't seem 
 * particularly tied to rendering of numeric shortcuts, but this was the name 
 * Doug asked me to use before he left, am I am only cleaning up his VBM for 
 * him in his absence, so it'll have to remain like this for now.
 * <p>
 * Perhaps we can rename it in future if necessary.
 */ 
public abstract class AbstractNumericShortcutMenuRenderer 
        extends AbstractMenuRenderer {

    protected AbstractNumericShortcutMenuRenderer(
            MenuRendererContext menuRendererContext, 
            MarinerPageContext pageContext) {
        super(menuRendererContext, pageContext);
    }

    // javadoc inherited
    protected DOMOutputBuffer getOutputBuffer(
            MenuAttributes attributes) throws ProtocolException {
        
        // retrieve the pane information from the menus attriubtes
        Pane pane = attributes.getPane();

        AbstractPaneInstance paneInstance = (AbstractPaneInstance)
                pageContext.getDeviceLayoutContext().getCurrentFormatInstance(
                        pane);
        
        // get hold of and return the buffer

        return (DOMOutputBuffer) paneInstance.getCurrentBuffer();
        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/4	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 29-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 16-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (use generic name for current format index)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 ===========================================================================
*/
