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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/Attic/WMLDollarEncoder.java,v 1.1.2.1 2003/04/16 15:41:28 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Apr-03    steve           VBM:2003041501 Encode a WML String
 * 28-May-03    Steve           VBM:2003042206 - Patch 2003041501 from Metis
 * 30-May-03    Mat             VBM:2003042911 Add a null to the end of the
 *                              the string in parse so that variables on the
 *                              end of a line are parsed correctly.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * WMLDollarEncoder
 * Class to parse some WML text and perform dollar substitutions on it.
 * This is almost the same as WMLDollarEncoderWriter except that we are writing
 * to a string instead of a Writer object. The reason we have two 'versions' of
 * this is that a general version would be too slow for both applications and
 * would create unneccessary garbage for one case or the other.
 *
 * @author steve
 */
public final class WMLDollarEncoder {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    WMLDollarEncoder.class);

    /**
     * States
     */
    private final int WRITE_THROUGH = 0;
    private final int FOUND_DOLLAR = 1;
    private final int IN_VARIABLE = 2;
    private final int IN_ESCAPE = 3;

    /**
     * Current state
     */
    private int state = WRITE_THROUGH;

    /**
     * Legal 'escape' characters
     */
    private final String esclegal = "acenopsu";
    private char escTerm = WMLVariable.WMLV_BRACKETED;

    /**
     * Character buffer
     */
    private char[] buff;
    private int buffIdx;

    /**
     * Create an instance of WMLDollarEncoder
     */
    public WMLDollarEncoder() {
    }

    /**
     * Reset the dollar encoder
     */
    public void reset() {
        buff = null;
        buffIdx = 0;
        state = WRITE_THROUGH;
        escTerm = WMLVariable.WMLV_BRACKETED;
    }

    /**
     * Parse WML into Mariner WML format with encoded variable
     * declarations and $ substitutions. The converted WML is
     * returned.
     *
     * @param wml  WML as a string
     * @return encoded WML
     * @throws WMLVariableException if invalid variable references are found
     */
    public String encode(String wml)
            throws WMLVariableException {

        String encoded = wml;
        int idx = 0;
        
        // If the WML does not contain any $ characters, do nothing
        idx = wml.indexOf('$');
        if (idx > -1) {
            // Count the number of $ characters
            int dollarCount = 1;
            while (idx > -1) {
                idx = wml.indexOf('$', idx + 1);
                dollarCount++;
            }
            
            // Allocate a character array big enough to hold the encoded data
            // the character count plus an extra character for each $ will be
            // more than enough.
            buff = new char[wml.length() + dollarCount];
            parse(wml);
            encoded = String.copyValueOf(buff, 0, buffIdx);
        }
        return encoded;
    }


    /**
     * Write a portion of an array of characters.
     *
     * @param wml  WML source to encode
     * @throws WMLVariableException if invalid variable references are found
     */
    private void parse(String wml) throws WMLVariableException {
        buffIdx = 0;
        int idx;
        char chr;
        
        // Copy until we get to the first $
        for (idx = 0; idx < wml.length(); idx++) {
            chr = wml.charAt(idx);
            if (chr == '$') {
                break;
            }
            write(chr);
        }
        
        // Start processing
        while (idx < wml.length()) {
            writeCharacter(wml.charAt(idx));
            idx++;
        }
        writeCharacter('\0');
    }

    /**
     * Write a character to the output buffer.
     *
     * @param chr the character to write
     */
    private void write(char chr) {
        buff[buffIdx] = chr;
        buffIdx++;
    }

    /**
     * Output a character
     *
     * @param chr  the character to write
     * @throws WMLVariableException if invalid variable references are found
     */
    private void writeCharacter(char chr)
            throws WMLVariableException {
        boolean retry = true;

        while (retry) {
            retry = false;
            switch (state) {
                case WRITE_THROUGH:
                    writeThrough(chr);
                    break;
                case FOUND_DOLLAR:
                    handleDollar(chr);
                    break;
                case IN_VARIABLE:
                    retry = handleVariable(chr);
                    break;
                case IN_ESCAPE:
                    handleEscape(chr);
                    break;
                default:
                    throw new WMLVariableException(exceptionLocalizer.format(
                            "illegal-encoding-state",
                            new Integer(state)));
            }
        }
    }

    /**
     * Write a character through to the underlying writer unless it is a $
     *
     * @param chr The processed character
     */
    private void writeThrough(char chr) {
        if (chr == '$') {
            state = FOUND_DOLLAR;
        } else if (chr == '\0') {
            // Do nothing
        } else {
            write(chr);
        }
    }

    /**
     * Handle the character following a $ in the character stream.
     *
     * @param chr  The character following the $ character
     * @throws WMLVariableException if invalid variable references are found
     */
    private void handleDollar(char chr)
            throws WMLVariableException {
        if (chr == '$' || chr == '\0') {
            // $$ sequence so write a single $
            write('$');
            state = WRITE_THROUGH;
        } else {
            if (chr == '(') {
                // $( sequence...  start of variable
                write(WMLVariable.WMLV_BRACKETED);
            } else if (Character.isLetter(chr)) {
                // $v sequence... non bracketed variable
                write(WMLVariable.WMLV_NOBRACKETS);
                escTerm = WMLVariable.WMLV_NOBRACKETS;
                write(chr);
            } else {
                throw new WMLVariableException(exceptionLocalizer.format(
                        "wml-variable-illegal-character-start",
                    new Character(chr)));
            }
            state = IN_VARIABLE;
        }
    }

    /**
     * Handle the characters that make up a variable name
     *
     * @param chr the character being processed
     * @return true if the character should be re-processed as the start of the
     *         next block
     * @throws WMLVariableException if invalid variable references are found
     */
    private boolean handleVariable(char chr) throws WMLVariableException {
        boolean retry = false;
        if (Character.isLetter(chr) || Character.isDigit(chr) ||
                (chr == '_')) {
            write(chr);
        } else if (escTerm == WMLVariable.WMLV_NOBRACKETS) {
            // We have come to the end of a non-bracketed variable
            write(WMLVariable.WMLV_NOBRACKETS);
            escTerm = WMLVariable.WMLV_BRACKETED;
            state = WRITE_THROUGH;
            // Go around again with the terminating character
            retry = true;
        } else if (chr == ':') {
            // Got an escape character in the middle of a bracketed variable
            state = IN_ESCAPE;
        } else if (chr == ')') {
            // End of bracketed variable
            write(WMLVariable.WMLV_BRACKETED);
            state = WRITE_THROUGH;
        } else {
            throw new WMLVariableException(exceptionLocalizer.format(
                    "wml-variable-illegal-character",
                    new Character(chr)));
        }
        return retry;
    }

    /**
     * Handle escape characters... these are the characters following
     * the ':' in $(fred:escape) for example.
     *
     * @param chr the character being processed
     * @throws WMLVariableException if invalid variable references are found
     */
    private void handleEscape(char chr) throws WMLVariableException {
        if (esclegal.indexOf(chr) > -1) {
            if (escTerm == WMLVariable.WMLV_BRACKETED) {
                if (chr == 'e') {
                    escTerm = WMLVariable.WMLV_ESCAPE;
                } else if (chr == 'u') {
                    escTerm = WMLVariable.WMLV_UNESC;
                } else {
                    escTerm = WMLVariable.WMLV_NOESC;
                }
            }
        } else if (chr == ')') {
            write(escTerm);
            escTerm = WMLVariable.WMLV_BRACKETED;
            state = WRITE_THROUGH;
        } else {
            throw new WMLVariableException(exceptionLocalizer.format(
                    "wml-variable-illegal-character-escape",
                    new Character(chr)));
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 25-Mar-04	3386/8	steve	VBM:2004030901 Supermerged and merged back with Proteus

 10-Mar-04	3370/1	steve	VBM:2004030901 Application crashes if protocols element is missing

 02-Mar-04	2736/6	steve	VBM:2003121104 Patched from Proteus2 and merged with MCS

 02-Mar-04	2736/4	steve	VBM:2003121104 Patched from Proteus2 and merged with MCS

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 23-Jan-04	2736/1	steve	VBM:2003121104 Configurable WMLC and dollar encoding

 22-Jan-04	2685/1	steve	VBM:2003121104 Allow WMLC and special character encoding to be turned off in Mariner Config

 10-Mar-04	3370/1	steve	VBM:2004030901 Application crashes if protocols element is missing

 22-Jan-04	2685/1	steve	VBM:2003121104 Allow WMLC and special character encoding to be turned off in Mariner Config

 05-Jun-03	285/2	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
