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
package com.volantis.map.ics.configuration;

import com.volantis.map.ics.configuration.ImageConstants;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 */
public class ConfigurationUtilities {
    /**
     * Deserializer for the JpegMode attribute
     *
     * @param mode the JpegMode value
     * @return the corresponding integer value
     */
    public static int deserializeJpegMode(String mode) {
        int result = ImageConstants.JPEG_BASELINE;
        if (mode != null) {
            if (mode.trim().equalsIgnoreCase("progressive")) {
                result = ImageConstants.JPEG_PROGRESSIVE;
            } else {
                result = ImageConstants.JPEG_BASELINE;
            }
        }
        return result;
    }

    /**
     * Serializer for the JpegMode attribute
     *
     * @param value the integer value to serialize
     * @return the string representation
     */
    public static String serializeJpegMode(int value) {
        String result;
        if (value == ImageConstants.JPEG_BASELINE) {
            result = "baseline";
        } else {
            result = "progressive";
        }
        return result;
    }

    /**
     * deserialize the servlet mdoe
     *
     * @param mode the string mode represenation
     * @return the native/transforce marker
     */
    public static boolean deserializeServerMode(String mode) {
        boolean result = true;
        if (mode != null) {
            if (mode.trim().equals("transforce")) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Serialize the servlet mdoe
     *
     * @param mode the servlet mode to serialize
     * @return the string represenation of the servlet mode
     */
    public static String serializeServerMode(boolean mode) {
        String result = "native";
        if (!mode) {
            result = "transforce";
        }
        return result;
    }

    /**
     * Deserialize the scale model mode
     *
     * @param mode the mode
     * @return the int scale model mode
     */
    public static int deserializeScaleMode(String mode) {
        int result = ImageConstants.SCALE_MODE_BILINEAR;
        if (mode != null) {
            mode = mode.trim();
            if (mode.equalsIgnoreCase("bicubic")) {
                result = ImageConstants.SCALE_MODE_BICUBIC;
            } else if (mode.equalsIgnoreCase("nearest")) {
                result = ImageConstants.SCALE_MODE_NEAREST;
            }
        }
        return result;
    }

    /**
     * Serialize the scale model mode
     *
     * @param mode the int representation of the scale model
     * @return the string representation of the scale model
     */
    public static String serializeScaleMode(int mode) {
        String result = "bilinear";
        if (mode == ImageConstants.SCALE_MODE_NEAREST) {
            result = "nearest";
        } else if (mode == ImageConstants.SCALE_MODE_BICUBIC) {
            result = "bicubic";
        }
        return result;
    }

    /**
     * Serialize the String
     *
     * @param string
     * @return
     */
    public static String serializeString(String string) {
        String result = null;
        if (null != string) {
            string = string.trim();
            if (!"".equals(string)) {
                result = string;
            }
        }
        return result;
    }

    /**
     * deserialize a string name (treat empty string name as no string name)
     *
     * @param string the string name to deserialize
     * @return the deserialized string name or null of one was not specified
     */
    public static String deserializeString(String string) {
        String result = null;
        if (null != string) {
            string = string.trim();
            if (!"".equals(string)) {
                result = string;
            }
        }
        return result;
    }


    // my tests

    /**
     * Checks that the given mode is valid
     * @param mode the mode to check
     */
    public static void assertJPegMode(int mode) {
        switch (mode) {
            case ImageConstants.JPEG_BASELINE:
            case ImageConstants.JPEG_PROGRESSIVE:
                // valid values so break
                break;
            default:
                throw new IllegalArgumentException(
                        "Invalid rendering mode '" + mode + "'");
        }
    }

     /**
     * Checks that the given scale mode is valid
     * @param mode the mode to check
     */
    public static void assertScaleMode(int mode) {
        switch (mode) {
            case ImageConstants.SCALE_MODE_NEAREST:
            case ImageConstants.SCALE_MODE_BICUBIC:
            case ImageConstants.SCALE_MODE_BILINEAR:
                // valid values so break
                break;
            default:
                throw new IllegalArgumentException(
                        "Invalid scale mode '" + mode + "'");
        }
    }

    public static void assertMinimumJPegQuality(int minQuality) {
        if ((minQuality < 0) || (minQuality > 100)) {
            throw new IllegalArgumentException(
                    "Invalid quality setting '" + minQuality +
                    "' .Value must be between 0 and 100");
        }
    }

    public static void assertMinimumBitDepth(int bitDepth) {
        switch (bitDepth) {
            case 1:
            case 2:
            case 4:
            case 8:
            case 16:
            case 24:
                 // valid values so break
                break;
            default:
                throw new IllegalArgumentException(
                        "Unsupported bit depth '" + bitDepth +
                        "'. Values must be on of 1, 2, 4, 8, 16 or 24");
        }
    }
           
    /**
     * This methods converts string representation of the list of integres in
     * the form of "[<number>](','[<number>])*" into an array of integers. The
     * string list can include empty arguments that will be represented in as
     * nulls in the ArrayList.
     *
     * @param list string representation of list of integrers
     * @return array representation of the list of integers
     *
     * @throws NumberFormatException when a non blank element of the string
     *                               list cannot be parsed to int
     */

    public static ArrayList listToIntList(String list)
        throws NumberFormatException {
        ArrayList result = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(list, ",", true);
        boolean addEmptyValue = true;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.equals(",")) {
                if (addEmptyValue) {
                    result.add(null);
                }
                addEmptyValue = true;
            } else {
                addEmptyValue = false;
                if (token.length() > 0) {
                    result.add(new Integer(token));
                } else {
                    result.add(null);
                }
            }
        }
        if (addEmptyValue && result.size() > 0) {
            result.add(null);
        }
        return result;
    }

    /**
     * This method converts a string representation of list of integers in the
     * form of "[<number>](','[<number>])*" to an array of ints. The result has
     * always the size of the provided array of default values. If some
     * elements are missing on input list or it is to short, the values are
     * taken from the array of default values.
     *
     * @param list      string representation of the list of integers
     * @param defValues array of default values
     * @return array representation of the list
     *
     * @throws NumberFormatException when an non blank element of the string
     *                               list cannot be parsed to int
     */

    public static int[] listToIntArray(String list, int defValues[])
        throws NumberFormatException {
        int result[] = new int[defValues.length];
        ArrayList argList = listToIntList(list);

        for (int i = 0; i < result.length; i++) {
            result[i] = (i < argList.size() && argList.get(i) != null) ?
                ((Integer) argList.get(i)).intValue() : defValues[i];
        }
        return result;
    }
}
