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
package com.volantis.map.ics.imageprocessor.parameters;


import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.common.param.MutableParameters;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.configuration.Configuration;
import com.volantis.map.ics.configuration.ConfigurationUtilities;
import com.volantis.map.ics.configuration.DitherMode;
import com.volantis.map.ics.configuration.ImageConstants;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.osgi.boot.BootConstants;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.path.Path;
import com.volantis.synergetics.url.URLIntrospector;
import org.osgi.service.component.ComponentContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Builds a {@link com.volantis.map.common.param.Parameters} instance from an
 * old style ICS URL.
 * <p>
 * <em>This class is thread safe</em>
 */
public final class ICSParamBuilder extends AbstractParamBuilder {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(ICSParamBuilder.class);

     /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
             LocalizationFactory.createLogger(ICSParamBuilder.class);

    private static final int[] DEF_IMG_PRESERVE = {
        ImageConstants.NO_CLIP_LEFT, ImageConstants.NO_CLIP_RIGHT};

    /**
     * Stores the {@link ParamSetter} commands
     */
    private Map paramSetters;

    /**
     * Constructor to creaet new instance
     */
    public ICSParamBuilder() {
        initialiseParameterSetters();
    }

    /**
     * Populates the given MutableParameters instance based on the given
     * URI request
     *
     * @param request the request URL
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    public void build(URI request, MutableParameters params)
            throws ParameterBuilderException {
        URLIntrospector uri = new URLIntrospector(request.toString());
        Enumeration e = uri.getParameterNames();
        while (e.hasMoreElements()) {
            String paramName = (String) e.nextElement();
            ParamSetter command =
                    (ParamSetter) paramSetters.get(paramName);
            if (command != null) {
                command.execute(uri.getParameterValue(paramName),
                                params);
            } else {
                // don't know how to process this parameter - log a warning
                LOGGER.warn("ics-param-unkown",
                            new String[] {paramName, request.toString()});
            }
        }
        // we need to process the information from the URL path (image rule,
        // and image source)
        processRequest(request, params);
    }

    /**
     * Populates the given MutableParameters instance based on the given
     * Configuration.
     *
     * @param configuration the request URL
     * @param params the MutableParameters instance to populate
     * @throws ParameterBuilderException if an error occurs
     */
    public void overlayConfigutionParameters(Configuration configuration,
                                             MutableParameters params,
                                             ComponentContext context)
            throws ParameterBuilderException {

        setJPegFormat(configuration.getJpegMode(), params);
        setGifEnabled(configuration.isGifEnabled(), params);
        setIsNativeMode(configuration.isNativeMode(), params);
        setImgHost(configuration.getHost(), params);
        setImgPort(configuration.getHostPort(), params);
        setImgProtocol(configuration.getHostProtocol(), params);
        setMaxRedirect(configuration.getMaxRedirects(), params);
        setProxyHost(configuration.getProxy(), params);
        setProxyPort(configuration.getProxyPort(), params);
        setUseInMemoryIOCache(configuration.getUseInMemoryIOCache(), params);
        setRepositoryURL(configuration.getRepositoryURL(), params);
        setScaleMode(configuration.getScaleMode(), params);
        setScaleLarger(configuration.canScaleLarger(), params);
        setMinJPegQuality(configuration.getMinimumJPEGQuality(), params);
        setMinBitDepth(configuration.getMinimumBitDepth(), params);
        setWaterMarkURL(configuration.getWatermarkURL(), params);

        DitherMode defaultDither;
        //noinspection UnusedCatchParameter
        try {
            defaultDither = DitherMode.deserialize(
                    params.getParameterValue(
                            ParameterNames.DEFAULT_DITHER_MODE));
        } catch (MissingParameterException e) {
            defaultDither = DitherMode.FLOYD;
        }
        setDitherModeForBitDepth(configuration.getDitherMode(1, defaultDither),
                                 1,
                                 params);
        setDitherModeForBitDepth(configuration.getDitherMode(2, defaultDither),
                                 2,
                                 params);
        setDitherModeForBitDepth(configuration.getDitherMode(4, defaultDither),
                                 4,
                                 params);
        setDitherModeForBitDepth(configuration.getDitherMode(8, defaultDither),
                                 8,
                                 params);
        setDitherModeForBitDepth(configuration.getDitherMode(16, defaultDither),
                                 16,
                                 params);


        resolveSourceURI(params, context);

    }

    /**
     * This specifies the mode in which a JPEG image will be rendered. Valid
     * values are {@link ImageConstants#JPEG_BASELINE } or
     * {@link ImageConstants#JPEG_PROGRESSIVE}
     *
     * @param mode the mode to set
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setJPegFormat(int mode,
                               MutableParameters params)
            throws ParameterBuilderException {
        ConfigurationUtilities.assertJPegMode(mode);
        if (!params.containsName(ParameterNames.JPEG_MODE)) {
            setIntParam(ParameterNames.JPEG_MODE, mode, params);
        }
    }

    /**
     * Specifies if GIF is supported as an input image type.
     *
     * @param enabled true if GIF is supported
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setGifEnabled(boolean enabled,
                               MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.GIF_ENABLED)) {
            setBooleanParam(ParameterNames.GIF_ENABLED, enabled, params);
        }

    }

    /**
     * Sets wether we are operating in native or transforce mode
     *
     * @param isNative true iff operating in native mode
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setIsNativeMode(boolean isNative,
                                 MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.NATIVE_MODE)) {
            setBooleanParam(ParameterNames.NATIVE_MODE, isNative, params);
        }
    }

    /**
     * Specifies the name of the host which is hosting the images. Supplying ""
     * as host name sets the host to null.
     *
     * @param host the image host
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setImgHost(String host, MutableParameters params)
            throws ParameterBuilderException {

        if (!"".equals(host) && !params.containsName(ParameterNames.HOST) ) {
            setStringParam(ParameterNames.HOST, host, params);
        }
    }

    /**
     * Specifies the port of the host which is hosting the images.
     *
     * @param port the port
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setImgPort(int port, MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.HOST_PORT)) {
            setIntParam(ParameterNames.HOST_PORT, port, params);
        }
    }

    /**
     * Specifies the port of the host which is hosting the images.
     *
     * @param protocol  the protocol used to talk to the source
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setImgProtocol(String protocol, MutableParameters params)
        throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.HOST_PROTOCOL)) {
            setStringParam(ParameterNames.HOST_PROTOCOL, protocol, params);
        }
    }

    /**
     * Sets the max number of redirects between hosts to be followed
     *
     * @param maxRedirects max number of redirects
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setMaxRedirect(int maxRedirects, MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.MAXIMUM_REDIRECTS)) {
            setIntParam(ParameterNames.MAXIMUM_REDIRECTS, maxRedirects, params);
        }
    }

    /**
     * Specifies the name of the proxy host. Supplying "" as proxy name sets
     * the host to null.
     *
     * @param host the proxy host
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setProxyHost(String host, MutableParameters params)
            throws ParameterBuilderException {
        if (!"".equals(host) &&
                !params.containsName(ParameterNames.PROXY_NAME)) {
            setStringParam(ParameterNames.PROXY_NAME, host, params);
        }
    }

    /**
     * Specifies the port of the proxy host
     *
     * @param port the port number of the proxy host
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setProxyPort(int port, MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.PROXY_PORT)) {
            setIntParam(ParameterNames.PROXY_PORT, port, params);
        }
    }

    /**
     * Specifies whether to use an in-memory IO Image IO cache or a file based
     * image cache.
     *
     * @param use true iff we should us an in-memory IO chace
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setUseInMemoryIOCache(boolean use, MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.IN_MEMORY_IO_CACHE)) {
            setBooleanParam(ParameterNames.IN_MEMORY_IO_CACHE, use, params);
        }
    }

    /**
     * Sets the parameter that specifies the location of the Device Repository
     *
     * @param url the device repository URL
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setRepositoryURL(String url, MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.DEVICE_REPOSITORY_URL)) {
            setStringParam(ParameterNames.DEVICE_REPOSITORY_URL, url, params);
        }
    }

    /**
     * This specifies the method of scaling used . This can take
     * the values {@link ImageConstants#SCALE_MODE_NEAREST},
     * {@link ImageConstants#SCALE_MODE_BILINEAR} or
     * {@link ImageConstants#SCALE_MODE_BICUBIC}.
     *
     * @param mode the mode to use
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setScaleMode(int mode, MutableParameters params)
            throws ParameterBuilderException {
        ConfigurationUtilities.assertScaleMode(mode);
        if (!params.containsName(ParameterNames.SCALE_MODE)) {
            setIntParam(ParameterNames.SCALE_MODE, mode, params);
        }
    }

    /**
     * This specifies if a transcoded image can be scaled to larger than the
     * input image. If set to true then the image will be scaled to the
     * specified size. If set to false then the image will be scaled to the
     * same size as the input image.
     *
     * @param scaleLarger true iff the transcoded image can be scaled larger
     * than the input image
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setScaleLarger(boolean scaleLarger, MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.SCALE_LARGER)) {
            setBooleanParam(ParameterNames.SCALE_LARGER, scaleLarger, params);
        }
    }

    /**
     * This specifies the minimum allowable quality as a result of lossy
     * compression that will be considered when trying to restrict the size of
     * the output image before a pixel reduction strategy will be considered.
     * The value ranges from 0 to 100 with 100 being no loss in image quality.
     *
     * @param minQuality the minimum quality
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setMinJPegQuality(int minQuality, MutableParameters params)
            throws ParameterBuilderException {
        ConfigurationUtilities.assertMinimumJPegQuality(minQuality);
        if (!params.containsName(ParameterNames.MINIMUM_JPEG_QUALITY)) {
            setIntParam(ParameterNames.MINIMUM_JPEG_QUALITY, minQuality, params);
        }
    }

    /**
     * This specifies the minimum allowable palette size as a result of lossy
     * compresion on indexed (palette-based) file formats that will be
     * considered when trying to restrict the size of the output image before a
     * pixel reduction strategy will be considered. The value may be 1, 2, 4 or
     * 8. If the value is greater than the bit depth of the image then the
     * value is reduced to the bit depth.
     *
     * @param minBitDepth the min bit depth
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setMinBitDepth(int minBitDepth, MutableParameters params)
            throws ParameterBuilderException {

        ConfigurationUtilities.assertMinimumBitDepth(minBitDepth);
        if (!params.containsName(ParameterNames.MINIMUM_BIT_DEPTH)) {
            setIntParam(ParameterNames.MINIMUM_BIT_DEPTH, minBitDepth, params);
        }
    }

    /**
     * A url to a image that is to be used as a watermark
     *
     * @param url the watermark url
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setWaterMarkURL(String url, MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.WATERMARK_URL)) {
            setStringParam(ParameterNames.WATERMARK_URL, url, params);
        }
    }

    /**
     * Sets the default mode to use for dithering.
     *
     * @param mode the mode to use
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setDefaultDitherMode(DitherMode mode,
                                      MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.DEFAULT_DITHER_MODE)) {
            setStringParam(ParameterNames.DEFAULT_DITHER_MODE,
                           mode.toString(),
                           params);
        }
    }

    /**
     * Sets the dither mode for a given bit depth
     *
     * @param mode the diter mode
     * @param bitDepth bit depth
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setDitherModeForBitDepth(DitherMode mode,
                                          int bitDepth,
                                          MutableParameters params)
            throws ParameterBuilderException {
        String param;
        switch (bitDepth) {
            case 1:
                param = ParameterNames.DITHER_MODE_1_BIT;
                break;
            case 2:
                param = ParameterNames.DITHER_MODE_2_BIT;
                break;
            case 4:
                param = ParameterNames.DITHER_MODE_4_BIT;
                break;
            case 8:
                param = ParameterNames.DITHER_MODE_8_BIT;
                break;
            case 16:
                param = ParameterNames.DITHER_MODE_16_BIT;
                break;
            default:
                throw new ParameterBuilderException(
                        EXCEPTION_LOCALIZER.format(
                                "dither-bit-depth-not-supported",
                                new String[] {String.valueOf(bitDepth),
                                              mode.toString()}));


        }
        if (!params.containsName(param)) {
            setStringParam(param, mode.toString(), params);
        }
    }

    /**
     * Set the width that the image should be scaled to or -1 if it should be
     * ignored.
     *
     * @param width the width
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setImageWidth(int width, MutableParameters params)
            throws ParameterBuilderException {
        if (width < -1) {
            throw new ParameterBuilderException("Invalid image width '" +
                                                width +
                                                "'. Value must be >= -1");
        }
        if (!params.containsName(ParameterNames.IMAGE_WIDTH)) {
            setIntParam(ParameterNames.IMAGE_WIDTH, width, params);
        }
    }

    /**
     * Set the number of bytes that the image must not exceed or -1 if it
     * should be ignored.
     *
     * @param size the size
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setMaxImageSize(int size, MutableParameters params)
            throws ParameterBuilderException {
        if (size < -1) {
            throw new ParameterBuilderException("Invalid max image size '" +
                                                size +
                                                "'. Value must be >= -1");
        }
        if (!params.containsName(ParameterNames.MAX_IMAGE_SIZE)) {
            setIntParam(ParameterNames.MAX_IMAGE_SIZE, size, params);
        }
    }

    /**
     * This sets the X left bound of the image preserved area
     *
     * @param left x left bound
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setPreserveXLeft(int left, MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.PRESERVE_X_LEFT)) {
            setIntParam(ParameterNames.PRESERVE_X_LEFT,
                        left,
                        params);
        }
    }

    /**
     * This sets the X right bound of the image preserved area
     *
     * @param right X right bound
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setPreserveXRight(int right, MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.PRESERVE_X_RIGHT)) {
            setIntParam(ParameterNames.PRESERVE_X_RIGHT,
                        right,
                        params);
        }
    }

    /**
     * set left x image coordinate
     *
     * @param left left x image coordinate
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setImgLeftX(String left, MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.LEFT_X)) {
            setStringParam(ParameterNames.LEFT_X, left, params);
        }
    }

    /**
     * set right x image coordinate
     *
     * @param right right x image coordinate
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setImgRightX(String right, MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.RIGHT_X)) {
            setStringParam(ParameterNames.RIGHT_X, right, params);
        }
    }

    /**
     * set top y image coordinate
     *
     * @param top top y image coordinate
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setImgTopY(String top, MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.TOP_Y)) {
            setStringParam(ParameterNames.TOP_Y, top, params);
        }
    }

    /**
     * set bottom y image coordinate
     *
     * @param bottom bottom y image coordinate
     * @param params the Parameters that are being built
     * @throws ParameterBuilderException if an error occurs
     */
    private void setImgBottomY(String bottom, MutableParameters params)
            throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.BOTTOM_Y)) {
            setStringParam(ParameterNames.BOTTOM_Y, bottom, params);
        }
    }

    private void processRequest(URI request, MutableParameters params)
            throws ParameterBuilderException {
        Path requestPath = Path.parse(request.getRawPath());
        String rule = getRule(requestPath);
        if (rule == null || "".equals(rule)) {
            throw new ParameterBuilderException(
                    EXCEPTION_LOCALIZER.format("conversion-rule-not-set",
                                               request.toString()));
        }
        setStringParam(ParameterNames.DESTINATION_FORMAT_RULE, rule, params);

        // store the server relative path from the request away. This will be
        // resolved to an absolute URL later.
        String relativeSource =
                removeRuleFromRequest(requestPath).asRelativeString();
        setStringParam(ParameterNames.ICS_FRAGMENT_URL, relativeSource, params);

        if (request.isAbsolute()) {
            setStringParam(ParameterNames.HOST, request.getHost(), params);
            setIntParam(ParameterNames.HOST_PORT, request.getPort(), params);
            setStringParam(ParameterNames.HOST_PROTOCOL, request.getScheme(), params);

        }

    }

    /**
     * Remove the "rule" from the request url.
     *
     * @param requestPath the request path from which the rule should be removed
     * @return the request path with the rule removed.
     * @throws ParameterBuilderException
     */
    private static Path removeRuleFromRequest(Path requestPath)
            throws ParameterBuilderException {

        int fragments = requestPath.getNumberOfFragments();
        if (fragments < 2) {
            throw new ParameterBuilderException(EXCEPTION_LOCALIZER.format("invalid-request", requestPath));
        }
        Path p1 = new Path(requestPath, 0, fragments - 2);
        String name = requestPath.getFragment(fragments - 1);
        p1 = p1.resolve(name);
        return p1;
    }

    /**
     * Build the URL required to obtain the source data.
     * @param params
     * @throws ParameterBuilderException
     */
    private static void resolveSourceURI(MutableParameters params,
                                         ComponentContext context)
        throws ParameterBuilderException {
        if (!params.containsName(ParameterNames.SOURCE_URL)) {
            URI sourceURL = null;
            String host = null;
            String relativeSource = null;
            int port = 80;
            String protocol = null;

            try {
                if (params.containsName(ParameterNames.HOST)) {
                    host = params.getParameterValue(ParameterNames.HOST);
                }
                if (params.containsName(ParameterNames.HOST_PORT)) {
                    port = params.getInteger(ParameterNames.HOST_PORT);
                }
                if(params.containsName(ParameterNames.HOST_PROTOCOL)) {
                    protocol = params.getParameterValue(
                        ParameterNames.HOST_PROTOCOL);
                }
                relativeSource = params.getParameterValue(
                    ParameterNames.ICS_FRAGMENT_URL);
            } catch (MissingParameterException e) {
                throw new ParameterBuilderException(e);
            }

            if (null != host && !"".equals(host)) {
                try {
                    sourceURL = new URI(protocol, null, host,
                                        port, "/"+relativeSource, null, null);
                } catch (URISyntaxException e) {
                    throw new ParameterBuilderException(e);
                }

            } else {
                String configArea = context.getBundleContext().
                    getProperty(BootConstants.CONTEXT_AREA);
                Path configPath = Path.parse(configArea);
                configPath = configPath.resolve(relativeSource);

                try {
                    sourceURL = new URI("file", null,
                                        configPath.asAbsoluteString(), null);
                } catch (URISyntaxException e) {
                    throw new ParameterBuilderException(e);
                }
            }

            params.setParameterValue(ParameterNames.SOURCE_URL,
                                     sourceURL.toString());
        }
    }

    /**
     * Extract the output rule from the URI. This is from the section
     * immediately before the filename.
     *
     * @param requestPath the servlets requestPath
     * @return the rule name.
     */
    private static String getRule(Path requestPath)
        throws ParameterBuilderException {

        int fragments = requestPath.getNumberOfFragments();
        if (fragments < 2) {
            throw new ParameterBuilderException(
                EXCEPTION_LOCALIZER.format("invalid-request", requestPath));
        }

        return requestPath.getFragment(fragments - 2);
    }

    /**
     * Adds the various {@link ParamSetter} command to the paramSetter map.
     * These will be keyed on the parameters that are allowed on the ICS
     * request URI's.
     */
    private void initialiseParameterSetters() {

        HashMap tmp =  new HashMap();

        tmp.put("v.imgHost", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setImgHost(value, params);
            }
        });

        tmp.put("v.imgPort", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setImgPort(Integer.valueOf(value).intValue(), params);
            }
        });

        tmp.put("v.imgProtocol", new ParamSetter() {

            public void execute(String value, MutableParameters params)
            throws ParameterBuilderException {
                setImgProtocol(value, params);
            }
        });

        tmp.put("v.scaleMode", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setScaleMode(ConfigurationUtilities.deserializeScaleMode(value),
                             params);
            }
        });

        tmp.put("v.scaleLarger", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setScaleLarger(Boolean.valueOf(value).booleanValue(), params);
            }
        });


        tmp.put("v.qualityMin", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setMinJPegQuality(Integer.valueOf(value).intValue(), params);
            }
        });

        tmp.put("v.paletteMin", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setMinBitDepth(Integer.valueOf(value).intValue(), params);
            }
        });

        tmp.put("v.ditherMode", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setDefaultDitherMode(DitherMode.deserialize(value), params);
            }
        });


        tmp.put("v.jpegFormat", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setJPegFormat(ConfigurationUtilities.deserializeJpegMode(value),
                              params);
            }
        });


        tmp.put("v.width", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setImageWidth(Integer.valueOf(value).intValue(), params);
            }
        });

        tmp.put("v.maxSize", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setMaxImageSize(Integer.valueOf(value).intValue(), params);
            }
        });

        tmp.put("v.gifEnabled", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setGifEnabled(Boolean.valueOf(value).booleanValue(), params);
            }
        });

        tmp.put("v.p", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {

                int[] args = ConfigurationUtilities.listToIntArray(
                        value, DEF_IMG_PRESERVE);
                if (args[1] != ImageConstants.NO_CLIP_RIGHT &&
                    args[1] < args[0]) {
                    throw new IllegalArgumentException(
                            EXCEPTION_LOCALIZER.format("preserve-x-illegal"));
                }
                setPreserveXLeft(args[0], params);
                setPreserveXRight(args[1], params);
            }
        });

        tmp.put("v.left.x", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setImgLeftX(value, params);
            }
        });

        tmp.put("v.right.x", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setImgRightX(value, params);
            }
        });


        tmp.put("v.top.y", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setImgTopY(value, params);
            }
        });

        tmp.put("v.bottom.y", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setImgBottomY(value, params);
            }
        });

        // transforce parameters
        tmp.put("tf.source.host", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setImgHost(value, params);
            }
        });

        tmp.put("tf.source.port", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setImgPort(Integer.valueOf(value).intValue(), params);
            }
        });

        tmp.put("tf.width", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setImageWidth(Integer.valueOf(value).intValue(), params);
            }
        });

        tmp.put("tf.maxfilesize", new ParamSetter() {
            public void execute(String value, MutableParameters params)
                    throws ParameterBuilderException {
                setMaxImageSize(Integer.valueOf(value).intValue(), params);
            }
        });

        paramSetters =
            Collections.unmodifiableMap(Collections.synchronizedMap(tmp));
    }

    /**
     * Command pattern interface that allows implementations to set parameters
     */
    private interface ParamSetter {

        /**
         * implementations should set the required parameter on the specified
         * {@link MutableParameters} instance
         *
         * @param value the value that needs to be set
         * @param params the MutableParameters instance to poplulate
         * @throws ParameterBuilderException if an error occcurs
         */
        public void execute(String value, MutableParameters params)
                throws ParameterBuilderException;
    }


}
