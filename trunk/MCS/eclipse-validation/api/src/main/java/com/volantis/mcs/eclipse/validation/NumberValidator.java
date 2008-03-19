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
 * $Header: /src/voyager/com/volantis/mcs/gui/validation/NumberDocument.java,v 1.4 2003/02/10 12:42:15 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 * ----------------------------------------------------------------------------
 * Date         Who             Description
 * --------    -------------    -----------------------------------------------
 * 30-Apr-02    Payal           VBM:2002041803 - Created document that
 *                              constrains input to integer values only.
 * 17-May-02    Payal           VBM:2002041803 - Modified insertString
 *                              to do isPositiveInteger check if length is
 *                              of string is > 0.
 * 22-May-02    Allan           VBM:2002052201 - Fixed bug  in insertString()
 *                              where the validation condition was incorrect.
 *                              Replaced positiveInteger with
 *                              hasPositiveConstraint. Replaced method
 *                              isPositiveInteger() with isValidChar(). Added
 *                              validateContent(). Extend ValidatedDocument.
 * 26-May-02    Allan           VBM:2002052201 - Check super.validateContent()
 *                              in validateContent() and set the appropriate
 *                              message key when the content is invalid.
 * 13-Jun-02    Allan           VBM:2002030615 - Added setContentObject() and
 *                              getContentObject().
 * 21-Jun-02    Doug            VBM:2002052102 - fixed null pointer exception
 *                              in validateContent() method.
 * 25-Jul-02    Allan           VBM:2002072509 - In getContentObject(),
 *                              super.getContentObject() will return null if
 *                              there is no value in the document. This
 *                              was causing a NullPointerException and is now
 *                              checked.
 * 05-Nov-02    Allan           VBM:2002110506 - Add support for range
 *                              validation via min and max value properties.
 *                              This has made hasPositiveConstraint obselete
 *                              it has been removed and the constructor
 *                              deprecated.
 * 08-Nov-02    Allan           VBM:2002110801 - Override getValidationMessage
 *                              to report min and max value violations.
 * 12-Nov-02    Allan           VBM:2002111206 - Moved from
 *                              com.volantis.mcs.gui to
 *                              com.volanti.mcs.gui.validation.
 * 26-Nov-02    Adrian          VBM:2002111503 - Renamed from IntDocument and
 *                              made more generic such that decimal values are
 *                              now allowed.  Added new constructor with flag
 *                              to indicate if decimals are permitted.  Updated
 *                              isValidChar to allow '.' char if decimals are
 *                              permitted, and validateContent to check that
 *                              only one '.' char exist in the document.
 * 14-Jan-03    Allan           VBM:2002112703 - Added to do against
 *                              getValidationmessage().
 * 04-Feb-03    Geoff           VBM:2003012917 - Refactored to split out the
 *                              double behaviour into BigDecimalDocument and
 *                              int behaviour into BigIntegerDocument, and
 *                              clean up the constructors.
 *-----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.validation;

import com.volantis.mcs.utilities.number.NumberHelper;
import com.volantis.mcs.utilities.FaultTypes;
import org.eclipse.core.runtime.Status;

/**
 * Validator that validates numbers.
 */
public abstract class NumberValidator extends SimpleValidator {

    /**
     * Volantis copyright.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003. "; //$NON-NLS-1$

    /**
     * The minimum valid value for this NumberDocument, or null if it doesn't
     * have a minimum.
     */
    protected Number minValue;

    /**
     * The maximum valid value for this NumberDocument, or null if it doesn't
     * have a minimum.
     */
    protected Number maxValue;

    /**
     * The appropriate number helper for this kind of number document.
     */
    protected NumberHelper helper;

    /**
     * Construct a NumberDocument which operates on Numbers of the example
     * type provided, and has a value which is unlimited in range.
     *
     * @param example demonstrates the type to operate on.
     */
    protected NumberValidator(Number example) {
        helper = NumberHelper.getInstance(example);
    }

    /**
     * Construct a NumberDocument which operates on Numbers of the example
     * type provided, and has a value which is limited to a range bounded by
     * the minimum value provided and maximum positive value for that type.
     *
     * @param example demonstrates the type to operate on.
     */
    protected NumberValidator(Number example, Number min) {
        this(example);
        minValue = min;
    }

    /**
     * Construct a NumberDocument which operates on Numbers of the example
     * type provided, and has a value which is limited to a range bounded by
     * the minimum and maximum values provided.
     *
     * @param example demonstrates the type to operate on.
     */
    protected NumberValidator(Number example, Number min, Number max) {
        this(example);
        if (min != null && max != null && helper.isLessThan(max, min)) {
            throw new IllegalArgumentException("Max value " + max + " < " + //$NON-NLS-1$ //$NON-NLS-2$
                    "Min value " + minValue); //$NON-NLS-1$
        }
        minValue = min;
        maxValue = max;
    }

    /**
     * Checks that if char c is a valid part of a number.
     * @param c the char to check
     * @return true if c is a valid character in this NumberDocument; false
     * otherwise.
     */
    protected boolean isValidChar(char c) {
        return Character.isDigit(c) || (c == '.' && helper.isDecimal()) ||
                (c == '-' && (minValue == null || !helper.isPositive(minValue)));
    }


    /**
     * Validate the specified object using the given message builder to
     * build any generated error messages. If the super class finds the object
     * to be invalid, then its fault type and format args will be used.
     * The fault types and message format args produced specifically by this
     * method are as follows:
     *
     * Fault type: INVALID_CHARACTER; args: object, invalid character, invalid
     * character position.
     * Fault type: LESS_THAN_ZERO; args: object, min value
     * Fault type: NOT_A_NUMBER; args: object
     * Fault type: OUT_OF_RANGE; args: object, min value, max value
     * Fault type: LESS_THAN; args: object, min value, max value
     * Fault type: MORE_THAN; args: object, min value, max value
     */
    // rest of javadoc inherited
    public ValidationStatus validate(Object object,
                                     ValidationMessageBuilder messageBuilder) {
        ValidationStatus status = super.validate(object, messageBuilder);

        if (status.getSeverity() == Status.OK) {

            Object formatArgs [] = new Object[3];
            int index = 0;
            formatArgs[index++] = object;

            String messageKey = null;
            String content = (object != null) ? object.toString() : ""; //$NON-NLS-1$
            int i;
            char c = 0;
            boolean hasDecimalPoint = false;
            for (i = 0; i < content.length() && messageKey == null; i++) {
                c = content.charAt(i);
                if (c == '-' && (minValue != null &&
                        helper.isPositive(minValue))) {
                    messageKey = FaultTypes.LESS_THAN_ZERO;
                    formatArgs[index++] = minValue;
                } else if (c == '.') {
                    if (!helper.isDecimal() || (helper.isDecimal() && hasDecimalPoint)) {
                        messageKey = FaultTypes.INVALID_CHARACTER;
                        formatArgs[index++] = new Character(c);
                        formatArgs[index] = new Integer(i);
                    }
                    hasDecimalPoint = true;
                } else if (!isValidChar(c)) {
                    messageKey = FaultTypes.NOT_A_NUMBER;
                }
            }

            if (content.length() > 0 && messageKey == null &&
                    (minValue != null || maxValue != null)) {
                if ((content.charAt(0) != '-' && content.charAt(0) != '.')
                        || content.length() > 1) {
                    // Do a range check.
                    Number value = helper.valueOf(content);
                    if ((minValue != null &&
                            helper.isGreaterThan(minValue, value)) &&
                            (maxValue != null &&
                            helper.isLessThan(maxValue, value))) {
                        messageKey = FaultTypes.OUT_OF_RANGE;
                    } else if (minValue != null &&
                            helper.isGreaterThan(minValue, value)) {
                        messageKey = FaultTypes.LESS_THAN;
                    } else if (maxValue != null &&
                            helper.isLessThan(maxValue, value)) {
                        messageKey = FaultTypes.MORE_THAN;
                    }
                }

                if (messageKey != null) {
                    if (minValue != null) {
                        formatArgs[index++] = minValue;
                    }
                    if (maxValue != null) {
                        formatArgs[index] = maxValue;
                    }
                }
            }

            if (messageKey != null) {
                String message =
                        messageBuilder.buildValidationMessage(messageKey,
                                formatArgs);

                status = new ValidationStatus(Status.ERROR, message);
            }
        }

        return status;
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

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 10-Oct-03	1512/1	allan	VBM:2003100702 Generic policy wizard with first wizard page

 03-Oct-03	1444/1	allan	VBM:2003091903 Port the validation framework to an Eclipse plugin

 ===========================================================================
*/
