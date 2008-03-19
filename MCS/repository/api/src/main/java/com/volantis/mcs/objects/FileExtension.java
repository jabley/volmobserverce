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
package com.volantis.mcs.objects;

import com.volantis.mcs.policies.PolicyType;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Typesafe enuerator for policy file extensions.
 */
public final class FileExtension {

    /**
     * A case insensitive map from String file extension to FileExtensions.
     */
    private static Map extensionToFileExtension =
            new TreeMap(String.CASE_INSENSITIVE_ORDER);

    /**
     * A map from policy type names to FileExtensions.
     */
    private static Map policyTypeNameToExtension = new HashMap();

    /**
     * A map from policy type to FileExtensions.
     */
    private static Map policyTypeToExtension = new HashMap();

    /**
     * The FileExtension for AssetGroup.
     */
    public static final FileExtension ASSET_GROUP =
            new FileExtension("mgrp", "assetGroup", PolicyType.BASE_URL);

    /**
     * The FileExtension for AudioComponent.
     */
    public static final FileExtension AUDIO_COMPONENT =
            new FileExtension("mauc", "audioComponent", PolicyType.AUDIO);

    /**
     * The FileExtension for ButtonImageComponent.
     */
    public static final FileExtension BUTTON_IMAGE_COMPONENT =
            new FileExtension("mbtn", "buttonImageComponent", PolicyType.BUTTON_IMAGE);

    /**
     * The FileExtension for ChartComponent.
     */
    public static final FileExtension CHART_COMPONENT =
            new FileExtension("mcht", "chartComponent", PolicyType.CHART);

    /**
     * The FileExtension for the device repository file.
     */
    public static final FileExtension DEVICE_REPOSITORY =
            new FileExtension("mdpr", null, null);

    /**
     * The FileExtension for DynamicVisualComponent.
     */
    public static final FileExtension DYNVIS_COMPONENT =
            new FileExtension("mdyv", "dynamicVisualComponent", PolicyType.VIDEO);

    /**
     * The FileExtension for ImageComponent.
     */
    public static final FileExtension IMAGE_COMPONENT =
            new FileExtension("mimg", "imageComponent", PolicyType.IMAGE);

    /**
     * The FileExtension for Layout.
     */
    public static final FileExtension LAYOUT =
            new FileExtension("mlyt", "layout", PolicyType.LAYOUT);

    /**
     * The FileExtension for LinkComponent.
     */
    public static final FileExtension LINK_COMPONENT =
            new FileExtension("mlnk", "linkComponent", PolicyType.LINK);

    /**
     * The FileExtension for ResourceComponent.
     */
    public static final FileExtension RESOURCE_COMPONENT =
            new FileExtension("mrsc", "resourceComponent", PolicyType.RESOURCE);

    /**
     * The FileExtension for RolloverImageComponent.
     */
    public static final FileExtension ROLLOVER_IMAGE_COMPONENT =
            new FileExtension("mrlv", "rolloverImageComponent", PolicyType.ROLLOVER_IMAGE);

    /**
     * The FileExtension for ScriptComponent.
     */
    public static final FileExtension SCRIPT_COMPONENT =
            new FileExtension("mscr", "scriptComponent", PolicyType.SCRIPT);

    /**
     * The FileExtension for TextComponent.
     */
    public static final FileExtension TEXT_COMPONENT =
            new FileExtension("mtxt", "textComponent", PolicyType.TEXT);

    /**
     * The FileExtension for Theme.
     */
    public static final FileExtension THEME =
            new FileExtension("mthm", "theme", PolicyType.THEME);


    /**
     * The extension associated with this PolicyExtension.
     */
    private final String extension;

    /**
     * The policy type associated with this PolicyExtension.
     */
    private final String policyTypeName;

    /**
     * The policy type.
     */
    private final PolicyType policyType;

    /**
     * Construct a new PolicyExtension.
     * @param extension The extension associated with this FileExtension
     * @param policyTypeName The type of policy associated with this
     * @param policyType
     */
    private FileExtension(
            String extension, String policyTypeName, PolicyType policyType) {
        this.extension = extension;
        this.policyTypeName = policyTypeName;
        this.policyType = policyType;

        extensionToFileExtension.put(extension, this);
        policyTypeNameToExtension.put(policyTypeName, this);
        policyTypeToExtension.put(policyType, this);
    }

    /**
     * Get the FileExtension for a specified extension.
     * @param extension The extension.
     * @return The FileExtension associated with extension or null if
     * none were found.
     */
    public static FileExtension getFileExtensionForExtension(
            String extension) {

        return (null != extension) ? 
                (FileExtension) extensionToFileExtension.get(extension) : null;
    }


    /**
     * Get the FileExtension for a specified policyTypeName.
     * @param policyType The policyTypeName.
     * @return The FileExtension associated with policyTypeName or null if
     * none were found.
     *
     * @deprecated Don't use this, use
     * {@link #getFileExtensionForPolicyType(PolicyType)}. 
     */
    public static FileExtension getFileExtensionForPolicyType(
            String policyType) {

        return (FileExtension) policyTypeNameToExtension.get(policyType);
    }

    /**
     * Get the FileExtension for a specified policyType.
     * @param policyType The policyType.
     * @return The FileExtension associated with policyType or null if
     * none were found.
     */
    public static FileExtension getFileExtensionForPolicyType(
            PolicyType policyType) {

        return (FileExtension) policyTypeToExtension.get(policyType);
    }

    /**
     * Get the FileExtension for a local policy name.
     * @param name The local policy name.
     * @return The FileExtension associated with extension or null if
     * none were found, or it was not a policy file extension.
     */
    public static FileExtension getFileExtensionForLocalPolicy(
            String name) {

        FileExtension extension = null;        
        if (null != name) {
            int index = name.lastIndexOf('.');
            if (index != -1) {
                String ext = name.substring(index + 1);
                extension = getFileExtensionForExtension(ext);
                if (null == extension || !extension.isPolicyFileExtension()) {
                    return null;
                }
            }
        }
        return extension;
    }

    /**
     * Checks that the supplied file extension matches the current
     * FileExtension object's extension. The check is case-independent.
     * @param ext the file extension to check
     * @return true if there is a match, false if no match or ext is null.
     */
    public boolean matches(String ext) {
        boolean matches = false;
        if (ext != null) {
            matches = ext.equalsIgnoreCase(extension);
        }
        return matches;
    }


    /**
     * Override toString() to return the extension part of this
     * FileExtension.
     *
     * NOTE: This method only returns the extension part of the FileExtension.
     * This is for historical - the original FileExtension simply a bunch
     * of Strings and code was written to use it in this way.
     *
     * @deprecated use FileExtension.extension instead
     */
    public String toString() {
        return extension;
    }

    /**
     * Determine whether this FileExtension is for a policy.
     * @return true if this FileExtension is for a policy; false otherwise.
     */
    public boolean isPolicyFileExtension() {
        return policyTypeName != null;
    }

    /**
     * Get the extension.
     *
     * @return The extension.
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Get a string representation of the policy type.
     *
     * @return The policy type.
     */
    public String getPolicyTypeName() {
        return policyTypeName;
    }

    /**
     * Get the policy type.
     *
     * @return The policy type.
     */
    public PolicyType getPolicyType() {
        return policyType;
    }

    /**
     * Get the policy type for the supplied policy name, or null if the name
     * is of an unknown type.
     *
     * @param name the policy name.
     * @return the type of the policy, or null if it is unknown.
     */
    public static PolicyType getPolicyTypeForPolicy(String name) {
        PolicyType policyType = null;
        FileExtension extension =
                getFileExtensionForLocalPolicy(name);
        if (extension != null) {
            policyType = extension.getPolicyType();
        }
        return policyType;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 11-Apr-05	7376/2	allan	VBM:2005031101 SmartClient bundler - commit for testing

 08-Feb-05	6910/1	allan	VBM:2005020702 New Resource Asset Template wizard

 06-Jan-05	6474/1	allan	VBM:2004121302 Migration framework

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Feb-04	3044/1	allan	VBM:2004021604 Ensure that MCSProjectBuilder only builds policies.

 13-Feb-04	3025/1	mat	VBM:2004021304 Changes to make import work

 12-Feb-04	2962/6	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 29-Dec-03	2258/1	allan	VBM:2003121725 Make Layout editor and wizard conform to new layout schema

 12-Dec-03	2123/2	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 05-Dec-03	2128/3	pcameron	VBM:2003112105 Renamed FileExtension.match to matches

 05-Dec-03	2128/1	pcameron	VBM:2003112105 Tweaks to TextDefinition

 27-Nov-03	2013/2	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 16-Nov-03	1909/5	allan	VBM:2003102405 All policy wizards.

 16-Nov-03	1909/1	allan	VBM:2003102405 Done Image Component Wizard.

 30-Oct-03	1639/2	byron	VBM:2003101602 Create a MCS Project properties page

 17-Oct-03	1502/1	allan	VBM:2003092202 Component selection dialog with filtering and error handling

 ===========================================================================
*/
