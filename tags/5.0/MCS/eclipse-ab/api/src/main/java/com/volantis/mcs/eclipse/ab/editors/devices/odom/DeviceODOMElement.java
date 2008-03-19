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

package com.volantis.mcs.eclipse.ab.editors.devices.odom;

import java.util.ArrayList;
import java.util.List;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * The device ODOM element.
 */
public class DeviceODOMElement extends ODOMElement {

    /**
     * The StandardElementHandler associated with this element.
     */
    private StandardElementHandler standardElementHandler;

    /**
     * A standardElementHandlerList of all the values of the name attributes of the named elements
     * that have had a StandardElementHandler created for them by this
     * DeviceODOMElement.
     */
    private List standardElementHandlerList;

    /**
     * Initializes the new instance using the given parameters.
     * @param name      the element's name
     */
    public DeviceODOMElement(String name) {
        super(name);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name      the element's name
     * @param namespace the element's namespace
     */
    public DeviceODOMElement(String name,
                             Namespace namespace) {
        super(name, namespace);
    }

    /**
     * Initializes the new instance using the given parameters.
     * @param name      the element's name
     * @param prefix    the element's namespace prefix
     * @param uri       the element's namespace URI
     */
    public DeviceODOMElement(String name,
                             String prefix,
                             String uri) {
        super(name, prefix, uri);
    }

    /**
     * Handle the overriding of a device policy.
     * @param policy the ODOMElement that is being overridden.
     */
    public void override(DeviceODOMElement policy) {
        if(standardElementHandler!=null) {
            standardElementHandler.dispose();
            // Get the standard element - there should always be one here
            ODOMElement standardElement =
                    (ODOMElement) this.getChild(
                            DeviceRepositorySchemaConstants.STANDARD_ELEMENT_NAME,
                            getNamespace());


            DeviceODOMElement parent = (DeviceODOMElement) getParent();

            // Detach the current policy element
            detach();

            // Detach the standard element and add it to the end of
            // the new policy.
            standardElement.detach();
            policy.getChildren().add(standardElement);

            // Move the listeners from the current policy to the new.
            moveListeners(policy);

            // Add the new policy.
            parent.addContent(policy);

            // Re-create the standardElementHandler after the policy has
            // been added
            initializeStandardElementHandler(parent);
        }
    }

    /**
     * Getter for the {@link com.volantis.mcs.eclipse.ab.editors.devices.odom.StandardElementHandler}.
     */
    public void restore() {
        if (standardElementHandler != null) {
            standardElementHandler.restore();
        }
    }

    // javadoc inherited
    public Element detach() {
        return super.detach();
    }

    /**
     * Associate this element with a StandardElementHandler.
      * @param handler the StandardElementHandler.
     */
    void setStandardElementHandler(StandardElementHandler handler) {
        if(standardElementHandler!=null) {
            standardElementHandler.dispose();
        }
        this.standardElementHandler = handler;
    }

    /**
     * Provide the value of the name attribute on
     * the "named" element that can have an associated StandardElementHandler.
     * @param nameValue the value of the name attribute on the type of
     * element that can have a StandardElementHandler associated with it.
     */
    public void submitRestorableName(String nameValue) {
        if (!hasRestorableChildren()) {
            throw new IllegalStateException("Element: " + getName() + " does" +
                    " not have any restorable children.");
        }
        if (standardElementHandlerList == null) {
            standardElementHandlerList = new ArrayList();
        }
        if (!standardElementHandlerList.contains(nameValue)) {
            standardElementHandlerList.add(nameValue);
            new StandardElementHandler(this, nameValue);
        }
    }


    /**
     * Determine whether or not this element can have restorable children i.e.
     * child elements that can have an assoicated StandardElementHandler and
     * are thus restorable.
     * @return true if this DeviceODOMElement can have restorable children;
     * false otherwise.
     */
    private boolean hasRestorableChildren() {
        return ((getNamespace().equals(MCSNamespace.DEVICE) &&
                DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME.equals(
                        getName())) ||
                (getNamespace().equals(MCSNamespace.DEVICE_IDENTIFICATION) &&
                DeviceRepositorySchemaConstants.IDENTIFICATION_ELEMENT_NAME.equals(
                        getName())) ||
                (getNamespace().equals(MCSNamespace.DEVICE_TAC_IDENTIFICATION) &&
                DeviceRepositorySchemaConstants.TAC_IDENTIFICATION_ELEMENT_NAME.
                equals(getName())));
    }

    /**
     * Create a new StandardElementHandler instance.
     * @param parent the parent ODOMElement.
     */
    private void initializeStandardElementHandler(ODOMElement parent) {
        standardElementHandler = new StandardElementHandler(parent,
                getAttributeValue("name"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Nov-04	4394/8	allan	VBM:2004051018 Undo/Redo in device editor.

 17-May-04	4394/4	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 14-May-04	4301/3	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 13-May-04	4301/1	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 ===========================================================================
*/
