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

package com.volantis.mcs.xdime;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.XDIMEException;

/**
 * Concrete implementation of {@link XDIMEElement} which handles expression
 * evaluation and push/popping the element onto the stack that the context
 * maintains.
 */
public abstract class XDIMEElementImpl implements XDIMEElementInternal {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(XDIMEElement.class);

    /**
     * The type of element that this class represents.
     */
    protected final ElementType elementType;

    /**
     * The context within which this element is used.
     */
    protected final XDIMEContextInternal context;

    /**
     * This flag is set to true in the elementStart method if the body of this
     * element is to be skipped. It is used to tell the elementEnd method if it
     * needs to do anything.
     */
    protected boolean skipped = false;

    /**
     * Responsible for constructing the {@link ElementOutputState} which
     * describes this element's output state.
     */
    protected ElementOutputStateBuilder outputStateBuilder;

    /**
     * The parent element.
     */
    protected final XDIMEElementInternal parent;

    /**
     * The strategy used to do element validation.
     */
    private final ValidationStrategy validationStrategy;

    /**
     * Initialise.
     *
     * @param type               The element type.
     * @param context            The context within which this element is used.
     * @param validationStrategy The strategy to use for validating this
     *                           element.
     */
    protected XDIMEElementImpl(
            ElementType type, XDIMEContextInternal context,
            ValidationStrategy validationStrategy) {
        this.elementType = type;
        this.context = context;
        this.validationStrategy = validationStrategy;

        // The parent element is the current element within the context as this
        // element has not yet been created and hence cannot yet have been
        // pushed.
        parent = context.getCurrentElement();
    }

    // Javadoc inherited.
    protected String getNamespace() {
        return elementType.getNamespaceURI();
    }

    public String getTagName() {
        return elementType.getLocalName();
    }

    /**
     * Returns true if specified element is atomic.
     * Element is atomic, if its children cannot exist without this element. Thus if this
     * element is removed, its children should be removed as well.
     * Examples of atomic elements are: xf:form, xf:submit, table, ul.
     *
     * @return true if element is atomic
     */
    public boolean isElementAtomic() {
        return false;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public XDIMEElementInternal getParent() {
        return parent;
    }

    // Javadoc inherited.
    public final XDIMEResult elementStart(XDIMEContext context,
            XDIMEAttributes attributes) throws XDIMEException {

        // should process the body by default
        XDIMEContextInternal contextInternal = (XDIMEContextInternal) context;
        XDIMEResult result;

        contextInternal.enteringXDIMECPElement();
       
        // perform validation
        validationStrategy.open(contextInternal, elementType);

        result = doElementStart(contextInternal, attributes);
        
        return result;
    }

    // Javadoc inherited.
    public final XDIMEResult elementEnd(XDIMEContext context)
            throws XDIMEException {

        XDIMEContextInternal contextInternal = (XDIMEContextInternal) context;
        XDIMEResult result = XDIMEResult.CONTINUE_PROCESSING;

        if (!skipped) {

            // Validate before actually finishing off the element as it
            // ensures that the contents are correct, i.e. all required
            // elements have been seen.
            validationStrategy.close(contextInternal, elementType);

            result = doElementEnd(contextInternal);
        }

        contextInternal.exitingXDIMECPElement();

        return result;
    }

    /**
     * Called at the start of processing an XDIME element. Subclasses should
     * consider this method the same as
     * #elementStart.
     *
     * @param context       The XDIMEContext within which this element is
     *                      being processed.
     * @param attributes    The implementation of XDIMEAttributes which
     *                      contains the attributes specific to the
     *                      element.
     * @return PROCESS_ELEMENT_BODY or SKIP_ELEMENT_BODY.
     * @throws XDIMEException If there was a problem processing the element.
     */
    public abstract XDIMEResult doElementStart(XDIMEContextInternal context,
          XDIMEAttributes attributes) throws XDIMEException;

    /**
     * Called at the end of processing an XDIME element.
     * <p>
     * If the doElementStart method was called then this method will also be
     * called unless an Exception occurred during the processing of the
     * body.
     * </p>
     * @param context       The XDIMEContext within which this element is
     *                      being processed.
     * @return CONTINUE_PROCESSING or ABORT_PROCESSING.
     * @throws XDIMEException If there was a problem processing the element.
     */
    public abstract XDIMEResult doElementEnd(XDIMEContextInternal context)
            throws XDIMEException;

    /**
     * Get the value of the requested attribute (with namespace="") from the
     * supplied XDIMEAttributes. May return null if the element has no value
     * set for that attribute.
     *
     * @param attribute     XDIMEAttribute to retrieve
     * @param attributes    from which to retrieve the attribute value
     * @return String value of the specified attribute. May be null if the
     * element has no value set for the attribute.
     */
    protected String getAttribute(XDIMEAttribute attribute,
            XDIMEAttributes attributes) {
        return getAttribute(attribute, attributes, "");
    }

    /**
     * Get the value of the requested attribute qualified with the given
     * namespace from the supplied XDIMEAttributes. May return null if the
     * element has no value set for that attribute.
     *
     * @param attribute     XDIMEAttribute to retrieve
     * @param attributes    from which to retrieve the attribute value
     * @param namespace     with which the attribute should be prefixed
     * @return String value of the specified attribute. May be null if the
     * element has no value set for the attribute.
     */
    protected String getAttribute(XDIMEAttribute attribute,
            XDIMEAttributes attributes, String namespace) {
        return attributes.getValue(namespace, attribute.toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/4	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 02-Oct-05	9637/4	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 22-Sep-05	9128/5	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 21-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 12-Sep-05	9415/3	emma	VBM:2005072710 Fixing element stack mistake

 09-Sep-05	9415/1	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 31-Aug-05	9391/2	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 25-Jul-05	9060/2	tom	VBM:2005071304 Interim Commit so Emma can see the changes we have made

 18-Jul-05	9021/1	ianw	VBM:2005071114 interim commit of XDIME API for DISelect integration

 ===========================================================================
*/
