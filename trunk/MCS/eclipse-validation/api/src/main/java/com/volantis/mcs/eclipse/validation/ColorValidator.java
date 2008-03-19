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

import com.volantis.mcs.eclipse.common.Convertors;
import com.volantis.mcs.eclipse.common.NamedColor;
import com.volantis.mcs.utilities.FaultTypes;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.graphics.RGB;

/**
 * Validator that validates colors. The color is either a color name or
 * a hex value. The color name must be contained in a specified collection
 * of colors. The hex value must translate to a valid RGB color triple.
 */
public class ColorValidator extends SimpleValidator {

    /**
     * The array of colors that the validator works with.
     */
    private NamedColor[] colors = null;

    /**
     * Construct the validator with the specified colors..
     * @param colors the array of colors to use in the validation.
     */
    public ColorValidator(NamedColor[] colors) {
        this.colors = colors;
    }


    /**
     * Checks that a value is either a color name present in
     * the array of colors that the validator is working with,
     * or a hex value corresponding to a valid RGB triple.
     * @param value
     * @return boolean flag
     */
    private boolean isValidValue(String value) {
        boolean valid = false;
        if (value == null || value.length() == 0) {
            valid = true;
        } else {
            value = value.trim();
            if (value.length() > 0) {
                if (value.charAt(0) == '#') {
                    try {
                        RGB newRGB = Convertors.hexToRGB(value);
                        valid = newRGB != null;
                    } catch (NumberFormatException nfe) {
                        valid = false;
                    }
                } else {
                    for (int i = 0; i < colors.length && !valid; i++) {
                        if (colors[i].getName().equalsIgnoreCase(value)) {
                            valid = true;
                        }
                    }
                }
            }
        }
        return valid;
    }

    /**
     * Validate the specified object using the given message builder to
     * build any generated error messages.
     *
     * The fault types and message format args produced specifically by this
     * method are as follows:
     *
     * Fault type: INVALID_COLOR; args: object
     */
    // rest of javadoc inherited
    public ValidationStatus validate(Object object,
                                     ValidationMessageBuilder messageBuilder) {
        ValidationStatus status = super.validate(object, messageBuilder);
        if (status.isOK() && !isValidValue((String) object)) {
            String faultType = FaultTypes.INVALID_COLOR;
            Object formatArgs [] = new Object[] {object};
            String message = messageBuilder.buildValidationMessage(
                    faultType, formatArgs);
            status = new ValidationStatus(Status.ERROR, message);
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

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 27-Nov-03	2024/1	pcameron	VBM:2003111704 Added ColorListSelectionDialog

 25-Nov-03	1634/8	pcameron	VBM:2003102205 Refactored to use the ColorValidator in the ColorSelector

 25-Nov-03	1634/6	pcameron	VBM:2003102205 A few changes to ColorSelector

 25-Nov-03	1634/4	pcameron	VBM:2003102205 A few changes to ColorSelector

 21-Nov-03	1634/1	pcameron	VBM:2003102205 Added ColorSelector, NamedColor and supporting classes

 ===========================================================================
*/
