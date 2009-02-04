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
package com.volantis.mcs.eclipse.builder.editors.common;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jdom.Element;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.policies.PolicyFactory;

/**
 * A content provider that extracts DeviceReferences from an element hierarchy.
 *
 */
public class DeviceTreeContentProvider implements ITreeContentProvider {
    /**
     * Factory to use to create device references.
     */
    private static final PolicyFactory POLICY_FACTORY = PolicyFactory.getDefaultInstance();

    /**
     * The root node for the device hierarchy.
     */
    private Element deviceRoot;

    /**
     * A map associating elements with their corresponding device references.
     */
    private Map elementToDeviceRef = new HashMap();

    /**
     * A map associating device references with their corresponding elements.
     */
    private Map deviceRefToElement = new HashMap();

    // Javadoc inherited
    public Object[] getChildren(Object o) {
        Element element = (Element) deviceRefToElement.get(o);
        List children = element.getChildren();
        Object[] childDeviceRefs = new Object[children.size()];
        for (int i = 0; i < childDeviceRefs.length; i++) {
            childDeviceRefs[i] = elementToDeviceRef.get(children.get(i));
        }
        return childDeviceRefs;
    }

    // Javadoc inherited
    public Object getParent(Object o) {
        Element element = (Element) deviceRefToElement.get(o);
        Object parentRef = null;
        if (element != null) {
            Element parent = element.getParent();
            if (parent != null) {
                parentRef = elementToDeviceRef.get(parent);
            }
        }
        return parentRef;
    }

    // Javadoc inherited
    public boolean hasChildren(Object o) {
        Element element = (Element) deviceRefToElement.get(o);
        return element.getChildren().size() > 0;
    }

    // Javadoc inherited
    public Object[] getElements(Object o) {
        return new Object[] { elementToDeviceRef.get(deviceRoot) };
    }

    // Javadoc inherited
    public void dispose() {
    }

    // Javadoc inherited
    public void inputChanged(Viewer viewer, Object o, Object o1) {
        if (o1 == null) {
            elementToDeviceRef.clear();
            deviceRefToElement.clear();
        } else {
            // Root node should be skipped
            deviceRoot = (Element) ((Element) o1).getChildren().get(0);
            initialiseMaps();
        }
    }

    /**
     * Initialise the maps between element and device name for the current
     * device hierarchy.
     */
    private void initialiseMaps() {
        elementToDeviceRef.clear();
        deviceRefToElement.clear();
        initialiseNode(deviceRoot);
    }

    /**
     * Initialise the maps between element and device name from a specified
     * node, iterating through any children.
     *
     * @param element The node from which to add mappings
     */
    private void initialiseNode(Element element) {
        String deviceName = element.getAttributeValue(DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE);
        DeviceReference deviceRef = POLICY_FACTORY.createDeviceReference(deviceName);
        elementToDeviceRef.put(element, deviceRef);
        deviceRefToElement.put(deviceRef, element);

        Iterator it = element.getChildren().iterator();
        while (it.hasNext()) {
            initialiseNode((Element) it.next());
        }
    }
}
