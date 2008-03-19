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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.forms.validation;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Parse a text input format, make sure that the format is valid and determine
 * whether an empty field is ok..
 */
public class TextInputFormatParser {

    /**
     * The format to return to force empty ok to be set even when validation
     * is not.
     */
    private static final TextInputFormat FORCE_EMPTY_OK =
            new TextInputFormat(null, true);

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(TextInputFormatParser.class);

    /**
     * True if the parser should always assume that empty is ok, false
     * otherwise.
     */
    private final boolean forceEmptyOk;

    /**
     * Initialise.
     *
     * <p>The need to force the parser to set empty ok was because the
     * WMLEmptyOK1_3 protocol always forced it to be true. However, it is not
     * clear whether that was a valid thing to do, it all depends on why the
     * WMLEmptyOK1_3 protocol was created.</p>
     *
     * @param forceEmptyOk True if the parser should always assume that empty is
     *                     ok, false otherwise.
     */
    public TextInputFormatParser(boolean forceEmptyOk) {
        this.forceEmptyOk = forceEmptyOk;
    }

    /**
     * Parser the format.
     *
     * @param fieldName The name of the field, for logging purposes.
     * @param format    The format to parse.
     * @return The {@link TextInputFormat} instance, or null if the format
     *         was null, or the format was invalid.
     */
    public TextInputFormat parseFormat(String fieldName, String format) {

        if (format == null) {
            if (forceEmptyOk) {
                return FORCE_EMPTY_OK;
            }
            return null;
        }

        boolean valid = true;

        // cannot end with *
        // not valid to have 2 or more adjacent *
        if (format.length() < 3 || format.endsWith("*") ||
                format.indexOf("**") != -1) {
            valid = false;
        }

        boolean emptyOk = forceEmptyOk;
        if (valid) {
            // Test if the first character is lower case
            // If so seet the emptyOK on the attributes and
            // continue testing for pattern validity
            String pattern = format.substring(2);

            if (Character.isLowerCase(format.charAt(0))) {
                emptyOk = true;
            }
            if (format.charAt(1) == ':') {
                switch (Character.toUpperCase(format.charAt(0))) {
                    case'N':
                        // Numeric pattern
                        // Only valid characters are
                        // #  numeric, .  decimal, * 0 or more
                        if (!strContainsOnly(pattern, "#.*")) {
                            valid = false;
                        }
                        break;
                    case'M':
                        if (!strContainsOnly(pattern, "*XxAaMm#S ")) {
                            valid = false;
                        }
                        break;
                    case'P':
                        // Phone no. pattern
                        // Only valid characters are
                        // #  numeric, - seperator, + country prefix
                        // * 0 or more
                        if (!strContainsOnly(pattern, "*#-+")) {
                            valid = false;
                        }
                        break;
                    case'D':
                        // Date and or time pattern
                        // Only valid characters are
                        // D day, M month, Y year, / seperator, - seperator
                        // H hour, m mins, s secs, : seperator, a PM indicator
                        if (!strContainsOnly(pattern, "DMYHmsa/-: ")) {
                            valid = false;
                        }
                        break;
                    default:
                        valid = false;
                }
            }
        }

        TextInputFormat inputFormat = null;
        if (valid) {
            inputFormat = new TextInputFormat(format, emptyOk);
        } else {
            logger.warn("xftextinput-format-attribute-invalid",
                    new Object[]{
                            fieldName, format
                    });
        }

        return inputFormat;
    }

    /**
     * Check to see whether the format contains only the allowed characters.
     *
     * @param format  The format to check.
     * @param allowed The allowed characters as a string.
     * @return True if it does only contain the allowed characters false
     *         otherwise.
     */
    private boolean strContainsOnly(String format, String allowed) {
        char c;
        for (int i = 0; i < format.length(); i++) {
            c = format.charAt(i);
            if (allowed.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }
}
