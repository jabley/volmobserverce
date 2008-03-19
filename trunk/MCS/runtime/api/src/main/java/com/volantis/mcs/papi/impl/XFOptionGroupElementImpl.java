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
 * $Header: /src/voyager/com/volantis/mcs/papi/XFOptionGroupElement.java,v 1.4 2003/03/12 16:10:43 sfound Exp $ 
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jul-02    Sumit           VBM:2002073109 - created for xfoptgroup 
 *                              support 
 * 01-Oct-02    Allan           VBM:2002093002 - Modified startElement() to
 *                              to process the prompt attribute.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.XFOptionGroupAttributes;
import com.volantis.mcs.protocols.SelectOptionGroup;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;

/*
*/
public class XFOptionGroupElementImpl
        extends AbstractExprElementImpl {

    private com.volantis.mcs.protocols.SelectOptionGroup optiongroup;

    /**
     * Creates a new instance of XFOptionGroupElement
     */
    public XFOptionGroupElementImpl() {
    }

    /**
     * Get this element's option group list attributes.
     */
    com.volantis.mcs.protocols.SelectOptionGroup getOptionGroup() {
        return optiongroup;
    }

    /**
     * Javadoc inherited from super class.
     */
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) throws PAPIException {
        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);
        XFOptionGroupAttributes xfoga =
                (XFOptionGroupAttributes) papiAttributes;
        optiongroup = new SelectOptionGroup();
        TextAssetReference object;

        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();

        // Process the caption as a mariner expression.
        object = resolver.resolveQuotedTextExpression(xfoga.getCaption());
        optiongroup.setCaption(object);

        // Process the prompt as a mariner expression.
        object = resolver.resolveQuotedTextExpression(xfoga.getPrompt());
        optiongroup.setPrompt(object);

        Object enclosing = pageContext.getCurrentElement();

        optiongroup.setStyles(pageContext.getStylingEngine().getStyles());

        if (enclosing instanceof XFSelectElementImpl) {
            XFSelectAttributes selectAttributes =
                    ((XFSelectElementImpl) enclosing).getProtocolAttributes();
            // Initialise the attributes specific to this field.
            selectAttributes.addOptionGroup(optiongroup);

        } else {
            SelectOptionGroup sfoga =
                    ((XFOptionGroupElementImpl) enclosing).getOptionGroup();
            sfoga.addSelectOptionGroup(optiongroup);
        }
        pageContext.pushElement(this);
        return PROCESS_ELEMENT_BODY;
    }

    /**
     * Javadoc inherited from super class.
     */
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) throws PAPIException {
        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);
        // Pop this element.
        pageContext.popElement(this);
        return CONTINUE_PROCESSING;
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10321/1	emma	VBM:2005103109 Forward port: Styling not applied correctly to some xf selectors

 14-Nov-05	10300/1	emma	VBM:2005103109 Styling not applied correctly to some xf selectors

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 ===========================================================================
*/
