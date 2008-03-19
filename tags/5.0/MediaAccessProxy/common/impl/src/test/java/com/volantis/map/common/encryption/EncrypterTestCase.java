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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.common.encryption;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 */
public class EncrypterTestCase extends TestCaseAbstract {

    public void testSimple() {
        final String input = "Hello World!";
        final Encrypter encrypter = new Encrypter("pwd");
        final String encrypted = encrypter.encrypt(input);
        assertNotEquals(input, encrypted);
        final String output = encrypter.decrypt(encrypted);
        assertEquals(input, output);
    }

    public void testDefault() {
        final String input = "Hello World!";
        final String encrypted = Encrypter.defaultEncrypt(input);
        assertNotEquals(input, encrypted);
        final String output = Encrypter.defaultDecrypt(encrypted);
        assertEquals(input, output);
    }

    public void testReuse() {
        final Encrypter encrypter = new Encrypter("pwd");
        String input = "Hello World!";
        String encrypted = encrypter.encrypt(input);
        assertNotEquals(input, encrypted);
        String output = encrypter.decrypt(encrypted);
        assertEquals(input, output);

        input = "this-is-the-second-run";
        encrypted = encrypter.encrypt(input);
        assertNotEquals(input, encrypted);
        output = encrypter.decrypt(encrypted);
        assertEquals(input, output);

        input = "something to encrypt/decrypt";
        encrypted = encrypter.encrypt(input);
        assertNotEquals(input, encrypted);
        output = encrypter.decrypt(encrypted);
        assertEquals(input, output);
    }
}
