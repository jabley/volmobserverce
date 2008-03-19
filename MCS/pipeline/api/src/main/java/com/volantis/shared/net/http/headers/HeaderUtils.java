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
package com.volantis.shared.net.http.headers;

import com.volantis.xml.pipeline.sax.conditioners.ContentConditioner;
import com.volantis.xml.pipeline.sax.drivers.web.conditioners.HTMLResponseConditioner;
import com.volantis.xml.pipeline.sax.drivers.web.conditioners.XMLResponseConditioner;

import java.util.StringTokenizer;
import java.util.ArrayList;

import org.xml.sax.XMLFilter;

/**
 * Utility class for http headers.
 */
public abstract class HeaderUtils {
    /**
     * Ensure this class cannot be specialized.
     */
    private HeaderUtils() {
    }

    /**
     * Convert a multi-valued header value into an array of values. Note
     * that only separate header values are separated nothing is done
     * with optional values.
     * @param headerValue the multi-valued header whose values to separate
     * into an array.
     * @return the multi-values in headerValue as an array of Strings. If
     * headerValue is null or contains no values then an empty array is
     * returned. If there is only a single value in headerValue then this
     * is returned in the array.
     * @todo later reduce the garbage creation in this method
     * @todo doesn't work if the header value contains quoted strings with commas in them...
     */
    public static String [] separateHeaderMultiValues(String headerValue) {
        String result [] = new String[0];

        if(headerValue!=null && headerValue.length()>0) {
            ArrayList list = new ArrayList();
            StringTokenizer tokenizer = new StringTokenizer(headerValue, ",");
            while(tokenizer.hasMoreTokens()) {
                list.add(tokenizer.nextToken().trim());
            }

            result = (String []) list.toArray(result);
        }

        return result;
    }

    /**
     * Get the value of the charset parameter from the content type.
     *
     * @param contentType The content type that may contain a charset
     * parameter.
     *
     * @return The value of the charset parameter, or null if it could not be
     * found.
     */
    public static String getCharSetFromContentType(String contentType) {

        String charEncoding = null;

        // Look to see whether there is a charset parameter.
        // See the following specification for details of the syntax.
        //
        // Syntax of Content-Type header, including restrictions on
        // parameters; consist of attributes (names) and values.
        //   http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.7
        //
        // Syntax of parameter
        //   http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.7
        //
        // Syntax of quoted-string and token that are used in the definition
        // of parameters.
        //   http://www.w3.org/Protocols/rfc2616/rfc2616-sec2.html#sec2.2
        //
        // Syntax of charset parameter itself.
        //   http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.4
        //
        // Parameters are preceded by a ;, names are case insensitive and
        // according to the specification must be immediately followed by an =
        // sign and then the value with no intervening spaces. A value may
        // either be a token, or a quoted string. Ideally there should be a
        // specialised parser for these headers but at the moment there is
        // just not enough time to write one.
        //
        // todo Abstract the headers so that all this nasty parsing is removed into separate classes.
        //
        // One known albeit pathological problem with this is that if a
        // parameter that comes before a real charset parameter has a value
        // that looks like a charset parameter, e.g. foo="charset=xyz" then
        // that will be used instead of the real one. The only way to solve
        // that is to use a proper parser to process the header.
        int index = contentType.indexOf(';');
        if (index != -1) {
            // There are parameters, the chances are that at least one of them
            // (if there is more than one) is the charset parameter. Therefore,
            // we will convert the whole string to lower case so that we can
            // do case insensitive matching. Values may or may not be case
            // insensitive depending on the definition of the parameter so the
            // lower case representation of the header is not suitable for
            // extracting the value so that will be obtained from the original
            // content type.
            String lowerContentType = contentType.toLowerCase();
            index = lowerContentType.indexOf("charset=", index);
            if (index != -1) {
                // A charset parameter has been found so get the value. The
                // value is defined to be a token so we do not need to worry
                // about quoted strings. Search forward for the first non token
                // character. According to the HTTP specification a token is
                // defined to be:
                //   CTL            = <any US-ASCII control character
                //                    (octets 0 - 31) and DEL (127)>
                //   SP             = <US-ASCII SP, space (32)>
                //   HT             = <US-ASCII HT, horizontal-tab (9)>
                //
                //   token          = 1*<any CHAR except CTLs or separators>
                //   separators     = "(" | ")" | "<" | ">" | "@"
                //                  | "," | ";" | ":" | "\" | <">
                //                  | "/" | "[" | "]" | "?" | "="
                //                  | "{" | "}" | SP | HT

                int start = index + 8;
                int end = start;
                int length = lowerContentType.length();
                for (boolean scanningForEndOfToken = true;
                     end < length && scanningForEndOfToken;) {

                    char c = lowerContentType.charAt(end);
                    if (Character.isISOControl(c)
                            || c == '(' || c == ')' || c == '<' || c == '>'
                            || c == '@' || c == ',' || c == ';' || c == ':'
                            || c == '\\' || c == '"' || c == '/' || c == '['
                            || c == ']' || c == '?' || c == '=' || c == '{'
                            || c == '}' || c == ' ' || c == '\t') {
                        scanningForEndOfToken = false;
                    } else {
                        // If we have not yet reached the end of the token then
                        // move onto the next character. This has to be done
                        // here rather than in the for (...) loop itself as
                        // otherwise if the end of the token is reached before
                        // the end of the string is reached then 'end' points
                        // to the first character after the token, rather than
                        // the last character of the token.
                        end += 1;
                    }
                }

                // Get the value of the charset parameter from the content
                // type.
                charEncoding = contentType.substring(start, end);
            }
        }

        return charEncoding;
    }

    /**
     * Create a ContentConditioner appropriate to a given content type
     * @param contentType the content type
     * @param filter the XMLFilter used by the ContentConditioner
     * @return the appropriate ContentConditioner
     */
    public static ContentConditioner
            createContentTypeConditioner(String contentType, XMLFilter filter) {
        ContentConditioner conditioner;
        if (contentType.startsWith("text/html")) {
            conditioner = new HTMLResponseConditioner(filter);
        } else {
            conditioner = new XMLResponseConditioner(filter);
        }
        return conditioner;
    }    
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Apr-05	7679/1	allan	VBM:2005041320 SmartClient Packager - minimal testing

 11-Apr-05	7376/1	allan	VBM:2005031101 SmartClient bundler - commit for testing

 ===========================================================================
*/
