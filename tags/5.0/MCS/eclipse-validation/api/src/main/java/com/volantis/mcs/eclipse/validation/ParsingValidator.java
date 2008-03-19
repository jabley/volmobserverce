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
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.validation;

import com.volantis.mcs.eclipse.validation.parsers.Parser;
import com.volantis.mcs.eclipse.validation.parsers.TextParseException;
import com.volantis.mcs.utilities.FaultTypes;
import org.eclipse.core.runtime.Status;

import java.io.EOFException;
import java.text.ParseException;

/**
 * A ConstrainedDocument that validates its content using a Parser.
 */
public class ParsingValidator extends CharacterSetValidator {

    /**
     * The log4j object to log to.
     */
    private static org.apache.log4j.Category logger
            = org.apache.log4j.Category.getInstance("com.volantis.mcs.eclipse.validation.ParsingValidator"); //$NON-NLS-1$

    /**
     * The Parser for this document.
     */
    Parser parser;

    /**
     * Construct a new ParsingValidator with a specified parser.
     * @param parser The Parser for this document.
     */
    public ParsingValidator(Parser parser) {
        this.parser = parser;
    }

    /**
     * Validate the specified object using the given message builder to
     * build any generated error messages. If the super class finds the object
     * to be invalid, then its fault type and format args will be used.
     * The fault types and message format args produced specifically by this
     * method are as follows:
     *
     * Fault type: UNEXPECTED_END; args: object
     * Fault type: INVALID_TEXT; args: object, invalid text, invalid text
     * position
     * Fault type: INVALID_CHARACTERS; args: object, invalid characters,
     * invalid character position
     */
    // rest of javadoc inherited
    public ValidationStatus validate(Object object,
                                     ValidationMessageBuilder messageBuilder) {
        ValidationStatus status = super.validate(object, messageBuilder);

        if (status.getSeverity() == Status.OK) {

            String text = (object != null) ? object.toString() : ""; //$NON-NLS-1$

            Object formatArgs [] = new Object[3];
            int index = 0;
            formatArgs[index++] = object;
            String messageKey = null;
            try {
                parser.parse(text);
            } catch (EOFException e) {
                messageKey = FaultTypes.UNEXPECTED_END;
            } catch (TextParseException e) {
                formatArgs[index++] = e.getText();
                formatArgs[index++] = new Integer(e.getErrorOffset());
                messageKey = (FaultTypes.INVALID_TEXT);
            } catch (ParseException e) {
                // Should happen only if the text is not valid.
                formatArgs[index++] = new Character(text.charAt(e.getErrorOffset()));
                formatArgs[index++] = new Integer(e.getErrorOffset());
                messageKey = FaultTypes.INVALID_CHARACTER;
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
