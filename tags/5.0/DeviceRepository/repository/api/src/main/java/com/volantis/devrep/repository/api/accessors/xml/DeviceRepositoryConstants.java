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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.devrep.repository.api.accessors.xml;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;

public class DeviceRepositoryConstants {

    public static final String REPOSITORY_EXTENSION = "mdpr";

    /**
     * The suffix used for XML files.
     */
    public static final String XML_SUFFIX = ".xml";

    /**
     * definitions.xml entry (found in the xml repository file).
     */
    public static final String DEFINITIONS_XML =
            DeviceRepositorySchemaConstants.DEFINITIONS_DOCUMENT_NAME +
            XML_SUFFIX;

    /**
     * hierarchy.xml entry (found in the xml repository file).
     */
    public static final String HIERARCHY_XML =
            DeviceRepositorySchemaConstants.HIERARCHY_DOCUMENT_NAME +
            XML_SUFFIX;

    /**
     * identification.xml entry (found in the xml repository file).
     */
    public static final String IDENTIFICATION_XML =
            DeviceRepositorySchemaConstants.IDENTIFICATION_DOCUMENT_NAME +
            XML_SUFFIX;

    /**
     * tac-identification.xml entry (found in the xml repository file).
     */
    public static final String TAC_IDENTIFICATION_XML =
            DeviceRepositorySchemaConstants.TAC_IDENTIFICATION_DOCUMENT_NAME +
            XML_SUFFIX;

    /**
     * The repository definition.
     */
    public static final String REPOSITORY = "repository";

    /**
     * Constant that will be used as the prefix to which the default namespace
     * will be bound for each Document in the repository.
     */
    public static final String DEFAULT_NAMESPACE_PREFIX = "device";

    /**
     * repository.xml entry (found in the xml repository file).
     */
    public static final String REPOSITORY_XML = REPOSITORY + XML_SUFFIX;

    public static final String STANDARD_DIRECTORY = "standard/";

    public static final String DEVICE_DIRECTORY = "devices/";
    /**
     * The directory containing devices standard policies
     */
    public static final String STANDARD_DEVICE_DIRECTORY =
            STANDARD_DIRECTORY + DEVICE_DIRECTORY;

    public static final String CUSTOM_DIRECTORY = "custom/";
    /**
     * The directory containing devices custom policies.
     */
    public static final String CUSTOM_DEVICE_DIRECTORY =
            CUSTOM_DIRECTORY + DEVICE_DIRECTORY;

    public static final String POLICIES_DIRECTORY = "policies/";

    public static final String CUSTOM_DEFINITIONS_DIRECTORY =
            CUSTOM_DIRECTORY + POLICIES_DIRECTORY;

    public static final String CUSTOM_DEFINITIONS_XML =
            CUSTOM_DEFINITIONS_DIRECTORY + DEFINITIONS_XML;

    public static final String STANDARD_DEFINITIONS_DIRECTORY =
            STANDARD_DIRECTORY + POLICIES_DIRECTORY;

    public static final String STANDARD_DEFINITIONS_XML =
            STANDARD_DEFINITIONS_DIRECTORY + DEFINITIONS_XML;

    public static final String VERSION_FILENAME = "version.txt";

    /**
     * Prefix for customer created devices.
     */
    public static final String CUSTOM_DEVICE_NAME_PREFIX = "_";

    /**
     * The prefix of the standard non-localized policies properties file.
     */
    public static final String STANDARD_POLICIES_PROPERTIES_PREFIX =
            "standard/policies/resources/policies";
    /**
     * The prefix of the standard non-localized policies properties file.
     */
    public static final String CUSTOM_POLICIES_PROPERTIES_PREFIX =
            "custom/policies/resources/policies";

    /**
     * The prefix of the standard non-localized policies properties file.
     */
    public static final String POLICIES_PROPERTIES_SUFFIX =
            ".properties";
    /**
     * The prefix in a property name to identify a custom property.
     */
    public static final String CUSTOM_POLICY_NAME_PREFIX = "custom.";

    /**
     * The resource prefix for standard policies.
     */
    public static final String STANDARD_POLICY_RESOURCE_PREFIX = "policy.";

    /**
     * The resource prefix for custom policies.
     */
    public static final String CUSTOM_POLICY_RESOURCE_PREFIX =
            STANDARD_POLICY_RESOURCE_PREFIX + CUSTOM_POLICY_NAME_PREFIX;

    /**
     * The version of the current device repository (NOTE: this is not the same
     * thing as the version of the schemas).
     */
    public static final String VERSION = "3.0";

    /**
     * The name of the fallback policy.
     * <p>
     * NOTE: this policy value is added at runtime. We currently expose it
     * (and some faked metadata) via the device repository api for backwards
     * compatibility only.
     */
    public static final String FALLBACK_POLICY_NAME = "fallback";

    /**
     * The category of the fallback policy.
     *
     * @see #FALLBACK_POLICY_NAME
     */
    public static final String FALLBACK_POLICY_CATEGORY = "identification";
    
    /**
     * The resource prefix for policies.
     */
    public static final String POLICY_RESOURCE_PREFIX = "policy.";

    /**
     * The resource prefix for categories.
     */
    public static final String CATEGORY_RESOURCE_PREFIX = "category.";

    /**
     * The name of the name property for both policy and categories.
     */
    public static final String NAME_PROPERTY_SUFFIX = ".name";

    /**
     * The name of the name property for both policy and categories.
     */
    public static final String DESCRIPTION_PROPERTY_SUFFIX = ".description";
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Nov-05	10404/1	geoff	VBM:2005112301 Implement meta data for JiBX device repository accessor

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 ===========================================================================
*/
