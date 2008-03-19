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
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.map.agent.MediaAgent;
import com.volantis.map.agent.Request;
import com.volantis.map.agent.RequestFactory;
import com.volantis.map.agent.impl.DefaultAgentParameters;
import com.volantis.map.agent.impl.DefaultRequestFactory;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ObjectAttribute;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.packagers.PackageResources;
import com.volantis.mcs.runtime.packagers.PackagedURLEncoder;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.utilities.StringConvertor;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * This class is responsible for processing device dependent object types like
 * images, movies. If mcs-transcode parameter is set to true it will send
 * request to Media Access Proxy for transcoding resource to fit device
 * capabilities.
 */
public class DeviceDependentTypeProcessor {
    /*
     * A bunch of device policy constants.
     */
    private static final String DOWNLOADABLE_AUDIO_PREFERENCE_ORDER_POLICY = 
        "downloadable.audio.preference.order";

    private static final String DOWNLOADABLE_AUDIO_MAXSIZE_POLICY  = 
        "downloadable.audio.maxsize";

    private static final String DOWNLOADABLE_AUDIO_X_CODEC_POLICY = 
        "downloadable.audio.{0}.codec";
    
    private static final String DOWNLOADABLE_VIDEO_X_AUDIO_CODEC_POLICY = 
        "downloadeble.video.{0}.audio.codec";

    private static final String DOWNLOADABLE_VIDEO_X_VISUAL_CODEC_POLICY = 
        "downloadable.video.{0}.visual.codec";

    private static final String DOWNLOADABLE_AUDIO_MAXBITRATE_POLICY = 
        "downloadable.audio.maxbitrate";

    private static final String DOWNLOADABLE_VIDEO_AUDIO_MAXBITRATE_POLICY = 
        "downloadable.video.audio.maxbitrate";

    private static final String DOWNLOADABLE_VIDEO_VISUAL_MAXBITRATE_POLICY = 
        "downloadable.video.visual.maxbitrate";

    private static final String DOWNLOADABLE_VIDEO_PIXELSY_POLICY = 
        "downloadable.video.pixelsy";

    private static final String DOWNLOADABLE_VIDEO_PIXELSX_POLICY = 
        "downloadable.video.pixelsx";

    private static final String DOWNLOADABLE_VIDEO_VISUAL_FRAMERATE_POLICY =
        "downloadable.video.visual.framerate";

    private static final String DOWNLOADABLE_VIDEO_MAXSIZE_POLICY = 
        "downloadable.video.maxsize";

    private static final String DOWNLOADABLE_VIDEO_PREFERENCE_ORDER_POLICY = 
        "downloadable.video.preference.order";

    /*
     * A bunch of MediaAgent property name constants.
     * TODO: These should be moved to MediaAccessProxy depot.
     */
    private static final String MAP_VIDEO_VISUAL_CODEC = 
        "VideoVisualCodec";

    private static final String MAP_VIDEO_AUDIO_CODEC = 
        "VideoAudioCodec";

    private static final String MAP_VIDEO_FRAME_RATE = 
        "VideoFrameRate";

    private static final String MAP_VIDEO_WIDTH = 
        "VideoWidth";

    private static final String MAP_VIDEO_HEIGHT = 
        "VideoHeight";

    private static final String MAP_MAX_VIDEO_SIZE = 
        "MaxVideoSize";

    private static final String MAP_MAX_VIDEO_VISUAL_BIT_RATE = 
        "MaxVideoVisualBitRate";

    private static final String MAP_MAX_VIDEO_AUDIO_BIT_RATE = 
        "MaxVideoVisualBitRate";

    private static final String MAP_MAX_AUDIO_SIZE = 
        "MaxAudioSize";

    private static final String MAP_AUDIO_CODEC = 
        "AudioCodec";

    private static final String MAP_MAX_AUDIO_BIT_RATE = 
        "MaxAudioBitRate";
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(VolantisProtocol.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(VolantisProtocol.class);
    

     
    /**
     * Handles processing object element in device dependent If mcs-transcode
     * parameter is set to true it will send request to Media Access Proxy for
     * transcoding resource to fit device capabilities.
     * 
     * @throws XDIMEException
     */
    public void writeObject(XDIMEContextInternal context,
                            ObjectAttribute objectAttribute,
                            OutputBuffer containedContent) throws XDIMEException {

        try {
            VolantisProtocol protocol = 
                ContextInternals.getMarinerPageContext(
                        context.getInitialRequestContext()).getProtocol();
            
            MarinerPageContext pageContext = ContextInternals.
                getMarinerPageContext(context.getInitialRequestContext());
            
            // Determine, whether the transcoding should take place, by
            // processing the value of the MCS_TRANSCODE object parameter.
            Boolean transcode = (Boolean) objectAttribute.getParamMap().
                get(ObjectParameter.MCS_TRANSCODE);
            
            if (transcode != null && transcode.booleanValue()) {
                // Case 1: Transcoding takes place.
                
                // Set the flag indicating, that finalizer is required, since
                // some attributes like "src" will be provided by MediaAgent at
                // later time.
                objectAttribute.setNeedsFinalizer();

                // Write object opening. The finalizer is provided now. 
                protocol.writeOpenObject(objectAttribute);
                
                // Write object content.
                pageContext.getCurrentOutputBuffer().
                    transferContentsFrom(containedContent);
    
                // Write object closure.
                protocol.writeCloseObject(objectAttribute);            
    
                // Send MediaAgent request.
                doMediaAgentRequest(context, objectAttribute);
                        
            } else {
                // Case 2: Transcoding does not take place. Simply open the
                // object, write its content and close the object on protocol.

                // Rewrite the URL with PageURLRewriter first.
                objectAttribute.setSrc(pageContext.getAssetResolver()
                        .rewriteURLWithPageURLRewriter(objectAttribute.getSrc(),
                                PageURLType.OBJECT));

                protocol.writeOpenObject(objectAttribute);
    
                pageContext.getCurrentOutputBuffer().
                    transferContentsFrom(containedContent);
    
                protocol.writeCloseObject(objectAttribute);            
            }
        } catch (ProtocolException e) {
            throw new XDIMEException(exceptionLocalizer.format(
                    "rendering-error", "object"), e);
        }
    }        

    /**
     * Sends MediaAgent request.
     * 
     * @param context
     * @param objectAttribute
     * @throws XDIMEException
     */
    private void doMediaAgentRequest(XDIMEContextInternal context, 
            ObjectAttribute objectAttribute) throws XDIMEException {
        // Send MediaAgent request.
        String srcType = objectAttribute.getSrcType();
        
        if (srcType.startsWith("image/")) {
            doImageRequest(context, objectAttribute);
            
        } else if (srcType.startsWith("video/")) {
            doVideoRequest(context, objectAttribute);
            
        } else if (srcType.startsWith("audio/")) {
            doAudioRequest(context, objectAttribute);
            
        } else if (srcType.equals("application/x-shockwave-flash")) {
            throw new XDIMEException(exceptionLocalizer
                    .format("media-not-transcodable", srcType));
        }        
    }
    
    private void doImageRequest(XDIMEContextInternal context, 
            ObjectAttribute objectAttribute) throws XDIMEException {
        
        try {
            MarinerRequestContext requestContext = context
                    .getInitialRequestContext();

            MarinerPageContext pageContext = ContextInternals
                    .getMarinerPageContext(context.getInitialRequestContext());

            String newUrl = ContextInternals.constructImageURL(requestContext,
                    objectAttribute.getSrc());

            // Set the width and height of the protocol attributes
            // using the aspect ration parameters and the width added
            // to the URL by the transcoder.
            setObjectDimensions(context, newUrl, objectAttribute, pageContext);
            
            // try to resolve against the page. This does nothing if the url is
            // absolute
            URI absoluteURI = new URI(pageContext.getAbsoluteURL(
                new MarinerURL(newUrl), true).toExternalForm());
            // use resovled absolute uri from now on
            newUrl = absoluteURI.toString();


            // Tell the protocol that this is a convertible image
            // asset so we can ignore the height and width we just
            // added!!!
            objectAttribute.setConvertibleImageAsset(true);

            createFakeAssetURLMapEntry(requestContext, newUrl);

            // Create MediaAgent request based on old-style transcoder URI.  
            Request request = pageContext.getMediaAgentRequestFactory()
                    .createRequestFromICSURI(absoluteURI);

            // Create MediaAgent response callback.
            MAPResponseCallback callback = new MAPResponseCallback(
                    objectAttribute,
                    MAPResponseCallback.createURLRewriter(context));

            // Send MediaAgent request.
            MediaAgent mediaAgent = pageContext.getMediaAgent(true);

            mediaAgent.requestURL(request, callback);
            
        } catch (URISyntaxException e) {
            throw new XDIMEException(e);
        } catch (RepositoryException e) {
            throw new XDIMEException(e);
        } catch (MalformedURLException e) {
            throw new XDIMEException(e);
        }
    }
    
    private void doVideoRequest(XDIMEContextInternal context,
            ObjectAttribute objectAttribute) throws XDIMEException {

        MarinerPageContext pageContext = ContextInternals.
            getMarinerPageContext(context.getInitialRequestContext());
        
        InternalDevice device = pageContext.getDevice();
        
        Map inputParameters = new HashMap();

        int maxSize = device
            .getIntegerPolicyValue(DOWNLOADABLE_VIDEO_MAXSIZE_POLICY, 0);
        if (maxSize != 0) {
            inputParameters.put(MAP_MAX_VIDEO_SIZE,
                    Integer.toString(maxSize));
        }
        
        List params = device
            .getCommaSeparatedPolicyValues(
                    DOWNLOADABLE_VIDEO_PREFERENCE_ORDER_POLICY);
        if (!params.isEmpty()) {
            String param = (String) params.get(0);
            Object[] formatParams = new Object[]{param};
            
            String visualCodec = device
                .getPolicyValue(MessageFormat.format(
                        DOWNLOADABLE_VIDEO_X_VISUAL_CODEC_POLICY,
                        formatParams));
            if (visualCodec != null) {
                inputParameters.put(MAP_VIDEO_VISUAL_CODEC,
                        visualCodec);
            }

            String audioCodec = device
                .getPolicyValue(MessageFormat.format(
                        DOWNLOADABLE_VIDEO_X_AUDIO_CODEC_POLICY,
                        formatParams));
            if (audioCodec != null) {
                inputParameters.put(MAP_VIDEO_AUDIO_CODEC,
                        audioCodec);
            }
        }
        
        int frameRate = device
            .getIntegerPolicyValue(DOWNLOADABLE_VIDEO_VISUAL_FRAMERATE_POLICY, 0);
        if (frameRate != 0) {
            inputParameters.put(MAP_VIDEO_FRAME_RATE,
                    Integer.toString(frameRate));
        }
        
        int width = device
            .getIntegerPolicyValue(DOWNLOADABLE_VIDEO_PIXELSX_POLICY,
                device.getPixelsX());    
        if (width != 0) {
            inputParameters.put(MAP_VIDEO_WIDTH,
                    Integer.toString(width));
        }

        int height = device
            .getIntegerPolicyValue(DOWNLOADABLE_VIDEO_PIXELSY_POLICY,
                device.getPixelsY());
        if (height != 0) {
            inputParameters.put(MAP_VIDEO_HEIGHT,
                    Integer.toString(height));
        }
        
        int maxVisualBitRate = device
            .getIntegerPolicyValue(DOWNLOADABLE_VIDEO_VISUAL_MAXBITRATE_POLICY, 0);
        if (maxVisualBitRate != 0) {
            inputParameters.put(MAP_MAX_VIDEO_VISUAL_BIT_RATE,
                    Integer.toString(maxVisualBitRate));
        }
        
        int maxAudioBitRate = device
            .getIntegerPolicyValue(DOWNLOADABLE_VIDEO_AUDIO_MAXBITRATE_POLICY, 0);
        if (maxAudioBitRate != 0) {
            inputParameters.put(MAP_MAX_VIDEO_AUDIO_BIT_RATE, 
                    Integer.toString(maxAudioBitRate));
        }

        try {
            URI absoluteURI = new URI(pageContext.getAbsoluteURL(
                new MarinerURL(objectAttribute.getSrc()), true).toExternalForm());
            // use resovled absolute uri from now on

            RequestFactory factory = pageContext.getMediaAgentRequestFactory();
            Request request = factory.createRequest(
                objectAttribute.getSrcType(), absoluteURI, inputParameters);
        
            MAPResponseCallback callback =
                new MAPResponseCallback(objectAttribute,
                    MAPResponseCallback.createURLRewriter(context));

            MediaAgent mediaAgent = pageContext.getMediaAgent(true);

            mediaAgent.requestURL(request, callback);
        } catch (Exception e) {
            logger.error("invalid-object-src-url", objectAttribute.getSrc());
        }

	}
    
    private void doAudioRequest(XDIMEContextInternal context,
            ObjectAttribute objectAttribute) throws XDIMEException {
        
        MarinerPageContext pageContext = ContextInternals.
            getMarinerPageContext(context.getInitialRequestContext());
        
        InternalDevice device = pageContext.getDevice();
                
        Map inputParameters = new HashMap();
    
        List params = device
            .getCommaSeparatedPolicyValues(
                DOWNLOADABLE_AUDIO_PREFERENCE_ORDER_POLICY);
        if (!params.isEmpty()) {
            String param = (String) params.get(0);
            Object[] formatParams = new Object[]{param};
            
            String visualCodec = device
                    .getPolicyValue(MessageFormat.format(
                            DOWNLOADABLE_AUDIO_X_CODEC_POLICY,
                            formatParams));
            
            if (visualCodec != null) {
                inputParameters.put(MAP_AUDIO_CODEC, visualCodec);
            }
        }

        int maxSize = device
            .getIntegerPolicyValue(DOWNLOADABLE_AUDIO_MAXSIZE_POLICY, 0);
        if (maxSize != 0) {
            inputParameters.put(MAP_MAX_AUDIO_SIZE,
                    Integer.toString(maxSize));
        }
        
        int maxVisualBitRate = device
            .getIntegerPolicyValue(DOWNLOADABLE_AUDIO_MAXBITRATE_POLICY, 0);
        if (maxVisualBitRate != 0) {
            inputParameters.put(MAP_MAX_AUDIO_BIT_RATE,
                    Integer.toString(maxVisualBitRate));
        }

        try {
            URI absoluteURI = new URI(pageContext.getAbsoluteURL(
                new MarinerURL(objectAttribute.getSrc()), true).toExternalForm());

            // use resovled absolute uri from now on
            RequestFactory factory = pageContext.getMediaAgentRequestFactory();
            Request request = factory.createRequest(
                objectAttribute.getSrcType(), absoluteURI, inputParameters);
        
            MAPResponseCallback callback =
                new MAPResponseCallback(objectAttribute,
                    MAPResponseCallback.createURLRewriter(context));
    
            MediaAgent mediaAgent = pageContext.getMediaAgent(true);
    
            mediaAgent.requestURL(request, callback);
        } catch (Exception e) {
            logger.error("invalid-object-src-url", objectAttribute.getSrc());
        }
    }
    
    /**
     * Calculate the width and height for the image.
     * The ratio of width and height are calcualted from the
     * aspect ratio parameters. This is used with the width
     * calcualted from the transcoder.
     *
     * @param context
     * @param url
     */
    private void setObjectDimensions(XDIMEContextInternal context, 
            String url,
            ObjectAttribute imageAttributes, 
            MarinerPageContext pageContext) throws XDIMEException {

        Map parameters = imageAttributes.getParamMap();
        
        final boolean containsHeight = parameters.containsKey(
                ObjectParameter.MCS_ASPECT_RATIO_HEIGHT);
        final boolean containsWidth = parameters.containsKey(
                ObjectParameter.MCS_ASPECT_RATIO_WIDTH);
        if (containsHeight && !containsWidth ||
                !containsHeight && containsWidth) {
            throw new XDIMEException(exceptionLocalizer.format(
                "missing-parameter-both-must-be-set",
                new Object[]{ObjectParameter.MCS_ASPECT_RATIO_HEIGHT,
                             ObjectParameter.MCS_ASPECT_RATIO_WIDTH}));
        }

        // Use the width parameter from the url. This parameter is always
        // present. This is a hack which follows on from the existing
        // implementation in AbstractImageElementImpl which I did not have
        // time to fix.
        // todo: better: get width properly rather than this nasty way.
        // TODO: factor together with AbstractImageElement (partially) ???

        final MarinerURL marinerURL = new MarinerURL(url);
        final Map paramMap = marinerURL.getParameterMap();
        String transcoderWidth = pageContext.getVolantisBean()
                .getAssetTranscoder().getWidthParameter();
        final String[] widthValues = (String[]) paramMap.get(transcoderWidth);
        if (widthValues != null) {
            final String widthString = widthValues[0];
            parameters.put("width", widthString);

            Integer ratioWidth = (Integer) parameters.get(
                    ObjectParameter.MCS_ASPECT_RATIO_WIDTH);
            Integer ratioHeight = (Integer) parameters.get(
                    ObjectParameter.MCS_ASPECT_RATIO_HEIGHT);
            if (ratioWidth != null && ratioHeight != null) {
                double ratio = ratioWidth.doubleValue() /
                        ratioHeight.doubleValue();

                // Calculate the height using the aspect ratio and width.
                final int width = Integer.parseInt(widthString);
                final int height = (int) Math.round(width / ratio);
                parameters.put("height",StringConvertor.valueOf(height));
                if (logger.isDebugEnabled()) {
                    logger.debug("Aspect ratio: " + ratioWidth + ":" +
                            ratioHeight +
                            " -> Dimensions: " + width + ":" + height);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("No aspect ratio detected");
                }
            }
        } else {
            // After the URL has been transcoded, the width parameter
            // should always be present. If not, we don't understand.
            throw new IllegalStateException("Cannot find width parameter " +
                    "on transcoded url " + url);
        }
    }

    /**
     * Create a fake asset URL map entry using the context and url.
     *
     * @param context the mariner request context.
     * @param url     the url use to create and add a fake asset URL map
     *                entry.
     */
    private static void createFakeAssetURLMapEntry(MarinerRequestContext context,
                                            String url) {

        // TODO: factor together with AbstractImageElement

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
}

