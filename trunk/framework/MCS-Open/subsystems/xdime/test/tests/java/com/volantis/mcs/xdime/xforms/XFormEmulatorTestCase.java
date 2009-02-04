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

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.DOMWalker;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.mcs.dom.WalkingDOMVisitorStub;
import com.volantis.mcs.protocols.DOMOutputBufferMock;
import com.volantis.mcs.protocols.DOMProtocolMock;
import com.volantis.mcs.protocols.OutputBufferFactoryMock;
import com.volantis.mcs.protocols.ProtocolConfigurationMock;
import com.volantis.mcs.protocols.ProtocolSupportFactoryMock;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.html.XHTMLBasic;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptorMock;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.styling.engine.StylingEngineMock;
import com.volantis.styling.StylesMock;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * Verifies the behaviour of the {@link XFormEmulationTransformer}.
 * @todo finish writing tests
 */
public class XFormEmulatorTestCase extends TestCaseAbstract {

    private static final String FORM_NAME = "testForm";
    private static final String FORM_SPECIFIER = "s0";

    public void testGetDepthInValidStructure() throws IOException, SAXException {
        DOMFactory factory = DOMFactory.getDefaultInstance();

        Element element = factory.createElement();
        element.setName("0");
        Element[] elements = createElementHierarchy(factory, element, 20, 13,
                20, "A");
        Element mainTrunkBottom = elements[0];
        Element branchBottom = elements[1];

        XFormEmulator emulator =
                new XFormEmulator();

        assertEquals(0, emulator.getDepth(element));
        assertEquals(19, emulator.getDepth(mainTrunkBottom));
        assertEquals(32, emulator.getDepth(branchBottom));
    }

    /**
     * Convenience method which generates an element hierarchy that is the
     * specified number of elements deep, with a branch at the specified depth.
     * The branch will be the specified depth. The names will be the specified
     * name prefix plus the element depth. The branch prefix will be "B".
     *
     * @param factory       to use to create the elements
     * @param root          root of the element hierarchy
     * @param depth         the number of elements deep the structure is e.g.
     *                      depth=3 would produce <a0><a1><a2/></a1></a0>
     * @param branchPoint   the depth at which a new branch should be created
     *                      (obviously -1 would result in no branch).
     * @param branchDepth   the number of elements (depth) in the branch
     * @param namePrefix    prefix to use for the main branch element names
     * @return Element[] containing two Elements, the first is the deepest
     * element in the main branch, the second is the deepest element in the
     * branch.
     */
    private Element[] createElementHierarchy(DOMFactory factory,
            Element root, int depth, int branchPoint, int branchDepth,
            String namePrefix) {

        Element[] elements = new Element[2];

        Element element = root;
        Element child = null;

        for (int i = 1; i < depth; i++) {
            child = factory.createElement(namePrefix + i);
            if (i == branchPoint) {
                elements[1] = createElementHierarchy(factory, element,
                                                     branchDepth + 1, -1, 1, "B")[0];
            }
            element.addHead(child);
            element = child;
        }
        elements[0] = element;
        return elements;
    }

    public void testInsertXFormEmulationElementWithNoCommonParent(){
        XFormEmulator emulator = new XFormEmulator();

        // Create test objects.
        StyledDOMTester tester = new StyledDOMTester();
        Document dom = tester.parse(getClass().getResourceAsStream(
                "testInsertXFormEmulationElement.xml"));

        DOMFactory domFactory = DOMFactory.getDefaultInstance();
        Element firstControl = selectNamedElement(dom, "html");
        Element lastControl = selectNamedElement(dom, "body");

        // Create Mocks.
        ProtocolSupportFactoryMock psf =
                new ProtocolSupportFactoryMock("psf", expectations);
        ProtocolConfigurationMock configuration =
                new ProtocolConfigurationMock("configuration",
                        expectations);
        EmulatedXFormDescriptorMock fd =
                new EmulatedXFormDescriptorMock("fd", expectations);

        // These expectations must be set before the DOMProtocolMock is created.
        psf.expects.getDOMFactory().returns(domFactory);
        configuration.expects.getValidationHelper().returns(null).any();
        DOMProtocolMock protocol = new DOMProtocolMock("protocol",
                expectations, psf, configuration);
        protocol.expects.getDocument().returns(dom).any();
        // Configure the emulator, and run the tests.
        emulator.firstControl = firstControl;
        emulator.lastControl = lastControl;
        emulator.protocol = protocol;
        emulator.currentFormName = FORM_NAME;
        emulator.fd = fd;

        assertFalse(emulator.completedForms.containsKey(FORM_NAME));
        emulator.insertXFormEmulationElement();
        assertTrue(emulator.completedForms.containsKey(FORM_NAME));
        assertEquals(null, emulator.completedForms.get(FORM_NAME));
    }

    public void testInsertXFormEmulationElementWithNoValidCommonParent(){
        XFormEmulator emulator = new XFormEmulator() {
            protected boolean isValidFormParent(Element candidateParent) {
                return false;
            }
        };

        // Create test objects.
        StyledDOMTester tester = new StyledDOMTester();
        Document dom = tester.parse(getClass().getResourceAsStream(
                "testInsertXFormEmulationElement.xml"));

        DOMFactory domFactory = DOMFactory.getDefaultInstance();
        Element firstControl = selectNamedElement(dom, "html");
        Element lastControl = selectNamedElement(dom, "body");

        // Create Mocks.
        ProtocolSupportFactoryMock psf =
                new ProtocolSupportFactoryMock("psf", expectations);
        ProtocolConfigurationMock configuration =
                new ProtocolConfigurationMock("configuration",
                        expectations);
        EmulatedXFormDescriptorMock fd =
                new EmulatedXFormDescriptorMock("fd", expectations);

        // These expectations must be set before the DOMProtocolMock is created.
        psf.expects.getDOMFactory().returns(domFactory);
        configuration.expects.getValidationHelper().returns(null).any();
        DOMProtocolMock protocol = new DOMProtocolMock("protocol",
                expectations, psf, configuration);
        protocol.expects.getDocument().returns(dom).any();
        // Configure the emulator, and run the tests.
        emulator.firstControl = firstControl;
        emulator.lastControl = lastControl;
        emulator.protocol = protocol;
        emulator.currentFormName = FORM_NAME;
        emulator.fd = fd;

        assertFalse(emulator.completedForms.containsKey(FORM_NAME));
        emulator.insertXFormEmulationElement();
        assertTrue(emulator.completedForms.containsKey(FORM_NAME));
        assertEquals(null, emulator.completedForms.get(FORM_NAME));
    }


    public void testInsertXFormEmulationElementWithInvalidFirstCommonParent(){
        // Create an xform emulator which will find the first common parent
        // (which will be a <b>) to be invalid, but all others to be fine.
        XFormEmulator emulator = new XFormEmulator() {
            protected boolean isValidFormParent(Element candidateParent) {
                if ("b".equals(candidateParent.getName())) {
                    return false;
                } else {
                    return true;
                }
            }
            protected boolean isFragmented() {
                return true;
            }
        };

        // create the Documents from the markup
        StyledDOMTester tester = new StyledDOMTester();
        Document dom = tester.parse(getClass().getResourceAsStream(
                "testInsertXFormEmulationElement.xml"));

        // Create test objects.
        DOMFactory domFactory = DOMFactory.getDefaultInstance();
        Element emulatedXFormElement =
                domFactory.createElement("EMULATED_XFORM_ELEMENT");
        Element vFormElement = domFactory.createElement("VFORM_ELEMENT");
        XFFormAttributes attributes = new XFFormAttributes();
        Element firstControl = selectNamedElement(dom, "FIRST_CONTROL");
        Element lastControl = selectNamedElement(dom, "SECOND_CONTROL");
        Element div = selectNamedElement(dom, "div");

        // Create Mocks.
        ProtocolSupportFactoryMock psf =
                new ProtocolSupportFactoryMock("psf", expectations);
        ProtocolConfigurationMock configuration =
                new ProtocolConfigurationMock("configuration",
                        expectations);
        EmulatedXFormDescriptorMock fd =
                new EmulatedXFormDescriptorMock("fd", expectations);
        OutputBufferFactoryMock bufferFactory = new OutputBufferFactoryMock(
                "bufferFactory", expectations);
        DOMOutputBufferMock buffer =
                new DOMOutputBufferMock("buffer", expectations);

        // These expectations must be set before the DOMProtocolMock is created.
        psf.expects.getDOMFactory().returns(domFactory);
        configuration.expects.getValidationHelper().returns(null).any();
        DOMProtocolMock protocol = new DOMProtocolMock("protocol",
                expectations, psf, configuration);

        // Set expectations.
        protocol.expects.createXFormEmulationElement(FORM_NAME, fd).
                returns(emulatedXFormElement);
        fd.expects.getFormSpecifier().returns(FORM_SPECIFIER);
        protocol.expects.createVFormElement(FORM_SPECIFIER).returns(vFormElement);
        // Should have identified that the div is the first valid parent of the
        // form controls.
        protocol.fuzzy.validateFormLinkParent("div", div,
                new Boolean(true)).returns(div);
        protocol.fuzzy.validateFormLinkParent("div", div,
                new Boolean(false)).returns(div);
        protocol.expects.getOutputBufferFactory().returns(bufferFactory).fixed(2);
        bufferFactory.expects.createOutputBuffer().returns(buffer).fixed(2);
        fd.expects.getFormAttributes().returns(attributes).fixed(2);
        protocol.expects.doTopFragmentLinks(buffer, attributes);
        buffer.expects.getRoot().returns(div).fixed(2);
        protocol.expects.doBottomFragmentLinks(buffer, attributes);

        // Configure the emulator, and run the tests.
        emulator.firstControl = firstControl;
        emulator.lastControl = lastControl;
        emulator.protocol = protocol;
        emulator.currentFormName = FORM_NAME;
        emulator.fd = fd;

        assertFalse(emulator.completedForms.containsKey(FORM_NAME));
        emulator.insertXFormEmulationElement();
        assertTrue(emulator.completedForms.containsKey(FORM_NAME));
        assertEquals(emulatedXFormElement,
                emulator.completedForms.get(FORM_NAME));
    }

    public void testInsertXFormEmulationElementWithValidCommonParent(){
        // Note - b will be found as valid!
        XFormEmulator emulator = new XFormEmulator() {
            protected boolean isValidFormParent(Element candidateParent) {
                return true;
            }
            protected boolean isFragmented() {
                return true;
            }
        };

        // create the Documents from the markup
        StyledDOMTester tester = new StyledDOMTester();
        Document dom = tester.parse(getClass().getResourceAsStream(
                "testInsertXFormEmulationElement.xml"));

        // Create test objects.
        DOMFactory domFactory = DOMFactory.getDefaultInstance();
        Element emulatedXFormElement =
                domFactory.createElement("EMULATED_XFORM_ELEMENT");
        Element vFormElement = domFactory.createElement("VFORM_ELEMENT");
        XFFormAttributes attributes = new XFFormAttributes();
        Element firstControl = selectNamedElement(dom, "FIRST_CONTROL");
        Element lastControl = selectNamedElement(dom, "SECOND_CONTROL");
        Element b = selectNamedElement(dom, "b");

        // Create Mocks.
        ProtocolSupportFactoryMock psf =
                new ProtocolSupportFactoryMock("psf", expectations);
        ProtocolConfigurationMock configuration =
                new ProtocolConfigurationMock("configuration",
                        expectations);
        EmulatedXFormDescriptorMock fd =
                new EmulatedXFormDescriptorMock("fd", expectations);
        OutputBufferFactoryMock bufferFactory = new OutputBufferFactoryMock(
                "bufferFactory", expectations);
        DOMOutputBufferMock buffer =
                new DOMOutputBufferMock("buffer", expectations);

        // These expectations must be set before the DOMProtocolMock is created.
        psf.expects.getDOMFactory().returns(domFactory);
        configuration.expects.getValidationHelper().returns(null).any();
        DOMProtocolMock protocol = new DOMProtocolMock("protocol",
                expectations, psf, configuration);

        // Set expectations.
        protocol.expects.createXFormEmulationElement(FORM_NAME, fd).
                returns(emulatedXFormElement);
        fd.expects.getFormSpecifier().returns(FORM_SPECIFIER);
        protocol.expects.createVFormElement(FORM_SPECIFIER).returns(vFormElement);
        // Should have identified that the b is the first valid parent of the
        // form controls.
        protocol.fuzzy.validateFormLinkParent("b", emulatedXFormElement, new Boolean(true)).returns(b);
        protocol.fuzzy.validateFormLinkParent("b", emulatedXFormElement, new Boolean(false)).returns(b);
        protocol.expects.getOutputBufferFactory().returns(bufferFactory).fixed(2);
        bufferFactory.expects.createOutputBuffer().returns(buffer).fixed(2);
        fd.expects.getFormAttributes().returns(attributes).fixed(2);
        protocol.expects.doTopFragmentLinks(buffer, attributes);
        buffer.expects.getRoot().returns(b).fixed(2);
        protocol.expects.doBottomFragmentLinks(buffer, attributes);

        // Configure the emulator, and run the tests.
        emulator.firstControl = firstControl;
        emulator.lastControl = lastControl;
        emulator.protocol = protocol;
        emulator.currentFormName = FORM_NAME;
        emulator.fd = fd;

        assertFalse(emulator.completedForms.containsKey(FORM_NAME));
        emulator.insertXFormEmulationElement();
        assertTrue(emulator.completedForms.containsKey(FORM_NAME));
        assertEquals(emulatedXFormElement,
                emulator.completedForms.get(FORM_NAME));
    }

    public void testSettingDisplayValueForAllInlineFormChildren() throws Exception {

        XFormEmulator emulator = new XFormEmulator();
        StyledDOMTester tester = new StyledDOMTester();
        prepareEmulator(emulator, tester, "testSettingDisplayValueForAllInlineFormChildren.xml", true);
        Document dom = emulator.protocol.getDocument();

        Element form_before = selectNamedElement(dom, "form");
        emulator.setXFFormDisplayValue(form_before);
        Element form_after = selectNamedElement(dom, "form");
        //all children are inline, so form should be set inline too (what is not default)
        assertEquals(DisplayKeywords.INLINE,
                     form_after.getStyles().getPropertyValues().
                             getComputedValue(StylePropertyDetails.DISPLAY));
    }

    public void testSettingDisplayValueForTableInForm() {
        XFormEmulator emulator = new XFormEmulator();
        StyledDOMTester tester = new StyledDOMTester();
        prepareEmulator(emulator, tester, "testSettingDisplayValueForAllBlockFormChildren.xml", true);
        Document dom = emulator.protocol.getDocument();

        Element form_before = selectNamedElement(dom, "form");
        emulator.setXFFormDisplayValue(form_before);
        Element form_after = selectNamedElement(dom, "form");
        // child is table, which is not inline, so form should be block
        // it is default, so styles are not set
        assertNull(form_after.getStyles());
    }

    public void testSettingDisplayValueForAllBlockFormChildren() {
        XFormEmulator emulator = new XFormEmulator();
        StyledDOMTester tester = new StyledDOMTester();
        prepareEmulator(emulator, tester, "testSettingDisplayValueForAllBlockFormChildren.xml", true);
        Document dom = emulator.protocol.getDocument();

        Element form_before = selectNamedElement(dom, "form");        
        emulator.setXFFormDisplayValue(form_before);
        Element form_after = selectNamedElement(dom, "form");
        // all children are blocks (<div>s), so form should be block
        // it is default, so styles are not set
        assertNull(form_after.getStyles());
    }

    public void testSettingDisplayValueForMixedFormChildren1() {
        XFormEmulator emulator = new XFormEmulator();
        StyledDOMTester tester = new StyledDOMTester();
        prepareEmulator(emulator, tester, "testSettingDisplayValueForMixedFormChildren1.xml", true);
        Document dom = emulator.protocol.getDocument();

        Element form_before = selectNamedElement(dom, "form");
        emulator.setXFFormDisplayValue(form_before);
        Element form_after = selectNamedElement(dom, "form");
        // some of the children are block, and the others are inline
        // form should be block...
        assertNull(form_after.getStyles());
        // and neighbour inline elements should be moved into form
        assertEquals(form_after, selectElementById(dom,"img1").getParent());
    }

    public void testSettingDisplayValueForMixedFormChildren2() {
         XFormEmulator emulator = new XFormEmulator();
        StyledDOMTester tester = new StyledDOMTester();
        prepareEmulator(emulator, tester, "testSettingDisplayValueForMixedFormChildren2.xml", true);
        Document dom = emulator.protocol.getDocument();

        Element form_before = selectNamedElement(dom, "form");
        emulator.setXFFormDisplayValue(form_before);
        Element form_after = selectNamedElement(dom, "form");

        // some of the children are block, and the others are inline
        // form should be block...
        assertNull(form_after.getStyles());
        // and neighbour inline elements should be moved into form
        assertEquals(form_after, selectElementById(dom,"img1").getParent());
        assertEquals(form_after, selectElementById(dom,"img2").getParent());
    }

    public void testSettingDisplayValueForOverlappingFormParts() throws Exception {
        XFormEmulator emulator = new XFormEmulator();
        StyledDOMTester tester = new StyledDOMTester();
        prepareEmulator(emulator, tester, "testSettingDisplayValueForOverlappingFormParts.xml", false);
        Document dom = emulator.protocol.getDocument();
        Element form_before = selectElementById(dom, "Form2");

        // there are neighbouring inline elements belonging to two different forms
        // and one of them is mixed
        // we don't support such things...
        try {
            emulator.setXFFormDisplayValue(form_before);
            fail("Exception should have been thrown before");
        } catch(ExtendedRuntimeException ex) {
            //it should have been thrown, all is correct
        }
    }


    private void prepareEmulator(XFormEmulator emulator, StyledDOMTester tester, String inputFilePath, boolean nullify) {
        // Create test objects.
        Document dom = tester.parseFull(getClass().getResourceAsStream(
                inputFilePath));

        DOMFactory domFactory = DOMFactory.getDefaultInstance();

        // Create Mocks.
        ProtocolSupportFactoryMock psf =
                new ProtocolSupportFactoryMock("psf", expectations);
        ProtocolConfigurationMock configuration =
                new ProtocolConfigurationMock("configuration",
                        expectations);

        // These expectations must be set before the DOMProtocolMock is created.
        psf.expects.getDOMFactory().returns(domFactory);
        configuration.expects.getValidationHelper().returns(null).any();
        DOMProtocolMock protocol = new DOMProtocolMock("protocol",
                expectations, psf, configuration);
        protocol.expects.getDocument().returns(dom).any();
        protocol.expects.getMarinerPageContext().returns(new MarinerPageContext()).any();

        //parsed Form Element differs from the one created by Emulator, so we need to nullify its styles
        if (nullify) {selectNamedElement(dom, "form").setStyles(null);}

        emulator.protocol = protocol;

    }

    /**
     * Selects the first element with the specified name from the supplied
     * Document.
     *
     * @param dom   from which to retrieve the first instance of the named
     *              element
     * @return Element or null if none exists with that name
     */
    protected Element selectNamedElement(Document dom, final String name) {

        final ElementSelectingVisitor visitor = new ElementSelectingVisitor() {

            // Javadoc inherited.
            public void visit(Element element) {
                if (foundElement == null && name.equals(element.getName())) {
                    this.foundElement = element;
                }
            }
        };

        final DOMWalker walker = new DOMWalker(visitor);
        walker.walk(dom);

        return visitor.foundElement;
    }


    /**
     * Selects the first element with the specified id from the supplied
     * Document.
     *
     * @param dom   from which to retrieve the first instance of the element
     * @return Element or null if none exists with that id
     */
    protected Element selectElementById(Document dom, final String id) {

        final ElementSelectingVisitor visitor = new ElementSelectingVisitor() {

            // Javadoc inherited.
            public void visit(Element element) {
                if (foundElement == null && id.equals(element.getAttributeValue("id"))) {
                    this.foundElement = element;
                }
            }
        };

        final DOMWalker walker = new DOMWalker(visitor);
        walker.walk(dom);

        return visitor.foundElement;
    }

    /**
     * Helper class to select an element from a DOM. It could also be done
     * using getHead/getTail, but that isn't as clear.
     */
    public static class ElementSelectingVisitor extends WalkingDOMVisitorStub {

        public Element foundElement;

    }
}

