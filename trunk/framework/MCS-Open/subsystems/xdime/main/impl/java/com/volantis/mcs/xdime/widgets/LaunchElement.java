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

import com.volantis.mcs.protocols.widgets.attributes.LaunchAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;

import java.util.Stack;

/**
 * Launch widget element.
 */
public class LaunchElement extends WidgetElement {
       
    /**
     * The launchee element enclosing for this Launch element. 
     */
    private Launchable launchee = null;
    
    /**
     * Creates and returns new instance of LaunchElement, 
     * initalised with empty attributes.
     * @param context
     */
    public LaunchElement(XDIMEContextInternal context) {
        super(WidgetElements.LAUNCH, context);
        
        protocolAttributes = new LaunchAttributes();        
    }    

    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {
               
        // Iterate the stack of XDIME elements from top to bottom,
        // looking for first lauchable element. 
        Stack stack = ((XDIMEContextImpl)context).getStack();
        
        // Start from the top of the stack.
        int elementIndex = stack.size() - 1;
        
        while (elementIndex >= 0) {
            Object element = stack.elementAt(elementIndex);
           
            // Check, if we got launchable element.
            if (element instanceof Launchable) {
                Launchable launchableCandidate = (Launchable)element;
                
                // Store the launchee, so that it could be simply 
                // accessed in callCloseOnProtocol() method.
                launchee = launchableCandidate;
                LaunchAttributes launchAttrs = (LaunchAttributes)protocolAttributes;
                launchAttrs.setWidgetId(launchee.getWidgetId());                                    
                // We have just one launchee, so break here
                break;
            }
            
            elementIndex--;
        }
        
        return super.callOpenOnProtocol(context, attributes);             
    }

    /**
     * Remember attribute in Launchable Element 
     */
    public void callCloseOnProtocol(XDIMEContextInternal context) throws XDIMEException {
        if (launchee != null) {
            launchee.addLaunch((LaunchAttributes)protocolAttributes);
        }
        
        super.callCloseOnProtocol(context);
    }        
}
