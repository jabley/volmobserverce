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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.map.agent.MediaAgent;
import com.volantis.map.agent.Request;
import com.volantis.map.common.param.MutableParameters;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.audio.AudioMetaData;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageMetaData;
import com.volantis.mcs.policies.variants.metadata.PixelDimensionsMetaData;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.policies.variants.video.VideoMetaData;
import com.volantis.mcs.protocols.ObjectAttribute;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.SelectedVariant;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.utilities.StringConvertor;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.synergetics.log.LogDispatcher;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Processor for Device Independent resources. Processor retrieves url to device
 * dependent resource from the best selected variant. It save all retrieved
 * information into OBjectAttribute and pass it to protocol
 */
public class DeviceIndependentTypeProcessor {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory
            .createLogger(DeviceIndependentTypeProcessor.class);

    private MarinerPageContext pageContext;

    /**
     * This method retrieves best variant and get url to device dependent
     * resource
     *
     * @param context
     *            XDIMEContextInternal
     * @param attributes
     *            protocol ObjectAttribute which contains all attributes and
     *            parameters set in XDIME
     * @param bodyBuffer
     *            OutputBuffer which contains body content processed object
     *            element's
     * @throws ProtocolException
     *             is passed from protocol if it is occured there
     */
    public void writeObject(XDIMEContextInternal context,
            ObjectAttribute attributes, OutputBuffer bodyBuffer)
            throws XDIMEException {

        String src = attributes.getSrc();

        pageContext = ContextInternals.getMarinerPageContext(context
                .getInitialRequestContext());

        // select the best variant for given policy in src
        SelectedVariant selectedVariant = selectVariant(src);
        if(selectedVariant == null) {
        	throw new XDIMEException("best selected variant not accessible");
        }

        AssetResolver assetResolver = pageContext.getAssetResolver();
                
        // The external link rendered for device independent resource will have 
        // correct URL if used device support encoding from selected variant 
        // otherwise returned variant will be null because selected variant is 
        // filtered by PolicyVariantSelector#filter method        
        Variant variant = selectedVariant.getVariant();
        VariantType variantType = null;

        if(variant != null) {
            variantType = variant.getVariantType();
        }

        // for MTXT resource
        if (variantType == VariantType.TEXT) {
            PolicyReferenceResolver resolver = pageContext
                    .getPolicyReferenceResolver();
            TextAssetReference textReference = resolver
                    .resolveUnquotedTextExpression(src);

            final String text = textReference.getText(TextEncoding.PLAIN);
            if (text != null) {
                // We have found the text, so let's try and write it out to
                // attributes
                attributes.setTextContainer(text);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("No text exists for text asset  " + src);
                }
            }
            // remove src pointed at text component
            attributes.setSrc(null);

            // remove srcType which contains list of device independent mime
            // types
            attributes.setSrcType(null);
        }

        // retrieve url only for IMAGE, AUDIO and VIDEO resource so far
        String url = assetResolver.retrieveVariantURLAsString(selectedVariant);

        // get dimensions for image and video and save it to params into
        // attributes
        if (variantType == VariantType.IMAGE
                || variantType == VariantType.VIDEO) {

            PixelDimensionsMetaData dimMetaData =
                (PixelDimensionsMetaData) selectedVariant
                    .getVariant().getMetaData();
            // get the Map of parameters
            Map params = attributes.getParamMap();

            // save height and width in pixels unit to object's parameter map
            if (dimMetaData.getHeight() != 0) {
                params.put("height", StringConvertor.valueOf(dimMetaData
                        .getHeight()));
            }
            if (dimMetaData.getWidth() != 0) {
                params.put("width", StringConvertor.valueOf(dimMetaData
                        .getWidth()));
            }
        }

        // convert image if needed
        if (variantType == VariantType.IMAGE) {
            ImageMetaData imageMetaData = (ImageMetaData) selectedVariant
                    .getVariant().getMetaData();

            // image must be convert
            if (url != null
                    && imageMetaData.getConversionMode() ==
                        ImageConversionMode.ALWAYS_CONVERT) {
                url = assetResolver.rewriteURLWithPageURLRewriter(url,
                        PageURLType.IMAGE);
            }
        }

        if (url != null) {
            // new mime types for device dependent resources
            String mimeTypes = "";
            Iterator encodingIterator = null;
            // for each variant type get encoding and save its all mime types
            // into attributes' srcType
            if (variantType == VariantType.VIDEO) {
                VideoMetaData metaData = (VideoMetaData) variant.getMetaData();
                encodingIterator = metaData.getVideoEncoding().mimeTypes();
            } else if (variantType == VariantType.AUDIO) {
                AudioMetaData metaData = (AudioMetaData) variant.getMetaData();
                encodingIterator = metaData.getAudioEncoding().mimeTypes();
            } else if (variantType == VariantType.IMAGE) {
                // This is incorrect if the asset is marked as
                // <conversion-mode>always</conversion-mode>
                // as we do not know what MAP will convert it to.
                ImageMetaData metaData = (ImageMetaData) variant.getMetaData();
                encodingIterator = metaData.getImageEncoding().mimeTypes();
            }

            int counter = 0;
            while (encodingIterator != null && encodingIterator.hasNext()) {
                if (counter > 0) {
                    mimeTypes += ",";
                }
                mimeTypes += (String) encodingIterator.next();
                counter++;
            }

            attributes.setSrc(url);
            attributes.setSrcType(mimeTypes);
            // This method will reset the src and possibly the type if
            // necessary. Otherwise it leaves them unchanged.
            resetAsMapURL(context, variant, attributes);

        } else {
            // if url cannot be processed remove values src and srcType
            // attribute
            attributes.setSrc(null);
            // remove srcType which contains list of device independent mime
            // types
            attributes.setSrcType(null);
        }

        VolantisProtocol protocol = ContextInternals.getMarinerPageContext(
                context.getInitialRequestContext()).getProtocol();

        try {
			protocol.writeOpenObject(attributes);
	        // copy containedContent to current buffer
	        pageContext.getCurrentOutputBuffer().transferContentsFrom(bodyBuffer);
	        protocol.writeCloseObject(attributes);
		} catch (ProtocolException e) {
			throw new XDIMEException(e);
		}
    }

    /**
     * Returns true if the url should be changed to be a url to the Media
     * Access Proxy.
     * @return
     */
    private void resetAsMapURL(XDIMEContextInternal context,
                                  Variant variant,
                                  ObjectAttribute objectAttribute)
        throws XDIMEException {

        // if this is an image variant and it is marked as transcodable
        // then go for it.
        if (variant.getVariantType() == VariantType.IMAGE) {
            ImageMetaData metadata = (ImageMetaData) variant.getMetaData();
            if (ImageConversionMode.ALWAYS_CONVERT ==
                metadata.getConversionMode()) {
                // Set the flag indicating, that finalizer is required, since
                // some attributes like "src" will be provided by MediaAgent at
                // later time.
                objectAttribute.setNeedsFinalizer();

                URI uri = null;
                try {
                    uri = new URI(objectAttribute.getSrc());
                }catch(URISyntaxException e){
                    throw new XDIMEException(e);
                }

                // Create MediaAgent request based on old-style transcoder URI.
                Request request = pageContext.getMediaAgentRequestFactory()
                    .createRequestFromICSURI(uri);
                setHostAndPort(request, uri);
                // Create MediaAgent response callback.
                MAPResponseCallback callback = new MAPResponseCallback(
                    objectAttribute,
                    MAPResponseCallback.createURLRewriter(context));

                // Send MediaAgent request.
                MediaAgent mediaAgent = pageContext.getMediaAgent(true);

                mediaAgent.requestURL(request, callback);
            }
        }
    }

    /**
     * If necessary this sets the host and port parameters for the MAP agent
     * request. This allows map to make a request to an abolute URL that was
     * defined as a relative one.
     * 
     * @param request
     * @param uri
     * @throws XDIMEException
     */
    private void setHostAndPort(Request request, URI uri) throws XDIMEException {
        MutableParameters params = (MutableParameters) request.getInputParams();
        if (!params.containsName(ParameterNames.HOST)) {
            try {
                // generate an absolute URL to the asset. This allows us to
                // hijack the host and port.
                URL fakeUrl = pageContext.getAbsoluteURL(
                    new MarinerURL(uri.toString()), true);
                params.setParameterValue(ParameterNames.HOST, fakeUrl.getHost());
                int port = fakeUrl.getPort();
                port = port < 0 ? fakeUrl.getDefaultPort(): port;
                if (port >= 0) {
                    params.setParameterValue(
                        ParameterNames.HOST_PORT, Integer.toString(port));
                }
                String protocol = fakeUrl.getProtocol();
                if (protocol != null && !"".equals(protocol)) {
                    params.setParameterValue(ParameterNames.HOST_PROTOCOL, protocol);
                }
            } catch (MalformedURLException e) {
                throw new XDIMEException(e);
            }
        }
    }

    /**
     * Select the best variant and return it.
     *
     * @param src uri to the device independent resource
     * @return best variant from MCS policy
     */
    private SelectedVariant selectVariant(String src) {

        PolicyReferenceResolver resolver = pageContext
                .getPolicyReferenceResolver();
        RuntimePolicyReference reference = null;

        reference = resolver.resolveUnquotedPolicyExpression(src, null);
        return pageContext.getAssetResolver()
                .selectBestVariant(reference, null);
    }
}
