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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/HDML_Version3TestCase.java,v 1.1 2003/04/10 12:53:24 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Apr-03    Phil W-S        VBM:2002111502 - Created. Tests phone number
 *                              attributes. Some tests have had to be stubbed
 *                              out to avoid additional refactoring effort
 *                              under this task (which probably would have
 *                              little value right now).
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SegmentGridAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.styling.StylesBuilder;

/**
 * Test the HDML_Version3 protocol.
 */
public class HDML_Version3TestCase extends HTMLRootTestAbstract {
    private HDML_Version3 protocol;
    private XHTMLBasicTestable testable;

    public HDML_Version3TestCase(String name) {
        super(name);
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestHDML_Version3Factory(),
                internalDevice);
        return protocol;
    }

    protected void setTestableProtocol(VolantisProtocol protocol,
                                       VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (HDML_Version3)protocol;
        this.testable = (XHTMLBasicTestable)testable;
    }

    /** Check we have the right protocol */
    public void testDefaultMimeType() {
        assertEquals("text/x-hdml", protocol.defaultMimeType());
    }

    /**
     * There should be absolutely no form output for this protocol
     */
    public void testDoForm() {
        XFFormAttributes attributes = new XFFormAttributes();
        protocol.doForm(attributes);
        // Should simply do nothing and come out.
        // @todo later add the actual testing in here!
    }

    public void testOpenPane() {
        // This is intentionally blank as panes are "not supported" by
        // HDML
    }

    public void testOpenPaneWithContainerCell() throws Exception {
        // This is intentionally blank as panes are "not supported" by
        // HDML
    }

    public void testClosePane() {
        // This is intentionally blank as panes are "not supported" by
        // HDML
    }

    /**
     * @todo later implement test as required by this protocol
     */
    public void testNoOptGroupsWithControlSelect() throws Exception {
        // div is not supported so the output is not as expected
    }

    /**
     * @todo later implement test as required by this protocol
     */
    public void testControlMultiSelect() throws Exception {
        // div is not supported so the output is not as expected
    }

    /**
     * @todo later implement test as required by this protocol
     */
    public void testControlMultiSelectOptionsSelected() throws Exception {
        // div is not supported so the output is not as expected
    }

    /**
     * @todo later implement test as required by this protocol
     */
    public void testControlSingleSelectOptionsSelected() throws Exception {
        // div is not supported so the output is not as expected
    }

    /**
     * @todo later implement test as required by this protocol
     */
    public void testControlSingleSelect() throws Exception {
        // div is not supported so the output is not as expected
    }

    /**
     * @todo later implement test as required by this protocol
     */
    public void testShardLinkStyleClass() throws Exception {
    }

    // Javadoc inherited.
    protected String getExpectedHorizontalRuleMarkup() {
        return "<hr align=\"left\" noshade=\"true\" size=\"5\" width=\"100\"/>";
    }

    /**
     * Test the HTML generated for action input tags. Because HDML does not
     * support forms no output should be produced for this protocol.
     *
     * @throws Exception if an error occurs
     */
    public void testActionInput() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        XFActionAttributes attributes = new XFActionAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        // Setup test for action tag located outside form (type=perform)
        String shortcut = "1";
        String name = "My Name";
        String actionType = "perform";
        String caption = "This is my caption";

        attributes.setShortcut(new LiteralTextAssetReference(shortcut));
        attributes.setName(name);
        attributes.setType(actionType);
        attributes.setCaption(new LiteralTextAssetReference(caption));

        protocol.doActionInput(buffer, attributes);
        String actualResult = bufferToString(buffer);

        assertEquals("Ensure that nothing is returned",
                "", actualResult);
    }

    /**
     * override OpenSegmentGrid test as this protocol does not
     * support the implemented behaviour. Test that nothing is produced
     * for this protocol.
     */
    public void testOpenSegmentGrid() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        int[] rows = null;
        int[] cols = null;
        SegmentGridAttributes ga = new SegmentGridAttributes();
        // set to null
        ga.setColumnWidths(cols);
        ga.setRowHeights(rows);

        rows = new int[]{3, -100, 3};
        cols = new int[]{-1, 4, -1};
        ga.setRowHeights(rows);
        ga.setColumnWidths(cols);
        buffer.clear();
        protocol.openSegmentGrid(buffer, ga);
        assertEquals("Ensure that nothing is returned",
                "", bufferToString(buffer));
    }


    /**
     * This implementation explicitly by-passes the hierarchy because
     * the protocol specifically overrides rather than augments the
     * attribute adding.
     *
     * @param element the element to be checked
     * @throws Exception if an error occurs
     */
    protected void checkAddPhoneNumberAttributes(Element element)
        throws Exception {
        assertNoAttributes(element);
    }

    // javadoc inherited
    protected String expectedDefaultMimeType() {
        return "text/x-hdml";
    }

    public void testAnchorNameIdentification() throws ProtocolException {
        // override this as HDML has it's own openAnchor temporary which does not
        // use the main infrastructure - it adds neither id nor name.
    }

    // javadoc inherited.
    public void testCreateEnclosingElement() throws Exception {
        // NOTE: This protocol doesn't support style classes and we should never
        // have a class attribute in real life situations. However, this is
        // explicitly set in the super class' method during this test.
        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());

        // Div not supported in HDML
        doTestCreateEnclosingElement(attributes,
                                     "<td/>");
    }

    /**
     * Ensure that valid image tags are generated when there is no source.
     * This specialization is needed as HDML does not support the \<span\> tags
     * normally used to write the altText.
     * @throws ProtocolException
     */
    public void testImageNoSrc() throws ProtocolException {
        // Set up the protocol and dependent objects.
       // privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        String expected = "Alternate Text";

        // Initialise our protocol attributes with an id attribute.
        ImageAttributes attributes = new ImageAttributes();
        attributes.setLocalSrc(true);
        attributes.setSrc(null);
        attributes.setAltText(expected);

        protocol.doImage(buffer, attributes);

        // No src with alt text generates text as there is currently no support
        // for <span> in HDML.
        Element root = buffer.getCurrentElement();
        Text altText = (Text) root.getHead();

        String txtAltText = new String(altText.getContents(),
                                       0, expected.length());
        assertEquals("Incorrect Text", expected, txtAltText);
    }

    protected void checkRenderAltText(String altText, DOMOutputBuffer buffer) {
        // We changed the default for (X)HTML to render a logical SPAN to
        // avoid adding whitespace around the text. I believe SPAN might be
        // better as the default, but this would be a larger change.
        Element root = buffer.getCurrentElement();
        Text text = (Text) root.getHead();
        checkTextEquals(altText, text);
    }

    // javadoc inherited
    protected String getExpectedXFSelectString() {
        return
                "<testRoot>" +
                    "<input id=\"OptionId\" name=\"TestSelect\" " +
                            "title=\"Prompt1\" type=\"radio\" value=\"Value1\"/>" +
                    "Caption1" +
                    "<br/>" +
                    "<input id=\"OptionId\" name=\"TestSelect\" " +
                            "title=\"Prompt2\" type=\"radio\" value=\"Value2\"/>" +
                    "Caption2" +
                "</testRoot>";
    }

    // javadoc inherited
    protected void checkResultForPre(final DOMOutputBuffer buffer) {
        final Text text = (Text) buffer.getRoot().getHead();
        assertEquals("     before          child     text          after     ",
            new String(text.getContents(), 0, text.getLength()));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 25-Aug-05	9370/1	gkoch	VBM:2005070507 xform select option to store caption styles instead of caption (style) class

 22-Aug-05	9363/5	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/2	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/3	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/3	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 04-Nov-04	5871/3	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - rework issues

 03-Nov-04	5871/1	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 08-Jul-04	4835/1	adrianj	VBM:2003040704 HDML protocol modified to ignore xfaction inputs outside forms

 17-May-04	4029/6	steve	VBM:2004042003 Supermerged again - Hurry up Mat :)

 17-May-04	4029/4	steve	VBM:2004042003 Supermerged again - Hurry up Mat :)

 26-Apr-04	4029/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 23-Apr-04	4020/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 05-May-04	4157/3	matthew	VBM:2003030319 added test implementation to HDML_Version3TestCase.testOpenSegmentGrid, changed signature of XHTMLTransitional.addRowColAttributes

 05-May-04	4157/1	matthew	VBM:2003030319 change the way default values of rowHeight and columnHeight attributes are handled (insert a bin bogus-hypersonic-db.properties bogus-hypersonic-db.script build build-ab.xml build-admin.xml build-charset.xml build-clean_war.xml build-cli.xml build-code-generators.xml build-common.xml build-controls.xml build-core.xml build-deploy.xml build-docs.xml build-dynamo.xml build-eclipse-common.xml build-eclipse-updateclient.xml build-eclipse.xml build-examples.xml build-external-plugins.xml build-i18n.xml build-librarian-generator.xml build-librarian.xml build-migrate30.xml build-properties.xml build-release.xml build-samples.xml build-servlet.xml build-targets.xml build-testsuite.xml build-tests.xml build-testtools.xml build-tomcat.xml build-tt_gui.xml build-tt_war.xml build-uaprof.xml build-ucp.xml build-update-client-cli.xml build-update-deploy.xml build-update.xml build-validation.xml build-version.properties build-vignette.xml build-weblogic.xml build-websphere.xml build.xml client.cer client.keystore com db doc hypersonic-db.data hypersonic-db.properties hypersonic-db.script jar javadoc key librarian-lookup-table.xml librarian.xml mcs.ipr mcs.iws product.key product.lkd redist report.txt Test testdata tests TESTS-TestSuites.xml testsuite volantis webapp rather then -1)

 15-Mar-04	3422/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 12-Mar-04	3403/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 26-Aug-03	1015/1	geoff	VBM:2003072208 Style Class on spatial iterated pane not set on table cell in generated markup (supermerge fixes)

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 07-Jul-03	728/1	adrian	VBM:2003052001 fixed pane attribute generation

 ===========================================================================
*/
