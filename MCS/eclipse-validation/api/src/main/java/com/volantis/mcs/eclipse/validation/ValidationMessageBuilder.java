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
package com.volantis.mcs.eclipse.validation;

import com.volantis.mcs.utilities.FaultTypes;

import java.util.ResourceBundle;
import java.util.Map;
import java.text.MessageFormat;


/**
 * When creating a message associated with validation it is sometimes
 * necessary to combine disparate pieces of information into a single
 * sensible message. For example combining the part of the message that
 * specifies what the problem is with the name of the thing that the
 * problem applies to. ValidationMessageBuilder is for objects that
 * build messages from one or more sources so that the message is
 * pertinent to the situtation within which it will be used.
 */
public class ValidationMessageBuilder implements FaultTypes {

    /**
     * Volantis copyright.
     */
    private static String mark =
            "(c) Volantis Systems Ltd 2003. "; //$NON-NLS-1$


    /**
     * The bundle containing localized validation message formats.
     */
    final static ResourceBundle DEFAULT_BUNDLE =
            ResourceBundle.getBundle("com.volantis.mcs.eclipse.validation.ValidationMessages"); //$NON-NLS-1$

    /**
     * The messageKeyMappings for this ValidationMessageBuilder.
     */
    private Map messageKeyMappings = null;

    /**
     * The format args to prepend to any provided to the
     * buildValidationMessage() method.
     */
    private Object supplementaryFormatArgs [] = null;

    /**
     * The bundle for this ValidationMessageBuilder to use in preference
     * to the default bundle.
     */
    private ResourceBundle bundle = null;


    /**
     * Construct a new ValidationMessageBuilder with the given
     * ResourceBundle and Map of Error type -> message key mappings.
     * @param bundle The ResourceBundle to use in preference to a
     * default. Can be null.
     * @param messageKeyMappings The Map of fault type -> message key
     * mappings where the given message keys correspond to property keys
     * in the provided ResourceBundle. Can be null. If the message key
     * for a generated fault type is not found in the map then a default
     * key will be used with a default bundle.
     * @param supplementaryFormatArgs Message format args that will be pre-
     * pended to all other message format args if any that are produced
     * by the Validator that is using this ValidationMessageBuilder.
     */
    public ValidationMessageBuilder(ResourceBundle bundle,
                                    Map messageKeyMappings,
                                    Object[] supplementaryFormatArgs) {
        this.bundle = bundle;
        this.messageKeyMappings = messageKeyMappings;
        setSupplementaryFormatArgs(supplementaryFormatArgs);
    }

    /**
     * Set the supplementary format arguments on this ValidationMessageBuilder.
     * @param supplementaryFormatArgs The supplementary format arguments that
     * will be pre-prended to any format arguments associated with a given
     * fault. Can be null.
     */
    public void setSupplementaryFormatArgs(Object [] supplementaryFormatArgs) {
        this.supplementaryFormatArgs = supplementaryFormatArgs;
    }
    
    /**
     * Build the localized validation message using the error message details
     * @param faultType The validation property key associated with the
     * error message to use.
     * @param faultTypeQualifier An optional piece of extra information
     * related to the fault.
     * @param formatArgs The argumnents for the error message.
     * @return The validation message.
     */
    public String buildValidationMessage(String faultType,
                                         String faultTypeQualifier,
                                         Object[] formatArgs) {

        String faultKey = faultType;
        if (SCHEMA_CONSTRAINT_VIOLATED.equals(faultType) &&
            messageKeyMappings != null &&
            bundle != null) {
            // the key needs to be created from the fault type and the
            // faultTypeQualifier. We only do this if we are not using the
            // default bundle and there is a mapping for the composite
            // key.
            StringBuffer buffer = new StringBuffer(
                faultType.length() + SCHEMA_CONSTRAINT_VIOLATED.length() + 1);
             buffer.append(SCHEMA_CONSTRAINT_VIOLATED)
                   .append(".") //$NON-NLS-1$
                   .append(faultTypeQualifier);

            String compositeKey = buffer.toString();
            if (messageKeyMappings.containsKey(compositeKey)) {
                // if a mapping has been provided then we use the composite key
                faultKey = compositeKey;
            }
        }
        return buildValidationMessage(faultKey, formatArgs);
    }

    /**
     * Build the localized validation message using the error message details
     * @param faultType The validation property key associated with the
     * error message to use.
     * @param formatArgs The argumnents for the error message.
     * @return The validation message.
     */
    String buildValidationMessage(String faultType,
                                  Object[] formatArgs) {


        // Set the bundle to use fot message creation.
        ResourceBundle bundleToUse = bundle == null ? DEFAULT_BUNDLE : bundle;

        // Establish the message key.
        String messageKey = messageKeyMappings != null ?
                (String) messageKeyMappings.get(faultType) :
                null;

        if (messageKey == null) {
            // Since no specific way to handle this kind of message has
            // been provided by the creator of this ValidationMessageBuilder,
            // we default to using in the faultType as the key and the
            // DEFAULT_BUNDLE for the bundle to use.
            messageKey = faultType;
            bundleToUse = DEFAULT_BUNDLE;
        } else {

            // Set up all of the format args.
            if (supplementaryFormatArgs != null &&
                    supplementaryFormatArgs.length > 0) {
                if (formatArgs == null || formatArgs.length == 0) {
                    formatArgs = supplementaryFormatArgs;
                } else {
                    // We need to combine the provided formatArgs with those
                    // provided by the creator of this ValidationMessageBuilder.
                    Object tmpArgs [] =
                            new Object[supplementaryFormatArgs.length +
                            formatArgs.length];
                    for (int i = 0; i < supplementaryFormatArgs.length; i++) {
                        tmpArgs[i] = supplementaryFormatArgs[i];
                    }
                    for (int i = 0; i < formatArgs.length; i++) {
                        tmpArgs[supplementaryFormatArgs.length + i] =
                                formatArgs[i];
                    }
                    formatArgs = tmpArgs;
                }
            }
        }


        MessageFormat format =
                new MessageFormat(bundleToUse.getString(messageKey));

        return format.format(formatArgs);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Mar-04	3416/1	pcameron	VBM:2004022309 Added ListBuilder and ListPolicyValueModifier with tests

 27-Feb-04	3200/1	allan	VBM:2004022410 Basic Update Client Wizard.

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 09-Jan-04	2215/5	pcameron	VBM:2003112405 TimeSelectionDialog, ListValueBuilder and refactoring

 06-Jan-04	2323/4	doug	VBM:2003120701 Added better validation error messages

 02-Jan-04	2332/1	richardc	VBM:2003122902 Property name changes and associated knock-ons

 31-Dec-03	2321/2	pcameron	VBM:2003121807 Rework issues and enhanced validation for container name

 31-Dec-03	2306/1	richardc	VBM:2003121723 Added UniqueAssetValidator and applied to AssetsSection

 17-Dec-03	2213/1	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 17-Dec-03	2219/2	doug	VBM:2003121502 Added dom validation to the eclipse editors

 15-Dec-03	2160/2	doug	VBM:2003120702 Addressed some rework issues

 10-Dec-03	2084/9	doug	VBM:2003120201 Xerces based DOMValidator implementation

 09-Dec-03	2084/1	doug	VBM:2003120201 xerces based DOMValidator implementation

 10-Dec-03	2057/7	doug	VBM:2003112803 Addressed several rework issues

 09-Dec-03	2057/4	doug	VBM:2003112803 Added SAX XSD Validation mechanism

 02-Dec-03	2069/1	allan	VBM:2003111903 Basic ODOMEditorPart completed with skeleton ImageComponentEditor.

 27-Nov-03	2024/1	pcameron	VBM:2003111704 Added ColorListSelectionDialog

 31-Oct-03	1587/8	byron	VBM:2003101503 Create the Device Selector Tree View

 23-Oct-03	1587/4	byron	VBM:2003101503 Create the Device Selector Tree View

 20-Oct-03	1502/1	allan	VBM:2003092202 Completed validation for PolicySelector.

 30-Oct-03	1639/1	byron	VBM:2003101602 Create a MCS Project properties page - addressed various review issues

 27-Oct-03	1618/2	byron	VBM:2003100804 Create a new project wizard for MCS projects

 23-Oct-03	1608/12	pcameron	VBM:2003101607 Refactored Preview controls and made UnavailablePreview center the label and text

 21-Oct-03	1608/9	pcameron	VBM:2003101607 Added ImagePreview

 20-Oct-03	1502/1	allan	VBM:2003092202 Completed validation for PolicySelector.

 21-Oct-03	1502/4	allan	VBM:2003092202 Don't validate extension when minChars is < 1.

 20-Oct-03	1502/1	allan	VBM:2003092202 Completed validation for PolicySelector.

 20-Oct-03	1604/1	byron	VBM:2003092302 Implement a validator based on a selection

 10-Oct-03	1512/3	allan	VBM:2003100702 Generic policy wizard with first wizard page

 03-Oct-03	1444/1	allan	VBM:2003091903 Port the validation framework to an Eclipse plugin

 ===========================================================================
*/
