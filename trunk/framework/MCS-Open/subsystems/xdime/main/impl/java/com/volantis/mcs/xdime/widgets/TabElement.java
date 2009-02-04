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

package com.volantis.mcs.xdime.widgets;

import java.io.IOException;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.assets.implementation.DefaultComponentImageAssetReference;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TabAttributes;
import com.volantis.mcs.protocols.widgets.attributes.WidgetAttributes;
import com.volantis.mcs.protocols.widgets.renderers.TabsRenderer;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.Styles;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Tab XDIME2 element
 */
public class TabElement extends WidgetElement implements Loadable {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = 
        LocalizationFactory.createLogger(TabElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer = 
        LocalizationFactory.createExceptionLocalizer(TabElement.class);

    public TabElement(XDIMEContextInternal context) {
        super(WidgetElements.TAB, context);
        protocolAttributes = new TabAttributes();
    }

    public void setLoadAttributes(LoadAttributes attrs) {
        getTabAttributes().setLoadAttributes(attrs);
    }

    public TabAttributes getTabAttributes() {
        return ((TabAttributes) protocolAttributes);
    }

    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        try {
            WidgetModule widgetModule = getWidgetModule(context);
            // Do nothing if widgets are not supported at all
            if (null == widgetModule) {
                // Do fallback if widget is not supported by the protocol
                return doFallbackOpen(context, attributes);
            }
            
            TabsRenderer tabsRenderer = widgetModule.getTabsRenderer();
            if (null == tabsRenderer) {
                // Do fallback if widget is not supported by the protocol
                return doFallbackOpen(context, attributes);        
            }
            // Open tab element
            tabsRenderer.renderTabOpen(getProtocol(context),
                    (WidgetAttributes) protocolAttributes);

        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);
            
            throw new XDIMEException(exceptionLocalizer.format("rendering-error",
                    getTagName()), e);
        }
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    public void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {

        WidgetModule widgetModule = getWidgetModule(context);
        // Do nothing if widgets are not supported at all
        if (null == widgetModule) {
            // Do fallback if widget is not supported by the protocol
            doFallbackClose(context);
            return;
        }

        try {
            TabsRenderer tabsRenderer = widgetModule.getTabsRenderer();
            if (null == tabsRenderer) {
                // Do fallback if widget is not supported by the protocol
                doFallbackClose(context);
                return;
            }
            // close tab element
            tabsRenderer.renderTabClose(getProtocol(context),
                    (WidgetAttributes) protocolAttributes);

        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);

            throw new XDIMEException(exceptionLocalizer.format(
                    "rendering-error", getTagName()), e);
        }
    }

    // Javadoc inherited
    protected XDIMEResult doFallbackOpen(XDIMEContextInternal context, XDIMEAttributes attributes)
            throws ProtocolException {

        VolantisProtocol protocol = getProtocol(context);
        protocol.writeOpenDiv(new DivAttributes());

        // render label
        renderTabLabel(protocol);

        protocol.writeCloseDiv(new DivAttributes());

        // process body
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    /**
     * Render label text from CSS ::mcs-label content
     * @param protocol VolantisProtocol
     * @throws ProtocolException
     */
    private void renderTabLabel(VolantisProtocol protocol) throws ProtocolException {
        
        String textLabel = "";
        // Get content value specified for ::mcs-label
        Styles tabStyles = protocolAttributes.getStyles();
        Styles labelStyles = (null == tabStyles ? null : tabStyles
                .getNestedStyles(PseudoElements.MCS_LABEL));
        StyleValue labelContent = (null == labelStyles ? null : labelStyles
                .getPropertyValues().getSpecifiedValue(
                        StylePropertyDetails.CONTENT));

        // Render label content
        if (null != labelContent) {
            if (labelContent instanceof StyleList) {
                StyleList contentList = (StyleList) labelContent;
                if (contentList.getList().isEmpty()) {
                    throw new ProtocolException(exceptionLocalizer
                            .format("widget-tab-label-empty-content"));
                }
                labelContent =  (StyleValue) contentList.getList().get(0);
                if (labelContent.getStyleValueType() == StyleValueType.STRING) {
                    textLabel = ((StyleString)labelContent).getString();
                } else {
                    textLabel = labelContent.getStandardCSS();
                }
            } else if (labelContent instanceof StyleComponentURI) {

                // Get asset and policy resolvers, resolve component policy
                PolicyReferenceResolver referenceResolver = protocol.getMarinerPageContext().getPolicyReferenceResolver();
                AssetResolver assetResolver = protocol.getMarinerPageContext()
                        .getAssetResolver();
                RuntimePolicyReference policyReference = referenceResolver
                        .resolvePolicyExpression(((StyleComponentURI)labelContent).getExpression());

                // Create image asset from the policy and get fallback
                DefaultComponentImageAssetReference imageAsset = new DefaultComponentImageAssetReference(
                        policyReference, assetResolver);
                TextAssetReference textAsset = imageAsset.getTextFallback();

                // If e.g policy file is missing and as consequence textAsset == null,
                // throw suitable exception
                if (null == textAsset) {
                    throw new ProtocolException(exceptionLocalizer.format(
                            "missing-policy-failure", policyReference.getName()));
                }

                // Return text in plain encoding, or empty string
                textLabel = textAsset.getText(TextEncoding.PLAIN);
                if (null == textLabel) {
                    textLabel = "";
                }
            } else if (labelContent instanceof StyleString) {
                textLabel = ((StyleString)labelContent).getString();
            }
        }
        // write label to output buffer
        try {
            protocol.getContentWriter().write(textLabel);
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
        }

    }

}
