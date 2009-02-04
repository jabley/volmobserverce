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

import java.util.ArrayList;
import java.util.List;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.ActionReferenceImpl;
import com.volantis.mcs.protocols.widgets.WidgetDefaultModule;
import com.volantis.mcs.protocols.widgets.attributes.ButtonAttributes;

/**
 * Base renderer for specialized buttons  
 */
public class SpecializedButtonBaseRenderer extends WidgetDefaultRenderer {

    /**
     * Attributes for the button element to render. 
     */
    private ButtonAttributes buttonAttributes;
    
    /**
     * The action name.
     */
    private final ActionName actionName;

    private final List referenceMemberNames;
    
    /**
     * @param actionName
     */
    public SpecializedButtonBaseRenderer(ActionName actionName) {
        this.actionName = actionName;
        
        this.referenceMemberNames = new ArrayList();
        this.referenceMemberNames.add(actionName);
    }
    
    /**
     * @throws ProtocolException 
     */    
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        WidgetDefaultModule module = getWidgetDefaultModule(protocol);
        
        buttonAttributes = new ButtonAttributes();
       
        buttonAttributes.copy(attributes);
        
        final String widgetId = module.getCurrentWidgetId(actionName);
        
        buttonAttributes.setActionReference(new ActionReferenceImpl(widgetId, referenceMemberNames));

        module.getWidgetRenderer(buttonAttributes)
            .renderOpen(protocol, buttonAttributes);
    }

    /**
     * @throws ProtocolException 
     */    
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {
        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(buttonAttributes)
            .renderClose(protocol, buttonAttributes);
    }
}
