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
package com.volantis.mcs.eclipse.ab.editors.devices;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.jface.util.ListenerList;
import org.jdom.Element;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;

/**
 * Provides an abstract implementation for the <code>PolicyValueModifier</code>
 * interface.
 */
public abstract class AbstractPolicyValueModifier
            implements PolicyValueModifier {

    /**
     * The Element representing a policy that is being modified.
     */
    protected Element policyElement;

    /**
     * Used to listeners that have been registered with this class.
     */
    protected ListenerList listeners;

    /**
     * Initializes a <code>AbstractPolicyValueModifier</code> instance
     */
    public AbstractPolicyValueModifier() {
        listeners = new ListenerList();
    }

    /**
     * Fires a ModifyText event to all registered listeners. The ModifyEvent
     * object that is fired contains the subclasse's control in its widget
     * field, and the actual text in its data field.
     *
     * This method is final to help prevent more complexity being incurred with
     * event notification.
     */
    protected final void fireModifyEvent(Object newValue) {
        Object[] interested = listeners.getListeners();
        if (interested != null && interested.length > 0) {
            Event event = new Event();
            event.data = newValue;
            event.widget = getControl();
            ModifyEvent me = new ModifyEvent(event);
            for (int i = 0; i < interested.length; i++) {
                ((ModifyListener) interested[i]).modifyText(me);
            }
        }
    }

    // javadoc inherited
    public void setPolicy(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("element cannot be null");
        }
        if (!DeviceRepositorySchemaConstants.POLICY_ELEMENT_NAME.equals(
                    element.getName())) {
            throw new IllegalArgumentException(
                        "Expected a policy element but got a '" +
                        element.getName() + "' element");
        }
        // let subclasses handle the new element
        setModifiableElement(element);
    }

    /**
     * This sets the modifiable element that this modifier is operating on.
     * Note this will be either a "policy" element or a "field" element.
     * @param element the modifiable element
     */
    final void setModifiableElement(Element element) {
        this.policyElement = element;
        refreshControl();
    }

    /**
     * Subclasses need to implements this so that the underlying SWT control
     * displays the underlying elements value
     */
    abstract void refreshControl();

    // javadoc inherited
    public Element getPolicy() {
        return policyElement;
    }

    // javadoc inherited
    public void addModifyListener(ModifyListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    // javadoc inherited
    public void removeModifyListener(ModifyListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Apr-04	3975/1	allan	VBM:2004042005 Fix multi-value policy migration and related issues.

 01-Apr-04	3602/3	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 01-Apr-04	3602/1	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 ===========================================================================
*/
