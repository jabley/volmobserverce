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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection;

import com.volantis.mcs.dissection.dom.DissectableContentHandler;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.dom.DissectableDocumentBuilder;
import com.volantis.mcs.dissection.dom.TestDocumentDetails;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.apache.log4j.Category;

import java.io.FileNotFoundException;
import java.net.URL;

/**
 * This class contains some methods useful for test cases that handle xml.
 * Some of these methods may be more generally useful as well.
 */
public class DissectionTestCaseHelper extends XMLTestCaseHelper {

    /**
     * The log4j object to log to.
     */
    private static Category logger = Category.getInstance(
            "com.volantis.mcs.dissection.DissectionTestCaseHelper");
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public static
        TestDocumentDetails createDissectableDocument(DissectableDocumentBuilder builder,
                                                      Class baseClass,
                                                      String path)
        throws Exception {

        if (logger.isDebugEnabled()) {
            logger.debug("Creating dissectable document for: " + path);
        }

        DissectableContentHandler contentHandler
            = new DissectableContentHandler();
        contentHandler.initialise(builder);
        XMLReader parser = getXMLReader();

        parser.setContentHandler(contentHandler);

        // Prevent the external DTD from being loaded.
        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",
                          false);
        parser.setProperty("http://xml.org/sax/properties/lexical-handler",
                           contentHandler);
        parser.setProperty("http://xml.org/sax/properties/declaration-handler",
                           contentHandler);
        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",
                          false);

        URL input = baseClass.getResource(path);
        if (input == null) {
            throw new FileNotFoundException(path);
        }
        try {
            parser.parse(input.toExternalForm());
            /*} catch (SAXParseException spe) {
                spe.printStackTrace();
                throw spe;*/
        } catch (SAXException se) {
            se.printStackTrace();
            Exception cause = se.getException();
            if (cause != null) {
                System.out.println("Root Cause");
                cause.printStackTrace();
            }
            throw se;
        }

        TestDocumentDetails document
            = contentHandler.getTestDocumentDetails();

        return document;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	7890/2	pduffin	VBM:2005042705 Committing results of supermerge

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Addressing review comments

 04-May-05	8007/1	philws	VBM:2005050311 Avoid problems with test case when network is down

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
