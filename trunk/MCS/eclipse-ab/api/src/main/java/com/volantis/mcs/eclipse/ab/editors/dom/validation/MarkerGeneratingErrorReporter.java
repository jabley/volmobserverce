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
package com.volantis.mcs.eclipse.ab.editors.dom.validation;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.EditorMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.PolicyUtils;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.xml.validation.DOMValidator;
import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.synergetics.log.LogDispatcher;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Element;
import org.jdom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * An ErrorReporter that reports errors using IMarkers against a specified
 * IResource.
 */
public class MarkerGeneratingErrorReporter implements ErrorReporter {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory
                    .createLogger(MarkerGeneratingErrorReporter.class);

    /**
     * An attribute key for identifying the encoded namespaces attribute
     * stored on a marker.
     */
    private static final String NAMESPACES_ATTRIBUTE =
            XPath.class.getName() + "/NAMESPACES";


    /**
     * Prefix with which to use for message key mappings.
     */
    public static final String MESSAGE_KEY_PREFIX =
            "MarkerGeneratingErrorReporter.";
    /**
     * Key into the EditorMessages bundle to a message that instructs the user
     * to provide a value for the AssetType section.
     */
    private static String ASSET_TYPE_REQUIRED =
            MESSAGE_KEY_PREFIX + "assetTypeRequired";

    /**
     * Message key mappings.
     */
    private static final Map MESSAGE_KEY_MAPPINGS = new HashMap();

    /**
     * Messages created for use by the THeme Editor expect the fault keys
     * to have the suffix defined here.
     */
    private static final String THEME_EDITOR_KEYS_SUFFIX = "Theme";

    /**
     * The key for the invalid attribute location theme. Messages generated
     * for the Theme Editor expect the key to have a "Theme" suffix.
     */
    private static final String INVALID_ATTRIBUTE_LOCATION_THEME =
            FaultTypes.INVALID_ATTRIBUTE_LOCATION + THEME_EDITOR_KEYS_SUFFIX;

    /**
     * The key for the invalid schema data type theme. Messages generated
     * for the Theme Editor expect the key to have a "Theme" suffix.
     */
    private static final String INVALID_SCHEMA_DATA_TYPE_THEME =
            FaultTypes.INVALID_SCHEMA_DATA_TYPE + THEME_EDITOR_KEYS_SUFFIX;

    /**
     * The set of fault types for messages associated with the Theme Editor
     * that need to be created differently.
     */
    private static final Set THEME_FAULT_TYPES;


    static {

        THEME_FAULT_TYPES = new HashSet(2, 1);
        THEME_FAULT_TYPES.add(FaultTypes.INVALID_ATTRIBUTE_LOCATION);
        THEME_FAULT_TYPES.add(FaultTypes.INVALID_SCHEMA_DATA_TYPE);

        // add the mappings that the validation message builder will
        // use.
        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.INVALID_ELEMENT_LOCATION,
                MESSAGE_KEY_PREFIX + "invalidElementContent");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.INVALID_ELEMENT_CONTENT,
                MESSAGE_KEY_PREFIX + "invalidElementContent");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.INVALID_ATTRIBUTE_LOCATION,
                MESSAGE_KEY_PREFIX + "invalidAttributeLocation");

        MESSAGE_KEY_MAPPINGS.put(
                INVALID_ATTRIBUTE_LOCATION_THEME,
                MESSAGE_KEY_PREFIX + INVALID_ATTRIBUTE_LOCATION_THEME);

        MESSAGE_KEY_MAPPINGS.put(
                INVALID_SCHEMA_DATA_TYPE_THEME,
                MESSAGE_KEY_PREFIX + INVALID_SCHEMA_DATA_TYPE_THEME);

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.INVALID_ATTRIBUTE_CONTENT,
                MESSAGE_KEY_PREFIX + "invalidValue");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.INVALID_SCHEMA_DATA_TYPE,
                MESSAGE_KEY_PREFIX + "invalidValue");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.INVALID_POLICY_NAME,
                MESSAGE_KEY_PREFIX + "invalidValue");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.INVALID_SCHEMA_PATTERN_VALUE,
                MESSAGE_KEY_PREFIX + "invalidValue");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.SCHEMA_CONSTRAINT_VIOLATED +
                        ".FragmentNameReference",
                MESSAGE_KEY_PREFIX +
                        "schemaConstraintViolated.FragmentNameReference");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.SCHEMA_CONSTRAINT_VIOLATED +
                        ".SegmentNameReference",
                MESSAGE_KEY_PREFIX +
                        "schemaConstraintViolated.SegmentNameReference");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.SCHEMA_CONSTRAINT_VIOLATED,
                MESSAGE_KEY_PREFIX + "invalidValue");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.DUPLICATE_NAME,
                MESSAGE_KEY_PREFIX + "duplicateName");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.MAX_INCLUSIVE_VIOLATED,
                MESSAGE_KEY_PREFIX + "maxInclusiveViolated");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.MIN_INCLUSIVE_VIOLATED,
                MESSAGE_KEY_PREFIX + "minInclusiveViolated");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.MIN_RANGE_MORE_THAN_MAX,
                MESSAGE_KEY_PREFIX + "minRangeMoreThanMax");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.MAX_LENGTH_VIOLATED,
                MESSAGE_KEY_PREFIX + "maxLengthViolated");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.INVALID_SELECTION,
                MESSAGE_KEY_PREFIX + "invalidValue");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.INVALID_ATTRIBUTE_CONTENT,
                MESSAGE_KEY_PREFIX + "invalidValue");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.INVALID_ATTRIBUTE_CONTENT,
                MESSAGE_KEY_PREFIX + "invalidValue");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.MISSING_ATTRIBUTE,
                MESSAGE_KEY_PREFIX + "valueRequired");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.DUPLICATE_ASSET,
                MESSAGE_KEY_PREFIX + "duplicateAsset");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.MUST_BE_IN,
                MESSAGE_KEY_PREFIX + "mustBeIn");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.MUST_NOT_BE_IN,
                MESSAGE_KEY_PREFIX + "mustNotBeIn");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.TOO_MANY_IN,
                MESSAGE_KEY_PREFIX + "tooManyIn");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.DUPLICATE_UNIQUE,
                MESSAGE_KEY_PREFIX + "duplicateUnique");

        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.INVALID_NULL_ASSET,
                MESSAGE_KEY_PREFIX + "invalidNullAsset");

        MESSAGE_KEY_MAPPINGS.put(FaultTypes.WHITESPACE,
                MESSAGE_KEY_PREFIX + "whitespace");

        // we need to provide this mapping so that the validation message
        // builder uses the correct bundle.
        MESSAGE_KEY_MAPPINGS.put(ASSET_TYPE_REQUIRED, ASSET_TYPE_REQUIRED);
    }

    /**
     * Resource bundle.
     */
    private static final ResourceBundle BUNDLE
            = EditorMessages.getResourceBundle();

    /**
     * The IResource associated with this ErrorReporter i.e. the IResource
     * that IMarkers are generated on.
     */
    private final IResource resource;

    /**
     * The ValidationMessageBuilder associated with this ErrorReporter.
     */
    private final ValidationMessageBuilder vmb;

    /**
     * The LocationDetailsRegistry associated with this
     * MarkerGeneratingErrorReporter
     */
    private LocationDetailsRegistry locationDetailsRegistry = null;

    /**
     * This Map stores XPath string to XPath Oject mappings. We need this as
     * we cannot store the XPath object in the marker. The
     * {@link IMarker#setAttribute} method only accepts String, Boolean or
     * Integer object as the attribute value. We therefore set the XPaths
     * external form as the attribute and provide a {@link #getXPath} method
     * that returnt the XPath for a given XPath string.
     */
    private final Map xPathMappings;

    /**
     * The Element that is the rootElement if the DOM that is invalid.
     */
    private final Element rootElement;

    /**
     * An identifier for the rootElement - for use with
     * MarkerGeneratingErrorReporters that report on errors
     * in resources that contain multiple documents.
     */
    private final String rootElementIdentifier;


    /**
     * Maintain the errors that are censored.
     */
    private final CensoredErrors censoredErrors = new CensoredErrors();


    /**
     * Construct a new MarkerGeneratingErrorReporter that will report
     * errors on the given resource.
     *
     * @param resource              The IResource.
     * @param rootElement           The root Element of the document whose validator
     *                              uses this MarkerGeneratingErrorReporter.
     * @param rootElementIdentifier A String identifying the root element -
     *                              for use with MarkerGeneratingErrorReporters that report on errors
     *                              in resources that contain multiple documents. Can be null.
     * @throws IllegalArgumentException if resouce or rootElement are null.
     */
    public MarkerGeneratingErrorReporter(IResource resource,
                                         Element rootElement,
                                         String rootElementIdentifier) {
        if (resource == null) {
            throw new IllegalArgumentException("Cannot be null: resource");
        }
        if (rootElement == null) {
            throw new IllegalArgumentException("Cannot be null: rootElement");
        }
        this.resource = resource;
        this.rootElement = rootElement;
        this.rootElementIdentifier = rootElementIdentifier;
        this.vmb = new ValidationMessageBuilder(BUNDLE,
                MESSAGE_KEY_MAPPINGS,
                null);
        this.xPathMappings = new HashMap();
    }

    /**
     * Put a message key mapping into this error reporter. If an error
     * is to be reported whose key matches the given fault type then the
     * given message key will be used to obtain the error message.
     * <p/>
     * If there is already a messageKey with the same faultType it will be
     * replaced.
     * <p/>
     * Due to the fact that the bundle used by MarkerGeneratingErrorReporter
     * is fixed all associated messages must be in the EditorMessages bundle.
     * Therefore all messageKey for MarkerGeneratingErrorReporter
     * including those added by this method must be prefixed with the
     * MESSAGE_KEY_PREFIX constant and have an associated message in
     * EditorMessages.properties.
     *
     * @param faultType  the key that decribes the fault type of the error
     * @param messageKey the message key corresponding to a resource property
     *                   that describes the appropriate error message for the given fault type
     * @return the replaced messageKey or null if no messageKey was replaced
     * @todo remove this method when validation is redesigned for more flexible message building
     */
    public String putMessageKeyMapping(String faultType, String messageKey) {
        return (String) MESSAGE_KEY_MAPPINGS.put(faultType, messageKey);
    }

    /**
     * Returns the {@link com.volantis.mcs.xml.xpath.XPath} object for a XPath
     * string contained in the given IMarker.
     *
     * @param marker the IMarker containing the XPath.class.getName() and
     *               NAMESPACES_ATTRIBUTES attributes that specify the problem XPath to find.
     * @return the associated XPath object or null if the IMarker contains no
     *         XPath string.
     * @throws IllegalStateException if no path is found or if more hen one path is
     *                               found.
     */
    public XPath getXPath(IMarker marker) {

        // retrieve the XPath string from the marker
        String xPathStr = null;
        String namespaces = null;
        XPath result = null;  // return value

        try { //try to get the attributes.
            xPathStr = (String) marker.getAttribute(XPath.class.getName());
            namespaces = (String) marker.
                    getAttribute(MarkerGeneratingErrorReporter.
                            NAMESPACES_ATTRIBUTE);
        } catch (CoreException ce) {
            // just report the error and return
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), ce);
            xPathStr = null;
        }

        if (xPathStr != null) {

            // get cached XPath if it exists
            result = (XPath) xPathMappings.get(xPathStr);

            if (result == null) { //if an XPath does not exist in the cache
                try {
                    // create an XPath to find the problem markers
                    XPath queryPath = new XPath(xPathStr, namespaces);
                    // find the problem markers. This potentially throws a CoreException
                    IMarker[] markers =
                            PolicyUtils.findProblemMarkers(resource, queryPath);

                    if ((markers == null) || (markers.length == 0)) {
                        throw new IllegalStateException(
                                "Could not obtain the XPath object for: " +
                                        xPathStr);
                    } else if (markers.length == 1) {
                        result = queryPath;
                        xPathMappings.put(xPathStr, queryPath);
                    } else if (markers.length > 1) {
                        throw new IllegalStateException(
                                "At most one marker should exist for a given XPath " +
                                        "on a given resource");
                    }
                } catch (CoreException ce) {
                    EclipseCommonPlugin.handleError(ABPlugin.getDefault(), ce);
                }
            }
        }
        return result;
    }

    /**
     * Specify that a particular xpath/key combination (identifying an
     * error or set of errors) should be censored (i.e. not reported) by
     * this MarkerGeneratingErrorReporting.
     * <p/>
     * When both xpath and key match the error report will be censored.
     * <p/>
     * Note that XPath final indices are not including in the matching.
     *
     * @param xPathIdentifier the identifier for the XPath to censor.
     *                        If this value is null then the XPath is ignored when identifying errors
     *                        to censor. This value should represent the end of the external form
     *                        of the XPath(s) to match - this could be a full XPath external form or
     *                        just the some last few characters. XPaths whose external form end with
     *                        xPathIdentifier will match and be candidates for censorship.
     * @param key             the message key (e.g. FaultType) to censor.
     */
    public void censor(String xPathIdentifier, String key) {
        censoredErrors.add(xPathIdentifier, key);
    }

    /**
     * Report the given error. If there are no errors already reported for
     * the given XPath or its children then create a new IMarker on the
     * IResource associated with this ErrorReport. The IMarker will have a
     * type of IMarker.PROBLEM and a message created using the
     * ValidationMessageBuilder associated with this ErrorReporter.
     */
    // rest of javadoc inherited
    public void reportError(ErrorDetails details) {
        XPath xPath = details.getXPath();
        String key = details.getKey();
        Element invalidElement = details.getInvalidElement();

        assert xPath != null;
        assert key != null;

        try {
            if (!censoredErrors.isCensored(xPath.getExternalForm(), key)) {
                IMarker[] markers =
                        PolicyUtils.findProblemMarkers(resource, xPath);
                if (markers.length == 0) {

                    String xPathStr = xPath.getExternalForm();
                    // store away the XPath in the map
                    xPathMappings.put(xPathStr, xPath);
                    IMarker marker = resource.createMarker(IMarker.PROBLEM);

                    marker.setAttribute(XPath.class.getName(), xPathStr);
                    marker.setAttribute(NAMESPACES_ATTRIBUTE,
                            xPath.getNamespacesString());
                    marker.setAttribute(IMarker.MESSAGE, createMessage(
                            details));
                    marker.setAttribute(IMarker.LOCATION,
                            getLocationDetailsString(invalidElement));
                    marker.setAttribute(IMarker.SEVERITY,
                            IMarker.SEVERITY_ERROR);
                    marker.setAttribute(DOMValidator.class.getName(), true);

                    // If there is an identifier for the root element then set
                    // that in the marker keyed on the Element class.
                    if (rootElementIdentifier != null) {
                        marker.setAttribute(Element.class.getName(),
                                rootElementIdentifier);
                    }
                }
            }
        } catch (CoreException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Creates the validation error message.
     *
     * @param errorDetails the ErrorDetails for the error message
     * @return a validation message
     */
    private String createMessage(ErrorDetails errorDetails) {
        String message;
        String key = errorDetails.getKey();

        Element invalidElement = errorDetails.getInvalidElement();
        if (invalidElement != null) {
            if (ODOMElement.UNDEFINED_ELEMENT_NAME.
                    equals(invalidElement.getName())) {
                key = ASSET_TYPE_REQUIRED;
            }
        }
        message = vmb.buildValidationMessage(key,
                errorDetails.getParam(),
                createFormatArgs(errorDetails));

        return message;
    }

    /**
     * Creates the format arg array that is need to format the validation error
     * message
     *
     * @param details the ErrorDetails
     * @return an array of string arguments
     */
    private String[] createFormatArgs(ErrorDetails details) {

        XPath xPath = details.getXPath();
        String param = details.getParam();
        String key = details.getKey();

        String[] args = null;
        if (key == FaultTypes.UNKNOWN_INVALID_XML) {
            // the arg is simply the xpath and the error param
            args = new String[]{xPath.getExternalForm(), param};
        } else if (key == FaultTypes.MISSING_ATTRIBUTE) {
            args = new String[]{getPropertyLabel(param)};
        } else if (FaultTypes.DUPLICATE_ASSET.equals(key)) {
            // currently the duplicate asset message takes no args
            args = null;
        } else if (FaultTypes.DUPLICATE_NAME.equals(key)) {
            Element element = details.getInvalidElement();
            String elementName;
            try {
                if (element == null) {
                    element = xPath.getElement(rootElement);
                }
                elementName =
                        getElementLabel(element.getAttributeValue("name"));

            } catch (XPathException e) {
                elementName = getElementLabel("InternalError: Unknown Element");
            }

            args = new String[]{
                    elementName,
                    getElementLabel(getPrimaryArg(xPath, "?"))
            };

        } else if (FaultTypes.MUST_BE_IN.equals(key) ||
                FaultTypes.MUST_NOT_BE_IN.equals(key) ||
                FaultTypes.TOO_MANY_IN.equals(key))

        {
            // @todo it would be nice if TOO_MANY_IN could show max and actual too
            // these messages take two arguments, being the name of the thing
            // that is invalid and the error parameter as a property label
            String invalid = getPrimaryArg(xPath, "?");
            args = new String[]{
                    getElementLabel(invalid),
                    getElementLabel(param)
            };
        } else if (FaultTypes.DUPLICATE_UNIQUE.equals(key))

        {
            String value = param;
            if (value.startsWith("ID Value: ")) {
                value = value.substring(11);
            }
            args = new String[]{value};
        } else if (FaultTypes.INVALID_NULL_ASSET.equals(key))

        {
            String primaryArg = getPrimaryArg(xPath, key);
            args = new String[]{param, getElementLabel(primaryArg)};
        } else if (FaultTypes.NOT_A_NUMBER.equals((key)))

        {
            args = new String[]{param};
        } else

        {
            // all other messages take a single argument that is the name of
            // the thing that is invalid
            String primaryArg = getPrimaryArg(xPath, param);
            args = new String[]{getPropertyLabel(primaryArg)};
        }

        return args;
    }

    /**
     * Retrieves the specified properties label from a bundle.
     *
     * @param property the property
     * @return the properties label or the property itself if the label can
     *         not be found.
     */
    private String getPropertyLabel(String property) {
        StringBuffer keyBuffer = new StringBuffer();
        keyBuffer.append("PolicyName.")
                .append(rootElement.getName())
                .append('.')
                .append(property);

        return EclipseCommonMessages.getString(keyBuffer.toString(), property);
    }

    /**
     * Retrieves the specified properties label from a bundle.
     *
     * @param property the property
     * @return the properties label or the property itself if the label can
     *         not be found.
     */
    private String getElementLabel(String property) {
        StringBuffer keyBuffer = new StringBuffer();
        keyBuffer.append("PolicyName.")
                .append(property);

        return EclipseCommonMessages.getString(keyBuffer.toString(), property);
    }

    /**
     * Returns the String representation of the element or attribute that
     * is invalid. If this cannot be calculated it returns the param argument.
     *
     * @param path  the XPath that points to the invalid node.
     * @param param the error param
     * @return the String representation of the element or attribute that
     *         is invalid. If this cannot be calculated it returns the param argument.
     */
    private String getPrimaryArg(XPath path, String param) {
        Object node = getInvalidNode(path);
        String arg = param;
        if (node instanceof Element) {
            arg = ((Element) node).getName();
        } else if (node instanceof Attribute) {
            arg = ((Attribute) node).getName();
        } else if (node instanceof CDATA) {
            CDATA cDATA = (CDATA) node;
            arg = cDATA.getParent().getName();
        } else if (node instanceof Text) {
            Text text = (Text) node;
            arg = text.getParent().getName();
        } else {
            // this should never happen as the XPath should always refer to
            // an element or attribute.
            logger.warn("localized-message",
                    ((node != null) ? node.getClass().getName() : "null"));
        }
        return arg;
    }

    /**
     * Returns the node that the invalid xpath points to.
     *
     * @param path the XPath that points to the node that is invalid
     * @return the node that is invalid. This is returned as an object it may
     *         be an instance of one of the following ODOM/JDOM classes: ODOM/Element,
     *         ODOM/Attribute, ODOM/Text, ODOM/CDATA. If the node cannot be selected
     *         null will be returned.
     */
    private Object getInvalidNode(XPath path) {
        Object node = null;
        try {
            while (node == null && path != null) {
                node = path.selectSingleNode(rootElement);
                if (node == null) {
                    path = path.getParent();
                }
            }
        } catch (XPathException e) {
            logger.warn("element-or-attribute-cannot-be-identified", e);
        }
        return node;
    }

    /**
     * Remove any existing problem IMarkers with the given XPath and element.
     */
// rest of javadoc inherited
    public void validationStarted(Element root, XPath xPath) {
        try {
            IMarker[] markers = rootElementIdentifier != null ?
                    PolicyUtils.findProblemMarkers(resource,
                            rootElementIdentifier, xPath) :
                    PolicyUtils.findProblemMarkers(resource, xPath);

            for (int i = 0; i < markers.length; i++) {
                IMarker marker = markers[i];
// remove the XPath mapping for the marker
                Object xpathAttrValue = marker.getAttribute(
                        XPath.class.getName());
                if (xpathAttrValue != null) {
                    xPathMappings.remove(xpathAttrValue);
                }
                marker.delete();
            }
        } catch (CoreException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Does nothing at the moment.
     */
// rest of javadoc inherited
    public void validationCompleted(XPath xPath) {
        xPathMappings.remove(xPath.getExternalForm());
    }

    /**
     * Set a LocationDetailsRegistry. If a LocationDetails is registered
     * for the Element in error then its associated LocationDetails will be
     * used to discover the appropriate location information that should be
     * reported in the error information. The XPath passed to the
     * LocationDetails.getLoccationDetailsString() method will be the
     * XPath associated with the error.
     *
     * @param registry the LocationDetailsRegistry for this ErrorReporter
     */
    public void setLocationDetailsRegistry(
            LocationDetailsRegistry registry) {
        this.locationDetailsRegistry = registry;
    }

    /**
     * Get the LocationDetailsRegistry associated with this
     * MarkerGeneratingErrorReporter.
     *
     * @return locationDetailsRegistry
     */
    public LocationDetailsRegistry getLocationDetailsRegistry() {
        return locationDetailsRegistry;
    }

    /**
     * Get the location details for the specified XPath.
     *
     * @param invalidElement the element in error
     * @return the location details for the erroneus element/xpath or null
     *         if no LocationDetailsRegistry has been set on this ErrorReporter or
     *         if no LocationDetails were available for the given XPath.
     */
    private String getLocationDetailsString(Element invalidElement) {

        String locationDetailsString = null;
        if (locationDetailsRegistry != null) {
            try {
                while (invalidElement != null &&
                        locationDetailsString == null) {
                    LocationDetails locationDetails =
                            locationDetailsRegistry.
                                    getLocationDetails(invalidElement);
                    if (locationDetails != null) {
                        locationDetailsString =
                                locationDetails.
                                        getLocationDetailsString(rootElement,
                                                invalidElement);
                    } else {
                        invalidElement = invalidElement.getParent();
                    }
                }

            } catch (XPathException e) {
                logger.warn("element-or-attribute-cannot-be-identified", e);
            }
        }

        return locationDetailsString;
    }

    /**
     * Container for specifying and determining what errors should be
     * censored i.e. not reported.
     */
    private static class CensoredErrors {
        private List censoredXPaths = new ArrayList();
        private List censoredKeys = new ArrayList();

        /**
         * Add an xpath/key combination (identifying an
         * error or set of errors) should be censored (i.e. not reported) by
         * this MarkerGeneratingErrorReporting.
         * <p/>
         * When both xpath and key match the error report will be censored.
         *
         * @param xPathIdentifier the identifier for the XPath to censor.
         *                        If this value is null then the XPath is ignored when identifying errors
         *                        to censor. This value should represent the end of the external form
         *                        of the XPath(s) to match - this could be a full XPath external form or
         *                        just the some last few characters. XPaths whose external form end with
         *                        xPathIdentifier will match and be candidates for censorship.
         * @param key             the message key (e.g. FaultType) to censor.
         */
        void add(String xPathIdentifier, String key) {
            censoredXPaths.add(stripFinalXPathIndicy(xPathIdentifier));
            censoredKeys.add(key);
        }


        /**
         * Determine if an error has been censorsed i.e. should not be reported.
         *
         * @param xPathString the XPath of the error
         * @param key         the key of the error
         */
        boolean isCensored(String xPathString, String key) {
            boolean isCensored = false;
            int index = 0;
            int size = censoredXPaths.size();

            xPathString = stripFinalXPathIndicy(xPathString);

            while (!isCensored && index < size) {
                String currentXPath = (String) censoredXPaths.get(index);
                String currentKey = (String) censoredKeys.get(index);

                boolean xPathMatch = currentXPath == null ||
                        xPathString == null ||
                        xPathString.endsWith(currentXPath);
                boolean keyMatch = currentKey == null || key == null ||
                        key.equals(currentKey);
                isCensored = xPathMatch && keyMatch;
                index++;
            }

            return isCensored;
        }


        /**
         * Provide an xPathString with no final indicy i.e. with the [] and
         * everything in between removed if there this exists as the final
         * part of the xPathString.
         *
         * @param xPathString
         * @return xPathString with its final [] and everything in bewteen
         *         removed if the [...] was at the end of the xPathString
         */
        private String stripFinalXPathIndicy(String xPathString) {
            if (xPathString.endsWith("]")) {
                int indicyStart = xPathString.lastIndexOf('[');
                if (indicyStart != -1) {
                    xPathString = xPathString.substring(0, indicyStart);
                }
            }
            return xPathString;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Sep-05	9513/1	adrianj	VBM:2005091408 Represent style values as elements rather than attributes

 16-May-05	8260/1	allan	VBM:2005042107 Implement and fix setFocus(XPath) for theme design

 16-May-05	8201/1	allan	VBM:2005042107 Implement and fix setFocus(XPath) for theme design

 27-Apr-05	7910/3	pcameron	VBM:2005042106 Fixed error reporting for Theme Editor

 27-Apr-05	7879/4	pcameron	VBM:2005042106 Fixed error reporting for Theme Editor

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 05-Jan-05	6578/3	byron	VBM:2004123002 Eclipse GUI: Theme attribute validation is broken

 05-Jan-05	6578/1	byron	VBM:2004123002 Eclipse GUI: Theme attribute validation is broken

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Dec-04	6354/2	adrianj	VBM:2004112605 Refactor XML validation error reporting

 30-Nov-04	6328/2	tom	VBM:2004112909 Added minRangeMoreThanMax

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 08-Oct-04	5557/1	allan	VBM:2004070608 Device search

 28-Sep-04	5676/1	allan	VBM:2004092302 Fixes to update client ported from v3.2.2

 28-Sep-04	5615/1	allan	VBM:2004092302 UpdateClient fixes and custom device distinction

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/6	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/4	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 08-Sep-04	5432/2	allan	VBM:2004081803 Validation for range min and max values

 13-Aug-04	5187/4	adrianj	VBM:2004080302 UAProf URI caching mechanism

 13-Aug-04	5036/2	byron	VBM:2004080202 Public API for device lookup by TAC or UAProf URI (umbrella) - fix merge issues

 11-Aug-04	5126/1	adrian	VBM:2004080303 Added GUI support for Device TACs

 09-Aug-04	5130/1	doug	VBM:2004080310 MCS

 09-Aug-04	5130/1	doug	VBM:2004080310 MCS

 11-May-04	4303/1	allan	VBM:2004051005 Restore button and Widget is disposed bug fix.

 11-May-04	4272/1	allan	VBM:2004050503 Unique problem markers fix for device editor.

 27-Apr-04	3983/6	matthew	VBM:2004040203 Fixes to setFocusWith(XPath)

 27-Apr-04	3983/4	matthew	VBM:2004040203 Fixes to setFocusWith(XPath)

 27-Apr-04	3983/2	matthew	VBM:2004040203 Fixes to setFocusWith(XPath)

 05-Apr-04	3706/1	pcameron	VBM:2004040108 Fixes to PolicyUtils and MarkerGeneratingErrorReporter

 31-Mar-04	3664/5	pcameron	VBM:2004032202 A few tweaks to PolicyUtils, PolicyUtilsTestCase and MarkerGeneratingErrorReporter

 31-Mar-04	3664/3	pcameron	VBM:2004032202 Added a new findProblemMarkers and test

 30-Mar-04	3614/1	byron	VBM:2004022404 Layout: Panes are allowed same names

 27-Feb-04	3246/1	byron	VBM:2004021205 Lack of validation for policy file extensions

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 18-Jan-04	2562/1	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 09-Jan-04	2506/1	philws	VBM:2004010810 Fix XPath marker persistence

 08-Jan-04	2431/1	allan	VBM:2004010404 Fix validation and display update issues.

 06-Jan-04	2323/10	doug	VBM:2003120701 Added better validation error messages

 06-Jan-04	2323/8	doug	VBM:2003120701 Added better validation error messages

 17-Dec-03	2219/4	doug	VBM:2003121502 Added dom validation to the eclipse editors

 04-Dec-03	2102/1	allan	VBM:2003112101 Create the AlertsActionsSection.

 02-Dec-03	2069/3	allan	VBM:2003111903 Pre-approve bug fixes.

 02-Dec-03	2069/1	allan	VBM:2003111903 Basic ODOMEditorPart completed with skeleton ImageComponentEditor.

 ===========================================================================
*/
