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
package com.volantis.devrep.repository.api.devices;

/**
 * Constants used by the device repository.
 */
public interface DeviceRepositorySchemaConstants {

    /**
     * The name of the hierarchy document.
     */
    public static final String HIERARCHY_DOCUMENT_NAME = "hierarchy";

    /**
     * The name of the identification document.
     */
    public static final String IDENTIFICATION_DOCUMENT_NAME = "identification";

    /**
     * The name of the TAC identification document.
     */
    public static final String TAC_IDENTIFICATION_DOCUMENT_NAME =
            "tac-identification";

    /**
     * The name of the definitions document.
     */
    public static final String DEFINITIONS_DOCUMENT_NAME = "definitions";

    /**
     * The name of the hierarhcy element of the hierarchy document.
     */
    public static final String HIERARCHY_ELEMENT_NAME = "hierarchy";

    /**
     * The name of the identification element of the identification document.
     */
    public static final String IDENTIFICATION_ELEMENT_NAME = "identification";

    /**
     * The name of the tacIdentification element of the TAC identification
     * document.
     */
    public static final String TAC_IDENTIFICATION_ELEMENT_NAME =
            "tacIdentification";

    /**
     * The name used to describe who created an element.
     */
    public static final String CHANGE_TRACKING_CREATED_BY = "createdBy";

    /**
     * The name used to declare when an element was created.
     */
    public static final String CHANGE_TRACKING_CREATED_TIME = "creationTime";

    /**
     * The name used to describe who last modified an element.
     */
    public static final String CHANGE_TRACKING_MODIFIED_BY = "modifiedBy";

    /**
     * The name used to declare when an element was last modified.
     */
    public static final String CHANGE_TRACKING_MODIFIED_TIME = "modificationTime";
    
    /**
     * The name of the definitions element.
     */
    public static final String DEFINITIONS_ELEMENT_NAME = "definitions";

    /**
     * The name of the category element of the policy definitions.
     */
    public static final String CATEGORY_ELEMENT_NAME = "category";

    /**
     * The name of the category name attribute.
     */
    public static final String CATEGORY_NAME_ATTRIBUTE = "name";

    /**
     * The name of the category prefix attribure.
     */
    public static final String CATEGORY_PREFIX_ATTRIBUTE = "prefix";

    /**
     * Constant designating the name of the custom category.
     */
    public static final String CUSTOM_CATEGORY_NAME = "custom";

    /**
     * The name of the device element.
     */
    public static final String DEVICE_ELEMENT_NAME = "device";

    /**
     * The name of the device name attribute.
     */
    public static final String DEVICE_NAME_ATTRIBUTE = "name";

    /**
     * The name of the userAgentPattern element.
     */
    public static final String USER_AGENT_PATTERN_ELEMENT_NAME
            = "userAgentPattern";

    /**
     * The name of the headerPattern element.
     */
    public static final String HEADER_PATTERN_ELEMENT_NAME = "headerPattern";

    /**
     * The name attribute of the headerPattern element.
     */
    public static final String HEADER_PATTERN_NAME_ATTRIBUTE = "name";

    /**
     * The baseDevice attribute of the headerPattern element.
     */
    public static final String HEADER_PATTERN_BASE_DEVICE_ATTRIBUTE =
            "baseDevice";

    /**
     * The name of the regularExpression element.
     */
    public static final String REGULAR_EXPRESSION_ELEMENT_NAME =
            "regularExpression";

    /**
     * The name of the policies element of a device element.
     */
    public static final String POLICIES_ELEMENT_NAME = "policies";

    /**
     * The name of the policy element of a device element.
     */
    public static final String POLICY_ELEMENT_NAME = "policy";
    
    /**
     * The name of the name attribute of a policy.
     */
    public static final String POLICY_NAME_ATTRIBUTE = "name";
    /**
     * The name of the name attribute of a policy.
     */
    public static final String POLICY_VALUE_ATTRIBUTE = "value";

    /**
     * The name of the value element of a policy.
     */
    public static final String POLICY_VALUE_ELEMENT_NAME = "value";

    /**
     * The name of the standard element.
     */
    public static final String STANDARD_ELEMENT_NAME = "standard";

    /**
     * The name of the inherit element.
     */
    public static final String INHERIT_ELEMENT_NAME = "inherit";

    /**
     * The name of the revision attribute in the repository document.
     */
    public static final String REVISION_ATTRIBUTE_NAME = "revision";

    /**
     * Constant for the policy defintion elements name attribute
     */
    public static final String POLICY_DEFINITION_NAME_ATTRIBUTE = "name";

    /**
     * The name of the selection element of a policy definition.
     */
    public static final String POLICY_DEFINITION_SELECTION_ELEMENT_NAME =
            "selection";

    /**
     * The name of the keyword element of a policy definition.
     */
    public static final String POLICY_DEFINITION_KEYWORD_ELEMENT_NAME =
            "keyword";

    /**
     * The name of the boolean element of a policy definition.
     */
    public static final String POLICY_DEFINITION_BOOLEAN_ELEMENT_NAME =
            "boolean";

    /**
     * The name of the int element of a policy definition.
     */
    public static final String POLICY_DEFINITION_INT_ELEMENT_NAME = "int";

    /**
     * The name of the orderedSet element of a policy definition.
     */
    public static final String POLICY_DEFINITION_ORDEREDSET_ELEMENT_NAME =
            "orderedSet";

    /**
     * The name of the range element of a policy definition.
     */
    public static final String POLICY_DEFINITION_RANGE_ELEMENT_NAME = "range";

    /**
     * The name of the text element of a policy definition.
     */
    public static final String POLICY_DEFINITION_TEXT_ELEMENT_NAME = "text";

    /**
     * The name of the unorderedSet element of a policy definition.
     */
    public static final String POLICY_DEFINITION_UNORDEREDSET_ELEMENT_NAME =
            "unorderedSet";

    /**
     * The name of the type element of a policy definition.
     */
    public static final String POLICY_DEFINITION_TYPE_ELEMENT_NAME = "type";

    /**
     * The name of the types element of a policy definition.
     */
    public static final String POLICY_DEFINITION_TYPES_ELEMENT_NAME = "types";


    /**
     * The name of the type element name attribute.
     */
    public static final String POLICY_DEFINITION_TYPE_NAME_ATTRIBUTE_NAME =
            "name";
    /**
     * The name of the structure element of a policy definition.
     */
    public static final String POLICY_DEFINITION_STRUCTURE_ELEMENT_NAME =
            "structure";

    /**
     * The name of the field element of a policy definition.
     */
    public static final String POLICY_DEFINITION_FIELD_ELEMENT_NAME = "field";

    /**
     * Constant for the type elements ref attribute
     */
    public static final String POLICY_DEFINITION_REF_ATTRIBUTE_NAME = "ref";

    /**
     * The name of the field name attribute.
     */
    public static final String POLICY_DEFINITION_FIELD_NAME_ATTRIBUTE = "name";

    /**
     * The name of the field value attribute.
     */
    public static final String POLICY_DEFINITION_FIELD_VALUE_ATTRIBUTE = "value";

    /**
     * Constant for the "enable" value of a field elements name attribute
     */
    public static final String POLICY_DEFINITION_ENABLE_FIELD_NAME = "enable";

    /**
     * Constant for the "EmulateEmphasisTag" name
     */
    public static final String POLICY_DEFINITION_EMULATE_EMPHASIS_TAG_NAME =
            "EmulateEmphasisTag";

    /**
     * The minInclusive attribute name of the range type.
     */
    public static final String RANGE_MIN_INCLUSIVE_ATTRIBUTE = "minInclusive";

    /**
     * The maxInclusive attribute name of the range type.
     */
    public static final String RANGE_MAX_INCLUSIVE_ATTRIBUTE = "maxInclusive";

    /**
     * The UAProf element name.
     */
    public static final String UAPROF_ELEMENT_NAME = "UAProf";

    /**
     * The UAProf attribute name.
     */
    public static final String UAPROF_ATTRIBUTE_NAME = "attribute";

    /**
     * The vocabulary attribute name.
     */
    public static final String VOCABULARY_ATTRIBUTE_NAME = "ccppVocabulary";

    /**
     * The number element name.
     */
    public static final String NUMBER_ELEMENT_NAME = "number";
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Apr-05	7834/2	matthew	VBM:2005041518 Add support for policy and category definition events in the device repository merge

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Nov-04	4394/2	allan	VBM:2004051018 Undo/Redo in device editor.

 14-May-04	4301/4	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 14-May-04	4301/2	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 08-Sep-04	5432/1	allan	VBM:2004081803 Validation for range min and max values

 04-Aug-04	5065/1	adrianj	VBM:2004080214 Added foundations for device lookup by TAC in XML repository

 17-May-04	4442/5	pcameron	VBM:2004042608 Usages of constants from DeviceRepositorySchemaConstants in XMLDeviceRepositoryAccessor

 13-May-04	4321/1	doug	VBM:2004051202 Added label decorating to the device hierarchy tree

 11-May-04	4161/2	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 10-May-04	4237/1	byron	VBM:2004031601 Provide the CCPP form section - update

 04-May-04	4007/1	doug	VBM:2004032304 Added a PrimaryPatterns form section

 27-Apr-04	4016/2	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 26-Apr-04	4040/3	pcameron	VBM:2004032211 DeviceStructurePart uses definitions element name

 22-Apr-04	3975/1	allan	VBM:2004042005 Fix multi-value policy migration and related issues.

 21-Apr-04	3935/4	allan	VBM:2004020906 Fix merge issues.

 20-Apr-04	3935/1	allan	VBM:2004020906 Migration, Device Browser & Import support for policy fields.

 20-Apr-04	3909/1	pcameron	VBM:2004031004 Added CategoryCompositeBuilder

 13-Apr-04	3683/5	pcameron	VBM:2004030401 Added PolicyController

 08-Apr-04	3686/8	pcameron	VBM:2004032204 Some further tweaks to PolicyType

 06-Apr-04	3686/6	pcameron	VBM:2004032204 Added new methods to PolicyType and refactored

 02-Apr-04	3659/3	allan	VBM:2004033002 Update client/server enhancements.

 01-Apr-04	3602/8	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 01-Apr-04	3574/7	allan	VBM:2004032401 Implement merging of device hierarchies.

 29-Mar-04	3574/5	allan	VBM:2004032401 Completed device repository merging. Needs more testing.

 23-Mar-04	3546/1	pcameron	VBM:2004031102 Added getPolicyNames to DeviceRepositoryAccessorManager

 22-Mar-04	3480/1	pcameron	VBM:2004030410 Added some keyword constants and some element name checking

 18-Mar-04	3416/1	pcameron	VBM:2004022309 Added ListBuilder and ListPolicyValueModifier with tests

 10-Mar-04	3383/5	pcameron	VBM:2004030412 Added exception to PolicyValueSelectionDialog constructor

 10-Mar-04	3383/1	pcameron	VBM:2004030412 Added PolicyValueSelectionDialog

 ===========================================================================
*/
