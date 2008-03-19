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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/DoSelectInputTestHelper.java,v 1.2 2003/01/28 03:58:00 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Jan-03    Doug            VBM:2003012408 - created. Helper class for
 *                              TestCases that are testing the doSelectInput()
 *                              method for a given protocol
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.NodeIteratee;
import com.volantis.mcs.dom.NodeSequence;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.forms.FormDescriptor;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.shared.iteration.IterationAction;
import junit.framework.Assert;

/**
 * Helper class for TestCases that are testing the doSelectInput()
 * method for a given protocol
 */
public class DoSelectInputTestHelper {

    private static final StylingFactory STYLING_FACTORY =
            StylingFactory.getDefaultInstance();

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * Run a test on the protocol doSelectInput() method
     * @param protocol the protocol being tested
     * @param buffer  the buffer
     * @param attributes the XFSelectAttributes
     * @param expected the expected output
     * @exception Exception if an error occurs
     */
    public void runTest(DOMProtocol protocol,
                        DOMOutputBuffer buffer,
                        XFSelectAttributes attributes,
                        String expected) throws Exception {

        buffer.initialise();

        protocol.doSelectInput(attributes);

        String result = bufferToString(buffer, protocol);

        Assert.assertEquals("Output did not match expected ",
                            expected, result);
    }

    /**
     * Convert a DOMOutputBuffer to a String
     * @param buffer the buffer to convert
     * @param protocol the protocol 
     * @return the buffer as a String
     */
    protected String bufferToString(DOMOutputBuffer buffer,
                                    DOMProtocol protocol) {

        StrictStyledDOMHelper helper = new StrictStyledDOMHelper(null);

        String result;
        final Document doc = domFactory.createDocument();
        NodeSequence contents = buffer.removeContents();
        contents.forEach(new NodeIteratee() {
            public IterationAction next(Node node) {
                doc.addNode(node);
                return IterationAction.CONTINUE;
            }
        });

        result = helper.render(doc);

        return result;
    }

    /**
     * Helper method to build a XFSelectAttributes object suitable
     * for testing with         N
     * @return a XFSelectAttributes instance
     */
    public XFSelectAttributes buildSelectAttributes() {
        XFSelectAttributes atts = new XFSelectAttributes();
        Styles styles = STYLING_FACTORY.createInheritedStyles(
                (Styles) null, DisplayKeywords.INLINE);
        atts.setStyles(styles);
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.TEXT_ALIGN,
                TextAlignKeywords._INTERNAL_DEFERRED_INHERIT);

        Form form = new Form(null) {
            public boolean isFragmented() {
                return false;
            }
        };
        FormInstance formInstance = new FormInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        formInstance.setFormat(form);

        XFFormAttributes formAtts = new XFFormAttributes();
        formAtts.setFormDescriptor(new FormDescriptor());

        atts.setFormData(formInstance);
        atts.setFormAttributes(formAtts);
        atts.setName("TestSelect");
        atts.setId("TestID");
        atts.setPrompt(new LiteralTextAssetReference("TestPrompt"));
        atts.setMultiple(false);
        PaneInstance entryPaneInstance = new PaneInstance(null);
        entryPaneInstance.setDeviceLayoutContext(new TestDeviceLayoutContext());
        atts.setEntryContainerInstance(entryPaneInstance);

        return atts;
    }

    /**
     * Add a new SelectOption as an option of a XFSelectAttributes instance
     * @param atts the XFSelectAttributes instance
     * @param caption the new SelectOptions caption
     * @param prompt the new SelectOptions prompt
     * @param value the new SelectOptions value
     * @param selected true if and only if the new SelectOption is selected
     * @return a the new SelectOption that was added to the XFSelectAttributes
     *        list of options
     */
    public SelectOption addOption(XFSelectAttributes atts, String caption,
                                  String prompt, String value,
                                  boolean selected) {
        SelectOption option = createOption(
                atts.getStyles(), caption, prompt, value, selected);
        option.setStyles(STYLING_FACTORY.createInheritedStyles(
                atts.getStyles(), DisplayKeywords.INLINE));
        atts.addOption(option);
        return option;
    }

    /**
     * Add a new SelectOption as an option to a given SelectOptionGroup 
     * instance
     * @param group the SelectOptionGroup instance
     * @param caption the new SelectOptions caption
     * @param prompt the new SelectOptions prompt
     * @param value the new SelectOptions value
     * @param selected true if and only if the new SelectOption is selected
     * @return a the new SelectOption that was added to the SelectOptionGroups
     *         list of options
     */
    public SelectOption addOption(SelectOptionGroup group, String caption,
                                  String prompt, String value, boolean selected) {
        SelectOption option = createOption(
                group.getStyles(), caption, prompt, value, selected);
        group.addSelectOption(option);
        return option;
    }

    /**
     * Add an new SelectOptionGroup to a XFSelectAttributes instance
     * @param atts the XFSelectAttributes instance
     * @param caption the new SelectOptionGroup caption
     * @param prompt the new SelectOptionGroup prompt
     * @return a the new SelectOptionGroup that was added to the 
     *         XFSelectAttributes instance
     */
    public SelectOptionGroup addOptionGroup(XFSelectAttributes atts,
                                            String caption,
                                            String prompt) {
        SelectOptionGroup group = createOptionGroup(
                atts.getStyles(), caption, prompt);
        atts.addOptionGroup(group);
        return group;
    }

    /**
     * Add an new SelectOptionGroup to an existing SelectOptionGroup instance
     * @param group the existing SelectOptionGroup instance
     * @param caption the new SelectOptionGroup caption
     * @param prompt the new SelectOptionGroup prompt
     * @return a the new SelectOptionGroup that was added to the 
     *         existing SelectOptionGroup instance
     */
    public SelectOptionGroup addOptionGroup(SelectOptionGroup group,
                                            String caption,
                                            String prompt) {
        SelectOptionGroup newGroup = createOptionGroup(
                group.getStyles(), caption, prompt);
        group.addSelectOptionGroup(newGroup);
        return newGroup;
    }

    /**
     * Factory method for creating a SelectOption instance
     * @param parentStyles
     * @param caption the Caption
     * @param prompt the Prompt
     * @param value the value
     * @param selected true if and only if the new option is to be selected
     * @return a SelectOption instance.
     */
    protected SelectOption createOption(
            Styles parentStyles, String caption, String prompt,
            String value, boolean selected) {
        SelectOption option = new SelectOption();
        option.setStyles(STYLING_FACTORY.createInheritedStyles(
                parentStyles, DisplayKeywords.INLINE));
        option.setCaption(new LiteralTextAssetReference(caption));
        option.setId("OptionId");
        option.setPrompt(new LiteralTextAssetReference(prompt));
        option.setValue(value);
        option.setSelected(selected);
        return option;
    }

    /**
     * Factory method for creating a SelectOptionGroup instance
     * @param parentStyles
     * @param caption the caption
     * @param prompt the prompt
     * @return the new SelectOptionGroup instance
     */
    protected SelectOptionGroup createOptionGroup(
            Styles parentStyles, String caption,
            String prompt) {
        SelectOptionGroup optionGroup = new SelectOptionGroup();
        optionGroup.setStyles(STYLING_FACTORY.createInheritedStyles(
                parentStyles, DisplayKeywords.INLINE));
        optionGroup.setCaption(new LiteralTextAssetReference(caption));
        optionGroup.setId("GroupId");
        optionGroup.setPrompt(new LiteralTextAssetReference(prompt));
        return optionGroup;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 ===========================================================================
*/
