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
 * $Header: /src/voyager/com/volantis/mcs/papi/AudioElement.java,v 1.7 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Dec-02    Mat             VBM:2002112212 - Created as a generic audio tag
 * 30-Jan-03    Mat             VBM:2003013015 - Use the PAPI attributes to
 *                              get the altText.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 16-Apr-03    Geoff           VBM:2003041603 - Add catch clause for
 *                              ProtocolException that throws PAPIException.
 * 23-Apr-03    Steve           VBM:2003041606 - Override hasMixedContent() to
 *                              return false
 * 19-May-03    Chris W         VBM:2003051902 - hasMixedContent() made package
 *                              protected.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.AudioAttributes;
import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.audio.AudioEncoding;
import com.volantis.mcs.policies.variants.audio.AudioMetaData;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.policies.variants.metadata.EncodingCollectionFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.SelectedVariant;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The Audio element.
 */
public class AudioElementImpl
        extends BlockElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(AudioElementImpl.class);

    private static final EncodingCollection ENCODING_COLLECTION =
            EncodingCollectionFactory.getDefaultInstance().
                createEncodingCollection(AudioEncoding.AMR);

    /**
     * The attributes to pass to the protocol methods.
     */
    protected com.volantis.mcs.protocols.AudioAttributes aAttributes;

    /**
     * Create a new <code>DynamicVisualElement</code>.
     */
    public AudioElementImpl() {
        aAttributes = new com.volantis.mcs.protocols.AudioAttributes();
    }

    // Javadoc inherited.
    MCSAttributes getMCSAttributes() {
        return aAttributes;
    }

    // Javadoc inherited from super class.
    protected int elementStartImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttribute)
            throws PAPIException {
        return SKIP_ELEMENT_BODY;
    }

    /**
     * Populate the protocol attributes and call the protocol method to write the
     * the audio element with those attributes.
     *
     * @param blockAttributes The PAPI attributes used to populate the protocol
     * attributes.
     * @param protocol The protocol used to write the element.
     * @param pageContext The PageContext that contains the AudioAsset.
     */
    void doAMR(
            SelectedVariant selected,
            BlockAttributes blockAttributes,
            VolantisProtocol protocol,
            MarinerPageContext pageContext)
            throws PAPIException {

        AudioAttributes attributes =
                (AudioAttributes) blockAttributes;

        // Nothing to do if the source cannot be generated.
        AssetResolver resolver = pageContext.getAssetResolver();
        String src = resolver.computeURLAsString(selected);
        if (src == null) {
            return;
        }

        aAttributes.setId(attributes.getId());
        aAttributes.setTitle(attributes.getTitle());
        aAttributes.setAssetURLSuffix(attributes.getAssetURLsuffix());
        aAttributes.setSrc(src);

        aAttributes.setTagName("audio");

        // Add any event attributes.
        PAPIInternals.initialiseGeneralEventAttributes(pageContext,
                attributes,
                aAttributes);
        protocol.writeAudio(aAttributes);
    }


    // Javadoc inherited from super class.
    protected int elementEndImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttributes)
            throws PAPIException {

        if (logger.isDebugEnabled()) {
            logger.debug("Volantis AudioElement elementEndImpl starts");
        }

        AudioAttributes attributes =
                (AudioAttributes) blockAttributes;

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        String audioComponentName = attributes.getSrc();

        if (audioComponentName != null) {
            PolicyReferenceResolver resolver =
                    pageContext.getPolicyReferenceResolver();

            // Resolve the expression to a reference.
            RuntimePolicyReference reference =
                    resolver.resolveUnquotedPolicyExpression(
                            audioComponentName, PolicyType.AUDIO);

            AssetResolver assetResolver = pageContext.getAssetResolver();

            // Only supports AMR encoding at the moment.
            SelectedVariant selected = assetResolver.selectBestVariant(
                    reference, ENCODING_COLLECTION);
            if (selected != null) {
                usePolicy(pageContext, selected, attributes);
            }
        }
        return CONTINUE_PROCESSING;
    }

    private void usePolicy(
            MarinerPageContext pageContext,
                           SelectedVariant selected,
                           AudioAttributes attributes)

            throws PAPIException {

        VolantisProtocol protocol = pageContext.getProtocol();

        // Request the best asset.
        //
        // We used to call the overload that finds the best asset
        // without providing an explicit encoding, but unfortunately
        // that overload is broken. See VBM:2005040116.
        //
        // Instead we now call the other overload which specifies an
        // explicit encoding as we know we only support AMR at this
        // stage anyway. See VBM:2005040106.

        ActivatedVariablePolicy policy = selected.getPolicy();
        Variant variant = selected.getVariant();
        if (variant == null) {
            MediaUtilities.tryAltText(attributes, pageContext, policy,
                    attributes.getAlt(), aAttributes.getStyles());
        } else {

            AudioMetaData audio = (AudioMetaData) variant.getMetaData();

            // Currently this audio tag is only supported by the MMS_SMIL_2_0
            // protocol.  This only suports AMR so we need only deal with
            // assets that have AMR encoding.
            AudioEncoding encoding = audio.getAudioEncoding();
            if (encoding == AudioEncoding.AMR) {
                doAMR(selected, attributes, protocol, pageContext);
                if (logger.isDebugEnabled()) {
                    logger.debug("Done a Audio tag");
                }
            } else {
                logger.warn("unsupported-asset-encoding", new Object[]{
                    policy.getName(), pageContext.getDeviceName(),
                    encoding});
            }
        }
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        aAttributes.resetAttributes();


        super.elementReset(context);
    }

    // Javadoc inherited from super class.
    boolean hasMixedContent() {
        return false;
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

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 04-Apr-05	7539/1	geoff	VBM:2005040106 MPS ignores audio elements when MMS is generated

 01-Apr-05	7535/1	geoff	VBM:2005040106 MPS ignores audio elements when MMS is generated

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 18-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 11-Aug-04	5139/1	geoff	VBM:2004080311 Implement Null Assets: ObjectSelectionPolicys

 08-Jun-04	4661/1	steve	VBM:2004060309 enable asset URL suffix attribute

 08-Jun-04	4643/1	steve	VBM:2004060309 enable asset URL suffix attribute

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 19-Feb-04	2789/7	tony	VBM:2004012601 rework changes

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	958/3	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 31-Jul-03	868/1	mat	VBM:2003070704 Initial work on this task

 ===========================================================================
*/
