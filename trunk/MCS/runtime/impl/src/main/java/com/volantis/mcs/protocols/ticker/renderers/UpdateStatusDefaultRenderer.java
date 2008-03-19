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

package com.volantis.mcs.protocols.ticker.renderers;

import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageMetaData;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.ticker.attributes.UpdateStatusAttributes;
import com.volantis.mcs.protocols.widgets.renderers.WidgetHelper;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.SelectedVariant;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.utilities.StringConvertor;
import com.volantis.styling.StatefulPseudoClass;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.synergetics.localization.MessageLocalizer;

import java.io.StringWriter;

/**
 * Renderer for FeedPoller element
 */
public class UpdateStatusDefaultRenderer
        extends ElementDefaultRenderer {
    /**
     * Used to retrieve localized messages.
     */
    protected static final MessageLocalizer MESSAGE_LOCALIZER =
            LocalizationFactory.createMessageLocalizer(
                    UpdateStatusDefaultRenderer.class);


    // Javadoc inherited
    public void doRenderOpen(
            VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }

        UpdateStatusAttributes updateStatusAttributes =
                (UpdateStatusAttributes) attributes;

        // Require libraries
        requireLibrary("/prototype.mscr", protocol);
        requireLibrary("/scriptaculous.mscr", protocol);

        requireLibrary("/vfc-base.mscr", protocol);
        requireLibrary("/vfc-ticker.mscr", protocol);

        // Open span element enclosing the widget.
        SpanAttributes spanAttributes = new SpanAttributes();

        spanAttributes.copy(attributes);

        protocol.writeOpenSpan(spanAttributes);

        // Render four elements for each status value.
        Element normalElement = renderStatusElement(protocol,
                updateStatusAttributes, StatefulPseudoClasses.MCS_NORMAL);
        Element busyElement = renderStatusElement(protocol,
                updateStatusAttributes, StatefulPseudoClasses.MCS_BUSY);
        Element failedElement = renderStatusElement(protocol,
                updateStatusAttributes, StatefulPseudoClasses.MCS_FAILED);
        Element suspendedElement = renderStatusElement(protocol,
                updateStatusAttributes, StatefulPseudoClasses.MCS_SUSPENDED);

        // Close span element enclosing the widget.
        protocol.writeCloseSpan(spanAttributes);

        // Prepare Javascript content.
        StringWriter scriptWriter = new StringWriter();

        // Finally, render the JavaScript part.
        scriptWriter.write("Ticker.createUpdateStatus({");
        scriptWriter.write("normalId:" +
                createJavaScriptString(normalElement.getAttributeValue("id")));
        scriptWriter.write(",busyId:" +
                createJavaScriptString(busyElement.getAttributeValue("id")));
        scriptWriter.write(",failedId:" +
                createJavaScriptString(failedElement.getAttributeValue("id")));
        scriptWriter.write(",suspendedId:" + createJavaScriptString(
                suspendedElement.getAttributeValue("id")));
        scriptWriter.write(",displayStyle:"
                + createJavaScriptString(updateStatusAttributes.getStyles().
                getPropertyValues().getComputedValue(
                StylePropertyDetails.DISPLAY).getStandardCSS()));
        scriptWriter.write("})");

        addUsedFeedPollerId(protocol);

        // Write JavaScript content to DOM.
        writeJavaScript(scriptWriter.toString());
    }

    // Javadoc inherited
    public void doRenderClose(
            VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }
    }

    private Element renderStatusElement(
            VolantisProtocol protocol, UpdateStatusAttributes attributes,
            StatefulPseudoClass status) throws ProtocolException {
        Styles styles = attributes.getStyles().getNestedStyles(status);

        if (styles == null) {
            styles = StylingFactory.getDefaultInstance().createInheritedStyles(
                    attributes.getStyles(), DisplayKeywords.NONE);
        }

        Element element =
                getCurrentBuffer(protocol).openStyledElement("span", styles);

        element.setAttribute("id",
                protocol.getMarinerPageContext().generateUniqueFCID());

        StyleValue content = styles.getPropertyValues()
                .getSpecifiedValue(StylePropertyDetails.CONTENT);

        // If content is not specified, render it inline.
        if (content == null) {
            renderDefaultStatusContent(protocol, status);
        } else {
            renderCustomStatusContent(protocol, element, content);
        }

        getCurrentBuffer(protocol).closeElement("span");

        return element;
    }

    private void renderDefaultStatusContent(
            VolantisProtocol protocol, StatefulPseudoClass status)
            throws ProtocolException {
        if (status == StatefulPseudoClasses.MCS_NORMAL) {
            renderImage(protocol, "/update-status-ok.mimg", MESSAGE_LOCALIZER
                    .format("ticker-update-status-normal"));
        } else if (status == StatefulPseudoClasses.MCS_BUSY) {
            renderImage(protocol, "/update-status-busy.mimg", MESSAGE_LOCALIZER
                    .format("ticker-update-status-busy"));
        } else if (status == StatefulPseudoClasses.MCS_FAILED) {
            renderImage(protocol, "/update-status-failed.mimg",
                    MESSAGE_LOCALIZER
                            .format("ticker-update-status-failed"));
        } else if (status == StatefulPseudoClasses.MCS_SUSPENDED) {
            renderImage(protocol, "/update-status-suspended.mimg",
                    MESSAGE_LOCALIZER
                            .format("ticker-update-status-suspended"));
        }
    }

    private void renderCustomStatusContent(
            VolantisProtocol protocol, Element element, StyleValue content)
            throws ProtocolException {
        ((DOMProtocol) protocol).getInserter().insert(element, content);
    }

    /**
     * Write out the image using the image policy supplied.
     *
     * @param protocol  The protocol to use for rendering.
     * @param policyURL The relative policy URL.
     * @param alt       The alternative string for rendering.
     * @throws ProtocolException
     */
    private void renderImage(
            VolantisProtocol protocol, String policyURL, String alt)
            throws ProtocolException {

        // Get policy reference.
        RuntimePolicyReference reference;
        try {
            reference = WidgetHelper.loadImageReference(policyURL,
                    protocol.getMarinerPageContext());
        } catch (MarinerContextException e) {
            throw new ProtocolException(e);
        }

        // TODO: factor together with AbstractImageElement
        MarinerPageContext pageContext = protocol.getMarinerPageContext();

        AssetResolver resolver = pageContext.getAssetResolver();

        SelectedVariant selectedVariant =
                resolver.selectBestVariant(reference, null);

        String url = resolver.retrieveVariantURLAsString(selectedVariant);
        if (url == null) {
            return;
        }

        url = resolver.rewriteURLWithPageURLRewriter(url, PageURLType.IMAGE);

        ImageAttributes imageAttributes = new ImageAttributes();

        BaseLocation location = resolver.getBaseLocation(selectedVariant);
        imageAttributes.setLocalSrc(location == BaseLocation.DEVICE);

        Variant variant = selectedVariant.getVariant();
        ImageMetaData image = (ImageMetaData) variant.getMetaData();
        // Make a note of whether this image is a convertible
        // image asset
        if (image.getConversionMode() == ImageConversionMode.ALWAYS_CONVERT) {
            imageAttributes.setConvertibleImageAsset(true);
        } else {
            imageAttributes.setConvertibleImageAsset(false);
        }

        imageAttributes.setHeight(StringConvertor.valueOf(image.getHeight()));
        imageAttributes.setWidth(StringConvertor.valueOf(image.getWidth()));
        imageAttributes.setSrc(url);
        imageAttributes.setAltText(alt);
        protocol.writeImage(imageAttributes);
    }
}
