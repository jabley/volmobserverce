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
 * $Header: /src/voyager/com/volantis/mcs/papi/AbstractImageElement.java,v 1.24 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Oct-01    Paul            VBM:2001111402 - Created
 * 28-Nov-01    Paul            VBM:2001112202 - Updated to reflect changes
 *                              in papi classes.
 * 19-Dec-01    Paul            VBM:2001120506 - Renamed from ImageElement so
 *                              it could be used as the basis for image and
 *                              logo elements.
 * 15-Jan-01    Adrian          VBM:2001122803 - Added setPane() on attributes.
 *                              Change need highlighted by testing
 *                              DynamicVisualElement using Sky Browser.
 * 11-Feb-02    Paul            VBM:2001122105 - Added call to initialise
 *                              the general event attributes.
 * 21-Feb-02    Allan           VBM:2002022007 - Modified elementStartImpl to
 *                              use StringConvertor for int to String.
 * 28-Feb-02    Paul            VBM:2002022804 - Replaced call to the
 *                              writeEncodedText method in the
 *                              MarinerPageContext with a call to writeEncoded
 *                              in the protocol.
 * 08-Mar-02    Adrian          VBM:2002020510 - cast BlockAttributes to
 *                              AbstractImageAttributes instead of
 *                              ImageAttributes as they may be LogoAttributes
 *                              or ImageAttributes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 03-Apr-02    Adrian          VBM:2001102414 - use ComponentIdentities in
 *                              calls to retrieveAsset methods in
 *                              MarinerPageContext to retrieve fallbacks.
 * 23-May-02    Paul            VBM:2002042202 - Use Writer to write the
 *                              alternate text.
 * 16-Aug-02    Sumit           VBM:2002081502 - Added support for urlc attr
 *                              for ConvertibleImageAsset, in elementStartImpl
 * 09-Sep-02    Ian             VBM:2002081307 - Added rule processing for urlc.
 * 02-Oct-02    Ian             VBM:2002092507 - Changed elementStartImpl to
 *                              get localSrc from AssetGroup.
 * 01-Nov-02    Ian             VBM:2002091806 - Moved calculation of MTS
 *                              widths for urlc into elementStartImpl.
 * 18-Nov-02    Geoff           VBM:2002111504 - Refactored to use new fallback
 *                              methods in the page context.
 * 22-Nov-02    Geoff           VBM:2002111504 - Refactored code in
 *                              elementStartImpl to do text fallbacks into
 *                              VolantisProtocol.writeAltText and
 *                              AltTextAttributes, cleaned up imports.
 * 02-Dec-02    Byron           VBM:2002103009 - Modified elementStartImpl()
 *                              to call method constructURL() in ContextInternals.
 *                              Reformatted this methods indentation.
 * 20-Dec-02    Chris W         VBM:2002121904 - elementStartImpl() calls
 *                              ImageAttributes.setConvertibleImageAsset() with
 *                              a boolean stating if the image is a convertible
 *                              image asset.
 * 29-Jan-03    Byron           VBM:2003012712 - Modified call to constructURL
 *                              to use correct new signature.
 * 11-Feb-03    Ian             VBM:2003020607 - Ported changes from Metis.
 * 10-Mar-02    Steve           VBM:2003022403 - Added API doclet tags
 * 16-Apr-03    Geoff           VBM:2003041603 - Add catch clause for
 *                              ProtocolException that throws PAPIException.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.assets.ConvertibleImageAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.PluggableAssetTranscoder;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.AbstractImageAttributes;
import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.protocols.AltTextAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.assets.implementation.FallbackComponentTextAssetReference;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.packagers.PackageResources;
import com.volantis.mcs.runtime.packagers.PackagedURLEncoder;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.SelectedVariant;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.utilities.StringConvertor;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Map;

/**
 * The image element.
 */
public abstract class AbstractImageElementImpl
        extends BlockElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(AbstractImageElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    AbstractImageElementImpl.class);

    /**
     * The name of the aspect ratio parameter.
     */
    private static final String MCS_ASPECT_RATIO_PARAM_NAME = "mcs.ar";

    /**
     * The attributes to pass to the protocol methods.
     */
    private com.volantis.mcs.protocols.ImageAttributes pattributes;

    /**
     * Create a new <code>AbstractImageElement</code>.
     */
    public AbstractImageElementImpl() {
        pattributes = new com.volantis.mcs.protocols.ImageAttributes();
    }

    // javadoc inherited
    MCSAttributes getMCSAttributes() {
        return pattributes;
    }

    // javadoc inherited
    protected int elementStartImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttributes)
            throws PAPIException {

        AbstractImageAttributes attributes
                = (AbstractImageAttributes) blockAttributes;

        String url = attributes.getUrl();
        String urlc = attributes.getUrlc();

        // The src is actually the component name.
        String imageComponentName = attributes.getSrc();
        int notNullCount = 0;

        if (url != null) {
            notNullCount++;
        }

        if (imageComponentName != null) {
            notNullCount++;
        }

        if (urlc != null) {
            notNullCount++;
        }

        if (notNullCount > 1) {
            throw new IllegalArgumentException
                    ("Only one of url, urlc and src may be set");
        } else if (notNullCount == 0) {
            throw new IllegalArgumentException
                    ("Either url, urlc or src must be set");
        }

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        VolantisProtocol protocol = pageContext.getProtocol();
        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();
        TextAssetReference altText = resolver.resolveQuotedTextExpression(
                attributes.getAltText());

        pattributes.setId(attributes.getId());
        pattributes.setTitle(attributes.getTitle());
        pattributes.setAltText(altText);
        pattributes.setPane(pageContext.getCurrentPane());
        pattributes.setAssetURLSuffix(attributes.getAssetURLsuffix());

        // Add any event attributes.
        PAPIInternals.initialiseGeneralEventAttributes(pageContext,
                attributes,
                pattributes);
        try {
            if (imageComponentName != null) {

                // Resolve the expression to a reference.
                RuntimePolicyReference reference =
                        resolver.resolveUnquotedPolicyExpression(
                                imageComponentName, PolicyType.IMAGE);

                AssetResolver assetResolver = pageContext.getAssetResolver();

                SelectedVariant selected =
                        assetResolver.selectBestVariant(reference, null);

                // Try and find the best image asset with the specified name.
                if (selected != null) {
                    ImageAsset imageAsset =
                            (ImageAsset) selected.getOldObject();
                    if (imageAsset == null) {
                        // No image asset which matches could be found so use the
                        // alt text, if any.

                        // And then try and write out the alt or fallback text,
                        // if we can
                        AltTextAttributes altTextAttrs =
                                new AltTextAttributes(attributes);
                        altTextAttrs.setStyles(pattributes.getStyles());
                        altText = new FallbackComponentTextAssetReference(
                                assetResolver, selected.getPolicy(), altText);
                        altTextAttrs.setAltText(altText);

                        if (protocol.writeAltText(altTextAttrs)) {
                            return SKIP_ELEMENT_BODY;
                        }

                    } else {
                        String[] srcArray = pageContext.
                                retrieveImageAssetURLAsString(imageAsset);
                        url = srcArray[0];

                        AssetGroup assetGroup =
                                assetResolver.getAssetGroup(imageAsset);
                        if (assetGroup != null) {
                            pattributes.setLocalSrc(AssetGroup.ON_DEVICE ==
                                    assetGroup.getLocationType());
                        } else {
                            pattributes.setLocalSrc(false);
                        }

                        // Make a note of whether this image is a convertible
                        // image asset
                        if (imageAsset instanceof ConvertibleImageAsset) {
                            pattributes.setConvertibleImageAsset(true);
                        } else {
                            pattributes.setConvertibleImageAsset(false);
                        }

                        pattributes.setHeight(StringConvertor.
                                valueOf(imageAsset.getPixelsY()));
                        pattributes.setWidth(StringConvertor.
                                valueOf(imageAsset.getPixelsX()));
                    }
                }
            } else if (urlc != null) {
                url = ContextInternals.constructImageURL(context, urlc);

                // processes and removes any aspect ratio parameter.
                url = processAspectRatioParameter(url,
                        pageContext.getVolantisBean().getAssetTranscoder());

                createFakeAssetURLMapEntry(context, url);
            } else {
                createFakeAssetURLMapEntry(context, url);
            }

            pattributes.setSrc(url);

            protocol.writeImage(pattributes);
        } catch (RepositoryException re) {
            throw new PAPIException(re);
        } catch (ProtocolException e) {

            logger.error("rendering-error",
                    new Object[]{pattributes.getTagName()}, e);
            throw new PAPIException(
                    exceptionLocalizer.format("rendering-error",
                            pattributes.getTagName()),
                    e);
        }

        return SKIP_ELEMENT_BODY;
    }

    /**
     * Processes and removes any aspect ratio parameter present in the given
     * URL string. The aspect ratio is used as follows.
     * <p>
     * If the aspect ratio parameter is present and this ImageElement does not
     * already have a height attribute, the aspect rato and width (see below)
     * is used to calculate the height. This value is then set as the value of
     * the height attribute on this element.
     * </p>
     * <p>
     * If this element had no width attribute to use for the height calculation,
     * the width to use (calculated by MCS) is obtained from the supplied URL
     * string. Note that this parameter is always present. (The actual name of
     * the width parameter is given by the transcoder in use.) In this case,
     * the width parameter's value is also set as the value of this element's
     * width attribute.
     * </p>
     * <p>
     * Finally, the aspect ratio parameter is removed from the URL string, and
     * the modified URL string is returned.
     * </p>
     *
     * @param url        the URL string of interest
     * @param transcoder the transcoder in use. Used to determine the name of
     *                   the width attribute.
     * @return the url with the aspect ratio parameter removed
     */
    private String processAspectRatioParameter(
            String url,
            PluggableAssetTranscoder transcoder) {
        final MarinerURL marinerURL = new MarinerURL(url);
        final Map paramMap = marinerURL.getParameterMap();
        final String[] ratioParamValues =
                (String[]) paramMap.get(MCS_ASPECT_RATIO_PARAM_NAME);
        final String incomingHeightParam = pattributes.getHeight();

        // If there is a non-null ratio, and there is no incoming
        // XDIME-supplied height, then calculate the height using the aspect
        // ratio and the width of this element or width from the url.
        if (ratioParamValues != null && incomingHeightParam == null) {

            final String ratioParam = ratioParamValues[0];

            // Extract the width and height values from the aspect ratio.
            final int colonIndex = ratioParam.indexOf(':');
            final double arw =
                    Double.valueOf(ratioParam.substring(0, colonIndex)).
                            doubleValue();
            final double arh =
                    Double.valueOf(ratioParam.substring(colonIndex + 1)).
                            doubleValue();
            final double aspectRatio = arw / arh;

            final String incomingWidthParam = pattributes.getWidth();

            // If there is no incoming width, use the width parameter from the
            // url. This parameter is always present.
            if (incomingWidthParam == null) {

                final String widthParamName = transcoder.getWidthParameter();
                final String[] widthValues =
                        (String[]) paramMap.get(widthParamName);
                if (widthValues != null) {
                    pattributes.setWidth(widthValues[0]);
                }
            }

            // Calculate the height using the aspect ratio and width.
            final int width =
                    Integer.valueOf(pattributes.getWidth()).intValue();
            final int calcPixelHeight =
                    (int) Math.round(width / aspectRatio);
            pattributes.setHeight(StringConvertor.valueOf(calcPixelHeight));
        }

        // Remove the aspect ratio parameter. If none exists then nothing is
        // removed.
        marinerURL.removeParameter(MCS_ASPECT_RATIO_PARAM_NAME);

        return marinerURL.getExternalForm();
    }

    /**
     * Create a fake asset URL map entry using the context and url.
     *
     * @param context the mariner request context.
     * @param url     the url use to create and add a fake asset URL map
     *                entry.
     */
    private void createFakeAssetURLMapEntry(
            MarinerRequestContext context,
            String url) {
        ApplicationContext ac = ContextInternals.getApplicationContext(context);
        PackageResources pr = ac.getPackageResources();

        if (pr != null) {
            PackagedURLEncoder packagedURLEncoder = ac.getPackagedURLEncoder();

            if (packagedURLEncoder != null) {
                String encoded = packagedURLEncoder.getEncodedURI(url);

                PackageResources.Asset prAsset = new PackageResources.Asset(
                        url, false);

                pr.addAssetURLMapping(encoded, prAsset);
            } else if (logger.isDebugEnabled()) {
                logger.debug("Package resources is not null but the packaged " +
                        "url encoder is null: " + pr);
            }
        }
    }

    // Javadoc inherited from super class.
    protected int elementEndImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttributes)
            throws PAPIException {

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        pattributes.resetAttributes();

        super.elementReset(context);
    }

    protected int calculateWidth() {
        return 100;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Nov-05	10173/1	emma	VBM:2005103107 Forward port: Fixes to correctly apply styles to various selectors

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 05-Jul-05	8813/12	pcameron	VBM:2005061608 Added aspect ratio parameter processing to XDIME-CP

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 25-Apr-05	7781/1	tom	VBM:2005040517 Rollup - rework

 11-Mar-05	7308/5	tom	VBM:2005030702 Added XHTMLSmartClient and support for image sequences

 11-Mar-05	7308/3	tom	VBM:2005030702 Added XHTMLSmartClient and support for image sequences

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 17-Jan-05	6693/1	allan	VBM:2005011403 Remove MPS specific image url parameters

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 18-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 15-Sep-04	5521/1	byron	VBM:2004091406 Multi-Part Mime: Does not handle url and urlc attributes on img tag

 15-Sep-04	5519/3	byron	VBM:2004091406 Multi-Part Mime: Does not handle url and urlc attributes on img tag

 14-Sep-04	5519/1	byron	VBM:2004091406 Multi-Part Mime: Does not handle url and urlc attributes on img tag

 08-Jun-04	4661/1	steve	VBM:2004060309 enable asset URL suffix attribute

 08-Jun-04	4643/1	steve	VBM:2004060309 enable asset URL suffix attribute

 10-May-04	4257/1	geoff	VBM:2004051002 Enhance Menu Support: Integration Bugs: NPE in getPageConnection

 16-Apr-04	3884/4	claire	VBM:2004040712 Fix merge/overlap problems

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/8	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/6	tony	VBM:2004012601 update localisation services

 16-Feb-04	2789/4	tony	VBM:2004012601 add localised logging and exception services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Feb-04	2966/1	ianw	VBM:2004011923 Added mcsi:policy function

 03-Nov-03	1760/1	philws	VBM:2003031710 Port image alt text component reference handling from PROTEUS

 02-Nov-03	1751/1	philws	VBM:2003031710 Permit image alt text to be component reference

 13-Aug-03	958/3	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 31-Jul-03	868/1	mat	VBM:2003070704 Initial work on this task

 ===========================================================================
*/
