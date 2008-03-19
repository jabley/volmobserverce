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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.Option;
import com.volantis.mcs.protocols.OptionVisitor;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.SelectOptionGroup;
import com.volantis.mcs.protocols.SelectionRenderer;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.assets.TextAssetReference;

import java.util.List;

/**
 * Provide a WMLDefaultSelectionRenderer.
 * <p/>
 * NOTE: there is no rendering of stylistic emulation markup here. This is
 * because select/option can only contain PCDATA, so even if we rendered it
 * it would be invalid and (almost always) removed by the transformer. The
 * only time it would not be removed would be if it was intercepted by the
 * "emulate emphasis" processing in the transformer and translated into just
 * text before and after (eg "[" & "]") - and it's not worth implementing just
 * for this edge condition.
 */
class WMLDefaultSelectionRenderer
        implements SelectionRenderer {

    private final RenderingVisitor visitor;
    protected XFSelectAttributes attributes;
    private final WMLRoot protocol;


    /**
     * Constructor that takes a WMLRoot protocol that it can then use to
     * delegate method calls to.
     *
     * @param protocol the <code>WMLRoot</code> protocol
     */
    public WMLDefaultSelectionRenderer(WMLRoot protocol) {
        this.protocol = protocol;
        this.visitor = new RenderingVisitor();
    }

    // javadoc inherited
    public void renderSelection(
            XFSelectAttributes attributes,
            OutputBuffer buffer)
            throws ProtocolException {

        this.attributes = attributes;
        DOMOutputBuffer dom = (DOMOutputBuffer) buffer;
        
        // Only render if we have options
        if (!this.attributes.getOptions().isEmpty()) {
            openSelect(dom);
            renderOptions(dom);
            closeSelect(dom);
        }
    }

    /**
     * Open the select element.
     *
     * @param dom the dom output buffer.
     * @throws ProtocolException if a ProtocolException occurs.
     */
    private void openSelect(DOMOutputBuffer dom)
            throws ProtocolException {

        Element element = dom.openStyledElement("select", attributes);

        // set any attribute values that should be applied for all form fields.
        protocol.addFormFieldAttributes(element, attributes);

        String tabindex = (String) attributes.getTabindex();
        if (protocol.supportsTabindex() && (tabindex != null)) {
            element.setAttribute("tabindex", tabindex);
        }

        // Initialise the title attribute from the prompt attribute, as
        // title is ALMOST one of the core attributes.
        // Need to fully qualify access to outer class due to bug in the
        // Sun 1.2 jdk compiler
        String title = getPlainTextFromReference(attributes.getPrompt());

        attributes.setTitle(title);

        protocol.addTitleAttribute(element, attributes, true);

        doSelectAttributes(element);

        String value =
                protocol.getInitialOptionHandler().getInitialValue(attributes);

        if (value != null) {
            element.setAttribute("ivalue", value);
        }
    }

    private String getPlainTextFromReference(TextAssetReference reference) {
        return (reference == null ? null : reference.getText(TextEncoding.PLAIN));
    }

    /**
     * Allow subclasses to alter the behaviour of the non-core select
     * attributes.
     *
     * @param element the element.
     */
    protected void doSelectAttributes(Element element) {
        element.setAttribute("name", attributes.getName());
        element.setAttribute("multiple", attributes.isMultiple()
                                         ? "true" : "false");
    }

    private void renderOptions(DOMOutputBuffer dom)
            throws ProtocolException {

        renderOptions(attributes.getOptions(), dom);
    }

    private void renderOptions(List options, DOMOutputBuffer dom)
            throws ProtocolException {

        Option option;
        for (int i = 0; i < options.size(); i++) {
            option = (Option) options.get(i);
            option.visit(visitor, dom);
        }
    }

    /**
     * Close the select element.
     *
     * @param dom the dom output buffer.
     */
    private void closeSelect(DOMOutputBuffer dom) {
        dom.closeElement("select");
    }

    /**
     * Provide a rendering vistor implementation.
     */
    protected class RenderingVisitor implements OptionVisitor {

        // javadoc inherited
        public void visit(
                SelectOption option,
                Object object) {

            DOMOutputBuffer dom = (DOMOutputBuffer) object;

            String value = option.getValue();
            // Get the caption from the object, if it is null then use the
            // value.
            // Need to fully qualify access to outer class due to bug in
            // in Sun 1.2 jdk compiler
            String caption = getPlainTextFromReference(option.getCaption());

            if (caption == null) {
                caption = value;
            }

            // Need to fully qualify access to outer class due to bug in
            // Sun 1.2 jdk compiler
            String title = getPlainTextFromReference(option.getPrompt());

            option.setTitle(title);

            // If this is a multiple select field then we need to make sure
            // that there are no ; in the values as ; is used as a separator.
            if (attributes.isMultiple()) {
                value = protocol.encodeMultipleSelectValue(value);
            }

            // delegate the work to the doOption() method
            protocol.doOption(dom, option, value, caption);
        }

        // javadoc inherited
        public void visit(
                SelectOptionGroup optionGroup,
                Object object) throws ProtocolException {

            DOMOutputBuffer dom = (DOMOutputBuffer) object;

            // open the option group
            Element element = dom.openStyledElement("optgroup", optionGroup);

            protocol.addOptGroupAttributes(element, attributes);

            // Need to fully qualify access to outer class due to bug in
            // the Sun 1.2 jdk compiler
            String title = getPlainTextFromReference(optionGroup.getCaption());

            if (title == null) {
                // Need to fully qualify access to outer class due to bug
                // Sun 1.2 jdk compiler
                title = getPlainTextFromReference(optionGroup.getPrompt());
            }
            if (title != null) {
                element.setAttribute("title", title);
            }

            // render any enclosed options
            renderOptions(optionGroup.getSelectOptionList(), dom);

            // close the option group
            dom.closeElement("optgroup");
        }
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Jul-04	4783/1	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 18-May-04	4461/1	ianw	VBM:2004051714 Only render select element if it contains options for wml

 10-Sep-03	1301/1	byron	VBM:2003082107 Support Openwave GUI Browser extensions

 ===========================================================================
*/
