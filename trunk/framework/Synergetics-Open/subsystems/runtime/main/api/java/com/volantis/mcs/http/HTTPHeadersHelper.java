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
package com.volantis.mcs.http;

import java.util.Enumeration;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This helper class provides methods to manipulate HttpHeaders.
 *
 * @volantis-api-exclude-from PublicAPI
 * @volantis-api-exclude-from ProfessionalServicesAPI
 * @volantis-api-exclude-from InternalAPI
 */
public class HTTPHeadersHelper {

    /**
     * Supporting method that returns <code>true</code> if the given request
     * actually has the specified mime type as an accepted mime type.
     *
     * @param headers  represents the request to be queried
     * @param mimeType the mime type to be checked
     * @return true if the given mime type is accepted
     */
    public static boolean hasAcceptMimeType(HttpHeaders headers,
                                            String mimeType) {
        boolean result = false;

        String[] mimeTypes = getAcceptMimeTypes(headers);

        if (mimeTypes != null) {
            for (int i = 0; !result && (i < mimeTypes.length); i++) {
                    result = (mimeType.equalsIgnoreCase(mimeTypes[i]));
            }
        }

        return result;
    }

    /**
     * Return an array of accept mime types without any qualified parts to the
     * values. ie. just mime/text
     *
     * @return Return an array of accept mime types without any qualified
     *         parts to the values. ie. just mime/text. Can be null if there
     *         are no accept headers.
     */
    public static String[] getAcceptMimeTypes(HttpHeaders headers) {

        Enumeration e = headers.getHeaders("accept");
        String[] parsedMimeTypes = null;

        if (e != null) {
            ArrayList mimeTypes = new ArrayList();
            while (e.hasMoreElements()) {
                mimeTypes.add(e.nextElement());
            }
            parsedMimeTypes = parseAcceptMimeTypes(mimeTypes);
        }
        return parsedMimeTypes;
    }

    /**
     * Parse the accept mime types header and return a string of accept mime
     * types from the comma separated values. Strip out anything in quotes,
     * and then break the string into csv items. These items are then stripped
     * of any values after the first ';', if any.
     * <p/>
     * For example.
     * text/html;q=0.9,text/plain;community="UC,I",text/*
     * should generate an array of:
     * text/html
     * text/plain
     * text/*
     *
     * @param mimeTypes the list of mimetypes
     * @return an array of strings just containing the
     * @todo this is broken, it should ignore q=0 types...
     */
    protected static String[] parseAcceptMimeTypes(List mimeTypes) {
        if ((mimeTypes != null) && (!mimeTypes.isEmpty())) {
            ArrayList list = new ArrayList();
            for (int i = 0; i < mimeTypes.size(); i++) {
                String value = removeQuotes((String) mimeTypes.get(i));

                StringTokenizer tokenizer = new StringTokenizer(value, ",");
                while (tokenizer.hasMoreElements()) {
                    String token = tokenizer.nextToken();

                    // Find the value excluding any parameters (delimited by a
                    // semi-colon in the value) and trimmed of whitespace in a
                    // garbage-efficient manner
                    int start = 0;
                    int end = token.indexOf(';');

                    if (end < 0) {
                        // No parameters found
                        end = token.length();
                    }

                    // Trim whitespace from start of string
                    while ((start < end) &&
                            Character.isWhitespace(token.charAt(start))) {
                        start++;
                    }

                    // Trim whitespace from end of string
                    while ((end > start) &&
                            Character.isWhitespace(token.charAt(end - 1))) {
                        end--;
                    }

                    // Only non-empty values are of interest
                    if (end != start) {
                        // Only substring if essential
                        if ((start != 0) ||
                                (end != token.length())) {
                            list.add(token.substring(start, end));
                        } else {
                            list.add(token);
                        }
                    }
                }
            }

            // Copy the list of values into an array of Strings.
            String result[] = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                result[i] = (String) list.get(i);
            }
            return result;
        }
        return null;
    }

    /**
     * Remove the double-quotes from the input string. Note that the request
     * header should never contain single quoted characters for the start and
     * end of the quoted string. However, single quotes may appear within the
     * double quoted string and will be ignored as any other char will be.
     * <p/>
     * E.g. "blah's test" and "This contains a \"special\" double-quote"
     * are both valid.
     *
     * @param input the input string that may or may not contain an even
     *              number of double-quotes
     * @return the unmodified string or the string without any the quoted
     *         parts.
     */
    protected static String removeQuotes(String input) {
        final char doubleQuote = '"';
        // If the input contains a doubleQuote then it cannot be preceded
        // by a '\',  hence we only search for quotes using indexOf and not
        // indexOfNextQuote(). If no one is found, then return the unmodified
        // input asap.
        int quoteBegin = input.indexOf(doubleQuote);
        if (quoteBegin < 0) {
            return input;
        }
        int startIndex = 0;
        StringBuffer buffer = new StringBuffer();
        boolean finished = false;
        while (!finished) {
            // Strip out the string from the beginning last doubleQuote
            // to start of the next doubleQuote. E.g. "Test"ing" this" should
            // strip out 'Test' first and then ' this' on the 2nd iteration.
            String value = input.substring(startIndex, quoteBegin);
            buffer.append(value);

            // We have a starting doubleQuote, find the end quote, ignoring
            // the \" special quotes.
            startIndex = indexOfNextQuote(input, doubleQuote, quoteBegin + 1);
            if (startIndex < 0) {
                finished = true;
            }
            ++startIndex;
            quoteBegin = indexOfNextQuote(input, doubleQuote, startIndex);
            if (quoteBegin < 0) {
                quoteBegin = input.length();
            }
        }
        return buffer.toString();
    }

    /**
     * Return the index of the next quote char whilst ignoring any single
     * quotes, or specialist quote chars (with \ before the quote).
     *
     * @param input the input String.
     * @param quote the quote char
     * @param index the index of to start from
     * @return the index of the quote or -1 if none found.
     */
    private static int indexOfNextQuote(String input, char quote, int index) {
        int quoteIndex = input.indexOf(quote, index);
        if ((quoteIndex > 0) && input.charAt(quoteIndex - 1) == '\\') {
            return indexOfNextQuote(input, quote, quoteIndex + 1);
        } else {
            return quoteIndex;
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-May-05	8413/2	emma	VBM:2005042012 Bug fix merged from 323 - now retrieves all available accept headers. Also added HttpHeadersHelper

 ===========================================================================
*/
