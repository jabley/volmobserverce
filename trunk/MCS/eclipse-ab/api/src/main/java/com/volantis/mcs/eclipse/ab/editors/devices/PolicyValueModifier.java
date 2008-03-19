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
package com.volantis.mcs.eclipse.ab.editors.devices;

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Control;
import org.jdom.Element;

/**
 * A PolicyValueModifier has the ability to modify a supplied policy element
 * and return the modified element to the caller. PolicyValueModifiers
 * can also inform registered ModifyListeners when the element has been
 * modified.
 */
public interface PolicyValueModifier {

    /**
     * Gets the current policy element.
     * @return the modified (or otherwise) policy element
     */
    public Element getPolicy();

    /**
     * Sets the policy element whose value can be modified.
     * @param policy the policy element to be modified
     */
    public void setPolicy(Element policy);

    /**
     * Adds a ModifyListener that is called when the policy value has changed.
     * @param listener the listener to add
     */
    public void addModifyListener(ModifyListener listener);

    /**
     * Removes the specified ModifyListener.
     * @param listener the listener to remove
     */
    public void removeModifyListener(ModifyListener listener);

    /**
     * Gets the control used to modify the value of the policy.
     * @return the control used
     */
    public Control getControl();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Mar-04	3398/3	pcameron	VBM:2004030906 Some further renamings

 11-Mar-04	3398/1	pcameron	VBM:2004030906 Renamed PolicyValueOriginSelector and associated classes and added method to PolicyValueModifier interface

 03-Mar-04	3284/2	pcameron	VBM:2004022007 Added TextPolicyValueModifier

 ===========================================================================
*/
