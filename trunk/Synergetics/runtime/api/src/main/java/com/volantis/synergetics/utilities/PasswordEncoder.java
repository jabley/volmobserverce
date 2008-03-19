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
package com.volantis.synergetics.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * Utility methods for password encoding.
 */
public class PasswordEncoder {

    /**
     * Constant for SHA-1 algorithm.
     */
    public static final String ALG_SHA1 = "SHA-1";

    /**
     * Constant for MD5 algorithm.
     */
    public static final String ALG_MD5 = "MD5";

    /**
     * Random number generator.
     */
    private static final SecureRandom RANDOM;

    static {
        try {
            RANDOM = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    private PasswordEncoder() {
        // hide constructor
    }

    /**
     * Hashes a password with the specified salt using SHA-1 algorithm.
     *
     * @param password the password to hash
     * @param salt to add randomness
     * @return the base64 encoded hashed (salted) password
     */
    public static String hashPassword(final String password, final byte[] salt) {
        try {
            return hashPassword(password, salt, ALG_SHA1);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unknown algorithm");
        }
    }

    /**
     * Hashes a password with the specified salt using the given algorithm.
     *
     * @param password the password to hash
     * @param salt to add randomness
     * @param algorithm the algorithm to use to hash
     * @return the base64 encoded hashed (salted) password
     */
    public static String hashPassword(
                final String password, final byte[] salt, final String algorithm)
            throws NoSuchAlgorithmException {

        final MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.reset();
        digest.update(salt);
        final byte[] hashedPassword = digest.digest(password.getBytes());
        return Base64.encodeBytes(hashedPassword, false);
    }

    /**
     * Generates a salt byte array of the specified length.
     *
     * @param length the length of the salt
     * @return the generated salt
     */
    public static byte[] generateSalt(final int length) {
        final byte[] salt = new byte[length];
        RANDOM.nextBytes(salt);
        return salt;
    }
}
