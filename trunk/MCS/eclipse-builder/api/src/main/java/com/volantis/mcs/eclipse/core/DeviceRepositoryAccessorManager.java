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
package com.volantis.mcs.eclipse.core;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.devrep.repository.accessors.EclipseDeviceRepository;
import com.volantis.devrep.repository.accessors.MDPRArchiveAccessor;
import com.volantis.mcs.eclipse.common.ObservableProperties;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TransformerMetaFactory;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import org.apache.regexp.RE;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;
import org.jdom.input.JDOMFactory;
import org.xml.sax.XMLFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class manages access to XML device repository. This management
 * includes creating an instance of an XMLDeviceRepository and ensuring that
 * results of requests are cached for performance.
 *
 * The DeviceRepositoryAccessorManager also facilitates access to the
 * localized policy names that are stored within the device repository.
 */
public class DeviceRepositoryAccessorManager {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(
                        DeviceRepositoryAccessorManager.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    DeviceRepositoryAccessorManager.class);

    /**
     * An arbitrary namespace prefix to use with XPath functions.
     */
    private static final String NAMESPACE_PREFIX = "ns";

    /**
     * The resource prefix for categories.
     */
    private static final String CATEGORY_RESOURCE_PREFIX = "category.";

    /**
     * The name of the name property for both policy and categories.
     */
    private static final String NAME_PROPERTY_SUFFIX = ".name";

    /**
     * The name of the name property for both policy and categories.
     */
    private static final String DESCRIPTION_PROPERTY_SUFFIX = ".description";

    /**
     * The XMLDeviceRepositoryAccessor associated with this
     * DeviceRepositoryAccessorManager.
     */
    private final EclipseDeviceRepository accessor;

    /**
     * A cache of device elements.
     */
    private Map deviceElementMap = new HashMap();

    /**
     * Store the name of the deviceRepository file.
     */
    private final String deviceRepositoryName;

    /**
     * Used to create JDOM nodes
     */
    private final JDOMFactory factory;

    /**
     * Maintain a map of policies to categories for performance.
     */
    private Map policyCategories = Collections.synchronizedMap(new HashMap());

    /**
     * The ObservableProperties that wrap the accessors properties
     */
    private ObservableProperties properties;

    /**
     * Construct a new DeviceRepositoryAccessorManager for the specified
     * XML based device repository.
     *
     * <strong>Note this no longer enables validation of xml files in the repository. This
     * change was made to improve the performance of the GUI. Editors or entities that
     * wish to modify the repository should directly use the XMLDeviceRepositoryAccessor
     * and specify validation if they need it.</strong>
     *
     * @param deviceRepository The name of the device repository file (the
     * .mdpr file).
     * @param transformerMetaFactory the meta factory for creating XSL
     *      transforms, used to do automagic upgrades.
     * @param jdomFactory the JDOMFactory for factoring JDOM objects
     * @param isAdminProject flag indicating whether or not there is an
     * Eclipse MCS project that is in admin mode that contains the
     * device repository.
     * @throws IllegalArgumentException If the specified device repository is
     * an illegal file name.
     * @throws java.io.IOException If the specified device repository cannot
     * be read or there was a problem initializing the policies properties
     * bundle.
     * @throws RepositoryException If the underlying XMLDeviceRepositoryAccessor
     * could not be created.
     */
    public DeviceRepositoryAccessorManager(String deviceRepository,
                                           TransformerMetaFactory transformerMetaFactory,
                                           JDOMFactory jdomFactory,
                                           boolean isAdminProject)
            throws IOException, RepositoryException {

        if (deviceRepository == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "deviceRepository");
        }
        if (jdomFactory == null) {
            throw new IllegalArgumentException("factory cannot be null");
        }
        if (!deviceRepository.endsWith(FileExtension.
                DEVICE_REPOSITORY.getExtension())) {
            throw new IllegalArgumentException("Illegal device repository " +
                    "name: \"" + deviceRepository +
                    "\". Name must end with: \"" +
                                               FileExtension.DEVICE_REPOSITORY.getExtension());
        }
        this.factory = jdomFactory;
        File deviceRepositoryFile = new File(deviceRepository);
        if (!deviceRepositoryFile.canRead()) {
            throw new IOException("Cannot read file: " + deviceRepository);
        }

        XMLFilter xmlFilter = isAdminProject ?
                EclipseDeviceRepository.STANDARD_ELEMENT_FILTER : null;

        accessor = new EclipseDeviceRepository(deviceRepository,
                transformerMetaFactory, jdomFactory, false, false, xmlFilter);

        deviceRepositoryName = deviceRepository;
    }


    /**
     * Construct a new DeviceRepositoryAccessorManager for the specified
     * XML based device repository.
     *
     * <strong>Note this no longer enables validation of xml files in the repository. This
     * change was made to improve the performance of the GUI. Editors or entities that
     * wish to modify the repository should directly use the XMLDeviceRepositoryAccessor
     * and specify validation if they need it.</strong>
     *
     * @param deviceRepository The MDPRArchiveAccessor that accesses the
     * device repository to be managed by this DeviceRepositoryAccessorManager
     * @param factory The JDOMFactory for parsing the XML within the
     * device repository.
     * @throws IllegalArgumentException If deviceRepository or factory are
     * null.
     * @throws RepositoryException If the underlying XMLDeviceRepositoryAccessor
     * could not be created.
     */
    public DeviceRepositoryAccessorManager(MDPRArchiveAccessor deviceRepository,
                                           JDOMFactory factory)
            throws RepositoryException {

        if (deviceRepository == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "deviceRepository");
        }
        if (factory == null) {
            throw new IllegalArgumentException("factory cannot be null");
        }
        accessor = new EclipseDeviceRepository(deviceRepository, factory,
                false, false, null);

        deviceRepositoryName = deviceRepository.getArchiveFileName();
        this.factory = factory;
    }

    /**
     * Get the name of the repository file associated with this
     * DeviceRepositoryAccessorManager.
     * @return The name of the device repository file (i.e. the .mdpr file).
     */
    public String getDeviceRepositoryName() {
        return deviceRepositoryName;
    }

    /**
     * Retrieve the name of the root device of the device hierarchy.
     */
    public String retrieveRootDeviceName() {
        String rootDeviceName = null;
        try {
            rootDeviceName = accessor.getRootDeviceName();
        } catch (RepositoryException e) {
            throw new UndeclaredThrowableException(e, e.getMessage());
        }

        return rootDeviceName;
    }

    /**
     * Retrieve the device TAC element associated with a specified device.
     *
     * @param deviceName The name of the device whose TAC element to retrieve.
     * @return The JDOM Element representing the device TAC information for the
     *         specified device or null if the named device has no TACs.
     * @throws IllegalArgumentException If the named device does not exist
     *                                  within the repository.
     */
    public Element retrieveDeviceTACElement(String deviceName)
            throws IllegalArgumentException {
        checkDevice(deviceName);
        return accessor.retrieveTACDeviceElement(deviceName);
    }

    /**
     * Returns the array of TACs for the given device name
     * @param device the name of the device whose TACs are required.
     * Cannot be null
     * @return a String[] representation of the device TACs. If the
     * device does not have any TACs an empty array will be returned
     * @throws RepositoryException if an error occurs.
     */
    public String[] getDeviceTACs(String device) throws RepositoryException {
        Element tacElement = accessor.retrieveTACDeviceElement(device);
        if (tacElement == null) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "tac-device-element-missing",
                            device));
        }
        Namespace namespace = tacElement.getNamespace();
        // get the list of tacs
        List tacNumbers = tacElement.getChildren(
                DeviceRepositorySchemaConstants.NUMBER_ELEMENT_NAME, namespace);
        String[] tacs = new String[tacNumbers.size()];
        int count = 0;
        for (Iterator i = tacNumbers.iterator(); i.hasNext();) {
            Element numberElement = (Element) i.next();
            // add the number to the array
            tacs[count++] = numberElement.getText();
        }
        return tacs;
    }

    /**
     * Sets the TACs for the specified device. Any existing
     * TACs for the device are replaced with the new TACs
     * @param device the name of the device. Cannot be null.
     * @param tacs the array of TACs to set. Cannot be null.
     * @throws RepositoryException if the device does not exist
     * @throws IllegalArgumentException if device or tacs argument is null
     */
    public void setDeviceTACs(String device, String[] tacs)
            throws RepositoryException {
        if (tacs == null) {
            throw new IllegalArgumentException("tacs cannot be null");
        }
        // obtain the tac element for the device.
        Element tacElement =
                accessor.retrieveTACDeviceElement(device);
        if (tacElement == null) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "tac-device-element-missing",
                            device));
        }

        Namespace namespace = tacElement.getNamespace();
        // remove all the existing TAC number elements
        tacElement.removeChildren(DeviceRepositorySchemaConstants.
                NUMBER_ELEMENT_NAME, namespace);

        // add the new TACs
        for (int i = tacs.length - 1; i >= 0; i--) {
            // create a tac number element
            Element number = factory.element(
                    DeviceRepositorySchemaConstants.NUMBER_ELEMENT_NAME,
                    namespace);
            number.setText(tacs[i]);
            tacElement.getChildren().add(0, number);
        }
    }

    /**
     * Retrieve the device identification element associated with a specified
     * device.
     *
     * @param deviceName The name of the device whose identification element to
     *                   retrieve.
     * @return The JDOM Element representing the device identification
     *         information for the specified device or null if the named device
     *         has no identification.
     * @throws IllegalArgumentException If the named device does not exist
     *                                  within the repository.
     */
    public Element retrieveDeviceIdentification(String deviceName)
            throws IllegalArgumentException {
        Element idElement;

        // First check that the specified device is valid.
        checkDevice(deviceName);

        // Now get the identification element for the specified device
        idElement = accessor.retrieveDeviceIdentificationElement(deviceName);

        return idElement;
    }

    /**
     * Returns the array of userAgent patterns for the given device name
     * @param device the name of the device whose userAgent patterns are
     * required. Cannot be null
     * @return a String[] representation of the device userAgent patterns.
     * If the device does not have any userAgent patterns an empty array
     * will be returned
     * @throws RepositoryException if an error occurs.
     * @throws IllegalArgumentException if device argument is null
     */
    public String[] getUserAgentPatterns(String device)
            throws RepositoryException {
        // obtain the pattern element for the device. This will contain
        // both the userAgent and header patterns.
        Element patternElement =
                accessor.retrieveDeviceIdentificationElement(device);
        if (patternElement == null) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "identification-element-missing",
                            device));
        }
        return getPrimaryPatterns(patternElement);
    }

    /**
     * Given a device element from the identification document return the
     * primary patterns assoicated with that device.
     * @param deviceIdentificationElement the device element
     * @return the primary patterns (or user agent patterns) associated with
     * the given deviceIdentificationElement
     */
    private static String[]
            getPrimaryPatterns(Element deviceIdentificationElement) {
        Namespace namespace = deviceIdentificationElement.getNamespace();
        // get the list of userAgent patterns
        List userAgentPatterns = deviceIdentificationElement.getChildren(
                DeviceRepositorySchemaConstants.
                USER_AGENT_PATTERN_ELEMENT_NAME,
                namespace);
        String[] patterns = new String[userAgentPatterns.size()];
        int count = 0;
        for (Iterator i = userAgentPatterns.iterator(); i.hasNext();) {
            // obtain the regularExpression element
            Element regularExpression = ((Element) i.next()).getChild(
                    DeviceRepositorySchemaConstants.
                    REGULAR_EXPRESSION_ELEMENT_NAME,
                    namespace);
            // add the regular expression to the array
            patterns[count++] = regularExpression.getText();
        }
        return patterns;
    }

    /**
     * Sets the userAgent patterns for the specified device. Any existing
     * userAgent patterns for the device are replaced with the new patterns
     * @param device the name of the device. Cannot be null.
     * @param patterns the array of patterns to set. Cannot be null.
     * @throws RepositoryException if the device does not exist
     * @throws IllegalArgumentException if device or patterns argument is null
     */
    public void setUserAgentPatterns(String device, String[] patterns)
            throws RepositoryException {
        if (patterns == null) {
            throw new IllegalArgumentException("patterns cannot be null");
        }
        // obtain the pattern element for the device. This will contain
        // both the userAgent and header patterns.
        Element patternElement =
                accessor.retrieveDeviceIdentificationElement(device);
        if (patternElement == null) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "identification-element-missing",
                            device));
        }
        Namespace namespace = patternElement.getNamespace();
        // remove all the existing userAgent elements
        patternElement.removeChildren(DeviceRepositorySchemaConstants.
                USER_AGENT_PATTERN_ELEMENT_NAME,
                namespace);

        // add the new user agent patterns ensuring that they are added
        // before any other child elements and in the right order
        for (int i = patterns.length - 1; i >= 0; i--) {
            // create a user agent element
            Element userAgent = factory.element(
                    DeviceRepositorySchemaConstants.
                    USER_AGENT_PATTERN_ELEMENT_NAME,
                    namespace);
            // create a regular expression element
            Element regularExpression = factory.element(
                    DeviceRepositorySchemaConstants.
                    REGULAR_EXPRESSION_ELEMENT_NAME,
                    namespace);
            // add the regular expression as a text node to the regular
            // expression element
            regularExpression.addContent(factory.text(patterns[i]));
            // set the regular expression element as a child of the userAgent
            // element.
            userAgent.addContent(regularExpression);

            // add the user agent element to the front of the patternElement
            patternElement.getChildren().add(0, userAgent);
        }
    }

    /**
     * Returns the array of header patterns for the given device name
     * @param device the name of the device whose userAgent patterns are
     * required. Cannot be null
     * @return a DeviceHeaderPattern[] instnace
     * If the device does not have any header patterns an empty array
     * will be returned
     * @throws RepositoryException if an error occurs.
     * @throws IllegalArgumentException if device argument is null
     */
    public DeviceHeaderPattern[] getHeaderPatterns(String device)
            throws RepositoryException {
        // obtain the pattern element for the device. This will contain
        // both the userAgent and header patterns.
        // The header patterns have the following structure
        // <headerPattern name="x" baseDevice="y">
        //   <regularExpression>text()</regularExpression>
        // </headerPattern>
        Element patternElement =
                accessor.retrieveDeviceIdentificationElement(device);
        if (patternElement == null) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "identification-element-missing",
                            device));
        }
        return getSecondaryPatterns(patternElement);
    }


    /**
     * Given a device element from the identification document return the
     * primary patterns assoicated with that device.
     * @param deviceIdentificationElement the device element
     * @return the primary patterns (or header patterns) associated with
     * the given deviceIdentificationElement
     */
    private static DeviceHeaderPattern[]
            getSecondaryPatterns(Element deviceIdentificationElement)
            throws RepositoryException {
        Namespace namespace = deviceIdentificationElement.getNamespace();
        // get the list of header patterns
        List headerPatterns = deviceIdentificationElement.getChildren(
                DeviceRepositorySchemaConstants.
                HEADER_PATTERN_ELEMENT_NAME,
                namespace);
        DeviceHeaderPattern[] patterns =
                new DeviceHeaderPattern[headerPatterns.size()];
        int count = 0;
        for (Iterator i = headerPatterns.iterator(); i.hasNext();) {
            Element headerElement = (Element) i.next();
            String name = headerElement.getAttributeValue(
                    DeviceRepositorySchemaConstants.
                    HEADER_PATTERN_NAME_ATTRIBUTE);
            String baseDevice = headerElement.getAttributeValue(
                    DeviceRepositorySchemaConstants.
                    HEADER_PATTERN_BASE_DEVICE_ATTRIBUTE);
            Element regularExpressionElement = headerElement.getChild(
                    DeviceRepositorySchemaConstants.
                    REGULAR_EXPRESSION_ELEMENT_NAME,
                    namespace);
            if (regularExpressionElement == null) {
                throw new RepositoryException(
                        exceptionLocalizer.format(
                                "element-missing",
                                DeviceRepositorySchemaConstants.
                                REGULAR_EXPRESSION_ELEMENT_NAME));
            }
            String regularExpression = regularExpressionElement.getText();
            // obtain the regularExpression element
            patterns[count++] = new DeviceHeaderPattern(name,
                    regularExpression,
                    baseDevice);
        }
        return patterns;
    }

    /**
     * Sets the userAgent patterns for the specified device. Any existing
     * userAgent patterns for the device are replaced with the new patterns
     * @param device the name of the device. Cannot be null.
     * @param patterns the array of patterns to set. Cannot be null.
     * @throws RepositoryException if the device does not exist
     * @throws IllegalArgumentException if device or patterns argument is null
     */
    public void setHeaderPatterns(String device,
                                  DeviceHeaderPattern[] patterns)
            throws RepositoryException {
        if (patterns == null) {
            throw new IllegalArgumentException("patterns cannot be null");
        }
        // obtain the pattern element for the device. This will contain
        // both the userAgent and header patterns.
        Element patternElement =
                accessor.retrieveDeviceIdentificationElement(device);
        if (patternElement == null) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "identification-element-missing",
                            device));
        }
        Namespace namespace = patternElement.getNamespace();
        // remove all the existing userAgent elements
        patternElement.removeChildren(DeviceRepositorySchemaConstants.
                HEADER_PATTERN_ELEMENT_NAME,
                namespace);

        // add the new header patterns ensuring that they are added
        // before any other child elements and in the right order
        for (int i = patterns.length - 1; i >= 0; i--) {
            DeviceHeaderPattern deviceHeaderPattern = patterns[i];
            // create a header element
            Element header = factory.element(
                    DeviceRepositorySchemaConstants.
                    HEADER_PATTERN_ELEMENT_NAME,
                    namespace);
            header.setAttribute(factory.attribute(
                    DeviceRepositorySchemaConstants.
                    HEADER_PATTERN_NAME_ATTRIBUTE,
                    deviceHeaderPattern.getName()));
            String baseDevice = deviceHeaderPattern.getBaseDevice();
            if (baseDevice != null && baseDevice.length() > 0) {
                header.setAttribute(factory.attribute(
                        DeviceRepositorySchemaConstants.
                        HEADER_PATTERN_BASE_DEVICE_ATTRIBUTE,
                        baseDevice));
            }
            // create a regular expression element
            Element regularExpression = factory.element(
                    DeviceRepositorySchemaConstants.
                    REGULAR_EXPRESSION_ELEMENT_NAME,
                    namespace);
            // add the regular expression as a text node to the regular
            // expression element
            regularExpression.addContent(factory.text(
                    deviceHeaderPattern.getRegularExpression()));
            // set the regular expression element as a child of the header
            // element.
            header.addContent(regularExpression);
            // add the header element to the patternElement
            patternElement.getChildren().add(0, header);
        }
    }

    /**
     * Retrieve a named device as an Element.
     *
     * @param deviceName The name of the device to retrieve.
     * @return The specified device as a JDOM Element.
     * @throws IllegalArgumentException If the named device was not found in
     *                                  the repository.
     */
    public Element retrieveDeviceElement(String deviceName)
            throws IllegalArgumentException {
        Element deviceElement = (Element) deviceElementMap.get(deviceName);
        if (deviceElement == null) {
            checkDevice(deviceName);
            try {
                deviceElement = accessor.retrieveDeviceElement(deviceName);
            } catch (RepositoryException e) {
                // This should never happen since we have called checkDevice().
                // So, just log the exception as an error in case. And
                // throw an IllegalArgumentException.
                logger.error("unexpected-exception", e);
                throw new IllegalArgumentException("Device \"" +
                        deviceName + "\" could not be retrieved from " +
                        " repository \"" + deviceRepositoryName +
                        "\" even though checkDevice() suceeded!");
            }
            deviceElementMap.put(deviceName, deviceElement);
        }

        return deviceElement;
    }

    /**
     * Determine whether or not the named device exists within the
     * device repository.
     * @param deviceName The name of the device whose existance to determine.
     * @return true if the named device exists within the device repository
     * (whose accessor is managed by this DeviceRepositoryAccessorManager);
     * false otherwise.
     */
    public boolean deviceExists(String deviceName) {
        // If the device is already loaded into the cache then we assume it
        // exists in the repository.
        boolean deviceExists = deviceElementMap.get(deviceName) != null;

        if (!deviceExists) {
            deviceExists = accessor.deviceExists(deviceName);
        }

        return deviceExists;
    }


    /**
     * Get the prefix for customer created device names.
     * @return the prefix for a device name that designates that device as
     * a customer created device.
     */
    public String getCustomDeviceNamePrefix() {
        return accessor.getCustomDeviceNamePrefix();
    }

    /**
     * Determine if a device is a standard device (i.e. defined by Volantis)
     * or not.
     * @param deviceName the name of the device to test
     * @return true if the named device is a standard device; otherwise false
     * (note that a return value of false does not indicate that the device
     * exists).
     */
    public boolean isStandardDevice(String deviceName) {
        return accessor.isStandardDevice(deviceName);
    }

    /**
     * Moves the specified device so that it has a new parent in the device
     * hierarchy. Note the new parent should not be a child of the device
     * that is being moved.
     * @param device the name of the device to move. Cannot be null.
     * @param newParentDevice the name of the device that is to be the new
     * parent for the device to be moved
     * @throws RepositoryException if the device or newParentDevice does not
     * exist
     * @throws IllegalArgumentException if the device or newParentDevice
     * arguments are null.
     * @throws org.jdom.IllegalAddException if the new parent device is a
     * descendent of the device being moved.
     */
    public void moveDevice(String device, String newParentDevice)
            throws RepositoryException {
        // delegate directly to the accessor
        accessor.moveDevice(device, newParentDevice);
    }

    public void renameDevice(String device, String newDeviceName)
            throws RepositoryException {
        // Copy the device
        accessor.renameDevice(device, newDeviceName);
        deviceElementMap.remove(device);
    }

    /**
     * Removes the specified device from the repository.
     * @param deviceName the name of the device to be removed. Cannot be null.
     * @throws RepositoryException if an error occurs
     */
    public void removeDevice(String deviceName) throws RepositoryException {
        accessor.removeDevice(deviceName);
        deviceElementMap.remove(deviceName);
    }

    /**
     * Remove a policy from everywhere in the device repository.
     *
     * There is no rollback mechanism in the method.
     *
     * @param policyName the short name of the policy.
     * @throws IllegalArgumentException if the named policy could not be found.
     * @throws RepositoryException if there is a problem clensing the policy
     */
    public void cleansePolicy(String policyName) throws RepositoryException {
        try {
            StringBuffer xPathBuffer = new StringBuffer();
            xPathBuffer.append("//").//$NON-NLS-1$
                    append(MCSNamespace.DEVICE_DEFINITIONS.getPrefix()).
                    append(':').
                    append(DeviceRepositorySchemaConstants.
                    POLICY_ELEMENT_NAME);
            xPathBuffer.append("[@").append(DeviceRepositorySchemaConstants.
                    POLICY_NAME_ATTRIBUTE).append("=\"").append(policyName).
                    append("\"]");
            XPath policyXPath = new XPath(xPathBuffer.toString(),
                    new Namespace[]{MCSNamespace.DEVICE_DEFINITIONS});
            Element policyElement =
                    policyXPath.selectSingleElement(getDeviceDefinitionsDocument().
                    getRootElement());

            if (policyElement == null) {
                throw new IllegalArgumentException("Could not find policy " +
                        policyName);
            }

            // Obtain the master device element from the hierarcrhy,
            xPathBuffer = new StringBuffer();
            xPathBuffer.append("//").//$NON-NLS-1$
                    append(MCSNamespace.DEVICE_HIERARCHY.getPrefix()).
                    append(':').
                    append(DeviceRepositorySchemaConstants.
                    DEVICE_ELEMENT_NAME);
            xPathBuffer.append("[@").append(DeviceRepositorySchemaConstants.
                    DEVICE_NAME_ATTRIBUTE).append("=\"").append(retrieveRootDeviceName()).
                    append("\"]");
            XPath masterXPath = new XPath(xPathBuffer.toString(),
                    new Namespace[]{MCSNamespace.DEVICE_HIERARCHY});
            Element masterElement =
                    masterXPath.selectSingleElement(getDeviceHierarchyDocument().
                    getRootElement());

            // Iterate over all the devices in the repository and remove the
            // policy from any device that includes it.
            removePolicyFromDeviceHierarchical(masterElement, policyName);

            String key = createPropertyKey(policyName, NAME_PROPERTY_SUFFIX);
            getProperties().remove(key);

            // Finally remove the policy from the definitions document.
            policyElement.detach();

        } catch (IllegalArgumentException e) {
            throw new RepositoryException(e);
        } catch (XPathException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * Creates a property key for the given short policy name and suffix.
     * @param policyName the short policy name of interest
     * @param suffix the suffix to use
     * @return the property key
     */
    private String createPropertyKey(String policyName, String suffix) {
        final String policyPrefix =
                EclipseDeviceRepository.getStandardPolicyResourcePrefix();
        StringBuffer keyBuffer = new StringBuffer(policyPrefix.length() +
                policyName.length() + suffix.length());
        keyBuffer.append(policyPrefix).append(policyName).
                append(suffix);

        return keyBuffer.toString();
    }

    /**
     * Recursively remove a named policy from a device and all of its children.
     * @param device the device element <strong>from the hierarchy</strong>
     * from which to remove the policy.
     * @param policyName the name of the policy
     */
    private void removePolicyFromDeviceHierarchical(Element device,
                                                    String policyName) {
        Iterator childDevices = device.getChildren().iterator();
        while (childDevices.hasNext()) {
            Element childDevice = (Element) childDevices.next();
            removePolicyFromDeviceHierarchical(childDevice, policyName);
        }

        Element policyElement = retrievePolicy(device.
                getAttributeValue(DeviceRepositorySchemaConstants.
                DEVICE_NAME_ATTRIBUTE),
                policyName);

        if (policyElement != null) {
            policyElement.detach();
        }
    }

    /**
     * Retrieve a named policy as a JDOM Element from a specified device.
     * @param deviceName The name of the device whose policy value to retrieve.
     * @param policyName The short name of the policy whose value to retrieve.
     * @return The JDOM Element representation of the named policy value for the
     * specified device or null if the policy value is not set in the
     * named device.
     * @throws IllegalStateException If the specified device was not
     * found in the repository.
     */
    public Element retrievePolicy(String deviceName, String policyName) {
        Element deviceElement = retrieveDeviceElement(deviceName);
        Element policies = deviceElement.
                getChild(DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME,
                        deviceElement.getNamespace());

        if (policies == null) {
            throw new IllegalStateException("Device element \"" + deviceName +
                    "\" has no child element named \"" +
                    DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME);
        }

        Element policy = null;
        Iterator iterator = policies.getChildren().iterator();

        while (iterator.hasNext() && policy == null) {
            Element policyElement = (Element) iterator.next();
            String policyElementName = policyElement.getAttributeValue(
                    DeviceRepositorySchemaConstants.POLICY_NAME_ATTRIBUTE);
            if (policyElementName.equals(policyName)) {
                policy = policyElement;
            }
        }

        return policy;
    }

    /**
     * Resolve a named policy as a ResolvedDevicePolicy for a specified device.
     * Resolving a policy will involve traversing the device hierarchy to
     * find the fallback policy should the policy either not be available
     * directly on the specified device, or has an <inherit /> element.
     * @param deviceName The name of the device whose policy value to retrieve.
     * @param policyName The short name of the policy whose value to retrieve.
     * @return The ResolvedDevicePolicy for the named device/policy combination
     * or null if the named policy cannot be found.
     */
    public ResolvedDevicePolicy resolvePolicy(String deviceName,
                                              String policyName) {
        ResolvedDevicePolicy resolvedPolicy = null;
        Element policy = retrievePolicy(deviceName, policyName);
        boolean inherit = false;

        if (policy != null) {
            inherit = !policy.getChildren(
                    DeviceRepositorySchemaConstants.INHERIT_ELEMENT_NAME,
                    policy.getNamespace()).isEmpty();
        }

        if (policy == null || inherit) {
            // If the device is the master device and we still have not
            // resolved the policy then the policy does not exist.
            try {
                if (deviceName.equals(accessor.getRootDeviceName())) {
                    logger.warn("repository-policy-missing",
                                new Object[] {policyName,
                                              deviceRepositoryName});
                } else {
                    // Get the fallback device and try again.
                    String fallback = accessor.getFallbackDeviceName(deviceName);
                    resolvedPolicy = resolvePolicy(fallback, policyName);
                }
            } catch (RepositoryException e) {
                // Panic - could not access the root device.
                throw new UndeclaredThrowableException(e, e.getMessage());
            }
        } else {
            resolvedPolicy = new ResolvedDevicePolicy(deviceName, policy);
        }
        return resolvedPolicy;
    }


    /**
     * Set the name of the device repository being managed by this
     * DeviceRepositoryAccessorManager - i.e. rename the file.
     * @param filename the absolute path of the filename for the repository.
     * @throws IllegalArgumentException if the provided filename would
     * produce an unwriteable repository.
     */
    public void setRepositoryName(String filename) {
        accessor.setRepositoryName(filename);
    }

    /**
     * Get the localized (long) name for a policy.
     * @param policyName The short name of the policy.
     * @return The long name of the specified policy, or the policy name if
     *         there is no long name.
     * @throws IllegalArgumentException if the policyName argument is null
     * or empty.
     */
    public String getLocalizedPolicyName(String policyName) {
        String key = createLocalizedPolicyNameKey(policyName);
        String name = getProperties().getProperty(key);
        return name == null ? policyName : name;
    }

    /**
     * Set the localized (long) name for a policy.
     * @param policyName The short name of the policy. Cannot be null or empty
     * @param localizedName the localized name to set.
     * @throws IllegalArgumentException if the policyName argument is null or
     * empty or if the localizedName argument is null.
     */
    public void setLocalizedPolicyName(String policyName,
                                       String localizedName) {
        if (localizedName == null) {
            throw new IllegalArgumentException("localizedName cannot be null");
        }
        String key = createLocalizedPolicyNameKey(policyName);
        getProperties().setProperty(key, localizedName);
    }

    /**
     * Creates the lookup key for the named policy that allows the policies
     * localized name to be retrieved from the properties file.
     * @param policyName the name of the policy. Cannot be null or empty
     * @return the lookup key.
     * @throws IllegalArgumentException if the policyName argument is null
     * or empty.
     */
    private String createLocalizedPolicyNameKey(String policyName) {
        if (policyName == null || policyName.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "policyName cannot be null or empty");
        }
        return createPropertyKey(policyName, NAME_PROPERTY_SUFFIX);
    }

    /**
     * Get the description for a policy.
     * @param policyName The short name of the policy.
     * @return The description for the specified policy, or the policy name if
     *         there is no description.
     */
    public String getPolicyDescription(String policyName) {
        String desc = getProperties().getProperty(
                createPolicyDescriptionKey(policyName));
        return desc == null ? policyName : desc;
    }

    /**
     * Set the description for a policy.
     * @param policyName The short name of the policy. Cannot be null or empty
     * @param description the description to set. Cannot be null
     * @throws IllegalArgumentException if the policyName argument is null or
     * empty or if the description argument is null.
     */
    public void setPolicyDescription(String policyName, String description) {
        if (description == null) {
            throw new IllegalArgumentException("description cannot be null");
        }
        String key = createPolicyDescriptionKey(policyName);
        getProperties().setProperty(key, description);
    }

    /**
     * Creates the lookup key for the named policy that allows the policies
     * description to be retrieved from the properties file.
     * @param policyName the name of the policy. Cannot be null or empty
     * @return the lookup key.
     * @throws IllegalArgumentException if the policyName argument is null
     * or empty.
     */
    private String createPolicyDescriptionKey(String policyName) {
        if (policyName == null || policyName.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "policyName cannot be null or empty");
        }
        return createPropertyKey(policyName, DESCRIPTION_PROPERTY_SUFFIX);
    }

    /**
     * Get the category of a specified policy.
     * @param policy The name of the policy whose category to retrieve.
     * @return The name of the category containing the specified policy or
     * an empty String if no category was found for the specified policy.
     */
    public String getPolicyCategory(String policy) {
        String categoryName = (String) policyCategories.get(policy);
        if (categoryName == null) {
            Element definitionsRoot =
                    accessor.getDevicePolicyDefinitions().getRootElement();

            // Create an XPath that can be used to retrieve a specific policy
            // based on the value of its name attribute.
            StringBuffer buffer = new StringBuffer("//").append(NAMESPACE_PREFIX);
            buffer.append(':')
                    .append(DeviceRepositorySchemaConstants.POLICY_ELEMENT_NAME)
                    .append("[@name=\"").append(policy).append("\"]");

            XPath xpath = new XPath(buffer.toString(),
                    new Namespace[]{Namespace.getNamespace(
                            NAMESPACE_PREFIX,
                            definitionsRoot.getNamespaceURI())
                    });

            Element policyElement = null;
            try {
                policyElement =
                        (Element) xpath.selectSingleNode(definitionsRoot);
            } catch (XPathException e) {
                throw new IllegalArgumentException("Cannot find policy " +
                        "element named " + policy);
            }

            if (policyElement != null) {
                categoryName = policyElement.getParent().
                        getAttributeValue("name");
                policyCategories.put(policy, categoryName);
            }
        }

        return categoryName;
    }

    /**
     * Get a device element from the device hierarchy document.
     *
     * @param deviceName the device name to get
     * @return the named device element if found, or null if not found.
     */
    public Element getHierarchyDeviceElement(String deviceName) {
        return accessor.getHierarchyDeviceElement(deviceName);
    }

    /**
     * Get the localized name for a category.
     * @param category the category
     * @return the localized name for the specified category or category if
     * no localization was available.
     */
    public String getLocalizedPolicyCategory(String category) {
        StringBuffer property = new StringBuffer(
                CATEGORY_RESOURCE_PREFIX.length() +
                category.length() + NAME_PROPERTY_SUFFIX.length());
        property.append(CATEGORY_RESOURCE_PREFIX).append(category).
                append(NAME_PROPERTY_SUFFIX);
        String localized = getProperties().getProperty(property.toString());
        return localized == null ? category : localized;
    }

    /**
     * Check that a specifed device exists within the repository and throw an
     * IllegalArgumentException if it does not.
     *
     * @param deviceName The name of the device to check.
     * @throws IllegalArgumentException If the named device does not exist
     *                                  within the repository.
     */
    private void checkDevice(String deviceName)
            throws IllegalArgumentException {
        if (!deviceExists(deviceName)) {
            throw new IllegalArgumentException("Device \"" + deviceName +
                    "\" was not found in device repository: " +
                    deviceRepositoryName);
        }
    }

    /**
     * Finds the originating device for the specified device and policy.
     * @param deviceName The name of the device whose policy value to retrieve.
     * @param policyName The short name of the policy whose value to retrieve.
     * @return the name of the device for the named device/policy combination
     * or null if no such combination can be found.
     */
    public String getOriginatingDevice(String deviceName,
                                       String policyName) {
        ResolvedDevicePolicy rdp = resolvePolicy(deviceName, policyName);
        String origDevice = null;
        if (rdp != null) {
            origDevice = rdp.deviceName;
        }
        return origDevice;
    }

    /**
     * Return the fallback device name for the specified device. If none is
     * found return null.
     *
     * @param deviceName the device name
     * @return the fallback device name for the specified device. If none is
     *         found return null.
     */
    public String getFallbackDeviceName(String deviceName) {
        return accessor.getFallbackDeviceName(deviceName);
    }

    /**
     * Provide access to the device hierarchy document.
     * <p><strong>The document returned is <b>live</b> any changes to this
     * document will result in changes the device repository</strong></p>
     * @return The JDOM Document that represents device hierarchy.
     */
    public Document getDeviceHierarchyDocument() {
        return accessor.getDeviceHierarchyDocument();
    }

    /**
     * Provide access to the device identification document.
     * <p><strong>The document returned is <b>live</b> any changes to this
     * document will result in changes the device repository</strong></p>
     * @return The JDOM Document for the device identification markup.
     */
    public Document getDeviceIdentificationDocument() {
        return accessor.getDeviceIdentificationDocument();
    }

    /**
     * Provide access to the device identification document.
     * <p><strong>The document returned is <b>live</b> any changes to this
     * document will result in changes the device repository</strong></p>
     * @return The JDOM Document for the device TAC identification markup, or
     * null if no TAC identification document exists.
     */
    public Document getDeviceTACIdentificationDocument() {
        return accessor.getDeviceTACIdentificationDocument();
    }

    /**
     * Provide access to the device definitions document.
     *
     * <p><strong>The document returned is <b>live</b> any changes to this
     * document will result in changes the device repository</strong></p>
     * @return The JDOM Document that represents device defnitions.
     */
    public Document getDeviceDefinitionsDocument() {
        return accessor.getDevicePolicyDefinitions();
    }

    /**
     * Gets the type element for the given policy definition.
     * @param policyName the name of the policy
     * @return the type element of the named policy
     */
    public Element getTypeDefinitionElement(String policyName) {
        Document definitionsDoc = this.accessor.getDevicePolicyDefinitions();
        Element rootElement = definitionsDoc.getRootElement();
        Element policyTypeElement = null;
        // create an xpath to the type element that we are interested in.
        // Note, we use a hardcoded namespace prefix of "ns", this prefix
        // will be bound to the namespace URI that the definitions document
        // uses.
        StringBuffer buffer = new StringBuffer("/ns:definitions/ns:category").
                append("/ns:policy[@name=\"").
                append(policyName).
                append("\"]/ns:type");
        // bind the "ns" prefix to the rootElements namespace.
        Namespace[] namespaces = new Namespace[]{
            Namespace.getNamespace("ns", rootElement.getNamespaceURI())
        };
        XPath xpath = new XPath(buffer.toString(), namespaces);
        try {
            policyTypeElement = xpath.selectSingleElement(rootElement);
            // see if the type element has a "ref" attribute
            if (policyTypeElement != null) {
                String ref = policyTypeElement.getAttributeValue(
                        DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_REF_ATTRIBUTE_NAME);
                // if the type element has a ref attribute then the attributes
                // value, references a named type whose definition can be
                // obtainded from the /definitions/types element.
                if (ref != null) {
                    // this is referencing a type that is defined in the
                    // types element
                    buffer = new StringBuffer();
                    buffer.append("/ns:definitions/ns:types/ns:type[@name=\"")
                            .append(ref)
                            .append("\"]");
                    xpath = new XPath(buffer.toString(), namespaces);
                    policyTypeElement = xpath.selectSingleElement(rootElement);
                }
            }
        } catch (XPathException e) {
            // There was a problem retrieving the type element.
            throw new UndeclaredThrowableException(e, e.getMessage());
        }
        return policyTypeElement;
    }

    /**
     * Gets an iterator which iterates over all available policy names, across
     * all categories.
     * @return a policy name iterator
     * @throws RepositoryException if there were problems retrieving
     * information from the device repository
     */
    public Iterator policyNamesIterator() throws RepositoryException {

        /**
         * The PolicyNameIterator class for returning policy names.
         */
        final class PolicyNameIterator implements Iterator {

            /**
             * The iterator used for category elements.
             */
            private final Iterator categoryElementIterator;

            /**
             * The iterator used for policy elements.
             */
            private Iterator categoryPolicyNameIterator;

            /**
             * Constructs a new PolicyNameIterator for the specified list of
             * category elements.
             * @param categoryList the list of category elements. Cannot be
             * null or empty.
             * @throws IllegalArgumentException if categoryList is null or
             * empty.
             * @throws RepositoryException if there are problems retrieving
             * information from the device repository.
             */
            public PolicyNameIterator(List categoryList)
                    throws RepositoryException {
                if (categoryList == null) {
                    throw new IllegalArgumentException("Cannot be null: " +
                            "categoryElementIterator.");
                }
                if (categoryList.isEmpty()) {
                    throw new IllegalArgumentException("There are no " +
                            "categories in " +
                            "categoryList.");
                }

                this.categoryElementIterator = categoryList.iterator();

                // Move to first category
                moveToNextCategory();
            }

            /**
             * Moves to the next category, updating the policy element
             * iterator to iterate over the next category's policy elements.
             */
            private void moveToNextCategory() throws RepositoryException {
                Element categoryElement =
                        (Element) categoryElementIterator.next();
                categoryPolicyNameIterator =
                        categoryPolicyNamesIterator(categoryElement.
                        getAttributeValue(
                                DeviceRepositorySchemaConstants.
                        CATEGORY_NAME_ATTRIBUTE));
            }

            // javadoc inherited
            public boolean hasNext() {
                while (!categoryPolicyNameIterator.hasNext() &&
                        categoryElementIterator.hasNext()) {
                    try {
                        moveToNextCategory();
                    } catch (RepositoryException e) {
                        throw new UndeclaredThrowableException(e);
                    }
                }
                return categoryPolicyNameIterator.hasNext();
            }

            /**
             * Get the next policy name.
             * @return the policy name
             */
            public Object next() {
                // Move to next category if current category is "exhausted" and
                // there is a next category, or move to the first category.
                if (!categoryPolicyNameIterator.hasNext() &&
                        categoryElementIterator.hasNext()) {
                    try {
                        moveToNextCategory();
                    } catch (RepositoryException e) {
                        throw new UndeclaredThrowableException(e);
                    }
                }
                // Gets the next policy name, or throws a
                // NoSuchElementException if there is no next policy name.
                String policyName = (String) categoryPolicyNameIterator.next();

                return policyName;
            }

            /**
             * Cannot remove policy names.
             * @throws UnsupportedOperationException always.
             */
            public void remove() {
                throw new UnsupportedOperationException("Remove not supported.");
            }
        }

        // Retrieve the definitions document and root element.
        Document definitionsDoc = this.accessor.getDevicePolicyDefinitions();
        Element rootElement = definitionsDoc.getRootElement();

        // Create the category element filter used by the PolicyNameIterator.
        final ElementFilter categoryElementFilter =
                new ElementFilter(
                        DeviceRepositorySchemaConstants.CATEGORY_ELEMENT_NAME,
                        rootElement.getNamespace());

        // Get the category element content, if any.
        List categoryList =
                rootElement.getContent(categoryElementFilter);

        // Create and return the PolicyName iterator
        return new PolicyNameIterator(categoryList);
    }

    /**
     * Returns an ObservableProperties that allows clients to listen for
     * device property changes
     * @return an ObservableProperties instance
     */
    public ObservableProperties getProperties() {
        if (properties == null) {
            properties = new ObservableProperties(accessor.getProperties());
        }
        return properties;
    }

    /**
     * Write out the "changes".  We assume that any device we have retrieved
     * has been modified.  This safe to assume as the versioning is handled at
     * the gui control level.
     * @throws RepositoryException if there was a problem writing the
     * repository.
     */
    public void writeRepository() throws RepositoryException {
        accessor.writeHierarchy();
        accessor.writeIdentifiers();
        accessor.writeTACs();
        accessor.writeDefinitions();
        accessor.writeProperties();
        accessor.writeDeviceElements(deviceElementMap);
        accessor.saveRepositoryArchive();
        ProjectDeviceRepositoryProvider.getSingleton().
                deviceRepositoryModified(this);
    }

    /**
     * Gets an iterator which iterates over all available policy names in the
     * given category.
     * @param categoryName the name of the category of interest
     * @return an iterator
     * @throws IllegalArgumentException if categoryName is null or empty, or
     * if there is no category element for the named category
     * @throws RepositoryException if the policy elements of the category
     * cannot be retrieved.
     */
    public Iterator categoryPolicyNamesIterator(String categoryName)
            throws RepositoryException {
        if (categoryName == null || categoryName.length() == 0) {
            throw new IllegalArgumentException("Cannot be null nor empty: " +
                    categoryName);
        }
        // Retrieve the definitions document and root element.
        Document definitionsDoc = this.accessor.getDevicePolicyDefinitions();
        Element rootElement = definitionsDoc.getRootElement();

        // Create an XPath that can be used to retrieve a specific category
        // based on the value of its name attribute.
        StringBuffer categoryBuffer = new StringBuffer(NAMESPACE_PREFIX);
        categoryBuffer.append(':')
                .append(DeviceRepositorySchemaConstants.CATEGORY_ELEMENT_NAME)
                .append("[@name=\"").append(categoryName).append("\"]");
        XPath xpath = new XPath(categoryBuffer.toString(),
                new Namespace[]{Namespace.getNamespace(
                        NAMESPACE_PREFIX,
                        rootElement.getNamespaceURI())
                });

        Element categoryElement = null;
        try {
            categoryElement =
                    (Element) xpath.selectSingleNode(rootElement);
        } catch (XPathException e) {
            throw new IllegalArgumentException("Cannot find category " +
                    "named " + categoryName);
        }

        // Create the policy element filter which retrieves all policy
        // element children of the category element.
        final ElementFilter policyElementFilter =
                new ElementFilter(
                        DeviceRepositorySchemaConstants.POLICY_ELEMENT_NAME,
                        rootElement.getNamespace());

        // Get an iterator for the category element's policy element content.
        final Iterator policyElementIterator =
                categoryElement.getContent(policyElementFilter).iterator();


        // Create and return an anonymous iterator which uses the
        // policyElementIterator to iterate over the policy elements and
        // returns the policy names.
        return new Iterator() {
            public boolean hasNext() {
                return policyElementIterator.hasNext();
            }

            public Object next() {
                Element policyElement = (Element) policyElementIterator.next();
                return policyElement.getAttributeValue(
                        DeviceRepositorySchemaConstants.POLICY_NAME_ATTRIBUTE);
            }

            public void remove() {
                throw new UnsupportedOperationException("remove is not " +
                        "supported");
            }
        };
    }

    /**
     * Create the device from the fallback device name and and the device name.
     *
     * @param fallbackDeviceName the fallback device name.
     * @param deviceName         the device name
     * @exception RepositoryException if any Repository related exception
     *                                occurs.
     */
    public void createDevice(String fallbackDeviceName, String deviceName)
            throws RepositoryException {
        accessor.addDeviceElement(deviceName);
        accessor.addIdentifiersDeviceElement(deviceName);
        accessor.addTACDeviceElement(deviceName);
        // todo The DeviceHierarchySection listens for device updates to the
        // hierarchy document. When an update occurs it requires the device
        // to have an entry in the identification document. To work around
        // this issue we must create the entry in the hierarchy document last.
        // This is extremely nasty and needs to be addressed.
        accessor.addHierarchyDeviceElement(deviceName, fallbackDeviceName);
    }

    /**
     * Gets the revision of the device repository.
     * @return the revision
     */
    public String getRevision() {
        return accessor.getRevision();
    }

    /**
     * Gets the version of the device repository.
     * @return the version
     */
    public String getVersion() {
        return accessor.getVersion();
    }

    /**
     * Checks whether the given named policy already exists.
     * @param policyName the policy of interest. Cannot be null.
     * @return true if the policy already exists; false otherwise
     * @throws IllegalArgumentException if policyName is null
     */
    public boolean policyExists(String policyName) {
        if (policyName == null) {
            throw new IllegalArgumentException("Cannot be null: policyName");
        }
        // If a policy already exists then it must have a type.
        return this.getTypeDefinitionElement(policyName) != null;
    }

    /**
     * Given a regular expression this method will select the names of all the
     * devices that match that expression.
     *
     * @param expression The regular expression to match.
     *
     * @return Am array of the device names that match the expression provided.
     */
    public String[] selectHierarchyDevices(RE expression) {
        return selectHierarchyDevices(getDeviceHierarchyDocument(), expression);
    }

    /**
     * Given a regular expression, the device repository to use and an
     * appropriate factory for creating XML documents, this method will
     * select the names of all the devices that match that expression.
     * <p>
     * This method is static so that it can be called independently of any
     * context within this class and it does not rely on it at all.  It
     * selects devices from the repository named.
     *
     * @param deviceRepository The device repository to check the devices
     *                         from against the expression provided.
     * @param transformerMetaFactory the meta factory for creating XSL
     *      transforms, used to do automagic upgrades.
     * @param jdomFactory          A means of creating XML documents.
     * @param expression       The regular expression to match.
     *
     * @return Am array of the device names that match the expression provided.
     */
    public static String[]
            selectHierarchyDevices(String deviceRepository,
                                   TransformerMetaFactory transformerMetaFactory,
                                   JDOMFactory jdomFactory, RE expression)
            throws RepositoryException {

        Document deviceHierarchy = EclipseDeviceRepository.
                getDeviceHierarchyDocument(deviceRepository,
                        transformerMetaFactory, jdomFactory);
        return selectHierarchyDevices(deviceHierarchy, expression);
    }

    /**
     * Given a suitable device hierarchy as an XML document and a regular
     * expression, this method will extract all named devices from the
     * document and then return those that match the regular expression
     * provided.
     *
     * @param deviceHierarchy An XML representation of a device hierarchy.
     * @param expression      The regular expression to match.
     *
     * @return Am array of the device names that match the expression provided.
     */
    private static String[] selectHierarchyDevices(Document deviceHierarchy,
                                                   RE expression) {
        // Validate the parameters
        if (deviceHierarchy == null) {
            throw new IllegalArgumentException(
                    "Cannot be null: deviceHierarchy");
        }

        if (expression == null) {
            throw new IllegalArgumentException("Cannot be null: expression");
        }

        Element root = deviceHierarchy.getRootElement();

        // Create the XPath - select all device nodes who have a name attribute
        StringBuffer xpathBuffer = new StringBuffer();
        xpathBuffer.append("//").
                append(MCSNamespace.DEVICE_HIERARCHY.getPrefix()).
                append(':').
                append("device[@name]");  //$NON-NLS-1$
        XPath xpath = new XPath(xpathBuffer.toString(),
                new Namespace[]{MCSNamespace.DEVICE_HIERARCHY});

        List matches = new ArrayList();

        try {
            // Apply the XPath expression to the root of the document provided
            List nodes = xpath.selectNodes(root);

            if (nodes != null) {
                // Iterate over any nodes selected by the XPath
                for (Iterator i = nodes.iterator(); i.hasNext(); /**/) {
                    Object current = i.next();

                    if (current instanceof ODOMElement) {
                        ODOMElement element = (ODOMElement) current;
                        Attribute name = element.getAttribute(
                                DeviceRepositorySchemaConstants.
                                DEVICE_NAME_ATTRIBUTE);
                        String value = name.getValue();

                        // Check the device name against the regular expression
                        // provided
                        if (expression.match(value)) {
                            matches.add(value);
                        }
                    }
                }
            }
        } catch (XPathException xpe) {
            // There was a problem selecting the nodes
            throw new UndeclaredThrowableException(xpe, xpe.getMessage());
        }

        // Needed to force the cast on the array to work hence matchArray
        String[] matchArray = new String[matches.size()];
        return (String[]) matches.toArray(matchArray);
    }


    /**
     * Given a regular expression this method will select the names of all the
     * devices that have identifier patterns matching that expression.
     *
     * @param expression The regular expression to match.
     * @return An array of the device names that have identifier patterns
     * matching the expression provided.
     */
    public String[] selectIdentityDevices(RE expression)
            throws RepositoryException {
        return selectIdentityDevices(getDeviceIdentificationDocument(),
                expression);
    }

    /**
     * Given a regular expression, the device repository to use and an
     * appropriate factory for creating XML documents, this method will
     * select the names of all the
     * devices that have identifier patterns matching that expression.
     * <p>
     * This method is static so that it can be called independently of any
     * context within this class and it does not rely on it at all.  It
     * selects devices from the repository named.
     *
     * @param deviceRepository The device repository to check the devices
     *                         from against the expression provided.
     * @param transformerMetaFactory the meta factory for creating XSL
     *      transforms, used to do automagic upgrades.
     * @param jdomFactory          A means of creating XML documents.
     * @param expression       The regular expression to match.
     *
     * @return Am array of the device names that match the expression provided.
     */
    public static String[]
            selectIdentityDevices(String deviceRepository,
                                  TransformerMetaFactory transformerMetaFactory,
                                  JDOMFactory jdomFactory, RE expression)
            throws RepositoryException {

        Document identityDocument = EclipseDeviceRepository.
                getDeviceIdentificationDocument(deviceRepository,
                        transformerMetaFactory, jdomFactory);
        return selectIdentityDevices(identityDocument, expression);
    }

    /**
     * Given a suitable device hierarchy as an XML document and a regular
     * expression, this method will extract all named devices from the
     * document and then return those that match the regular expression
     * provided.
     *
     * @param identityDocument An XML representation of a device hierarchy.
     * @param expression      The regular expression to match.
     *
     * @return Am array of the device names that match the expression provided.
     */
    private static String[] selectIdentityDevices(Document identityDocument,
                                                  RE expression)
            throws RepositoryException {
        // Validate the parameters
        if (identityDocument == null) {
            throw new IllegalArgumentException(
                    "Cannot be null: identityDocument");
        }

        if (expression == null) {
            throw new IllegalArgumentException("Cannot be null: expression");
        }

        Element root = identityDocument.getRootElement();

        // Create the XPath - select all device nodes who have a name attribute
        StringBuffer xpathBuffer = new StringBuffer();
        xpathBuffer.append("//").
                append(MCSNamespace.DEVICE_IDENTIFICATION.getPrefix()).
                append(':').
                append(DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME);
        XPath xpath = new XPath(xpathBuffer.toString(),
                new Namespace[]{MCSNamespace.DEVICE_IDENTIFICATION});

        List matches = new ArrayList();

        try {
            // Apply the XPath expression to the root of the document provided
            List nodes = xpath.selectNodes(root);

            if (nodes != null) {
                // Iterate over the devices selected by the XPath. For each
                // device check its patterns agains the regular expression. If
                // there is a match then add the device to the list of matches.
                for (Iterator i = nodes.iterator(); i.hasNext(); /**/) {
                    ODOMElement deviceElement = (ODOMElement) i.next();

                    boolean match = false;

                    // Check primary patterns
                    String primaryPatterns [] = getPrimaryPatterns(deviceElement);
                    for (int i2 = 0; i2 < primaryPatterns.length && !match; i2++) {
                        match = expression.match(primaryPatterns[i2]);
                    }
                    if (!match) {
                        // Check secondary patterns
                        DeviceHeaderPattern secondaryPatterns [] =
                                getSecondaryPatterns(deviceElement);
                        for (int i2 = 0; i2 < secondaryPatterns.length &&
                                !match; i2++) {
                            match = expression.match(secondaryPatterns[i2].
                                    getName()) ||
                                    expression.match(secondaryPatterns[i2].
                                    getRegularExpression());
                        }
                    }

                    if (match) {
                        Attribute name = deviceElement.getAttribute(
                                DeviceRepositorySchemaConstants.
                                DEVICE_NAME_ATTRIBUTE);
                        String value = name.getValue();
                        matches.add(value);
                    }
                }

            }
        } catch (XPathException xpe) {
            // There was a problem selecting the nodes
            throw new UndeclaredThrowableException(xpe, xpe.getMessage());
        }

        // Needed to force the cast on the array to work hence matchArray
        String[] matchArray = new String[matches.size()];
        return (String[]) matches.toArray(matchArray);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/3	adrianj	VBM:2005111712 Added 'New' button for device themes

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 17-Jan-05	6697/1	philws	VBM:2005011401 Fix NPE when re-creating just deleted device

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 11-Jan-05	6646/1	allan	VBM:2005010403 Use URLs for asset paths and don't write empty baseDevice attrs

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	6260/1	allan	VBM:2004110907 NullPointerException and WidgetDisposed error

 17-Nov-04	6012/5	allan	VBM:2004051307 Remove standard elements in admin mode.

 16-Nov-04	4394/1	allan	VBM:2004051018 Undo/Redo in device editor.

 09-Nov-04	6146/1	pcameron	VBM:2004102910 Custom policy property fixes

 08-Oct-04	5557/4	allan	VBM:2004070608 Unit tests and rework issues

 08-Oct-04	5557/2	allan	VBM:2004070608 Device search

 28-Sep-04	5676/1	allan	VBM:2004092302 Fixes to update client ported from v3.2.2

 09-Nov-04	6106/8	pcameron	VBM:2004102910 Custom policy property fixes

 08-Nov-04	6106/5	pcameron	VBM:2004102910 Custom policy property fixes

 28-Sep-04	5615/1	allan	VBM:2004092302 UpdateClient fixes and custom device distinction

 01-Sep-04	5363/1	allan	VBM:2004081705 Fix initial selection in DeviceSelector - port from 3.2.2

 01-Sep-04	5351/1	allan	VBM:2004081705 Fix initial selection in DeviceSelector

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 16-Aug-04	5206/2	allan	VBM:2004081201 Auto-migration of mdpr with dialog.

 11-Aug-04	5126/5	adrian	VBM:2004080303 Added GUI support for Device TACs

 11-Aug-04	5126/3	adrian	VBM:2004080303 Added GUI support for Device TACs

 08-Jul-04	4822/3	claire	VBM:2004070606 Allow devices to be selected using a regular expression

 07-Jul-04	4822/1	claire	VBM:2004070606 Allow devices to be selected using a regular expression

 03-Jun-04	4532/1	byron	VBM:2004052104 ASCII-Art Image Asset Encoding

 14-May-04	4413/1	doug	VBM:2004051412 Fixed PolicyValueModifier labelling issue

 14-May-04	4384/3	pcameron	VBM:2004050703 NewDevicePolicyWizard checks for duplicate policy names

 13-May-04	4333/4	allan	VBM:2004051015 Handle relative urls in JarFileEntityResolver.

 13-May-04	4333/2	allan	VBM:2004051015 Handle relative urls in JarFileEntityResolver.

 12-May-04	4307/3	allan	VBM:2004051201 Fix restore button and moveListeners()

 12-May-04	4309/1	matthew	VBM:2004051112 DeviceRepositoyAccessorManager.setUserAgentPatterns and .setHeaderPatterns modified to keep standard elements as the last children of the element

 11-May-04	4161/7	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 10-May-04	4239/2	allan	VBM:2004042207 SaveAs on DeviceEditor.

 10-May-04	4068/5	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 06-May-04	4068/3	allan	VBM:2004032103 Structure page policies section.

 04-May-04	4007/7	doug	VBM:2004032304 Added a PrimaryPatterns form section

 04-May-04	4113/4	doug	VBM:2004042906 Fixed migration problem with the device repository

 29-Apr-04	4072/7	matthew	VBM:2004042601 Sorting of device hierarchy views removed

 29-Apr-04	4072/4	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 29-Apr-04	4072/2	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 29-Apr-04	4103/2	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 27-Apr-04	4035/3	byron	VBM:2004032403 Create the NewDeviceWizard class

 27-Apr-04	4050/2	pcameron	VBM:2004040701 Added a device Information page and augmented DeviceRepositoryBrowser's title

 27-Apr-04	4016/1	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 22-Apr-04	3878/9	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 22-Apr-04	3878/7	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 21-Apr-04	3016/4	adrian	VBM:2004021301 Fixed merge problems with updated XMLDeviceRepositoryAccessor

 21-Apr-04	3935/12	allan	VBM:2004020906 Rework issues.

 21-Apr-04	3935/10	allan	VBM:2004020906 Fix bug introduced into resolvePolicy and do getCategoryPolicy() to-do.

 21-Apr-04	3935/8	allan	VBM:2004020906 Fix bug introduced into resolvePolicy.

 21-Apr-04	3935/6	allan	VBM:2004020906 Fix bug introduced into resolvePolicy.

 20-Apr-04	3935/1	allan	VBM:2004020906 Migration, Device Browser & Import support for policy fields.

 21-Apr-04	3909/9	pcameron	VBM:2004031004 Refactored the iterators

 21-Apr-04	3909/7	pcameron	VBM:2004031004 Some rework issues for CategoryCompositeBuilder

 20-Apr-04	3909/5	pcameron	VBM:2004031004 Added CategoryCompositeBuilder

 19-Apr-04	3904/3	allan	VBM:2004020903 Support localized device policy categories

 14-Apr-04	3683/4	pcameron	VBM:2004030401 Some tweaks to PolicyController and refactoring of PolicyOriginSelection

 13-Apr-04	3683/2	pcameron	VBM:2004030401 Added PolicyController

 08-Apr-04	3806/1	doug	VBM:2004040810 Paramaterized the DeviceRepositoryAccessorManager and the XMLDeviceRepositoryAccessor contstructors with a JDOMFactory

 07-Apr-04	3774/7	pcameron	VBM:2004040705 Some tweaks

 07-Apr-04	3774/5	pcameron	VBM:2004040705 DeviceRepositoryAccessorManager#resolvePolicy(String, String) uses the <inherit /> tag if present

 01-Apr-04	3602/3	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 30-Mar-04	3574/5	allan	VBM:2004032401 Rework issues.

 25-Mar-04	3568/2	pcameron	VBM:2004032105 Added ObservableProperties and refactored XMLDeviceRepositoryAccessor and DeviceRepositoryAccessorManager to use Properties

 23-Mar-04	3546/4	pcameron	VBM:2004031102 remove is not supported

 23-Mar-04	3546/2	pcameron	VBM:2004031102 Added getPolicyNames to DeviceRepositoryAccessorManager

 23-Mar-04	3389/2	byron	VBM:2004030905 NLV properties files need adding to build

 22-Mar-04	3480/6	pcameron	VBM:2004030410 Added PolicyType

 22-Mar-04	3480/4	pcameron	VBM:2004030410 Added PolicyType

 10-Mar-04	3383/1	pcameron	VBM:2004030412 Added PolicyValueSelectionDialog

 04-Mar-04	3284/7	pcameron	VBM:2004022007 Rework issues with TextPolicyValueModifier

 02-Mar-04	3197/15	pcameron	VBM:2004021904 Further tweaks to PolicyValueOriginSelector

 01-Mar-04	3197/9	pcameron	VBM:2004021904 Rework issues

 01-Mar-04	3197/4	pcameron	VBM:2004021904 Added PolicyValueOriginSelector

 12-Feb-04	2962/1	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 11-Feb-04	2862/8	allan	VBM:2004020411 Rework issues.

 11-Feb-04	2862/6	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 ===========================================================================
*/
