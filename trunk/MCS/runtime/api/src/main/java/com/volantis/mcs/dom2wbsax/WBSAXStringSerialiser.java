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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-May-03    Mat             VBM:2003042911 - Parse characters to enable them
 *                              to be represented as WBSAX.
 * 01-Jun-03    Steve           VBM:2003042906 - WMLV_LITERAL writes only one
 *                              dollar character.
 * 02-Jun-03    Chris W         VBM:2003042906 - WMLV_NOESC writes Extension.TWO
 *                              rather than Extension.ZERO
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2wbsax;

import com.volantis.charset.CharacterRepresentable;
import com.volantis.charset.Encoding;
import com.volantis.mcs.protocols.wml.WMLVariable;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * This class takes an array of Java characters, identifies the "components"
 * which may be translated into indivudual WBSAX values, and forwards those
 * component values on to the {@link com.volantis.mcs.dom2wbsax.WBSAXValueSerialiser} that it is using
 * to turn them into individual WBSAX events.
 * <p>
 * This involves:
 * <ol>
 *   <li>checking for WML variables, which must be serialised as WBSAX
 *      extension values,
 *   <li>checking for unrepresentable characters, which must be serialised as
 *      WBSAX entities,
 *   <li>serialising the remaining components as WBSAX inline strings.
 * </ol>
 */
public class WBSAXStringSerialiser {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(WBSAXStringSerialiser.class);

    /**
     * Final variable to create a char array holding "$$"
     */
    private static final char[] DOLLAR_LITERAL = {'$', '$'};
    
    /**
     * The encoding, used to identify unrepresentable characters.
     */
    private Encoding encoding;

    /**
     * The value serialiser used to serialise value components once they have
     * been split by this class.
     */
    private WBSAXValueSerialiser valueSerialiser;

    /**
     * Construct an instance of this class.
     *
     * @param encoding the encoding to use to identify unrepresentable
     *      characters.
     */
    public WBSAXStringSerialiser(Encoding encoding,
            WBSAXValueSerialiser valueSerialiser) {
        this.encoding = encoding;
        // TODO: since this class has little state, we could externalise the serialiser?
        this.valueSerialiser = valueSerialiser;
    }

    /**
     * Walk through the character array.  If we find an unrepresentable
     * character, write out the array up to that character, then write
     * out the unrepresentable character.
     *
     * @param chars The character array to parse
     * @param length The number of characters to parse.
    */
    public void parseValue(char[] chars, int length) throws WBSAXException {

        int writeStart = 0;
        int writeLength = 0;
        char lastWMLVariable = '\u0000';

        // Walk through the character array.  If we find an unrepresentable
        // character, write out the array up to that character, then write
        // out the unrepresentable character.

        if(logger.isDebugEnabled()) {
            logger.debug("Content = " + new String(chars, 0, length));
        }
        for (int i = 0; i < length; i++) {
            char c = chars[i];
            switch (c) {

                // Handle any variables
                case WMLVariable.WMLV_BRACKETED :
                case WMLVariable.WMLV_NOBRACKETS :

                    if (lastWMLVariable == '\u0000') {
                        // Write chars up to the variable.
                        writeChars(chars, writeStart, writeLength);
                        lastWMLVariable = c;
                    } else {
                        // Write the variable name.
                        valueSerialiser.addExtensionString(Extension.TWO,
                                chars, writeStart, writeLength);
                        lastWMLVariable = '\u0000';
                    }
                    writeStart = i + 1;
                    writeLength = 0;
                    break;
                case WMLVariable.WMLV_NOESC :
                    valueSerialiser.addExtensionString(Extension.TWO, chars,
                            writeStart, writeLength);
                    lastWMLVariable = '\u0000';
                    writeStart = i + 1;
                    writeLength = 0;
                    break;
                case WMLVariable.WMLV_ESCAPE :
                    valueSerialiser.addExtensionString(Extension.ZERO, chars,
                            writeStart, writeLength);
                    lastWMLVariable = '\u0000';
                    writeStart = i + 1;
                    writeLength = 0;
                    break;
                case WMLVariable.WMLV_UNESC :
                    valueSerialiser.addExtensionString(Extension.ONE, chars,
                            writeStart, writeLength);
                    lastWMLVariable = '\u0000';
                    writeStart = i + 1;
                    writeLength = 0;
                    break;
                case '$' :
                    // Write out $$
                    writeChars(chars, writeStart, writeLength);
                    writeChars(DOLLAR_LITERAL, 0, 1);
                    writeStart = i + 1;
                    writeLength = 0;
                    break;
                default :
                    CharacterRepresentable rep = encoding.checkCharacter(c);
                    if (rep.isRepresentable()) {
                        writeLength++;
                    } else {
                        writeChars(chars, writeStart, writeLength);
                        valueSerialiser.addEntity(c);
                        writeStart = i + 1;
                        writeLength = 0;
                    }
                    break;
            }
        }
        // Now mop up any final characters.
        writeChars(chars, writeStart, writeLength);
    }

    /**
     * Convenience method add the characters in the buffer to the content
     * handler.
     *
     * @param chars Characters to write
     * @param offset Start character
     * @param length Length of write.
     * @throws com.volantis.mcs.wbsax.WBSAXException A WBSAX problem
     */
    private void writeChars(char[] chars, int offset, int length)
        throws WBSAXException {
        if (length > 0) {
            //  Write literal string so far
            valueSerialiser.addString(chars, offset, length);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 19-Jan-04	2653/2	steve	VBM:2004011304 Merge from proteus

 02-Oct-03	1469/6	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 ===========================================================================
*/
