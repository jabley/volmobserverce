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

import com.volantis.mcs.eclipse.common.ProblemMarkerFinder;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.PolicyUtils;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.jdom.Element;

/**
 * A {@link ProblemMarkerFinder} that can be used to find problems associated
 * with a device repository
 */
public class DeviceProblemMarkerFinder implements ProblemMarkerFinder {

    /**
     * Used to access the device repository
     */
    private DeviceRepositoryAccessorManager dram;

    /**
     * Creates a <code>DeviceProblemMarkerFinder</code> instance
     * <p><strong>This problem marker finder expects the XPath that is
     * passed to the {@link #findProblemMarkers} method to be an
     * path to a device element in the device hierarchy document</p></strong>
     * @param dram a <code>DeviceRepositoryAccessorManager</code> that will
     * be used to access the device repository. Cannot be null.
     * @throws IllegalArgumentException if the dram argument is null.
     */
    public DeviceProblemMarkerFinder(DeviceRepositoryAccessorManager dram) {
        if (dram == null) {
            throw new IllegalArgumentException(
                        "DeviceRepositoryAccessorManager cannot be null");
        }
        this.dram = dram;
    }

    // javadoc inerited
    public IMarker[] findProblemMarkers(IResource resource,
                                        XPath hierarchyPath)
                throws CoreException {
        // get the root element of the hierarchy document
        Element hierarchyRoot =
                    dram.getDeviceHierarchyDocument().getRootElement();

        IMarker[] markers = null;
        try {
            // select the device that the hierarchy XPath points to.
            Element heirarchyDevice =
                        (Element) hierarchyPath.selectSingleNode(hierarchyRoot);
            String deviceName = heirarchyDevice.getAttributeValue(
                        DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE);
            // retrieve the markers for the hierarchy, identification and
            // device documents
            IMarker[] hierarchyMarkers = findHierarchyMarkers(resource,
                                                              hierarchyPath);
            IMarker[] identificationMarkers = findIdentificationMarkers(
                        resource, deviceName);
            IMarker[] tacMarkers = findTACIdentificationMarkers(
                        resource, deviceName);
            IMarker[] deviceMarkers = findDeviceMarkers(resource, deviceName);

            int hierarchyCount = hierarchyMarkers.length;
            int identificationCount = identificationMarkers.length;
            int tacCount = tacMarkers.length;
            int deviceCount = deviceMarkers.length;
            // create a new array that will contain all the markers
            markers = new IMarker[hierarchyCount +
                                   tacCount +
                                  identificationCount +
                                  deviceCount];
            // copy the all the markers into the array
            System.arraycopy(hierarchyMarkers,
                             0,
                             markers,
                             0,
                             hierarchyCount);
            System.arraycopy(identificationMarkers,
                             0,
                             markers,
                             hierarchyCount,
                             identificationCount);
            System.arraycopy(tacMarkers,
                             0,
                             markers,
                             hierarchyCount + identificationCount,
                             tacCount);
            System.arraycopy(deviceMarkers,
                             0,
                             markers,
                             hierarchyCount + identificationCount + tacCount,
                             deviceCount);
        } catch (XPathException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
        return markers;
    }

    /**
     * Finds all the <code>IMarkers</code> for the given hierarchy XPath
     * @param resource the associated resource
     * @param hierarchyPath the XPath that points to an element in the
     * hierarchy document
     * @return the Array of IMarkers that match the XPath, or an empty array
     * if there are no matching markers.
     * @throws CoreException if an error occurs
     */
    private IMarker[] findHierarchyMarkers(IResource resource,
                                           XPath hierarchyPath)
                throws CoreException {
        return PolicyUtils.findProblemMarkers(resource,
                                              DeviceRepositorySchemaConstants.
                                                HIERARCHY_ELEMENT_NAME,
                                              hierarchyPath);
    }

    /**
     * Finds all problem markers for the named device that are associated
     * with the identification document
     * @param resource the associate resource
     * @param deviceName the name of the device whose idenfication problem
     * markers are required
     * @return the Array of IMarkers that match, or an empty array
     * if there are no matching markers.
     * @throws CoreException if an error occurs
     */
    private IMarker[] findIdentificationMarkers(IResource resource,
                                                String deviceName)
                throws CoreException {
        Element identificationNode =
                    dram.retrieveDeviceIdentification(deviceName);
        return PolicyUtils.findProblemMarkers(resource,
                                              DeviceRepositorySchemaConstants.
                                                IDENTIFICATION_ELEMENT_NAME,
                                              new XPath(identificationNode));
    }

    /**
     * Finds all problem markers for the named device that are associated
     * with the identification document
     * @param resource the associate resource
     * @param deviceName the name of the device whose idenfication problem
     * markers are required
     * @return the Array of IMarkers that match, or an empty array
     * if there are no matching markers.
     * @throws CoreException if an error occurs
     */
    private IMarker[] findTACIdentificationMarkers(IResource resource,
                                                String deviceName)
                throws CoreException {
        Element identificationNode =
                    dram.retrieveDeviceTACElement(deviceName);
        return PolicyUtils.findProblemMarkers(resource,
                                              DeviceRepositorySchemaConstants.
                                              TAC_IDENTIFICATION_ELEMENT_NAME,
                                              new XPath(identificationNode));
    }

    /**
     * Finds all problem markers for the named device that are associated
     * with the devices policy values
     * @param resource the associate resource
     * @param deviceName the name of the device whose policies problem
     * markers are required
     * @return the Array of IMarkers that match, or an empty array
     * if there are no matching markers.
     * @throws CoreException if an error occurs
     */
    private IMarker[] findDeviceMarkers(IResource resource,
                                        String deviceName)
                throws CoreException {
        Element deviceElement =
                    dram.retrieveDeviceElement(deviceName);
        return PolicyUtils.findProblemMarkers(resource,
                                              deviceName,
                                              new XPath(deviceElement));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Aug-04	5206/2	allan	VBM:2004081201 findTACIdentificationMarkers using wrong element

 11-Aug-04	5126/1	adrian	VBM:2004080303 Added GUI support for Device TACs

 13-May-04	4321/1	doug	VBM:2004051202 Added label decorating to the device hierarchy tree

 ===========================================================================
*/
