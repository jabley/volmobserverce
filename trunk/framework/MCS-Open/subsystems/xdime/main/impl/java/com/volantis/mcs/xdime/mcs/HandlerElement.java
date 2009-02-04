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
package com.volantis.mcs.xdime.mcs;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.HandlerScriptRegistry;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.runtime.components.QuotedReferenceFactory;
import com.volantis.mcs.xdime.StylableXDIMEElement;
import com.volantis.mcs.xdime.UnstyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The XDIME 2 'handler' element, used to define XML Events scripts.
 * <p>
 * This is a simplified version of the element defined <a
 * href="http://www.w3.org/TR/2005/WD-xhtml2-20050527/mod-handler.html#s_handlermodule">
 * here</a>.
 * <p>
 * Note that it is currently defined as an MCS element rather than XHTML or
 * XML Events since the XHTML WD above defines it but says it should be in
 * a future XML Events version. Until this settles down this element will live
 * in the MCS namespace.
 * <p>
 * See R1189 and the XML Events spec for more details.
 */
public class HandlerElement extends StylableXDIMEElement {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(HandlerElement.class);

    /**
     * Buffer to hold the content seen inside this handler element.
     */
    private DOMOutputBuffer bodyContentBuffer;

    /**
     * The id of the handler.
     */
    private String id;

    /**
     * Initialise.
     * @param context
     */
    public HandlerElement(XDIMEContextInternal context) {
        super(MCSElements.HANDLER, UnstyledStrategy.STRATEGY,
                context);
    }

    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        // Get the id= attribute.
        id = getAttribute(XDIMEAttribute.ID, attributes);
        if (id == null) {
            throw new IllegalArgumentException(
                    "Handler element must have id attribute");
        }

        // Ensure that type="text/javascript" which is the only value we
        // currently support.
        final String type = attributes.getValue("", "type");
        if (!"text/javascript".equals(type)) {
            throw new IllegalArgumentException(
                    "Handler element must have type=\"text/javascript\"");
        }

        // Create an output buffer for the content of the handler element.
        // This will enable us to grab it easily when the element ends and
        // either throw it away if the src attribute can be used or use it
        // instead if not.
        bodyContentBuffer = (DOMOutputBuffer) getProtocol(context).
            getOutputBufferFactory().createOutputBuffer();

        // Stick the new output buffer onto the stack so that it collects the
        // handler content.
        getPageContext(context).pushOutputBuffer(bodyContentBuffer);

        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited
    protected void callCloseOnProtocol(XDIMEContextInternal context) {

        final MarinerPageContext pageContext = getPageContext(context);
        pageContext.popOutputBuffer(bodyContentBuffer);

        // Get the textual script. Should be easy as handler is PCDATA.
        String script = bodyContentBuffer.getPCDATAValue();
        if (script != null) {
            // Turn the textual script into a literal script reference.
            ScriptAssetReference scriptReference = (ScriptAssetReference)
                    QuotedReferenceFactory.SCRIPT_REFERENCE_FACTORY.
                            createLiteralReference(script);

            // Register the script in the registry for later use by the
            // 'listener' element.
            if (logger.isDebugEnabled()) {
                logger.debug("Registering handler id '" + id + "' with script '"
                        + script + "'");
            }
            HandlerScriptRegistry scriptRegistry =
                    pageContext.getHandlerScriptRegistry();
            scriptRegistry.addScriptById(id, scriptReference);
        }
    }

}
