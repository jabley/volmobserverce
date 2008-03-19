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
 * $Header: /src/voyager/com/volantis/mcs/papi/MediaUtilities.java,v 1.16 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 01-Jan-02    Adrian          VBM:2001122803 - Created
 * 18-Jan-02    Adrian          VBM:2001121003 - Added encoding parameter to
 *                              tryAltText and tryAltImage so that different
 *                              types of component can be retrieved from the
 *                              pageContext.
 * 22-Jan-02    Allan           VBM:2001121703 - PluginAttribute now in
 *                              objects package.
 * 28-Feb-02    Paul            VBM:2002022804 - Replaced call to the
 *                              writeEncodedText method in the
 *                              MarinerPageContext with a call to writeEncoded
 *                              in the protocol.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 03-Apr-02    Adrian          VBM:2001102414 - use ComponentIdentities in
 *                              call to MarinerPageContext retrieve.. methods.
 *                              Using componentIdentities instead of String
 *                              names also removed the need for encoding
 *                              parameters as we can now use the type of the
 *                              identity to determine encoding.
 * 23-May-02    Adrian          VBM:2002041503 - modified setBrPlugin to
 *                              retrieve Plugin Attributes by calling the
 *                              method getPluginAttributes on Volantis.java
 * 23-May-02    Paul            VBM:2002042202 - Use Writer to write the
 *                              alternate text.
 * 26-Jul-02    Allan           VBM:2002072602 - Modified altText to use a
 *                              FallbackTextComponentNameVisitor.
 * 18-Nov-02    Geoff           VBM:2002111504 - Refactored to use new fallback
 *                              methods in the page context, cleaned up javadoc
 *                              and imports.
 * 22-Nov-02    Geoff           VBM:2002111504 - Refactored code in tryAltText 
 *                              into VolantisProtocol.writeAltText and 
 *                              AltTextAttributes, fixed probable style bug in
 *                              tryAltImg, cleaned up imports.
 * 13-Jan-03    Steve           VBM:2002112101 - Pass on the pane from the
 *                              block attributes to the image in tryAltImg().
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 16-Apr-03    Geoff           VBM:2003041603 - Add catch clause for 
 *                              ProtocolException that throws PAPIException.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.policies.ConcretePolicy;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.protocols.AltTextAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.assets.implementation.FallbackComponentTextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.styling.Styles;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Utility methods for processing DynamicVisualElements.
 */
class MediaUtilities {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MediaUtilities.class);


    MediaUtilities() {
    }

    /**
     * Try AltImage fallback for the DynamicVisualElement.
     *
     * @param attributes  the generic attributes of the DynamicVisualElement.
     * @param pageContext the PageContext for this pluginAttribute.
     * @param policy      the Asset policy.
     * @param altAttr     the AltText attribute of the Asset
     * @throws PAPIException if there is problem accessing repository
     *                       or data.
     */
    static boolean tryAltImg(
            BlockAttributes attributes,
            MarinerPageContext pageContext,
            ConcretePolicy policy, String altAttr)
            throws PAPIException {

        // @todo all this code should really be refactored ala the alt text stuff
        // for consistency
        if (logger.isDebugEnabled()) {
            logger.debug("Trying altImage for asset named \"" +
                    policy.getName()
                    + " on device " + pageContext.getDeviceName());
        }

        String imageURL = null;
        AssetResolver resolver = pageContext.getAssetResolver();
        RuntimePolicyReference fallback = (RuntimePolicyReference)
                policy.getAlternatePolicy(PolicyType.IMAGE);
        if (fallback != null) {
            imageURL = resolver.retrieveVariantURLAsString(fallback, null);
        }

        if (imageURL == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No altImage for component \"" + policy.getName());
            }
            return false;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("component " + policy.getName() +
                    " alt image url is " + imageURL);
        }

        com.volantis.mcs.protocols.ImageAttributes ia =
                new com.volantis.mcs.protocols.ImageAttributes();
        ia.setTagName(attributes.getElementName());
        ia.setSrc(imageURL);
        ia.setId(attributes.getId());
        ia.setTitle(attributes.getTitle());
        ia.setAltText(altAttr);

        if (attributes.getPane() != null) {
            ia.setPane(pageContext.getPane(attributes.getPane()));
        }

        try {
            VolantisProtocol protocol = pageContext.getProtocol();
            protocol.writeImage(ia);
        } catch (ProtocolException e) {
            String msg = "Error during altimage rendering";
            logger.error("alt-image-rendering-error", e);
            throw new PAPIException(msg, e);
        }

        return true;
    }

    /**
     * Build TextAssetReference for altText and try to render it.
     *
     * @param attributes  the generic attributes of the DynamicVisualElement.
     * @param pageContext the PageContext for this pluginAttribute.
     * @param policy      the Asset policy.
     * @param altAttr     the AltText attribute of the Asset
     * @param styles      styles that should be applied to the alternate text
     * @return true if altText rendered.
     */
    static boolean tryAltText(
            BlockAttributes attributes,
            MarinerPageContext pageContext,
            ConcretePolicy policy, String altAttr,
            Styles styles) throws PAPIException {

        if (logger.isDebugEnabled()) {
            logger.debug("Trying altText for dynvis asset named \"" +
                    policy.getName() + " on device " +
                    pageContext.getDeviceName());
        }

        AssetResolver resolver = pageContext.getAssetResolver();

        TextAssetReference altText = new LiteralTextAssetReference(altAttr);
        altText = new FallbackComponentTextAssetReference(resolver, policy,
                altText);

        return finishAltText(altText, attributes, pageContext, styles);
    }

    /**
     * Build TextAssetReference for altText and try to render it.
     *
     * @param attributes  the generic attributes of the DynamicVisualElement.
     * @param pageContext the PageContext for this pluginAttribute.
     * @param reference   the reference to the Asset policy.
     * @param altAttr     the AltText attribute of the Asset
     * @param styles      styles that should be applied to the alternate text
     * @return true if altText rendered
     */
    static boolean tryAltText(
            BlockAttributes attributes,
            MarinerPageContext pageContext,
            RuntimePolicyReference reference,
            String altAttr,
            Styles styles) throws PAPIException {

        if (logger.isDebugEnabled()) {
            logger.debug("Trying altText for dynvis asset named \"" +
                    reference.getName() + " on device " +
                    pageContext.getDeviceName());
        }

        TextAssetReference altText = new LiteralTextAssetReference(altAttr);

        return finishAltText(altText, attributes, pageContext, styles);
    }

    /**
     * Try to render altText fallback for the DynamicVisualElement.
     *
     * @param altText     to render.
     * @param attributes  the generic attributes of the DynamicVisualElement.
     * @param pageContext the PageContext for this pluginAttribute.
     * @param styles      styles that should be applied to the alternate text
     * @return true if altText rendered
     * @throws PAPIException
     */
    static private boolean finishAltText(
            TextAssetReference altText,
            BlockAttributes attributes,
            MarinerPageContext pageContext,
            Styles styles) throws PAPIException {

        AltTextAttributes altTextAttrs = new AltTextAttributes(attributes);
        altTextAttrs.setAltText(altText);
        altTextAttrs.setStyles(styles);

        try {
            VolantisProtocol protocol = pageContext.getProtocol();
            return protocol.writeAltText(altTextAttrs);
        } catch (ProtocolException e) {
            String msg = "Error during alttext rendering";
            logger.error("alt-text-rendering-error", e);
            throw new PAPIException(msg, e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10550/1	geoff	VBM:2005113025 MCS35: mcsExport no longer works with the -all flag

 07-Nov-05	10173/1	emma	VBM:2005103107 Forward port: Fixes to correctly apply styles to various selectors

 07-Nov-05	10116/2	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
