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
package com.volantis.mcs.xdime.diselect;

import com.volantis.mcs.expression.SelectState;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.util.ListIterator;
import java.util.Stack;

/**
 * Abstract class which is the superclass of all elements which can go inside
 * the body of a &lt;select&gt; element. See {@link SelectElementImpl}.
 */
public abstract class XDIMESelectBodyElement extends DISelectElement {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    XDIMESelectBodyElement.class);

    protected XDIMESelectBodyElement(
            ElementType type,
            AttributeUsage exprUsage,
            XDIMEContextInternal context) {
        super(type, context, exprUsage);
    }

    // javadoc inherited
    public XDIMEResult exprElementStart(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {
        // We know that both the select and child element (either otherwise or
        // when) expressions must have evaluated to true if we've reached this
        // point, so the element body should be evaluated.
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // javadoc inherited.
    public XDIMEResult exprElementEnd(XDIMEContextInternal context) {
        return XDIMEResult.CONTINUE_PROCESSING;
    }

    /**
     * Helper method which obtains the &lt;select&gt; element from the context.
     *
     * @param context the context element
     * @return the &lt;select&gt; element closest to the top of the element stack
     * @throws XDIMEException if the element at the top of the stack is not an
     * instance of {@link XDIMESelectBodyElement} or the next non
     * {@link XDIMESelectBodyElement} above that is not an instance of
     * {@link SelectElementImpl}
     *
     */
    private SelectElementImpl getSelectElement(XDIMEContextInternal context)
            throws XDIMEException {

        Stack elementStack = ((XDIMEContextImpl)context).getStack();
        ListIterator elementIterator =
                elementStack.listIterator(elementStack.size());

        Object selectElement = null;
        boolean foundSelectBody = false;

        while (selectElement == null && elementIterator.hasPrevious()) {
            Object currentElement = elementIterator.previous();

            if (foundSelectBody) {
                if (currentElement instanceof SelectElementImpl) {
                    selectElement = currentElement;
                } else if (currentElement instanceof XDIMESelectBodyElement) {
                    // do nothing, but continue
                } else {
                    if (!(currentElement instanceof XDIMESelectBodyElement)) {
                        throw new XDIMEException(
                            EXCEPTION_LOCALIZER.format("xdime-bad-stack"));
                    }
                }
            } else {
                if (currentElement instanceof XDIMESelectBodyElement) {
                    foundSelectBody = true;
                }
            }
        }

        if (selectElement == null) {
            throw new XDIMEException(
                    EXCEPTION_LOCALIZER.format("select-markup-missing"));
        }

        return (SelectElementImpl)selectElement;
    }

    /**
     * Helper method which obtains the {@link SelectState} from the
     * &lt;select&gt; element which encloses this &lt;when&gt; element.
     * @param context the context
     * @return the state of the &lt;select&gt; element.
     * @throws XDIMEException if there is a problem getting the state
     */
    protected SelectState getState(XDIMEContextInternal context)
            throws XDIMEException {
        SelectElementImpl selectElementImpl = getSelectElement(context);
        return selectElementImpl.getState();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/4	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 12-Sep-05	9415/5	emma	VBM:2005072710 Fixing element stack mistake

 09-Sep-05	9415/3	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 28-Jul-05	9129/1	emma	VBM:2005071304 Modifications after review

 27-Jul-05	9060/3	tom	VBM:2005071304 Added Sel Select

 ===========================================================================
*/
