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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.mcs;

import com.volantis.mcs.assets.ScriptAsset;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.implementation.DefaultComponentScriptAssetReference;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.components.QuotedReferenceFactory;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactory;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.xdime.StylableXDIMEElement;
import com.volantis.mcs.xdime.UnstyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The XDIME 2 'script' element, used to define scripts.
 *
 * <p>Since the XHTML 2 script element is not completely defined yet, this is an
 * implementation in the MCS namespace.</p>
 */
public class ScriptElement extends StylableXDIMEElement {

    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ScriptElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(ScriptElement.class);

    /**
     * Buffer to hold the content seen inside this script element.
     */
    private DOMOutputBuffer bodyContentBuffer;

    /**
     * The script attributes collected.
     */
    private ScriptAttributes scriptAttributes;

    /**
     * Initialise.
     * @param context
     */
    public ScriptElement(XDIMEContextInternal context) {
        super(MCSElements.SCRIPT, UnstyledStrategy.STRATEGY,
                context);
    }

    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(
                final XDIMEContextInternal context,
                final XDIMEAttributes attributes)
            throws XDIMEException {

        final MarinerPageContext pageContext = getPageContext(context);

        // check the src attribute
        final String src = getAttribute(XDIMEAttribute.SRC, attributes);
        ScriptAssetReference scriptReference = null;
        if (src != null) {
//            Note:ATM script element is in the MCS namespace so there is no use
//            of having a srctype attribute.
//
//            final String srcType =
//                getAttribute(XDIMEAttribute.SRC_TYPE, attributes);
//            if (srcType != null && !srcType.equals("")) {
//                // @to do find out what the srctype should be for mscr files
//                throw new XDIMEException(
//                    "Invalid source type. Found: " + srcType + " expected: ");
//            }
            scriptReference = getScriptAssetReference(src, pageContext);
            if (scriptReference == null) {
                LOGGER.warn("cannot-create-script-asset-reference-for-url",
                    src);
            }
        }

        // set the attributes
        scriptAttributes = new ScriptAttributes();
        final String id = getAttribute(XDIMEAttribute.ID, attributes);
        scriptAttributes.setId(id);
        if (scriptReference == null) {
            // Ensure that type="text/javascript" which is the only value we
            // currently support.
            final String type = attributes.getValue("", "type");
            if (type != null && !"text/javascript".equals(type)) {
                throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "script-must-have-text-javascript"));
            }

            scriptAttributes.setType("text/javascript");
        } else {
            scriptAttributes.setScriptReference(scriptReference);
            final ScriptAsset scriptAsset = scriptReference.getScriptAsset();
            scriptAttributes.setCharSet(scriptAsset.getCharacterSet());
            scriptAttributes.setLanguage(scriptAsset.getProgrammingLanguage());
            scriptAttributes.setType(scriptAsset.getMimeType());
        }

        // Create an output buffer for the content of the script element.
        // This will enable us to grab it easily when the element ends and
        // either throw it away if the src attribute can be used or use it
        // instead if not.
        bodyContentBuffer = (DOMOutputBuffer) getProtocol(context).
            getOutputBufferFactory().createOutputBuffer();
        // Stick the new output buffer onto the stack so that it collects the
        // script content.
        pageContext.pushOutputBuffer(bodyContentBuffer);

        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited
    protected void callCloseOnProtocol(final XDIMEContextInternal context)
            throws XDIMEException {

        final MarinerPageContext pageContext = getPageContext(context);
        pageContext.popOutputBuffer(bodyContentBuffer);

        if (scriptAttributes.getScriptReference() == null) {
            // Get the textual script.
            String script = bodyContentBuffer.getPCDATAValue();
            if (script != null) {
                // Turn the textual script into a literal script reference.
                scriptAttributes.setScriptReference((ScriptAssetReference)
                    QuotedReferenceFactory.SCRIPT_REFERENCE_FACTORY.
                        createLiteralReference(script));
            }
        }
        if (scriptAttributes.getScriptReference() == null) {
            throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                "cannot-create-script-asset-reference"));
        }

        // write the script out using the protocol
        final VolantisProtocol protocol = getProtocol(context);
        protocol.pushHeadBuffer();
        protocol.writeOpenScript(scriptAttributes);
        protocol.writeCloseScript(scriptAttributes);
        protocol.popHeadBuffer();
    }

    /**
     * Creates a ScriptAssetReference for the specified URL.
     *
     * <p>Returns null if variant for the URL cannot be loaded.</p>
     *
     * @param policyName the URL/policy name to use
     * @param pageContext the current page context
     * @return the script asset reference or null
     */
    private static ScriptAssetReference getScriptAssetReference(
            final String policyName, final MarinerPageContext pageContext) {

        final RuntimeProject project = pageContext.getCurrentProject();
        final MarinerURL baseURL = pageContext.getBaseURL();
        final PolicyReferenceFactory factory =
            pageContext.getPolicyReferenceFactory();
        final RuntimePolicyReference reference =
            factory.createLazyNormalizedReference(
                project, baseURL, policyName, PolicyType.SCRIPT);
        final ScriptAssetReference scriptAssetReference =
            new DefaultComponentScriptAssetReference(
                reference, pageContext.getAssetResolver());
        return scriptAssetReference.getScriptAsset() != null ?
            scriptAssetReference : null;
    }
}
