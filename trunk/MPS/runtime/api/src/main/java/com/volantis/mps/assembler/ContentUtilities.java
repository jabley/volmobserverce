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
 * $Header: /src/mps/com/volantis/mps/assembler/ContentUtilities.java,v 1.2 2002/12/09 18:22:55 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    Steve           VBM:2002103008 - Utility class to guess the
 *                              type of a byte array full of content.
 * 09-Dec-02    Chris W         VBM:2002120913 - Added a test for mime type
 *                              application/smil
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.assembler;

import java.io.UnsupportedEncodingException;

public class ContentUtilities {

    public static final int CU_TEXT_PLAIN = 0;

    public static final int CU_TEXT_HTML = 1;

    public static final int CU_TEXT_WML = 2;

    public static final int CU_APPLICATION_SMIL = 3;

    public static final int CU_IMAGE_GIF = 10;

    public static final int CU_IMAGE_JPEG = 11;

    public static final int CU_IMAGE_PNG = 12;

    public static final int CU_IMAGE_TIFF = 13;

    public static final int CU_IMAGE_WBMP = 14;

    /** Copyright */
    private static String mark = "(c) Volantis Systems Ltd 2000.";

    public static int guessContentType(byte[] content) {
        int bestGuess = CU_TEXT_PLAIN;

        StringBuffer buff = new StringBuffer();
        int length = content.length;

        boolean isBinary = false;
        for (int i = 0; i < length; i++) {
            if ((content[i] > 127) || (content[i] < 5)) {
                isBinary = true;
                break;
            }
            buff.append((char) (content[i]));
        }

        if (isBinary) {
            if (length > 3) {
                // Check for GIF
                if ((content[0] == 'G') && (content[1] == 'I') &&
                        (content[2] == 'F') && (content[3] == '8')) {
                    bestGuess = CU_IMAGE_GIF;
                }
                // Check for PNG
                if ((content[1] == 'P') && (content[2] == 'N') &&
                        (content[3] == 'G')) {
                    bestGuess = CU_IMAGE_PNG;
                }
            }

            if (length > 9) {
                // Check for JPEG
                if ((content[6] == 'J') && (content[7] == 'F') &&
                        (content[8] == 'I') && (content[9] == 'F')) {
                    bestGuess = CU_IMAGE_JPEG;
                }
            }

            if (length > 1) {
                // Check for WBMP
                if ((content[0] == 0) && (content[1] == 0)) {
                    bestGuess = CU_IMAGE_WBMP;
                }
                // Check for TIFF
                if (((content[0] == 'I') && (content[1] == 'I')) ||
                        ((content[0] == 'M') && (content[1] == 'M'))) {
                    bestGuess = CU_IMAGE_TIFF;
                }
            }
        } else {
            String s = buff.toString().toLowerCase();
            if (s.indexOf("<html>") > -1) {
                bestGuess = CU_TEXT_HTML;
            }
            if (s.indexOf("<wml>") > -1) {
                bestGuess = CU_TEXT_WML;
            }
            if (s.indexOf("<smil>") > -1) {
                bestGuess = CU_APPLICATION_SMIL;
            }
        }
        return bestGuess;
    }

    /**
     * Utility method to convert a <code>String</code> from one encoding to
     * another. If the target encoding is <code>null</code>, the <code>String
     * </code> is converted using the platform default encoding
     *
     * @param contentStr        The <code>String</code> to convert
     * @param currentEncoding   The content <code>String</code>'s current encoding
     * @param targetEncoding    The target encoding or <code>null</code> for
     * platform default
     *
     * @return                  A <code>String</code> in the new encoding
     *
     * @throws UnsupportedEncodingException If the <code>currentEncoding</code>
     * or <code>targetEncoding</code> is not a valid character encoding
     */
    public final static String convertEncoding(String contentStr,
                                  String currentEncoding,
                                  String targetEncoding)
            throws UnsupportedEncodingException{

        // check that we have all we need
        if (contentStr == null || currentEncoding == null) {
            throw new NullPointerException("Neither the content string nor " +
                                           "its current encoding may be null");
        }

        // if the contentStr is empty, just return
        if ("".equals(contentStr)) {
            return contentStr;
        }

        byte[] strBytes = contentStr.getBytes(currentEncoding);
        if (targetEncoding != null) {
            return new String(strBytes, targetEncoding);
        } else {
            return new String(strBytes);
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	829/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 01-Jul-05	776/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 29-Nov-04	243/1	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 ===========================================================================
*/
