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

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.widgets.attributes.DismissAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.util.Stack;

/**
 * Dismiss widget element for button to close widgets
 */
public class DismissElement extends WidgetElement {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(DismissElement.class);
    
    public DismissElement(XDIMEContextInternal context) {
        super(WidgetElements.DISMISS, context);
        protocolAttributes = new DismissAttributes();        
    }   
        
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) 
        throws XDIMEException {

        DismissAttributes dismissAttrs = (DismissAttributes)protocolAttributes;

        // Propagate 'type' attribute
        dismissAttrs.setType(attributes.getValue("","type"));
        
        // Iterate the stack of XDIME elements from top to bottom,
        // looking for enclosing dismissable widget
        Dismissable widgetToDismiss = null;
        Stack stack = ((XDIMEContextImpl)context).getStack();
        // Start from the top of the stack.
        int elementIndex = stack.size() - 1;        
        while (elementIndex >= 0) {
            Object element = stack.elementAt(elementIndex);           
            if (element instanceof Dismissable) {
                widgetToDismiss = (Dismissable)element; 
                dismissAttrs.setDismissableId(
                        widgetToDismiss.getDismissableId());
                break;
            }
            elementIndex--;
        }        
        if (null == widgetToDismiss) {
            // This can happen only in case of invalid markup
            throw new XDIMEException(EXCEPTION_LOCALIZER
                    .format("xdime-dismiss-not-in-dismissable"));
        }
    }    
}
