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

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.NodeIteratee;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.dom.output.DocumentOutputter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ProtocolConstants;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.forms.AbstractForm;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.html.XHTMLBasic;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.shared.throwable.ExtendedRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.StringWriter;
import java.io.IOException;

/**
 * {@link com.volantis.mcs.dom.NodeIteratee} implementation which ensures that
 * all emulated XForm controls are included in a valid form element.
 *
 * <p>In summary, this transforms a DOM that has been preprocessed to indicate
 * which controls should appear in which form into a well-formed document
 * containing forms.</p>
 *
 * <p>When an XDIME-CP XForm is emulated, the individual form control elements
 * are not encapsulated in a form element of any sort. The emulation stage will
 * have added a marker attribute (containingXFFormName) to all emulated
 * controls. The value of this marker attribute is the name of the form in
 * which the control should appear.
 *
 * <p>The control elements (except implicit) will appear sequentially i.e. once
 * a control which should appear in a second form is encountered, no more
 * elements will reference the first form, and so on.</p>
 *
 * <p>The appropriate form element for the protocol being used should be
 * generated and populated with the action and method data stored in the
 * EmulatedFormDescriptor. Descriptors for all known forms will have been
 * registered with the page and session contexts in
 * XFormBuilder#registerFormDescriptors, and can be retrieved using the form
 * name.</p>
 *
 * <p>The form element should be inserted below the common valid ancestor of the
 * first and last controls to reference the form.  It should be inserted
 * immediately before the ancestor of the first control that has a common
 * sibling with the ancestor of the last control, and all the nodes in between
 * should be moved to be children of the form element.
 *
 *  * E.g.
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
 * </p>
 *
 * <p>An implicit element which contains the form specifier should also be
 * inserted as a child of the form element.</p>
 *
 * <p>Emulated implicit elements have to be handled slightly differently. They
 * will be generated for any XForm model items that are not referenced by
 * controls. However it is not possible to do this until all form controls have
 * definitely been processed (once we leave the body element). Therefore
 * emulated implicit elements will appear after the rest of the controls have
 * been processed, and not necessarily sequentially. They should be moved to
 * appear in the appropriate form.</p>
 *
 */
public class XFormEmulator {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(
                    XFormEmulator.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    XFormEmulator.class);

    HashMap completedForms = new HashMap();
    String currentFormName = null;
    DOMProtocol protocol;
    Element firstControl = null;
    Element lastControl = null;
    EmulatedXFormDescriptor fd = null;

    /**
     * A list of all implicit elements gathered from the DOM by the
     * <code>implicitElementsExtractorNodeIteratee</code> iteratee.
     */
    List implicitElements = new ArrayList();
    
    /**
     * An iteratee used to extract all implicit elements from DOM and store them
     * in the <code>implicitElements</code> array.
     */
    private NodeIteratee implicitElementsExtractorNodeIteratee = 
        new ImplicitElementsExtractorNodeIteratee();

    /**
     * An iteratee performing actual document transformation.
     * Invoked after implicit elements have been extracted by
     * implicitElementsExtractorNodeIteratee.
     */
    private NodeIteratee transformingNodeIteratee = 
        new TransformingNodeIteratee();

    /**
     * Transforms specified document to emulate XForm elements.
     * 
     * @param protocol The protocol used.
     * @param document The document to transform
     * @return The transformed document.
     */
    public Document transform(DOMProtocol protocol, Document document) {

        this.protocol = protocol;

        // Extract all implicit emulation elements, and store the in the
        // implicitElements array.
        document.getContentRoot().forEachChild(implicitElementsExtractorNodeIteratee);
        
        // After all the implicit elements have been extracted,
        // process the document
        document.getContentRoot().forEachChild(transformingNodeIteratee);

        // form elements are inserted when the next form is encountered, so
        // call this to insert the last form emulation element
        insertXFormEmulationElement();

        return document;
    }

    protected String getFormName(Element element) {
        return element.getAttributeValue(
                ProtocolConstants.CONTAINING_XFFORM_ATTRIBUTE);
    }

    /**
     * Insert the xform emulation element at the common ancestor of the
     * first and last controls found in this form.
     * <p/>
     * No emulation element will be inserted if no valid common parent can be
     * identified.
     */
    protected void insertXFormEmulationElement() {

        if (currentFormName != null) {

            Element xfformElement = null;

            // Assume this means this xform is finished, so need to insert the
            // xfform emulation element as a child of the first valid common
            // parent of the first and last controls.
            Element lhs = firstControl;
            Element rhs = lastControl;

            {
                // determine the elements' depth in the document
                int lhsDepth = getDepth(lhs);
                int rhsDepth = getDepth(rhs);
    
                // move up until they're at the same depth
                for(;lhsDepth > rhsDepth; --lhsDepth, lhs = lhs.getParent());
                for(;rhsDepth > lhsDepth; --rhsDepth, rhs = rhs.getParent());
            }

            // keep moving up together until there is a common parent
            while (lhs.getParent() != rhs.getParent()) {
                lhs = lhs.getParent();
                rhs = rhs.getParent();
            }

            // Ensure that the parent is a valid parent element for a form.
            Element formParent = lhs.getParent();
            while (formParent != null && !isValidFormParent(formParent)) {
                formParent = lhs = rhs = formParent.getParent();
            }

            if (formParent == null) {
                // If there is no valid parent for a form element, we cannot
                // insert one, and the output markup may be invalid.
                LOGGER.error("xforms-no-valid-parent", currentFormName);
            } else {
                // now create the form element
                if (fd != null) {
                    
                    final String formParentName = formParent.getName();

                    // initially we assume fragment links should be inserted 
                    // into form parent...
                    Element fragmentLinksParent = formParent;

                    xfformElement = protocol.createXFormEmulationElement(
                            currentFormName, fd);

                    // some protocols don't need to emulate the xfform element
                    // with a tag e.g. WMLRoot
                    if (xfformElement != null) {

                        // ... assumption is correct if form parent will be wrapped
                        // in newly created form element, otherwise we must ensure 
                        // fragment links are created inside form element
                        if (lhs != formParent) {
                            fragmentLinksParent = xfformElement;
                        }

                        // now actually insert the form element!
                        xfformElement.insertBefore(lhs);
                        for(Node n = lhs; n != rhs;
                                n = xfformElement.getNext()) {
                            n.remove();
                            xfformElement.addTail(n);
                        }
                        rhs.remove();
                        xfformElement.addTail(rhs);

                        Element vformElement = protocol.createVFormElement(
                                fd.getFormSpecifier());
                        if (vformElement != null) {
                            xfformElement.addHead(vformElement);
                        }
                        // move the cached implicit elements into the form.
                        moveImplicitElements(currentFormName, xfformElement);

                        setXFFormDisplayValue(xfformElement);
                    }

                    // Check if the form parent can contain form links, and if not
                    // add in the necessary elements (under the form parent) to
                    // enable it.
                    if (isFragmented()) {
                        final Element topLinkParent = protocol
                                .validateFormLinkParent(formParentName,
                                                        fragmentLinksParent,
                                                        true);
                        final Element bottomLinkParent = protocol
                                .validateFormLinkParent(formParentName,
                                                        fragmentLinksParent,
                                                        false);

                        // Insert the fragment links.
                        insertTopFragmentLinks(topLinkParent);
                        insertBottomFragmentLinks(bottomLinkParent);
                    }
                } else {
                    // this should never happen.
                    LOGGER.error("xforms-missing-descriptor",
                            currentFormName);
                }
            }

            // Add the form to the list of those completed (regardless of
            // whether an emulation element was actually inserted) because
            // no more controls should appear in that form.
            completedForms.put(currentFormName, xfformElement);

            // reset the form state
            currentFormName = null;
            firstControl = null;
            lastControl = null;
            fd = null;
        }
    }

    /**
     * Check if form is fragmeted
     *
     * @return true if form is fragmented
     */
    protected boolean isFragmented() {
        AbstractForm formData = fd.getFormAttributes() != null ?
                fd.getFormAttributes().getFormData() : null;
        return formData != null && formData.isFragmented();
    }

    /**
     * Determine how many elements deep in the document the element is.
     * E.g. in the structure <a0><a1><a2><a3/></a2></a1><a0>, getDepth(a0)
     * would return 0, getDepth(a1) would return 1 and so on.
     *
     * @param element whose depth to find
     * @return int depth in the document of the supplied element
     */
    protected int getDepth(Element element) {
        int result = 0;
        for (Element temp = element; temp.getParent() != null;
                temp = temp.getParent()) {
            result++;
        }
        return result;
    }

    /**
     * Determine if the candidate parent element is a valid parent for a
     * form. 
     *
     * @param candidateParent   element to be checked to determine if it is
     *                          a valid parent for a form
     * @return true if the element is a valid parent for a form
     */
    protected boolean isValidFormParent(Element candidateParent) {

        boolean result = false;

        if (candidateParent != null) {
            // If this element's name is null then effectively it is not a
            // valid form parent (should check it's parent's name instead).
            final String name = candidateParent.getName();
            if (name != null) {
                if (protocol instanceof XHTMLBasic) {
                    // only XHTML has a restriction
                    result = ((XHTMLBasic) protocol).isValidFormParent(name);
                } else {
                    result = true;
                }
            }
        } else {
            // this shouldn't happen as it means that they were not in the
            // document
            // @todo decide what should happen here
            LOGGER.error(EXCEPTION_LOCALIZER.format("xforms-processing-error"));
        }        
        return result;
    }

    /**
     * Emulated implicit elements are generated at the end of the body
     * element, when we know that they definitely are not referenced by
     * any of the controls. They are all inserted in the form preamble buffer
     * and will therefore they will appear out of sync with the rest of the
     * form controls and should be moved inside the emulated form element.
     * They are cached when encountered, and moved inside the form when the
     * form element is being inserted.
     *
     * @param formName  name of the form whose implicit element to move
     * @param form      element to whom the implicit elements should be added
     */
    protected void moveImplicitElements(String formName, Element form) {
        for (int i = 0; i < implicitElements.size(); i++) {
            final Element implicit = (Element) implicitElements.get(i);
            final String containingFormName = getFormName(implicit);
            if (formName.equals(containingFormName)) {
                // remove the marker attribute
                implicit.removeAttribute(
                        ProtocolConstants.CONTAINING_XFFORM_ATTRIBUTE);
                form.addHead(implicit);
                implicitElements.remove(i);
                i--;
            }
        }
    }

    protected void insertTopFragmentLinks(Element parent) {

        DOMOutputBuffer dom = (DOMOutputBuffer) protocol.
                getOutputBufferFactory().createOutputBuffer();
        try {
            protocol.doTopFragmentLinks(dom, fd.getFormAttributes());
        } catch (ProtocolException e) {
            // Do nothing deliberately.
        }
        dom.getRoot().addChildrenToHead(parent);
    }

    protected void insertBottomFragmentLinks(Element parent) {

        DOMOutputBuffer dom = (DOMOutputBuffer) protocol.
                getOutputBufferFactory().createOutputBuffer();
        try {
            protocol.doBottomFragmentLinks(dom, fd.getFormAttributes());
        } catch (ProtocolException e) {
            // Do nothing deliberately.
        }
        dom.getRoot().addChildrenToTail(parent);
    }

    /**
     *  This method determines display value of the form.
     *
     *  1. Form should be inline (display:inline) if all its children are inline.
     *  2. Form should be block (display:block) if none its children are inline
     *  3. If some of the children are inline and some are block, form should be block,
     *  and its borders should be expanded to not separate two inline elements.
     *  If as a result of this expansion an inline form becomes nested within this form
     *  then it is an error and the page should fail, just as if the initial placement failed.
     *
     * @param form XFForm element inserted into document
     */
    protected void setXFFormDisplayValue(Element form) {
        DisplayValueCheckingDOMVisitor dvcni = new DisplayValueCheckingDOMVisitor();
        Styles styles = null;

        form.forEachChild(dvcni);

        if (dvcni.isInline()) {
            styles = StylingFactory.getDefaultInstance()
                    .createInheritedStyles(
                            protocol.getMarinerPageContext().getStylingEngine()
                                    .getStyles(), DisplayKeywords.INLINE);
            form.setStyles(styles);
        } else if (dvcni.isMixed()) {
            includeNeighbourInlineElements(form);
        } // by default, form is block, so we just leave case when
          // all children are block (!isMixed && !isInline)
    }

    /**
     *
     * This is part of setting xfform display value functionality.
     * Expands xfform delimiters to include next and previous inline siblings.
     * Non-element Nodes (e.g. Text), are treated as inline elements,
     * and do not stop expansion.
     *
     * @param form
     */
    private void includeNeighbourInlineElements(Element form) {

        Node nextNode;

        for (Node node = form.getPrevious(); node != null; node = nextNode) {
            if (checkCorrectInlineNode(node, form)) {
                nextNode = node.getPrevious();
                node.remove();
                form.addHead(node);
            } else {
                break;
            }
        }

        for (Node node = form.getNext(); node != null; node = nextNode) {
            if (checkCorrectInlineNode(node, form)) {
                nextNode = node.getNext();
                node.remove();
                form.addTail(node);
            } else {
                break;
            }
        }
    }

    /**
     * Method created only to be used by {@link #includeNeighbourInlineElements}
     * Returns false if Node is Element, has styles set and display value is not INLINE, true otherwise.
     * Throws exception, if Element is inline <form>
     *
     * @param node
     * @return
     */
    private boolean checkCorrectInlineNode(Node node, Element form) {
        if (!(node instanceof Element)) return true;
        Element element = (Element)node;

        boolean isInline;
        Styles styles = element.getStyles();
        
        if (styles != null) {
          isInline = DisplayKeywords.INLINE == styles.getPropertyValues().
                getComputedValue(StylePropertyDetails.DISPLAY);
        } else {
          isInline = "form" != element.getName();
        }
        
        if (isInline && element.getName() == "form") {
            throw new XFormEmulatorException(
                        EXCEPTION_LOCALIZER.format("xforms-mixed-forms",
                                new String[]{element.getAttributeValue("id"),
                                             form.getAttributeValue("id")}));
        }
        return isInline;
    }


    /**
     *
     */
    private final class DisplayValueCheckingDOMVisitor extends RecursingDOMVisitor {

        private boolean mixed;
        private StyleValue display;

        public void visit(Element element) {
            StyleValue newDisplay;

            if (!mixed) {
                Styles styles  = element.getStyles();
                if (styles != null) {
                    newDisplay = styles.getPropertyValues().getComputedValue(StylePropertyDetails.DISPLAY);
                    if (displayValueChanged(newDisplay)) {
                        mixed = true;
                        skipRemainder();  
                    }
                    display = newDisplay;
                }
            }
        }

        public boolean isMixed() {
            return mixed;
        }

        public boolean isInline() {
            return !mixed && DisplayKeywords.INLINE == display;
        }

        /**
         *
         */
        private boolean displayValueChanged(StyleValue newDisplay) {
            return this.display != null &&
                    ((DisplayKeywords.INLINE == display && DisplayKeywords.INLINE != newDisplay ) ||
                    (DisplayKeywords.INLINE != display && DisplayKeywords.INLINE == newDisplay ));

        }

    }

    /**
     * Node iteratee, which extracts all implicit elements from given DOM tree,
     * storing them the in the <code>implicitElements</code> array.
     */
    private final class ImplicitElementsExtractorNodeIteratee
            extends RecursingDOMVisitor {


        public void visit(Element element) {

            // Get containing form name of the element.
            // This method will return null value for all non-form elements.
            String formName = getFormName(element);

            if (formName != null) {

                // We got a form element. Check, if it's an implicit one.
                if (protocol.isImplicitEmulationElement(element)) {

                    // Remove the implicit element from DOM...
                    element.remove();

                    // ...and cache it for further retrieval.
                    implicitElements.add(element);
                }
            } else {
                // iterate through its children
                element.forEachChild(this);
            }
        }
    }
    
    /**
     * Node iteratee performing actual DOM transformation.
     * Invoked after implicit elements are extracted by
     * ImplicitElementsExtractorNodeIteratee.
     */
    private final class TransformingNodeIteratee
            extends RecursingDOMVisitor {


        public void visit(Element element) {

            // if it's already in an xform emulation element then just
            // ignore it
            if (!protocol.isXFormEmulationElement(element)) {

                String formName = getFormName(element);

                if (formName != null) {
                    // remove the marker attribute
                    element.removeAttribute(
                            ProtocolConstants.CONTAINING_XFFORM_ATTRIBUTE);

                    // this has been tagged as an emulated XForm element
                    // and should be enclosed in a "form" tag

                    if (formName.equals(currentFormName)) {
                        // this the current last control in the form
                        lastControl = element;
                    } else {
                        // it's a new form - check it's not completed
                        if (completedForms.containsKey(formName)) {
                            // this should never happen but if it does
                            // then just log and continue
                            LOGGER.error(EXCEPTION_LOCALIZER.format(
                                    "xforms-mixed-forms-transformer",
                                    new String[]{formName, currentFormName}));
                        } else {
                            // insert the xform element for the
                            // completed form
                            insertXFormEmulationElement();

                            // this is the first control in the new form
                            currentFormName = formName;
                            firstControl = lastControl = element;
                            fd = protocol.getMarinerPageContext().
                                    getEmulatedXFormDescriptor(currentFormName);
                            if (fd == null) {
                                // this should never happen.
                                LOGGER.error("xforms-missing-descriptor",
                                        currentFormName);
                           }
                        }
                    }
                    // if this is an action element, then do any work
                    // required to fully populate it
                    protocol.populateEmulatedActionElement(element, fd);
                } else {
                    // iterate through its children
                    element.forEachChild(this);
                }
            }
        }
    }

    /**
     * Exception thrown when XFormEmulator cannot transform DOM correctly
     */
    final static class XFormEmulatorException extends ExtendedRuntimeException {
        public XFormEmulatorException(String s) {
            super(s);
        }

        public XFormEmulatorException(String s, Throwable throwable) {
            super(s,throwable);
        }

        public XFormEmulatorException(Throwable throwable) {
            super(throwable);
        }

        public XFormEmulatorException() {
            super();
        }
    }
}
