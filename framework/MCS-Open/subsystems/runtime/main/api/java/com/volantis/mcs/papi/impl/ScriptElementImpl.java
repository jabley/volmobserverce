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
 * $Header: /src/voyager/com/volantis/mcs/papi/ScriptElement.java,v 1.14 2003/04/22 13:56:08 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created
 * 11-Feb-02    Paul            VBM:2001122105 - Added new script behaviour.
 * 22-Feb-02    Adrian          VBM:2002021906 - changed method elementStart to
 *                              call pageContext.writeScriptText() instead of
 *                              pageContext.writeRawText() to write the value
 *                              of LITERAL script assets.  The new method
 *                              writes to  the current string buffer if the
 *                              parent tag is a block element, otherwise the
 *                              DeviceLayoutContext preamble buffer.
 * 22-Feb-02    Adrian          VBM:2002021906 - undid change above.
 *                              StringProtocol now adds a new outputbuffer to
 *                              the stack on writeOpenScript so writeRawText
 *                              will always find a currentOutputBuffer for a
 *                              ScriptElement.
 * 28-Feb-02    Paul            VBM:2002022804 - Replaced call to the
 *                              writeRawText method in the
 *                              MarinerPageContext with a call to writeDirect
 *                              in the protocol.
 * 12-Mar-02    Adrian          VBM:2002021910 - changed call to writeDirect to
 *                              writeScriptBody as each protocol may require
 *                              different processing on the asset data.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 23-May-02    Paul            VBM:2002042202 - Use Writer to write the
 *                              script body.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 16-Apr-03    Geoff           VBM:2003041603 - Add to do comment.
 * 16-Apr-03    Geoff           VBM:2003041603 - Fixed previous bogus comment.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.assets.ScriptAsset;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.ScriptAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;

/**
 * The script element.
 */
public class ScriptElementImpl
        extends AbstractExprElementImpl
        implements DevicePolicyConstants {

    /**
     * Flag which indicates whether the elementStart method opened the element,
     * or did nothing.
     */
    private boolean opened;

    /**
     * The attributes to pass to the protocol methods.
     */
    private com.volantis.mcs.protocols.ScriptAttributes pattributes;

    /**
     * Create a new <code>ScriptElement</code>.
     */
    public ScriptElementImpl() {
        pattributes = new com.volantis.mcs.protocols.ScriptAttributes();
    }

    // Javadoc inherited.
    MCSAttributes getMCSAttributes() {
        return pattributes;
    }

    // Javadoc inherited.
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        ScriptAttributes attributes = (ScriptAttributes) papiAttributes;

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        VolantisProtocol protocol = pageContext.getProtocol();

        // Check if the Policy x-element.script.supported is set to default, via
        // the supportsScriptType() method of protocol. If so, exercise the same
        // behaviour as before VBM 20080131102 ie. check the "protocol"
        // support and followup check on boolean device policy "javascript".
        if ((protocol.supportsScriptType() == null) ||
             ("default".equals(protocol.supportsScriptType())) ) {

            // Check to see whether the protocol supports java script and the
            // device supports JavaScript, if either don't then skip the body.
            //
            // Note: This functionality is copied directly from the ScriptTag.
            if (!protocol.supportsJavaScript ()
              || !pageContext.getBooleanDevicePolicyValue (SUPPORTS_JAVASCRIPT)) {
               return SKIP_ELEMENT_BODY;
            }

        } else if ("full".equals(protocol.supportsScriptType())) {
            // Scripts are supported in an override due to the, so just check
            // on the device javascript policy to enable it fully. If it's not
            // set then return (SKIP), otherwise carry on. NOTE: Here, where are
            // getting the opinion of the actual device, not an inference derived
            // from the protocol.
            if (!pageContext.getBooleanDevicePolicyValue (SUPPORTS_JAVASCRIPT)) {
               return SKIP_ELEMENT_BODY;
            }
            
        } else {
        // If "none" or other is specified, SKIP this entirely irrespective
        // of any other settings.
               return SKIP_ELEMENT_BODY;
        }

        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();

        // Parse the source expression.
        ScriptAssetReference src
                = resolver.resolveQuotedScriptExpression(attributes.getSrc());

        // Set the attributes used in both modes.
        String id = attributes.getId();
        pattributes.setId(id);
        pattributes.setDefer(attributes.getDefer());

        // @todo this code duplicates the functionality of the protocols Script
        // object. we should refactor it away...
        if (src != null && src.isPolicyReference()) {
            ScriptAsset asset = src.getScriptAsset();
            // If no asset was found then there is nothing more to do.
            if (asset == null) {
                return SKIP_ELEMENT_BODY;
            }

            // If an id was specified then we need to remember whether we found a
            // suitable ScriptAsset, or not.
            if (id != null) {
                pageContext.putIdValue(id, asset);
            }

            pattributes.setScriptReference(src);
            pattributes.setCharSet(asset.getCharacterSet());
            pattributes.setType(asset.getMimeType());
            pattributes.setLanguage(asset.getProgrammingLanguage());

            // Write the open script.
            protocol.writeOpenScript(pattributes);
            opened = true;
            return SKIP_ELEMENT_BODY;

        } else {
            // Use the deprecated attributes.
            pattributes.setTitle(attributes.getTitle());

            pattributes.setCharSet(attributes.getCharSet());
            pattributes.setLanguage(attributes.getLanguage());
            pattributes.setScriptReference(src);
            pattributes.setType(attributes.getType());

            protocol.writeOpenScript(pattributes);
            opened = true;

            return PROCESS_ELEMENT_BODY;
        }
    }

    // Javadoc inherited.
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        if (!opened) {
            return CONTINUE_PROCESSING;
        }

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        VolantisProtocol protocol = pageContext.getProtocol();

        protocol.writeCloseScript(pattributes);

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        opened = false;

        pattributes.resetAttributes();

        super.elementReset(context);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	6183/4	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 22-Oct-04	5907/1	claire	VBM:2004100108 mergevbm: Handling suitable encoding of scripts depending on protocol

 21-Oct-04	5887/1	claire	VBM:2004100108 Handling suitable encoding of scripts depending on protocol

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 25-Mar-04	3576/1	steve	VBM:2003032711 type attribute on Script element

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 ===========================================================================
*/
