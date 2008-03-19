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
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.PropertyReferenceImpl;
import com.volantis.mcs.protocols.widgets.WidgetDefaultModule;
import com.volantis.mcs.protocols.widgets.attributes.DisplayAttributes;

/**
 * Renderer for PropertyDisplayElement 
 */
public class GenericPropertyDisplayDefaultRenderer extends WidgetDefaultRenderer {

    /**
     * Attributes for the button element to render. 
     */
    private DisplayAttributes propertyDisplayAttributes;
    
    /**
     * The property name.
     */
    private final PropertyName propertyName;

    /**
     * The member names ready to build an instance of PropertyReference.
     */
    private final List referenceMemberNames;

    /**
     * @param actionName
     */
    public GenericPropertyDisplayDefaultRenderer(PropertyName propertyName) {
        super();
        
        this.propertyName = propertyName;
        
        this.referenceMemberNames = new ArrayList();
        this.referenceMemberNames.add(propertyName);
    }
    
    /**
     * Open div element for widget:PropertyDisplay and set unique value for id attribute   
     * @throws ProtocolException 
     */    
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        WidgetDefaultModule module = getWidgetDefaultModule(protocol);
        
        propertyDisplayAttributes = new DisplayAttributes();
        
        propertyDisplayAttributes.copy(attributes);

        if (propertyDisplayAttributes.getId() == null) {
            propertyDisplayAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }
        
        final String widgetId = module.getCurrentWidgetId(propertyName);
        
        propertyDisplayAttributes.setPropertyReference(
                new PropertyReferenceImpl(widgetId, referenceMemberNames));
        
        module.getWidgetRenderer(propertyDisplayAttributes)
            .renderOpen(protocol, propertyDisplayAttributes);
    }

    /**
     * Close div element for widget:PropertyDisplay    
     * @throws ProtocolException 
     */    
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(propertyDisplayAttributes)
            .renderClose(protocol, propertyDisplayAttributes);
    }
}
