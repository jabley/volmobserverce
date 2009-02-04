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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/ScriptTestCase.java,v 1.2 2003/04/23 09:44:20 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Apr-03    Geoff           VBM:2003040305 - Created; a TestCase for the 
 *                              Script class.
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.DOMAssertionUtilities;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.ScriptAssetReferenceMock;
import com.volantis.mcs.protocols.assets.implementation.LiteralScriptAssetReference;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A TestCase for the {@link Script} class.
 */ 
public class ScriptTestCase extends TestCaseAbstract {


    /**
     * Creates the appropriate script object; enables inheritance of tests.
     * 
     * @param context context to use while creating
     * @param scriptObject object to create from.
     * @return the created script object for this testcase.
     * 
     * @throws ProtocolException
     */ 
    Script createScript(MarinerPageContext context,
                        ScriptAssetReference scriptObject)
            throws ProtocolException {
        return Script.createScript(scriptObject);
    }
    
    /**
     * Test creation of a script with a null value, should fail.
     * 
     * @throws ProtocolException
     */ 
    public void testCreateNull() throws ProtocolException {
        TestMarinerPageContext context = new TestMarinerPageContext();
        try {
            createScript(context, null);
            fail("None scripts are invalid");
        } catch (NullPointerException e) {
            // Normal
        }
    }

    /**
     * Test creation of a script with a (non asset) object which returns a
     * null value from toString() should fail.
     *
     * @throws ProtocolException
     */
    public void testCreateSimpleNull() throws ProtocolException {
        TestMarinerPageContext context = new TestMarinerPageContext();

        final ScriptAssetReferenceMock scriptMock =
                new ScriptAssetReferenceMock("scriptMock",
                        expectations);
        scriptMock.expects.getScript().returns(null).any();

        Script script = createScript(context, scriptMock);
        assertNull("Script should not have been created from null script asset",
            script);
    }

    /**
     * Create a reasonable "default" script value for this type of script.
     * 
     * @return the script value.
     */ 
    ScriptAssetReference getDefaultScriptValue() {
        // a normal script treats values as text, so doesn't really matter.
        return new LiteralScriptAssetReference("a script");
    }
    
    /**
     * Test creation of a script from a simple text value (no asset)
     * Also doubles as a test of stringValue().
     * 
     * @throws ProtocolException
     */ 
    public void testCreateSimple() throws ProtocolException {
        ScriptAssetReference expected = getDefaultScriptValue();
        TestMarinerPageContext context = new TestMarinerPageContext();
        Script script = createScript(context, expected);
        String actual = script.stringValue();
        assertEquals(expected.getScript(), actual);
    }

    /**
     * Test the equals method.
     * 
     * @throws ProtocolException
     */ 
    public void testEquals() throws ProtocolException {
        TestMarinerPageContext context = new TestMarinerPageContext();
        ScriptAssetReference scriptObject = getDefaultScriptValue();
        Script script = createScript(context, scriptObject);
        assertTrue(! script.equals(null));
        assertTrue(! script.equals("dummy object"));
        
        Script script2 = createScript(context, scriptObject);
        assertEquals(script, script2);
        
        Script script3 = createScript(context,
                new LiteralScriptAssetReference("another script"));
        assertNotEquals(script,script3);
    }
    
    /**
     * Test the appendTo() method.
     * 
     * @throws ProtocolException
     */ 
    public void testAppendTo() throws ProtocolException {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        TestMarinerPageContext context = new TestMarinerPageContext();
        ScriptAssetReference scriptObject = getDefaultScriptValue();
        Script script = createScript(context, scriptObject);
        script.appendTo(buffer);
        Node node = buffer.getRoot().getHead();
        checkAppendToResult(node);
    }

    /**
     * Check that the Node contains the value as returned from 
     * {@link #getDefaultScriptValue}.
     * 
     * @param node The node to check.
     */ 
    void checkAppendToResult(Node node) {
        DOMAssertionUtilities.assertText(getDefaultScriptValue().getScript(), node);
        assertNull(node.getNext());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 06-Aug-03	956/1	geoff	VBM:2003080601 finally fix bodgy marinerurlregistry

 ===========================================================================
*/
