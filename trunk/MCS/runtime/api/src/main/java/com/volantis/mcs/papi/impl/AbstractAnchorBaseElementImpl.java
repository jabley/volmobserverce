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
 * $Header: /src/voyager/com/volantis/mcs/papi/AbstractAnchorBaseElement.java,v 1.1 2003/04/10 12:53:24 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Mar-03    Phil W-S        VBM:2002111502 - Created. Refactors common
 *                              attributes shared by Anchor and PhoneNumber
 *                              elements.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.AbstractAnchorBaseAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.AnchorBaseAttributes;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * The abstract anchor base element. This base class is used to handle the
 * attributes common to anchor and phone number elements.
 */
public abstract class AbstractAnchorBaseElementImpl
        extends BlockElementImpl {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    AbstractAnchorBaseElementImpl.class);

    /**
     * Permits the common attributes to be transferred from the PAPI attributes
     * to the given protocol attributes.
     *
     * <strong>This is intentionally package protected. Do not change the
     * accessibility</strong>
     *
     * @param context     the request context
     * @param attributes  the attributes common to anchor and phone number
     *                    elements
     * @param pattributes the protocol attributes common to anchor and phone
     *                    number elements
     * @throws PAPIException if a problem is encountered transferring the
     *                       attributes
     */
    void transferAttributes(
            MarinerRequestContext context,
            AbstractAnchorBaseAttributes attributes,
            AnchorBaseAttributes pattributes)
            throws PAPIException {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        pattributes.setId(attributes.getId());
        pattributes.setTitle(attributes.getTitle());

        // Initialise the general event attributes
        PAPIInternals.initialiseGeneralEventAttributes(pageContext,
                attributes,
                pattributes);

        // Initialise the focus event attributes
        PAPIInternals.initialiseFocusEventAttributes(pageContext,
                attributes,
                pattributes);

        String value;
        TextAssetReference shortcut = null;
        TextAssetReference accessKey = null;

        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();

        // Resolve the shortcut string to a mariner expression
        if ((value = attributes.getShortcut()) != null) {
            shortcut = resolver.resolveQuotedTextExpression(value);
        }

        // Resolve the accesskey string to a mariner expression
        if ((value = attributes.getAccessKey()) != null) {
            accessKey = resolver.resolveQuotedTextExpression(value);
        }

        if (shortcut != null) {
            if (accessKey != null) {
                throw new PAPIException(exceptionLocalizer.
                        format("anchor-shortcut-and-accesskey-error"));
            }
        } else {
            shortcut = accessKey;
        }

        pattributes.setShortcut(shortcut);
        pattributes.setTabindex(attributes.getTabindex());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 31-May-05	8036/2	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 27-May-05	8599/3	pcameron	VBM:2005052605 Fixed accesskey alias rendering on anchors for WML

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
