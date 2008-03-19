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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/trans/TransVisitorTestAbstract.java,v 1.2 2003/04/07 09:34:37 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 04-Apr-03    Adrian          VBM:2003031701 - Added this as the base class 
 *                              for TransVisitor testcases 
 * 03-Jun-03    Byron           VBM:2003042204 - Added doDOMComparison method.
 * 04-Jun-03    Byron           VBM:2003042204 - Removed transVisitor property.
 *                              Added getProtocolSpecificFactory. Moved 
 *                              doProcessingTest, debugOuput, toString methods
 *                              from subclass. Added parameter to debugOutput.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junit.framework.TestCase;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

/**
 * Parent for all {@link TransVisitor} testcases and their subclasses; this 
 * defines the structures which ensure the inheritance of test case fixtures 
 * works properly.
 * <p>
 * Each subclass of this class must:
 * <ul>
 * <li> Implement the {@link #getProtocolSpecificFactory} method to return
 *      the subtype of {@link TransFactory} that it is testing.
 *
 * <li> Implement the {@link #createDOMProtocol} method to return the subtype
 *      of {@link DOMProtocol} that is associated with the {@link TransVisitor}
 *      which is being tested.
 *
 * <li> Override the {@link #setDOMProtocol} method, saving a reference to the
 *      created {@link DOMProtocol} into an instance variable and calling the
 *      superclass version as well to allow it to get a reference to the
 *      created class. 
 * 
 * <li>Avoid redefining the {@link TestCase#setUp} method since the behaviour
 *     already defined here is required for this to all work.
 * </ul>
 * <p>
 * Note that it is not possible for subclasses to use the 
 * {@link TestCase#setUp} method as normal since each test case must set up
 * independently of it's subclasses, and there is no way to model this with a
 * protected method, even given the super() method.  
 */ 
public abstract class TransVisitorTestAbstract extends TestCaseAbstract {    

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    /**
     * The {@link DOMProtocol} associated with the {@link TransVisitor} that
     * we are testing.
     */ 
    protected DOMProtocol protocol;

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();


    protected static final String INDENT = "    ";
    private StrictStyledDOMHelper helper;

    // Javadoc inherited from superclass
    protected void setUp() throws Exception {
        super.setUp();

        InternalDevice internalDevice = InternalDeviceTestHelper.createTestDevice();

        setDOMProtocol(createDOMProtocol(internalDevice));

        helper = new StrictStyledDOMHelper(null);

    }

    /**
     * Get the protocol specific factory. Each subclassed 'protocol' will
     * need to implement this method. We do not need the visitor methods cos
     * we can now get the visitor from the factory.
     * <p/>
     * The implementations of this method should ensure that the returned
     * {@link TransFactory} is correctly initialised for testing (e.g. has a
     * non null {@link TransformationConfiguration}).
     *
     * @return the protocol specific factory.
     */
    abstract protected TransFactory getProtocolSpecificFactory();

    /**
     * Create the {@link DOMProtocol} which we are using in testing.
     * Implementations of this method must not call super()
     * @return An instance of a {@link DOMProtocol}
     * @param internalDevice
     */ 
    protected abstract DOMProtocol createDOMProtocol(
            InternalDevice internalDevice);

    /**
     * Set the {@link DOMProtocol} which we are using in testing 
     * @param protocol the {@link DOMProtocol} to set.
     */ 
    protected void setDOMProtocol(DOMProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * This tests the method isWhitespaceTextNode(Node)
     * The method is designed to check if the specified 
     * {@link Node} is an instance of
     * {@link Text} which contains only whitespace
     * characters. 
     */ 
    public void testIsWhitespaceTextNode() throws Exception {
        Text text = domFactory.createText();
        text.append("some content");
        TransVisitor visitor = getProtocolSpecificFactory().getVisitor(protocol);
        boolean result = visitor.isWhitespaceTextNode(text);
        assertTrue("Node was not a whitespace only text node.", !result);
        
        Element element = domFactory.createElement();
        result = visitor.isWhitespaceTextNode(element);
        assertTrue("Node was not a whitespace only text node.", !result);
        
        Text whitespace = domFactory.createText();
        text.append("   ");
        result = visitor.isWhitespaceTextNode(whitespace);
        assertTrue("Node was a whitespace only text node.", result);
    }
    
    /**
     * This tests the method removeWhitespaceChildren(Element).
     * The method is designed to iterate over the children of the specified
     * element.  Where a child {@link Node} is found to be
     * an instance of {@link Text} which contains only
     * whitespace characters it is removed as a child of the parent.
     * 
     * Here we will check that the whitespace {@link Node}
     * have been correctly removed.
     */
    public void testRemoveWhitespaceChildren() throws Exception {
        String original =
                "<html>" +
                  " " + 
                  "<table>" +
                    "<tr><td>content</td></tr>" +
                  "</table>" +
                  " " + 
                  "<p>content</p>" +
                  "<p>content</p>" +
                  " " +
                "</html>";
        
        String expected =
                "<html>" +
                  "<table>" +
                    "<tr><td>content</td></tr>" +
                  "</table>" +                
                  "<p>content</p>" +
                  "<p>content</p>" +                
                "</html>";

        Document dom = helper.parse(original);
        Document expectedDOM = helper.parse(expected);

        TransVisitor trans = getProtocolSpecificFactory().getVisitor(protocol);
        Element html = dom.getRootElement();
        trans.removeWhitespaceChildren(html);

        doDOMComparison(expectedDOM, dom);
    }

    /**
     * Read the expected and actual strings into a XMLReader and produce a
     * document for each which is parsed into a string used for string
     * comparison.
     *
     * @param  expected     the expected dom as a string
     * @param  actual       the actual dom as a string
     * @throws SAXException
     * @throws IOException
     */
    protected void doDOMComparison(Document expected, Document actual)
            throws SAXException, IOException {

        // Remove any null elements added by the remapping before rendering.
        // In normal operation this is done at the DOMTransformer level.        
        // @todo I would rather pass a DOMProtocolMock than null, but mocks are
        // currently not accessible from integration tests, and StyledDOMTester
        // is not accessible from unit tests.... so move this to unit tests and
        // use a DOMProtocolMock when that becomes possible.
        NullRemovingDOMTransformer nullRemover = new NullRemovingDOMTransformer();
        nullRemover.transform(null, actual);

        String actualXML = helper.render(actual);
        String expectedXML = helper.render(expected);

        assertEquals("DOM not transformed as expected",
                expectedXML, actualXML);
    }

    /**
     * Perform the processing test with the given input parameters.
     *
     * @param original the original dom as a string.
     * @param expected the expected dom as a string
     */
    protected void doProcessingTest(String original, String expected, boolean debug)
            throws IOException, Exception {

        Document dom = helper.parse(original);
        Document expectedDOM = helper.parse(expected);

        TransVisitor visitor = getProtocolSpecificFactory().
                getVisitor(protocol);

        visitor.preprocess(dom);

        if (debug) {
            debugOutput(dom, "table");
        }
        // Process the dom using the visitor.
        visitor.process();

        if (debug) {
            debugOutput(dom, "preprocessed table");
        }

        doDOMComparison(expectedDOM, dom);
    }

    /**
     * Utility method that may be quite handy for debugging.
     *
     * @param dom  the dom as a Document
     * @param msg  a specific message to output for the debug statements.
     */
    private void debugOutput(Document dom, String msg)
            throws Exception {
        // Useful debug output
        System.out.println(DOMUtilities.toString(
                dom, protocol.getCharacterEncoder()));

        List tables = getProtocolSpecificFactory().getVisitor(protocol).getTables();

        for (int i = 0;i < tables.size(); i++) {
            TransTable table = (TransTable)tables.get(i);
            System.out.println(msg + "[" + i + "] =");
            System.out.println(toString(table, "    ",  protocol));
        }
    }

    /**
     * Utility method used by the debugOutput method
     *
     * PLEASE DO NOT DELETE
     */
    private String toString(TransTable table,
                            String indent,
                            DOMProtocol protocol) throws Exception {
        String result = indent +
            "<TransTable rows=\"" + table.getRows() + "\" cols=\"" +
            table.getCols() +
            "\" depth=\"" + table.getDepth() + "\">\n";

        result += toString(table.getElement(),
                           indent + INDENT,
                           protocol);

        for (int row = 0; row < table.getCellRows(); row++) {
            for (int col = 0; col < table.getCellCols(); col++) {
                TransCell cell = table.getCell(row, col);
                if (cell != null) {
                    result += toString(cell, indent + INDENT, protocol);
                }
            }
        }

        result += indent + "</TransTable>\n";

        return result;
    }

    /**
     * Utility method used by the debugOutput method
     *
     * PLEASE DO NOT DELETE
     */
    private String toString(TransCell cell,
                            String indent,
                            DOMProtocol protocol) throws Exception {
        String result = indent + "<TransCell row=\"" + cell.getStartRow() +
            "\" col=\"" + cell.getStartCol() +
            "\" depth=\"" + cell.getDepth() + "\">\n";

        result += toString(cell.getElement(), indent + INDENT, protocol);

        if (cell.getTable() != null) {
            result += toString(cell.getTable(), indent + INDENT, protocol);
        }

        result += indent + "</TransCell>\n";

        return result;
    }

    /**
     * Utility method used by the debugOutput method
     *
     * PLEASE DO NOT DELETE
     */
    private String toString(Element original,
                            String indent,
                            DOMProtocol protocol) throws Exception {
        String result = indent;

        if (original != null) {
            result += "<element>";
            Document doc = domFactory.createDocument();
            Element element = domFactory.createElement();
            element.copy(original);
            original.addChildrenToTail(element);
            doc.addNode(element);
            result += DOMUtilities.toString(
                    doc, protocol.getCharacterEncoder()) + "</element>\n";
            element.addChildrenToTail(original);
        } else {
            result += "<element/>\n";
        }

        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Nov-05	10381/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 22-Aug-05	9223/5	emma	VBM:2005080403 Remove style class from within protocols and transformers

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
