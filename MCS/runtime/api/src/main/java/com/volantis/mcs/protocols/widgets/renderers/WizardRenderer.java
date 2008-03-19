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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.widgets.renderers;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.styling.Styles;

/**
 * Widget renderer implementation for Wizard element.
 * 
 * @mock.generate base="WidgetRenderer" 
 */
public interface WizardRenderer extends WidgetRenderer {

    /**
     * Renders opening of wizard step.
     * 
     * @param xfGroupStyles styles of xf:group element.  
     */
    public void renderOpenStep(VolantisProtocol protocol, Styles xfGroupStyles) throws ProtocolException;

    /**
     * Renders closing of current wizard step.
     */
    public void renderCloseStep(VolantisProtocol protocol) throws ProtocolException;
    
    /**
     * Returns temporary string buffer for storing javascript registration validators 
     * and others just after registration wizard widget
     * 
     * @return StringBuffer
     */
    public StringBuffer getValidatorsBuffer(); 

}
