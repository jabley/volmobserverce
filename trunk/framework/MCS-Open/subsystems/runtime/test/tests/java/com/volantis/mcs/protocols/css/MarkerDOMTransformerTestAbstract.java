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

package com.volantis.mcs.protocols.css;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * 
 */
public abstract class MarkerDOMTransformerTestAbstract extends DOMTransformerTestAbstract {

    /**
     * Do the transform using the input XML and check the results against the
     * expected XML.
     *
     * @param inputXML    the XML to build the document from.
     * @param expectedXML the XML the document should represent post-transform.
     * @throws java.io.IOException  if an IO exception occurs.
     * @throws org.xml.sax.SAXException if a SAX exception occurs.
     */
    protected void doTransform(String inputXML,
                               String expectedXML)
            throws IOException,
                   SAXException,
                   ParserConfigurationException {

        StrictStyledDOMHelper styledDOMHelper = new StrictStyledDOMHelper(null);
        Document document = styledDOMHelper.parse(inputXML);

        DOMTransformer transformer = new MarkerDOMTransformer();
        transformer.transform(createDOMProtocol(), document);

        String actualXML = styledDOMHelper.render(document);

        Document styledDom = styledDOMHelper.parse(expectedXML);
        String canonicalExpectedXML = styledDOMHelper.render(styledDom);

        System.out.println("Expected: " + canonicalExpectedXML);
        System.out.println("Actual  : " + actualXML);
        assertXMLEquals("Actual XML does not match expected XML",
                        canonicalExpectedXML,
                        actualXML);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	9440/2	pduffin	VBM:2005070711 Merged changes from main trunk

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 ===========================================================================
*/
