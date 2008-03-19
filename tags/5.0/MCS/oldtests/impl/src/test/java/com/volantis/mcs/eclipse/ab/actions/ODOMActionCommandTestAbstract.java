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
package com.volantis.mcs.eclipse.ab.actions;

import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;
import org.jdom.input.JDOMFactory;
import org.jdom.input.SAXBuilder;
import org.custommonkey.xmlunit.XMLAssert;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.ab.editors.dom.LPDMODOMFactory;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.xml.xpath.XPath;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * This class supports the various {@link ODOMActionCommand} tests.
 */
public abstract class ODOMActionCommandTestAbstract extends TestCaseAbstract {
    /**
     * Used when creating XPaths from XPath strings. The only supported
     * namespace is LPDM
     */
    protected final static Namespace[] namespaces = new Namespace[] {
        MCSNamespace.LPDM
    };

    /**
     * A factory for creating elements in the LPDM namespaced by default
     */
    protected final static JDOMFactory factory = new LPDMODOMFactory();

    /**
     * Provides access to the test data.
     */
    public interface TestData {
        /**
         * Returns the command being tested.
         *
         * @return the command being tested
         */
        ODOMActionCommand getCommand();

        /**
         * The document on which the command is being run.
         *
         * @return the document on which the command is being run
         */
        ODOMElement getDocument();

        /**
         * The selections against which the command is being run.
         *
         * @return the selections against which the command is being run
         */
        ODOMElement[] getSelection();

        /**
         * Returns an arbitrary object associated with the test data.
         *
         * @return an arbitrary object associated with the test data
         */
        Object getObject();

        /**
         * Stores an arbitrary object in the test data.
         *
         * @param object the object to store
         */
        void setObject(Object object);

        /**
         * Stores a selection manager
         */
        void setSelectionManager(ODOMSelectionManager selectionManager);

        /**
         * Return the stored selection manager
         */
        ODOMSelectionManager getSelectionManager();
    }

    /**
     * Implements the test data interface.
     */
    private class TestDataImpl implements TestData {
        /**
         * The command represented by the data
         *
         * @supplierCardinality 1
         */
        protected ODOMActionCommand command;

        /**
         * The document on which the command will operate
         *
         * @supplierCardinality 0..1
         */
        protected ODOMElement document;

        /**
         * The selection on which the command will operate
         *
         * @supplierCardinality 0..*
         */
        protected ODOMElement[] selection = new ODOMElement[0];

        /**
         * The arbitrary object associated with the test
         */
        protected Object object = null;


        protected ODOMSelectionManager selectionManager = null;

        /**
         * Initializes the new instance using the given parameters.
         *
         * @param command the command
         */
        public TestDataImpl(ODOMActionCommand command) {
            this.command = command;
        }

        // javadoc inherited
        public ODOMActionCommand getCommand() {
            return command;
        }

        // javadoc inherited
        public ODOMElement getDocument() {
            return document;
        }

        // javadoc unnecessary
        public void setDocument(ODOMElement document) {
            this.document = document;
        }

        // javadoc inherited
        public ODOMElement[] getSelection() {
            return selection;
        }

        // javadoc unnecessary
        public void setSelection(ODOMElement[] selection) {
            this.selection = selection;
        }

        // javadoc inherited
        public Object getObject() {
            return object;
        }

        // javadoc inherited
        public void setObject(Object object) {
            this.object = object;
        }

         // javadoc inherited
        public void setSelectionManager(ODOMSelectionManager selectionManager) {
            this.selectionManager = selectionManager;
        }

         // javadoc inherited
        public ODOMSelectionManager getSelectionManager() {
            return selectionManager;
        }
    }

    /**
     * Initializes the new instance with the given parameters.
     */
    public ODOMActionCommandTestAbstract() {
    }

    /**
     * Creates a concrete instance of the {@link TestData} interface compatible
     * with the {@link #setDocument} and {@link #setSelections} methods.
     *
     * @param command the command for which a {@link TestData} instance is
     *                required
     * @return a {@link TestData} implementation instance
     */
    protected TestData createTestData(ODOMActionCommand command) {
        return new TestDataImpl(command);
    }

    /**
     * Supporting method that allows the test data document to be defined from
     * an XML string that doesn't specify namespaces: the default namespace
     * is set to LPDM. May only be used with {@link TestData} implementation
     * instances returned by {@link #createTestData}.
     *
     * @param data the test data to be modified
     * @param xml the XML string
     */
    protected void setDocument(TestData data,
                               String xml) throws Exception {
        ((TestDataImpl)data).setDocument(createDocument(xml));
    }

    /**
     * Supporting method that allows the test data document to be set.
     *
     * @param data the test data to be modified
     * @param document The element representing the root of the test
     * data document
     * @throws Exception
     */
    protected void setDocument(TestData data, ODOMElement document)
            throws Exception {
        ((TestDataImpl)data).setDocument(document);
    }

    /**
     * Utility class to set the seleciton manager of the test data
     * @param data the test data to be modified
     * @param selManager the selection manager to add to the test data
     */
    protected void setSelectionManager(TestData data,
                                       ODOMSelectionManager selManager) {
        ((TestDataImpl)data).setSelectionManager(selManager);
    }

    /**
     * Supporting method that converts the given XML string (with default
     * namespace of LPDM) into a JDOM tree, returning the root node.
     *
     * @param xml the XML string
     * @return a JDOM document root node
     */
    protected ODOMElement createDocument(String xml) throws Exception {
        SAXBuilder builder = new SAXBuilder();

        // Default the namespace to LPDM
        builder.setFactory(factory);

        return (ODOMElement)builder.build(
            new StringReader(xml)).getRootElement();
    }

    /**
     * Supporting method that allows the data data selection to be defined
     * using a set of given XPath strings. These must use the "lpdm" prefix to
     * address elements from the LPDM namespace. May only be used with
     * {@link TestData} implementation instances returned by
     * {@link #createTestData}.
     *
     * @param data the test data to be modified
     * @param xpaths an array of XPath strings used to populate the test data. May be null
     * @throws Exception if one or more XPath cannot be resolved to an element
     */
    protected void setSelections(TestData data,
                                 String[] xpaths) throws Exception {
        ODOMElement[] selection;

        if (xpaths == null) {
            selection = new ODOMElement[0];
        } else {
            XPath xpath;
            selection = new ODOMElement[xpaths.length];

            for (int i = 0; i < xpaths.length; i++) {
                xpath = new XPath(xpaths[i], namespaces);

                selection[i] = (ODOMElement)xpath.selectSingleElement(
                        data.getDocument());
            }
        }

        ((TestDataImpl)data).setSelection(selection);
    }

    /**
     * Creates an {@link ODOMActionDetails} implementation instance based on
     * the given test data.
     *
     * @param data the test data from which the details are to be populated
     * @return an ODOMActionDetails implementation
     */
    protected ODOMActionDetails createDetails(final TestData data) {
        /**
         * An anonymous implementation of the ODOMActionDetails interface that
         * provides access to the test data's selection information.
         */
        return new ODOMActionDetails() {
            // javadoc inherited
            public int getNumberOfElements() {
                return data.getSelection().length;
            }

            // javadoc inherited
            public ODOMElement getElement(int i) {
                return data.getSelection()[i];
            }

            // javadoc inherited
            public ODOMElement[] getElementsClone() {
                ODOMElement[] clone =
                        new ODOMElement[data.getSelection().length];

                System.arraycopy(data.getSelection(), 0,
                        clone, 0,
                        data.getSelection().length);

                return clone;
            }

        };
    }

    /**
     * Used to perform enable tests.
     *
     * @param data the test data containing the configuration to be tested
     * @param testName the test name as a string for error reporting
     * @param expected the expected enable status
     */
    protected void doTestEnable(TestData data,
                                String testName,
                                boolean expected) throws Exception {
        assertEquals("Enablement " + testName + " not as",
                     expected,
                     data.getCommand().enable(createDetails(data)));
    }

    /**
     * Used to perform run tests.
     *
     * @param data the test data containing the configuration to be tested
     * @param expectedXml the expected document structure
     */
    protected void doTestRun(TestData data,
                             String expectedXml) throws Exception {
        data.getCommand().run(createDetails(data));

        assertDocumentsEqual(data, expectedXml);
    }

    /**
     * Asserts that the document in the test data and the given string are
     * the "same" (according to XMLUnit).
     *
     * @param data the test data who's document is to be compared
     * @param expectedXml the expected XML structure as a string (with default
     *                    LPDM namespace)
     */
    protected void assertDocumentsEqual(TestData data,
                                        String expectedXml) throws Exception {
        XMLOutputter out = new XMLOutputter();
        StringWriter expectedWriter = new StringWriter();
        StringWriter actualWriter = new StringWriter();

        ODOMElement expectedDocument = createDocument(expectedXml);

        out.output(expectedDocument, expectedWriter);
        out.output(data.getDocument(), actualWriter);

        XMLAssert.assertXMLEqual(expectedWriter.toString(),
                actualWriter.toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 11-Feb-04	2939/1	eduardo	VBM:2004020506 ODOM DeleteActionCommand changed to be undo/redo friendly

 21-Jan-04	2635/1	philws	VBM:2003121513 Implement the New Grid and non-Grid Action Commands

 ===========================================================================
*/
