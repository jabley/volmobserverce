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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common;

/**
 * This exchange implementation is appropriate to URI fragment control type.
 */
public class URIFragmentExchange implements AttributeValueExchange {
    // javadoc inherited
    public String toModelForm(String controlForm) {
        String encoded = controlForm;

        if ((encoded.indexOf(' ') != -1) ||
                (encoded.indexOf('%') != -1)) {
            // Replace all spaces with %20 and '%' characters with the
            // equivalent escaped value
            StringBuffer buffer = new StringBuffer(encoded.length() * 3);

            for (int i = 0; i < encoded.length(); i++) {
                final char ch = encoded.charAt(i);

                if (ch == ' ') {
                    buffer.append("%20");
                } else if (ch == '%') {
                    String enc = Integer.toHexString(ch).toUpperCase();

                    if (enc.length() != 2) {
                        throw new IllegalStateException(
                                "The encoding for character '" + ch + "' " +
                                "is not two characters (is \"" + enc + "\")");
                    }

                    buffer.append('%').
                            append(enc);
                } else {
                    buffer.append(ch);
                }
            }

            encoded = buffer.toString();
        }

        return encoded;
    }

    // javadoc inherited
    public String toControlForm(String modelForm) {
        String fragment = modelForm;

        if (modelForm.indexOf('%') != -1) {
            StringBuffer buffer = new StringBuffer(modelForm.length());

            for (int i = 0;
                 i < modelForm.length();
                 i++) {
                final char ch = modelForm.charAt(i);

                if (ch == '%') {
                    if (i <= modelForm.length() - 3) {
                        String code = modelForm.substring(i + 1,
                                                          i + 3);

                        buffer.append((char) Integer.parseInt(code,
                                                              16));

                        // Skip over the value
                        i += 2;
                    } else {
                        throw new IllegalStateException(
                                "Could not decode URI fragment " +
                                "\"" + modelForm + "\" because " +
                                "there is a badly formed " +
                                "trailing encoded value");
                    }
                } else {
                    buffer.append(ch);
                }
            }

            fragment = buffer.toString();
        }

        return fragment;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Mar-05	7374/1	philws	VBM:2004121405 Allow asset values to contain space characters via URI encoding

 ===========================================================================
*/
