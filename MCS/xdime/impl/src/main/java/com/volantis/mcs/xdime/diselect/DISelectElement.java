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

import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElementImpl;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.ElementOutputState;
import com.volantis.mcs.xdime.ValidationStrategy;
import com.volantis.mcs.xml.schema.model.ElementType;

/**
 * This is the superclass for all Element implementations in the DISelect
 * namespace.
 */
public abstract class DISelectElement extends XDIMEElementImpl {

    /**
     * Indicates whether the expr attribute is mandatory or not.
     */
    private final AttributeUsage exprUsage;

    protected DISelectElement(
            ElementType type, XDIMEContextInternal context,
            AttributeUsage exprUsage) {
        this(type, context, ValidationStrategy.ANYWHERE, exprUsage);
    }

    protected DISelectElement(
            ElementType type,
            XDIMEContextInternal context, ValidationStrategy validationStrategy,
            AttributeUsage exprUsage) {
        super(type, context, validationStrategy);
        this.exprUsage = exprUsage;
    }

    // Javadoc inherited.
    protected boolean evaluateExpression(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        // retrieve the value of the expr attribute (no namespace)
        String expr = getAttribute(XDIMEAttribute.EXPR, attributes);
        if (expr == null) {
            if (exprUsage == AttributeUsage.MANDATORY) {
                throw new IllegalStateException(
                        "Expr attribute is mandatory on " + getElementType());
            }
        } else if (exprUsage == AttributeUsage.ILLEGAL) {
            throw new IllegalStateException(
                    "Expr attribute is illegal on " + getElementType());
        } else {
            return evaluateExpression(context, expr);
        }

        return true;
    }

    /**
     * Override to return the output state from the containing element.
     *
     * <p>DISelect elements are not styled and have no impact on the
     * output.</p>
     *
     * @return The output state of the parent.
     */
    public ElementOutputState getOutputState() {
        if (parent == null) {
            return null;
        }
        return parent.getOutputState();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/4	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 02-Oct-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 09-Sep-05	9415/5	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 28-Jul-05	9129/2	emma	VBM:2005071304 Modifications after review

 27-Jul-05	9060/1	tom	VBM:2005071304 Added Sel Select

 ===========================================================================
*/
