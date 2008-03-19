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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/DOMScriptTestCase.java,v 1.2 2003/04/23 09:44:20 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Apr-03    Geoff           VBM:2003040305 - Created; a TestCase for the 
 *                              DOMScript class.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.DOMAssertionUtilities;
import com.volantis.mcs.protocols.assets.implementation.LiteralScriptAssetReference;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import org.xml.sax.SAXParseException;

/**
 * A TestCase for the {@link DOMScript} class.
 */ 
public class DOMScriptTestCase extends ScriptTestCase {

    // Inherit Javadoc.
    Script createScript(MarinerPageContext context,
                        ScriptAssetReference scriptObject)
            throws ProtocolException {
        return DOMScript.createScript(scriptObject);
    }

    // Inherit Javadoc.
    ScriptAssetReference getDefaultScriptValue() {
        // a DOMScript parses content as XML, so lets give it a bit of a 
        // workout by providing some XML content to parse.
        return new LiteralScriptAssetReference(
                "<tag>content<empty/>more content</tag>");
    }

    /**
     * Test creation of a script from a simple value (no asset), containing
     * XML which is invalid.
     * 
     * @throws ProtocolException
     */ 
    public void testCreateSimpleXmlInvalid() throws ProtocolException {
        TestMarinerPageContext context = new TestMarinerPageContext();
        ScriptAssetReference expected = new LiteralScriptAssetReference(
                "<unbalanced-tag>");
        try {
            createScript(context, expected);
            fail("Unbalanced XML tag should fail to parse");
        } catch (ProtocolException e) {
            if (e.getCause() instanceof SAXParseException) {
                // We expect this
            } else {
                fail("Unexpected protocol exception cause" + e.getCause());
            }
        }
    }

    // Inherit Javadoc.
    void checkAppendToResult(Node node) {
        // Ensure we have created separate DOM elements for our XML content
        // rather than just a single text node "blob".
        Element e = DOMAssertionUtilities.assertElement("tag", node);
        Text t = DOMAssertionUtilities.assertText("content", e.getHead());
        e = DOMAssertionUtilities.assertElement("empty",t.getNext());
        t = DOMAssertionUtilities.assertText("more content",e.getNext());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
