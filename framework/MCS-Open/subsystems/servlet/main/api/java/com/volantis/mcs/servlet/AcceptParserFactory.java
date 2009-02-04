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
package com.volantis.mcs.servlet;

/**
 * A class with static factory methods for createing AcceptParsers.
 *
 * @volantis-api-include-in InternalAPI
 */
public class AcceptParserFactory {

    private AcceptParserFactory() {
    }

    /**
     * Creates a standard AcceptParser which parses the headers and
     * returns them sorted but unmodified
     * 
     * @return an AcceptParser
     */
    public static AcceptParser createDefault() {
        return new AcceptParser();
    }

    /**
     * Creates an AcceptParser for parsing Accept-Charset headers.
     *
     * @return an AcceptParser for parsing Accept-Charset headers.
     *
     * @todo AcceptParser was ported to Synergetics (see VBM 2006032810).
     */
    public static AcceptParser createCharsetParser() {
        return new AcceptParser(new AcceptParser.OnComplete() {
            public void completed(com.volantis.synergetics.servlet.AcceptParser parser) {
                //This is hardcoded to ISO-8859-1,
                //as per the RFC2616 (HTTP 1.1) spec, section 14.2.
                String defaultCharsetName = "ISO-8859-1";
                // If there was no *, and no mention of ISO-8859-1, then add
                // ISO-8859-1 with a qvalue of one to the end of the list.
                if (!parser.containsAcceptable("*") &&
                        !parser.containsAcceptable(defaultCharsetName) &&
                        !parser.containsNotAcceptable(defaultCharsetName)) {
                    parser.addHeaderField(defaultCharsetName);
                }
            }
        });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Jun-05	8675/2	trynne	VBM:2005052602 Generalised AcceptCharsetParser to AcceptParser

 ===========================================================================
*/
