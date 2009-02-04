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
package com.volantis.mcs.utilities;

/**
 * Generic constants that identify types of fault.
 *
 * Note this class should be removed when the validation message generation
 * mechanism is replaced.
 */
public interface FaultTypes {
    /**
     * Fault type constants.
     */
    String CANNOT_BE_NULL = "cannotBeNull";
    String DUPLICATE_ASSET = "duplicateAsset";
    String DUPLICATE_NAME = "duplicateName";
    String INVALID_CHARACTER = "invalidCharacter";
    String INVALID_CHARACTERS = "invalidCharacters";
    String INVALID_COLOR = "invalidColor";
    String INVALID_DIRECTORY = "invalidDirectory";
    String INVALID_DEVICE_REPOSITORY_FILENAME =
            "invalidDeviceRepositoryFilename";
    String INVALID_FIRST_CHARACTER =
            "invalidFirstCharacter";
    String INVALID_FILENAME = "invalidFilename";
    String INVALID_HOUR = "invalidHour";
    String INVALID_TEXT = "invalidText";
    String INVALID_EXTENSION = "invalidExtension";
    String INVALID_MINUTE = "invalidMinute";
    String INVALID_SECOND = "invalidSecond";
    String INVALID_TIME = "invalidTime";
    String LESS_THAN = "lessThan";
    String LESS_THAN_ZERO = "lessThanZero";
    String MORE_THAN = "moreThan";
    String MUST_BE_IN = "mustBeIn";
    String MUST_NOT_BE_IN = "mustNotBeIn";
    String MUST_START_WITH = "mustStartWith";
    String NOT_A_NUMBER = "notANumber";
    String NOT_IN_PROJECT = "notInProject";
    String NOT_IN_REPOSITORY = "notInRepository";
    String NOT_IN_SELECTION = "notInSelection";
    String NOT_WRITEABLE = "notWriteable";
    String OUT_OF_RANGE = "outOfRange";
    String TOO_FEW_CHARACTERS = "tooFewCharacters";
    String TOO_MANY_CHARACTERS = "tooManyCharacters";
    String TOO_MANY_IN = "tooManyIn";
    String UNEXPECTED_END = "unexpectedEnd";
    String ZERO_TIME = "zeroTime";
    /**
     * Constant for an invalid element location error
     */
    String INVALID_ELEMENT_LOCATION =
            "invalidElementLocation";
    /**
     * Constant for an invalid element content error
     */
    String INVALID_ELEMENT_CONTENT =
            "invalidElementContent";
    /**
     * Constant for an invalid attribute location error
     */
    String INVALID_ATTRIBUTE_LOCATION =
            "invalidAttributeLocation";
    /**
     * Constant for an invalid attribute content error
     */
    String INVALID_ATTRIBUTE_CONTENT =
            "invalidAttributeContent";
    /**
     * Constant for an invalid value for a schema data type
     */
    String INVALID_SCHEMA_DATA_TYPE
            = "invalidSchemaDataType";

    /**
     * Constant for an invalid value for a policy name.
     */
    String INVALID_POLICY_NAME
            = "invalidPolicyName";

    /**
     * Constant for an invalid value for schema pattern value
     */
    String INVALID_SCHEMA_PATTERN_VALUE
            = "invalidSchemaPatternValue";
    /**
     * Constant for a violation of a schema constraint
     */
    String SCHEMA_CONSTRAINT_VIOLATED
            = "schemaConstraintViolated";
    /**
     * Constant for error that occurs when a required attribute is missing.
     */
    String MISSING_ATTRIBUTE = "missingAttribute";
    /**
     * Constant for when a max inclusive constraint is violated. This occurs
     * when a numeric value exceeds a specified upper bound
     */
    String MAX_INCLUSIVE_VIOLATED = "maxInclusiveViolated";
    /**
     * Constant for when a min inclusive constraint is violated. This occurs
     * when a numeric value exceeds a specified lower bound
     */
    String MIN_INCLUSIVE_VIOLATED = "minInclusiveViolated";
    /**
     * Constant for when specifing the minimum value of a range that is more
     * than the maximum value for the range.
     */
    String MIN_RANGE_MORE_THAN_MAX = "minRangeMoreThanMax";

    /**
     * Constant for when a max length constraint is violated
     */
    String MAX_LENGTH_VIOLATED = "maxLengthViolated";
    /**
     * Constant for when the value is not in the available selection.
     */
    String INVALID_SELECTION = "invalidSelection";

    /**
     * Constant for when a null asset and a targetable asset target thw
     * same device
     */
    String INVALID_NULL_ASSET = "invalidNullAsset";

    /**
     * Constant for an unknown XML validation error. This is used as a last
     * resort   "catch-all" error.
     */
    String UNKNOWN_INVALID_XML =
            "unknownInvalidXML";
    /**
     * Constant for when a unique constraint is violated.
     */
    String DUPLICATE_UNIQUE = "duplicateUnique";

    /**
     * Constant for when a value is required that is non-whitespace and
     * non-empty
     */
    String WHITESPACE = "whitespace";

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/2	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Sep-04	5488/3	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/3	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 08-Sep-04	5432/1	allan	VBM:2004081803 Validation for range min and max values

 16-Aug-04	5206/4	allan	VBM:2004081201 Auto-migration of mdpr with dialog.

 13-Aug-04	5187/5	adrianj	VBM:2004080302 UAProf URI caching mechanism

 13-Aug-04	5036/2	byron	VBM:2004080202 Public API for device lookup by TAC or UAProf URI (umbrella) - fix merge issues

 11-Aug-04	5126/3	adrian	VBM:2004080303 Added GUI support for Device TACs

 11-Aug-04	5126/1	adrian	VBM:2004080303 Added GUI support for Device TACs

 09-Aug-04	5130/2	doug	VBM:2004080310 MCS

 30-Mar-04	3614/1	byron	VBM:2004022404 Layout: Panes are allowed same names

 19-Mar-04	3471/1	byron	VBM:2004030504 Component Wizard does not add / to front of fallback components

 27-Feb-04	3246/1	byron	VBM:2004021205 Lack of validation for policy file extensions

 18-Feb-04	3068/3	allan	VBM:2004021115 Rework issues.

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 ===========================================================================
*/
