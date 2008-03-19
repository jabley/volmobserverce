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

import com.volantis.synergetics.utilities.Base64;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.map.localization.LocalizationFactory;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.lang.reflect.UndeclaredThrowableException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 */
public class Encrypter {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(Encrypter.class);

    /**
     * Salt (must be 8 bytes long).
     */
    private static final byte[] SALT =
        new byte[] {24, 59, 75, 122, 66, 43, 99, 78};

    /**
     * Iteration count.
     */
    private static final int ITERATION_COUNT = 31;

    /**
     * Default MAP password.
     */
    private static final String MAP_PASSWORD = "MAP password";

    private static final Encrypter DEFAULT_ENCRYPTER =
        new Encrypter(MAP_PASSWORD);


    /** @todo later I do not think that Cipher is thread safe **/
    private final Cipher ecipher;
    private final Cipher dcipher;

    public Encrypter(final String passPhrase) {
        try {
            // Create the key
            final KeySpec keySpec =
                new PBEKeySpec(passPhrase.toCharArray(), SALT, ITERATION_COUNT);
            final SecretKeyFactory keyFactory =
                SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            final SecretKey key = keyFactory.generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameter to the ciphers
            AlgorithmParameterSpec paramSpec =
                new PBEParameterSpec(SALT, ITERATION_COUNT);

            // Create the ciphers
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (java.security.InvalidAlgorithmParameterException e) {
            throw new UndeclaredThrowableException(e);
        } catch (java.security.spec.InvalidKeySpecException e) {
            throw new UndeclaredThrowableException(e);
        } catch (javax.crypto.NoSuchPaddingException e) {
            throw new UndeclaredThrowableException(e);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new UndeclaredThrowableException(e);
        } catch (java.security.InvalidKeyException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    public String encrypt(final String str) {
        try {
            // Encode the string into bytes using utf-8
            final byte[] utf8 = str.getBytes("UTF8");
            // Encrypt
            final byte[] enc = ecipher.doFinal(utf8);
            // Encode bytes to base64 to get a string
            return Base64.encodeBytes(enc);
        } catch (javax.crypto.BadPaddingException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalBlockSizeException e) {
            throw new UndeclaredThrowableException(e);
        } catch (UnsupportedEncodingException e) {
            throw new UndeclaredThrowableException(e);
        } catch (java.io.IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    public String decrypt(final String str) {
        try {
            // Decode base64 to get bytes
            final byte[] dec = Base64.decode(str);
            if (null == dec) {
                throw new IllegalArgumentException(
                    EXCEPTION_LOCALIZER.format("invalid-resource-id", str));
            }
            // Decrypt
            final byte[] utf8 = dcipher.doFinal(dec);
            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (javax.crypto.BadPaddingException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalBlockSizeException e) {
            throw new UndeclaredThrowableException(e);
        } catch (UnsupportedEncodingException e) {
            throw new UndeclaredThrowableException(e);
        } catch (java.io.IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Convenience method to encrypt strings using the default MAP password.
     *
     * @param string the string to encrypt
     * @return the encrypted string.
     */
    public static String defaultEncrypt(final String string) {
        return DEFAULT_ENCRYPTER.encrypt(string);
    }

    /**
     * Convenience method to decrypt strings using the default MAP password.
     *
     * @param string the string to decrypt
     * @return the decrypted string.
     */
    public static String defaultDecrypt(final String string) {
        return DEFAULT_ENCRYPTER.decrypt(string);
    }
}
