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
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.mcs.eclipse.validation.CharacterValidator;
import com.volantis.mcs.eclipse.validation.CharacterSetValidator;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

/**
 * A set of constant values for device information and constraints.
 */
public class DeviceConstants {
    /**
     * The maximum length for device names.
     */
    public static final int TEXT_MAX_LENGTH = 20;

    /**
     * The regular expression string for device names.
     */
    public static final String DEVICE_NAME_REGEXP_STRING = "[A-Za-z0-9\\-_@.]+";

    /**
     * The regular expression for device names.
     */
    public static RE DEVICE_NAME_REGULAR_EXPRESSION;

    /**
     * The CharacterValidator that will use pattern matching to determine
     * the validity of characters.
     */
    private static final CharacterValidator CHARACTER_VALIDATOR;

    /**
     * A character set validator for validating the structure of device names.
     */
    public static final CharacterSetValidator DEVICE_NAME_VALIDATOR;

    static {
        // Create and initialize the regular expression.
        try {
            DEVICE_NAME_REGULAR_EXPRESSION = new RE(DEVICE_NAME_REGEXP_STRING);
        } catch (RESyntaxException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

        // Create and initialize the character validator.
        CHARACTER_VALIDATOR = new CharacterValidator() {
            public boolean isValidChar(char c) {
                String charString = (new Character(c)).toString();
                return DEVICE_NAME_REGULAR_EXPRESSION.match(charString);
            }
        };

        // Create and initialize the character set validator.
        DEVICE_NAME_VALIDATOR = new CharacterSetValidator(CHARACTER_VALIDATOR);
        DEVICE_NAME_VALIDATOR.setMaxChars(TEXT_MAX_LENGTH);
        DEVICE_NAME_VALIDATOR.setMinChars(1);
    }
}
