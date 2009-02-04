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
package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolConfigurationMock;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.ProtocolSupportFactoryMock;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FragmentableFormData;
import com.volantis.mcs.protocols.forms.ImplicitFieldType;
import com.volantis.mcs.protocols.forms.MultipleSelectFieldType;
import com.volantis.mcs.protocols.forms.SingleSelectFieldType;
import com.volantis.mcs.protocols.forms.TextInputFieldType;
import com.volantis.mcs.protocols.forms.FormDataManagerMock;
import com.volantis.mcs.protocols.forms.SessionFormDataMock;
import com.volantis.mcs.protocols.forms.FormDescriptor;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.runtime.URLConstants;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.schema.JarFileEntityResolver;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * This test verifies that the {@link XFormEmulationTransformer} behaves
 * correctly (see class javadoc for the definition of correct behaviour).
 * <p/>
 * In summary, the tests should check that the transformer:
 * <ul>
 * <li>creates the element used to emulate a form in the protocol being used</li>
 * <li>inserts this element immediately before the ancestor of the first
 * control that has a common sibling with the ancestor of the last control
 * E.g.
 *             E1                                   E1
 *  ...   _____|______  ...                ...    __|___  ...
 *      /   /     \   \                         /   |   \
 *     E2  E3    E4  E5         -->            E2   X   E5
 *         |       |                               _|_
 *       /  \    /  \                            /    \
 *      E6  E7  E8  E9                          E3     E4
 *              |                               |       |
 *             E10                            /  \    /  \
 *                                           E6  E7  E8  E9
 *                                                   |
 *                                                  E10
 * </li>
 * <li>inserts an implicit element which includes the form specifier<li>
 * <li>moves implicit elements with marker attributes into the correct form</li>
 * </ul>
 */
public abstract class XFormEmulationTransformerTestAbstract
    extends TestCaseAbstract {

    // Declare objects required for testing.
    ProtocolConfigurationMock protocolConfig;
    ProtocolSupportFactoryMock psf;
    MarinerPageContextMock pageContext;
    DOMFactory domFactory;
    EmulatedXFormDescriptor default_FD;
    EmulatedXFormDescriptor FD2;
    EmulatedXFormDescriptor FD3;

    /**
     * Names of the files containing the markup to use in the tests.
     */
    protected String inputSimpleSingleFormFileName;
    protected String inputSimpleMultipleFormsFileName;
    protected String expectedSimpleSingleFormFileName;
    protected String expectedSimpleMultipleFormsFileName;

    /**
     * The following 2-d arrays (arrays of String name and FieldType pairs)
     * are used to set the expectations of the fields in the
     * EmulatedXFormDescriptors defined above.
     */
    protected static final Object[][] SINGLE_DEFAULT_FIELDS = {
        {"Unreferenced value", ImplicitFieldType.getSingleton()},
        {"MyPassword", TextInputFieldType.getSingleton()},
        {"Select1", SingleSelectFieldType.getSingleton()},
        {"Field2", TextInputFieldType.getSingleton()},
        {"Field1", TextInputFieldType.getSingleton()},
        {"Select", MultipleSelectFieldType.getSingleton()},
        {"Field-1", TextInputFieldType.getSingleton()}
    };
    protected static final Object[][] MULTIPLE_DEFAULT_FIELDS = {
        {"Unreferenced value", ImplicitFieldType.getSingleton()},
        {"Select", MultipleSelectFieldType.getSingleton()},
        {"Field-1", TextInputFieldType.getSingleton()}
    };
    protected static final Object[][] MULTIPLE_MODEL2_FIELDS = {
        {"UnreferencedValueTwo", ImplicitFieldType.getSingleton()},
        {"MyPassword", TextInputFieldType.getSingleton()},
        {"Select1", SingleSelectFieldType.getSingleton()},
        {"Field2", TextInputFieldType.getSingleton()},
        {"Field1", TextInputFieldType.getSingleton()},
    };
    protected static final Object[][] MULTIPLE_MODEL3_FIELDS = {
        {"UnreferencedValue3", ImplicitFieldType.getSingleton()},
        {"Field-1", TextInputFieldType.getSingleton()}
    };

    /**
     * Returns the {@link DOMProtocol} instance which should be used when
     * testing.
     *
     * @param protocolConfig    configuration to use when creating the protocol
     * @param psf               support factory to use when creating the protocol
     * @return {@link DOMProtocol} instance which should be used when testing.
     */
    public abstract DOMProtocol createProtocol(
            ProtocolConfiguration protocolConfig, ProtocolSupportFactory psf);

    // Javadoc inherited.
    public void setUp() throws Exception {
        super.setUp();

        // Create mock objects
        protocolConfig = new ProtocolConfigurationMock("protocolConfig",
                expectations);
        protocolConfig.expects.getStyleEmulationElementConfiguration()
                .returns(null).optional();
        protocolConfig.fuzzy.isInvalidFormLinkParent(
                mockFactory.expectsInstanceOf(String.class)).returns(false).any();
        psf = new ProtocolSupportFactoryMock("psf", expectations) ;
        pageContext = new MarinerPageContextMock("pageContext", expectations);

        // Create test objects
        domFactory = DOMFactory.getDefaultInstance();
        final String action1 = "http://www.volantis.com";
        default_FD = new EmulatedXFormDescriptor(action1, "get", "s1");
        default_FD.setContainingFormName("__DEFAULT__");
        XFFormAttributes formAttributes1 = new XFFormAttributes();
        final FragmentableFormData form =
                new FragmentableFormData(default_FD, -1, false, new ArrayList());
        formAttributes1.setFormData(form);
        formAttributes1.setAction(new LiteralLinkAssetReference(action1));
        default_FD.setFormAttributes(formAttributes1);

        final String action2 = "http://www.smudge.com";
        FD2 = new EmulatedXFormDescriptor(action2, "get", "s2");
        FD2.setContainingFormName("model2");
        XFFormAttributes formAttributes2 = new XFFormAttributes();
        final FragmentableFormData form2 =
                new FragmentableFormData(FD2, -1, false, new ArrayList());
        formAttributes2.setFormData(form2);
        formAttributes2.setAction(new LiteralLinkAssetReference(action2));
        FD2.setFormAttributes(formAttributes2);

        final String action3 = "http://www.volantis.com/delivery";
        FD3 = new EmulatedXFormDescriptor(action3, "get", "s3");
        FD3.setContainingFormName("model3");
        XFFormAttributes formAttributes3 = new XFFormAttributes();
        final FragmentableFormData form3 =
                new FragmentableFormData(FD3, -1, false, new ArrayList());
        formAttributes3.setFormData(form3);
        formAttributes3.setAction(new LiteralLinkAssetReference(action3));
        FD3.setFormAttributes(formAttributes3);

        // set expectations
        psf.expects.getDOMFactory().returns(domFactory);
        pageContext.expects.getEmulatedXFormDescriptor("__DEFAULT__").returns(default_FD);
        pageContext.expects.getEmulatedXFormDescriptor("model2").returns(FD2).optional();
        pageContext.expects.getEmulatedXFormDescriptor("model3").returns(FD3).optional();
    }

    /**
     * Converts the XML in the supplied files to DOMs and then verifies that
     * the transformed input XML is the same as the expected XML.
     *
     * @param inputFile     file containing the XML to be transformed
     * @param expectedFile  file containing expected markup after transformation
     * @throws IOException  if there was a problem running the test
     * @throws SAXException if there was a problem running the test
     */
    private void doTestTransform(String inputFile, String expectedFile)
            throws IOException, SAXException, ParserConfigurationException {

        JarFileEntityResolver entityResolver = new JarFileEntityResolver();
        entityResolver.addSystemIdMapping(
                "http://www.wapforum.org/DTD/wml_1.1.xml",
                "com/volantis/mcs/protocols/dtd/wml_1.1.xml");
        // create the Documents from the markup
        StyledDOMTester tester = new StyledDOMTester(entityResolver);
        Document inputDoc =
                tester.parse(getClass().getResourceAsStream(inputFile));
        Document expectedDoc =
                tester.parse(getClass().getResourceAsStream(expectedFile));
        DOMProtocol protocol = createProtocol(protocolConfig, psf);
        protocol.setMarinerPageContext(pageContext);

        // run the test
        XFormEmulationTransformer transformer = new XFormEmulationTransformer();
        Document outputDoc = transformer.transform(protocol, inputDoc);

        // convert Document to markup
        String canonicalExpectedXML = tester.render(expectedDoc);
        String actualXML = tester.render(outputDoc);

        // compare the actual markup with that expected
        assertXMLEquals("Actual XML does not match expected XML." +
                "\nExpectedXML:\n" + canonicalExpectedXML +
                "\nActualXML:\n" + actualXML,
                canonicalExpectedXML, actualXML);
    }

   /**
    * Verifies that markup containing a simple single form is correctly
    * transformed (see {@link XFormEmulationTransformer} for information on
    * how the form should be transformed).
    *
     * @throws IOException if there was a problem running the test
     * @throws SAXException if there was a problem running the test
     * @throws ParserConfigurationException if there was a problem running the test
     */
    public void testTransformSimpleXForm() throws IOException, SAXException,
           ParserConfigurationException {

       setSimpleSingleExpectations();       
       doTestTransform(inputSimpleSingleFormFileName,
               expectedSimpleSingleFormFileName);
   }

   /**
    * Verifies that markup containing simple multiple forms is correctly
    * transformed (see {@link XFormEmulationTransformer} for information on
    * how the markup should be transformed).
    *
     * @throws IOException if there was a problem running the test
     * @throws SAXException if there was a problem running the test
     * @throws ParserConfigurationException if there was a problem running the
    * test
     */
    public void testTransformSimpleMultipleXForms() throws IOException,
            ParserConfigurationException, SAXException {

        setSimpleMultipleExpectations();       
        doTestTransform(inputSimpleMultipleFormsFileName,
                expectedSimpleMultipleFormsFileName);
    }

    // Javadoc inherited.
    protected abstract void setSimpleSingleExpectations();

    // Javadoc inherited.
    protected abstract void setSimpleMultipleExpectations();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/2	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 12-Oct-05	9673/2	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/1	emma	VBM:2005092807 Adding tests for XForms emulation

 ===========================================================================
*/
