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

package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.apache.regexp.RE;

/**
 * Test the NewDeviceWizardPage non gui methods.
 */
public class NewDeviceWizardPageTestCase extends TestCaseAbstract {

    /**
     * Test that the regular expresssion has been set up correctly.
     * (see device-core.xsd DeviceNameType definition) [A-Za-z0-9_.\-@]+
     */
    public void testRegularExpression() throws Exception {
        RE re = new RE("[A-Za-z0-9\\-_@.]+");

        // Has to have at least one character.
        assertFalse(re.match(""));

        // Should be OK
        assertTrue(re.match("a"));

        // Test all chars/numbers
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        assertTrue(re.match(alphabet));
        assertTrue(re.match(alphabet.toUpperCase()));
        assertTrue(re.match(numbers));

        assertTrue(re.match("a_sentence.that-has@separators"));

        // Should match first part of string (up to '*' which is an illegal char)
        assertTrue(re.match("a_sentence.th*at-has@separators"));

        char validOtherChars[] = {
            '.', '_', '.', '-', '@'
        };
        // Test that each valid char may be matched individually.
        for (int i = 0; i < validOtherChars.length; i++) {
            String str = String.valueOf(validOtherChars[i]);
            assertTrue("Value should match: '" + str + "'", re.match(str));
        }
        assertTrue(re.match(new String(validOtherChars)));

        String allValidChars = alphabet.toLowerCase() + numbers +
                String.valueOf(validOtherChars) + alphabet.toUpperCase();

        for (int i = 0; i < allValidChars.length(); i++) {
            String str = String.valueOf(allValidChars.charAt(i));
            assertTrue("Value should match: '" + str + "' in " + allValidChars +
                    " at position " + i, re.match(str));
        }

        char someInvalidChars[] = {
            '`', '~', '*', '!', '<', '>', '(', ')', '[', ']', '^', '$', '|',
            '?', ',', '\'', ':', '"', ';', '{', '}', '=', '+', '&', '%', '/'
        };

        for (int i = 0; i < someInvalidChars.length; i++) {
            String str = String.valueOf(someInvalidChars[i]);
            assertFalse("Value should NOT match: '" + str + "'", re.match(str));
        }

        // This will not match only if all the characters are invalid. If we
        // insert even just one valid character, the RE will match that char
        // and return true.
        assertFalse(re.match(String.valueOf(someInvalidChars)));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Apr-04	4035/3	byron	VBM:2004032403 Create the NewDeviceWizard class - review issues addressed

 27-Apr-04	4035/1	byron	VBM:2004032403 Create the NewDeviceWizard class

 ===========================================================================
*/
