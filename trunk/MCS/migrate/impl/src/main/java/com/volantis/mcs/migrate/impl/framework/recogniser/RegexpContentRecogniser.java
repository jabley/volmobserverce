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
package com.volantis.mcs.migrate.impl.framework.recogniser;

import com.volantis.mcs.migrate.api.framework.ContentRecogniser;
import org.apache.regexp.RE;
import org.apache.regexp.ReaderCharacterIterator;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * A content recogniser that recognises content using a regular expression.
 */
public class RegexpContentRecogniser implements ContentRecogniser {

    /**
     * The regular expression used to recognise content.
     */
    private RE contentExpression;

    /**
     * Initialise.
     *
     * @param contentExpressionString the regular expression used to recognise
     *      content.
     */
    public RegexpContentRecogniser(String contentExpressionString) {

        this.contentExpression = new RE(contentExpressionString);
    }

    // Javadoc inherited.
    public boolean recogniseContent(InputStream input) {

        // For now we assume that all project files are UTF8.
        // Apparently, the GUI always writes out project files as UTF8.
        // If this turns out to not be the case we may need to use a more
        // complex approach that involves parsing project files as XML.
        String charsetName = "UTF-8";

        InputStreamReader inputStreamReader = null;
        try {
           inputStreamReader = new InputStreamReader(input, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("unable to load charset " + charsetName,
                    e);
        }

        ReaderCharacterIterator iterator = new ReaderCharacterIterator(
                inputStreamReader);

        return contentExpression.match(iterator, 0);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-May-05	8036/9	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/7	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
