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
package com.volantis.mcs.eclipse.ab.editors.devices.validation;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidator;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.xpath.XPath;
import org.jdom.Element;
import org.jdom.Attribute;

import java.math.BigDecimal;

/**
 * Supplementary validator that validates the range element of the
 * device definitions document.
 */
public class RangeValidator implements DOMSupplementaryValidator {

    /**
     * Validate that the min range value is less than or equal to the
     * max range value.
     * @param element the range Element
     * @param errorReporter the ErrorReporter to report any errors to
     */
    public void validate(Element element,
                         ErrorReporter errorReporter) {

        if (element == null) {
            throw new IllegalArgumentException("Cannot be null: element");
        }

        if (!element.getName().
                equals(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_RANGE_ELEMENT_NAME)) {
            throw new IllegalArgumentException("Expected element named \"" +
                    DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_RANGE_ELEMENT_NAME +
                    "\" but got element named \"" + element.getName() + "\"");
        }

        String minString = null;
        Attribute min = element.getAttribute(DeviceRepositorySchemaConstants.
                RANGE_MIN_INCLUSIVE_ATTRIBUTE);
        if (min != null) {
            minString = min.getValue();
        }

        String maxString = null;
        Attribute max = element.getAttribute(DeviceRepositorySchemaConstants.
                RANGE_MAX_INCLUSIVE_ATTRIBUTE);
        if (max != null) {
            maxString = max.getValue();
        }

        if (minString != null && minString.length() > 0 &&
                maxString != null && maxString.length() > 0) {
            // Use BigDecimals since min & max can be any number
            BigDecimal minDecimal = getBigDecimal(minString, element,
                    errorReporter, min);
            BigDecimal maxDecimal = getBigDecimal(maxString, element,
                    errorReporter, max);
            if (minDecimal != null && maxDecimal != null &&
                    minDecimal.compareTo(maxDecimal) == 1) {
                ErrorDetails details = new ErrorDetails(element, new XPath(element),
                        null, FaultTypes.MIN_RANGE_MORE_THAN_MAX, null, null);
                errorReporter.reportError(details);
            }
        }
    }

    /**
     * Get a validated big decimal number, or return null and add the
     * appropriate validation message.
     *
     * @param value the number as a string.
     * @param element
     * @param errorReporter
     * @return a big decimal number or null if it was invalid.
     */
    private BigDecimal getBigDecimal(String value, Element element,
            ErrorReporter errorReporter, Attribute attribute) {
        BigDecimal result = null;
        try {
            result = new BigDecimal(value);
        } catch (NumberFormatException e) {
            ErrorDetails details = new ErrorDetails(element, new XPath(attribute),
                    null, FaultTypes.NOT_A_NUMBER, value, null);
            errorReporter.reportError(details);
        }
        return result;
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

 02-Dec-04	6354/1	adrianj	VBM:2004112605 Refactor XML validation error reporting

 08-Sep-04	5432/2	allan	VBM:2004081803 Validation for range min and max values

 ===========================================================================
*/
