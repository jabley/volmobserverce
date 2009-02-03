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
 * (c) Volantis Systems Ltd 2003.
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.configuration.impl;

import com.volantis.map.ics.configuration.Configuration;
import com.volantis.map.ics.configuration.ConfigurationUtilities;
import com.volantis.map.ics.configuration.DitherMode;
import com.volantis.map.ics.configuration.ImageConstants;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;


/**
 * Class to hold the configuration for VTS.
 */
public class DefaultConfiguration implements Configuration {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DefaultConfiguration.class);

    /**
     * Constant indicating that the image can be of any size.
     */
    public final static int NO_MAXIMUM_BYTE_SIZE_SET_FOR_IMAGE = -1;

    /**
     * The host containing the images.
     */
    private String host = null;

    private int hostPort = 80;

    private String hostProtocol = "http";

    /**
     * Parameters for cropping image tool. imgLeftX, imgTopY describe left top
     * corner imgRightX, imgBottomY describe upper right corner.
     */
    private String imgLeftX;

    private String imgRightX;

    private String imgTopY;

    private String imgBottomY;


    /**
     * The squid proxy servlet.
     */
    private String proxy = null;

    private int proxyPort = 80;

    /**
     * Max number of redirects between hosts followed.
     */
    private int maxRedirects = 5;

    /**
     * This specifies the mode for scalling. This can be one of the
     * SCALE_MODE_XXXX constants.
     */
    private int scaleMode = ImageConstants.SCALE_MODE_BILINEAR;

    /**
     * This specifies if a transcoded image can be scaled to larger than the
     * input image. If set to true then the image will be scaled to the
     * specified size. If set to false then the image will be scaled to the
     * same size as the input image. The default is false but can be overridden
     * in native mode by using the v.scaleLarger parameter.
     */
    private boolean scaleLarger = false;


    /**
     * This specified the  X left bound of the image preserved area. When the
     * image is clipped the area will not be copped.
     */
    private int preserveXLeft = ImageConstants.NO_CLIP_LEFT;

    /**
     * This specified the  X right bound of the image preserved area. When the
     * image is clipped the area will not be copped.
     */

    private int preserveXRight = ImageConstants.NO_CLIP_RIGHT;

    /**
     * This specifies the minimum allowable quality as a result of lossy
     * compression that will be considered when trying to restrict the size of
     * the output image before a pixel reduction strategy will be considered.
     * The value ranges from 0 to 100 with 100 being no loss in image quality.
     * This is ignored if the user has not specified the v.maxSize parameter in
     * native mode or the tf.maxfilesize in Transforce compatibility mode. This
     * can be overridden by using v.qualityMin in native mode.
     */
    private int minimumJPEGQuality = 30;

    /**
     * This specifies the minimum allowable palette size as a result of lossy
     * compresion on indexed (palette-based) file formats that will be
     * considered when trying to restrict the size of the output image before a
     * pixel reduction strategy will be considered. The value may be 1, 2, 4 or
     * 8. If the value is greater than the bit depth of the image then the
     * value is reduced to the bit depth. This is ignored if the user has not
     * specified the v.maxSize parameter in native mode or the tf.maxfilesize
     * in Transforce compatibility mode. The default value is 8 and can be
     * overridden by using the v.paletteMin parameter in native mode.
     */
    private int minimumBitDepth = 8;

    private DitherMode defaultDither = DitherMode.FLOYD;

    private ArrayList ditheringModes = new ArrayList();

    /**
     * This specifies if GIF is supported as an input image type. In future
     * this will be extended to cover gif as an output type as well.
     */
    private boolean gifEnabled = false;

    private int jpegMode = ImageConstants.JPEG_BASELINE;

    /**
     * The required width of the image in pixels.
     */
    private int imageWidth;

    /**
     * The maximum image height.
     */
    private int imageHeight = -1;

    /**
     * The maximum number of bytes the image is allowed to occupy.
     */
    private int maxImageSize = NO_MAXIMUM_BYTE_SIZE_SET_FOR_IMAGE;

    /**
     * True if an in-memory IO cache should be used in preference to a disk
     * based IO cache.
     */
    private boolean useInMemoryIOCache = false;

    /**
     * If true, host and port defined in config file should not be overridden by values from request.
     */
    private boolean ignoreHostParameterInRequestUrl;

    /**
     * watermark URL if watermarking is needed or null.
     */
    private String watermarkURL = null;

    /**
     * URL of the device repository
     */
    private String repositoryURL = null;


    private final static int[] DEF_IMG_PRESERVE = {
        ImageConstants.NO_CLIP_LEFT, ImageConstants.NO_CLIP_RIGHT};

    /**
     * Information if original image format should be preserved.
     *
     * @see com.volantis.ics.configuration.Configuration#isPreserveOriginalImageFormat()
     */
    private boolean preserveOriginalImageFormat = false;

    /**
     * New getInstance.
     */
    public DefaultConfiguration() {
        reset();
    }

    /**
     * Copy constructor
     *
     * @param config the configuration to copy
     */
    public DefaultConfiguration(DefaultConfiguration config) {
        host = config.host;
        hostPort = config.hostPort;
        hostProtocol = config.hostProtocol;
        scaleMode = config.scaleMode;
        scaleLarger = config.scaleLarger;
        minimumJPEGQuality = config.minimumJPEGQuality;
        minimumBitDepth = config.minimumBitDepth;
        ditheringModes = new ArrayList(config.ditheringModes);
        gifEnabled = config.gifEnabled;
        jpegMode = config.jpegMode;
        imageWidth = config.imageWidth;
        maxImageSize = config.maxImageSize;
        maxRedirects = config.maxRedirects;
        preserveXLeft = config.preserveXLeft;
        preserveXRight = config.preserveXRight;
        watermarkURL = config.watermarkURL;
        repositoryURL = config.repositoryURL;
        ignoreHostParameterInRequestUrl = config.ignoreHostParameterInRequestUrl;
        preserveOriginalImageFormat = config.preserveOriginalImageFormat;
        proxy = config.proxy;
        proxyPort = config.proxyPort;
        imgLeftX = config.imgLeftX;
        imgRightX = config.imgRightX;
        imgTopY = config.imgTopY;
        imgBottomY = config.imgBottomY;
        defaultDither = config.defaultDither;
        imageHeight = config.imageHeight;
        useInMemoryIOCache = config.useInMemoryIOCache;
    }

    public void reset() {
        host = null;
        hostPort = 80;
        hostProtocol = "http";
        scaleMode = ImageConstants.SCALE_MODE_BILINEAR;
        scaleLarger = false;
        minimumJPEGQuality = 30;
        minimumBitDepth = 8;
        ditheringModes = new ArrayList();
        gifEnabled = false;
        jpegMode = ImageConstants.JPEG_BASELINE;
        imageWidth = -1;
        maxImageSize = NO_MAXIMUM_BYTE_SIZE_SET_FOR_IMAGE;
        maxRedirects = 5;
        preserveXLeft = ImageConstants.NO_CLIP_LEFT;
        preserveXRight = ImageConstants.NO_CLIP_RIGHT;
        watermarkURL = null;
        repositoryURL = null;
        ignoreHostParameterInRequestUrl = false;
        preserveOriginalImageFormat = false;
        proxy = null;
        proxyPort = 80;
        imgLeftX = null;
        imgRightX = null;
        imgTopY = null;
        imgBottomY = null;
        defaultDither = DitherMode.FLOYD;
        imageHeight = -1;
        useInMemoryIOCache = false;
    }

    // javadoc inherited
    public DitherMode getDitherMode(int bitDepth) {
        return getDitherMode(bitDepth, defaultDither);
    }

    // javadoc inherited
    public DitherMode getDitherMode(int bitDepth, DitherMode defaultMode) {
        DitherMode result = defaultMode;
        boolean finished = false;
        for (int i = 0; i < ditheringModes.size() && !finished; i++) {
            Dither value = (Dither) ditheringModes.get(i);
            if (value.getBitDepth() == bitDepth) {
                result = value.getMode();
                finished = true;
            }
        }
        return result;
    }

    public boolean isGifEnabled() {
        return gifEnabled;
    }

    public String getHost() {
        return host;
    }


    public String getRepositoryURL() {
        return repositoryURL;

    }

    public int getHostPort() {
        return hostPort;
    }


    public String getHostProtocol() {
        return hostProtocol;
    }

    public String getProxy() {
        return proxy;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public int getMaxRedirects() {
        return maxRedirects;
    }

    public int getJpegMode() {
        return jpegMode;
    }

    public int getMinimumBitDepth() {
        return minimumBitDepth;
    }

    public int getMinimumJPEGQuality() {
        return minimumJPEGQuality;
    }

    public boolean canScaleLarger() {
        return scaleLarger;
    }

    public int getScaleMode() {
        return scaleMode;
    }

    /**
     * Set the dither mode to use when colour reducing an image to the
     * specified number of bits.
     *
     * @param bits the number of bits in the target image
     * @param mode the dithering mode.
     */
    public void setDitherMode(int bits, DitherMode mode) {
        boolean finished = false;
        for (int i = 0; i < ditheringModes.size() && !finished; i++) {
            Dither d = (Dither) ditheringModes.get(i);
            if (d.getBitDepth() == bits) {
                d.setMode(mode);
                finished = true;
            }
        }
    }

    /**
     * Set device repository URL to use for image format preservation feature.
     *
     * @param url - URL pointing into device repository location.
     */
    public void setRepositoryURL(String url) {
        repositoryURL = url;
    }

    /**
     * Specifies if GIF is supported as an input image type. In future this
     * will be extended to cover gif as an output type as well.
     *
     * @param b true if GIF input is allowed, false otherwise
     */
    public void setGifEnabled(boolean b) {
        gifEnabled = b;
    }

    /**
     * Specifies the name of the host which is hosting the images. Supplying ""
     * as host name sets the host to null.
     *
     * @param string the name of the host
     */
    public void setHost(String string) {
        if (!"".equals(string)) {
            host = string;
        } else {
            host = null;
        }
    }

    /**
     * Specifies the port to connect to the image host
     *
     * @param i the image host connection port
     */
    public void setHostPort(int i) {
        hostPort = i;
    }

    /**
     * Specifies the port to connect to the image host
     *
     * @param s the image host connection port
     */
    public void setHostPort(String s) {
        final String port = deserializeString(s);
        if (null != port) {
            try {
                setHostPort(Integer.parseInt(port));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid port number " + port);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Ignoring empty host port, will use " + hostPort);
            }
        }
    }

    /**
     * @param maxRedirects The max number of redirects between hosts to be
     *                     followed
     */
    public void setMaxRedirects(int maxRedirects) {
        this.maxRedirects = maxRedirects;
    }

    /**
     * @param maxRedirects The max number of redirects between hosts to be
     *                     followed
     */
    public void setMaxRedirects(String maxRedirects) {
        try {
            setMaxRedirects(Integer.parseInt(maxRedirects));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid number of redirects " + maxRedirects);
        }
    }

    /**
     * Specifies the name of the proxy servlet. Supplying "" as proxy name sets
     * the host to null.
     *
     * @param string the name of the host
     */
    public void setProxy(String string) {
        if (!"".equals(string)) {
            proxy = string;
        } else {
            proxy = null;
        }
    }

    /**
     * Specifies the port to connect to the proxy servlet
     *
     * @param i the proxy servlet connection port
     */
    public void setProxyPort(int i) {
        proxyPort = i;
    }

    /**
     * Specifies the port to connect to the proxy servlet
     *
     * @param s the proxy servlet connection port
     */
    public void setProxyPort(String s) {
        final String port = deserializeString(s);
        if (null != port) {
            try {
                setProxyPort(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid port number " + s);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Ignoring empty proxy port, will use " + proxyPort);
            }
        }
    }

    /**
     * This specifies the mode in which a JPEG image will be rendered. Valid
     * values are JPEG_BASELINE or JPEG_PROGRESSIVE.
     *
     * @param i the JPEG rendering mode
     * @throws IllegalArgumentException if an invalid mode is requested
     */
    public void setJpegMode(int i) {
        ConfigurationUtilities.assertJPegMode(i);
        jpegMode = i;
    }

    /**
     * This specifies the minimum allowable palette size as a result of lossy
     * compresion on indexed (palette-based) file formats that will be
     * considered when trying to restrict the size of the output image before a
     * pixel reduction strategy will be considered. The value may be 1, 2, 4 or
     * 8. If the value is greater than the bit depth of the image then the
     * value is reduced to the bit depth.
     *
     * @param i the minimum bit depth allowed
     * @throws IllegalArgumentException if an invalid bit depth is set
     */
    public void setMinimumBitDepth(int i) {
        ConfigurationUtilities.assertMinimumBitDepth(i);
        minimumBitDepth = i;
    }

    /**
     * This specifies the minimum allowable palette size as a result of lossy
     * compresion on indexed (palette-based) file formats that will be
     * considered when trying to restrict the size of the output image before a
     * pixel reduction strategy will be considered. The value may be 1, 2, 4 or
     * 8. If the value is greater than the bit depth of the image then the
     * value is reduced to the bit depth.
     *
     * @param s the minimum bit depth allowed
     * @throws IllegalArgumentException if an invalid bit depth is set
     */
    public void setMinimumBitDepth(String s) {
        try {
            setMinimumBitDepth(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid bit depth");
        }
    }

    /**
     * This specifies the minimum allowable quality as a result of lossy
     * compression that will be considered when trying to restrict the size of
     * the output image before a pixel reduction strategy will be considered.
     * The value ranges from 0 to 100 with 100 being no loss in image quality.
     *
     * @param i the minimum allowable JPEG quality
     * @throws IllegalArgumentException if an invalid percentage is set
     */
    public void setMinimumJPEGQuality(int i) {
        ConfigurationUtilities.assertMinimumJPegQuality(i);
        minimumJPEGQuality = i;
    }

    /**
     * This specifies the minimum allowable quality as a result of lossy
     * compression that will be considered when trying to restrict the size of
     * the output image before a pixel reduction strategy will be considered.
     * The value ranges from 0 to 100 with 100 being no loss in image quality.
     *
     * @param s the minimum allowable JPEG quality
     * @throws IllegalArgumentException if an invalid percentage is set
     */
    public void setMinimumJPEGQuality(String s) {
        try {
            setMinimumJPEGQuality(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid quality setting");
        }
    }

    /**
     * This specifies if a transcoded image can be scaled to larger than the
     * input image. If set to true then the image will be scaled to the
     * specified size. If set to false then the image will be scaled to the
     * same size as the input image.
     *
     * @param b true if images are allowed to increase in size
     */
    public void setScaleLarger(boolean b) {
        scaleLarger = b;
    }


    /**
     * This specifies the method of scaling used by the servlet. This can take
     * the values SCALE_MODE_NEAREST, SCALE_MODE_BILINEAR or
     * SCALE_MODE_BICUBIC.
     *
     * @param i the required scaling mode
     */
    public void setScaleMode(int i) {
        scaleMode = i;
    }

    public void requestOverride(HttpServletRequest request) {

        // read all parameters, if parameter is duplicated and set for both modes
        // native parameter will overwrite transforce
        transforceOverride(request);
        nativeOverride(request);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(this);
        }
    }

    /**
     * Override the configuration with native mode request parameters
     */
    private void nativeOverride(HttpServletRequest request) {
        Enumeration e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String paramName = (String) e.nextElement();
            if (paramName.startsWith("v.")) {
                if (paramName.equals("v.imgHost")) {
                    if(!getIgnoreHostParameterInRequestUrl()) {
                        setHost(request.getParameter(paramName));
                    }
                } else if (paramName.equals("v.imgPort")) {
                    if(!getIgnoreHostParameterInRequestUrl()) {
                        setHostPort(request.getParameter(paramName));
                    }
                } else if (paramName.equals("v.scaleMode")) {
                    String mode = request.getParameter(paramName);
                    if (mode.equalsIgnoreCase("bicubic")) {
                        setScaleMode(ImageConstants.SCALE_MODE_BICUBIC);
                    } else if (mode.equalsIgnoreCase("nearest")) {
                        setScaleMode(ImageConstants.SCALE_MODE_NEAREST);
                    } else {
                        setScaleMode(ImageConstants.SCALE_MODE_BILINEAR);
                    }
                } else if (paramName.equals("v.scaleLarger")) {
                    String mode = request.getParameter(paramName);
                    if (mode.equalsIgnoreCase("true")) {
                        setScaleLarger(true);
                    } else {
                        setScaleLarger(false);
                    }
                } else if (paramName.equals("v.qualityMin")) {
                    setMinimumJPEGQuality(request.getParameter(paramName));
                } else if (paramName.equals("v.paletteMin")) {
                    setMinimumBitDepth(request.getParameter(paramName));
                } else if (paramName.equals("v.ditherMode")) {
                    String mode = request.getParameter(paramName);
                    if (mode.equalsIgnoreCase("ordered-dither")) {
                        defaultDither = DitherMode.PATTERNED;
                    } else if (mode.equalsIgnoreCase("floyd-steinberg")) {
                        defaultDither = DitherMode.FLOYD;
                    } else if (mode.equalsIgnoreCase("stucki")) {
                        defaultDither = DitherMode.STUCKI;
                    } else if (mode.equalsIgnoreCase("jarvis")) {
                        defaultDither = DitherMode.JARVIS;
                    }
                } else if (paramName.equals("v.jpegFormat")) {
                    String mode = request.getParameter(paramName);
                    if (mode.equalsIgnoreCase("progressive")) {
                        setJpegMode(ImageConstants.JPEG_PROGRESSIVE);
                    } else {
                        setJpegMode(ImageConstants.JPEG_BASELINE);
                    }
                } else if (paramName.equals("v.width")) {
                    setImageWidth(request.getParameter(paramName));
                } else if (paramName.equals("v.height")) {
                    setImageHeight(request.getParameter(paramName));
                } else if (paramName.equals("v.maxSize")) {
                    setMaxImageSize(request.getParameter(paramName));
                } else if (paramName.equals("v.gifEnabled")) {
                    // NOTE: gif support only added for testing!
                    // todo: replace this option with a more functional testcase
                    // which can create the web.xml and config file dynamically.
                    // This will probably require moving TemporaryFileCreator
                    // into Synergetics for a start...
                    setGifEnabled(request.getParameter(paramName) != null);
                } else if (paramName.equals("v.p")) {
                    setPreserveX(request.getParameter(paramName));
                } else if (paramName.equals("v.left.x")) {
                    this.setImgLeftX(request.getParameter(paramName));
                } else if (paramName.equals("v.right.x")) {
                    this.setImgRightX(request.getParameter(paramName));
                } else if (paramName.equals("v.top.y")) {
                    this.setImgTopY(request.getParameter(paramName));
                } else if (paramName.equals("v.bottom.y")) {
                    this.setImgBottomY(request.getParameter(paramName));
                }
            }
        }
    }

    /**
     * Override the configuration with transforce mode request parameters
     */
    private void transforceOverride(HttpServletRequest request) {
        Enumeration e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String paramName = (String) e.nextElement();
            if (paramName.startsWith("tf.")) {
                if (paramName.equals("tf.source.host")) {
                    if(!getIgnoreHostParameterInRequestUrl()) {
                        setHost(request.getParameter(paramName));
                    }
                } else if (paramName.equals("tf.source.port")) {
                    if(!getIgnoreHostParameterInRequestUrl()) {
                        setHostPort(request.getParameter(paramName));
                    }
                } else if (paramName.equals("tf.width")) {
                    setImageWidth(request.getParameter(paramName));
                } else if (paramName.equals("tf.height")) {
                    setImageHeight(request.getParameter(paramName));
                } else if (paramName.equals("tf.maxfilesize")) {
                    setMaxImageSize(request.getParameter(paramName));
                }
            }
        }
    }

    // javadoc inherited
    public int getImageWidth() {
        return imageWidth;
    }

    // javadoc inherited
    public int getImageHeight() {
        return imageHeight;
    }

    // javadoc inherited
    public int getMaxImageSize() {
        return maxImageSize;
    }

    // javadoc inherited
    public boolean exceedsMaxImageSize(int imageSize) {
        boolean result = false;
        if ((maxImageSize != NO_MAXIMUM_BYTE_SIZE_SET_FOR_IMAGE) &&
            (imageSize > maxImageSize)) {
            result = true;
        }
        return result;
    }

    // javadoc inherited
    public void setImageWidth(int i) {
        if (i < -1) {
            throw new IllegalArgumentException("Illegal image width");
        }

        imageWidth = i;
    }

    /**
     * Set the width that the image should be scaled to
     *
     * @param s the width to scale the image to in pixels
     * @throws IllegalArgumentException if the width is illegal
     */
    public void setImageWidth(String s) {
        try {
            setImageWidth(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Illegal image width");
        }
    }

    // javadoc inherited
    public void setImageHeight(int i) {
        if (i < -1) {
            throw new IllegalArgumentException("Illegal image height");
        }

        imageHeight = i;
    }

    /**
     * Set the height that the image should be scaled to
     *
     * @param s the height to scale the image to in pixels
     * @throws IllegalArgumentException if the height is illegal
     */
    public void setImageHeight(String s) {
        try {
            setImageHeight(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Illegal image height");
        }
    }

    /**
     * Set the number of bytes that the image must not exceed or -1 if it
     * should be ignored.
     *
     * @param i the maximum image size or -1
     * @throws IllegalArgumentException if the width is illegal
     */
    public void setMaxImageSize(int i) {
        if (i < -1) {
            throw new IllegalArgumentException("Illegal image size");
        }

        maxImageSize = i;
    }

    /**
     * Set the number of bytes that the image must not exceed or -1 if it
     * should be ignored.
     *
     * @param s the maximum image size in pixels
     * @throws IllegalArgumentException if the width is illegal
     */
    public void setMaxImageSize(String s) {
        try {
            setMaxImageSize(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Illegal image size");
        }
    }

    // javadoc inherited
    public boolean getUseInMemoryIOCache() {
        return useInMemoryIOCache;
    }

    /**
     * Determine whether to use an in-memory IO Image IO cache or a file based
     * image cache.
     *
     * @param value true if you wish to use an in-memory io cache false if you
     *              wish to use the file based cache.
     */
    public void setUseInMemoryIOCache(boolean value) {
        useInMemoryIOCache = value;
    }

    /**
     * Sets watermark URL
     *
     * @param value value to be set
     */
    public void setWatermarkURL(String value) {
        watermarkURL = value;
    }

    // javadoc inherited
    public String getWatermarkURL() {
        return watermarkURL;
    }

    /**
     * Return a clone of this object
     *
     * @return a cloned DefaultConfiguration
     */
    public Configuration createCopy() {
        return new DefaultConfiguration(this);
    }


    /**
     * Return a string representation of this configuration
     *
     * @return a string representation of this configuration
     */
    public String toString() {
        StringBuffer buff = new StringBuffer();

        buff.append(" Mode:");
        buff.append("Volantis");

        if (getHost() != null) {
            buff.append(" Image Host:").append(getHost()).append(":").
                append(getHostPort());
        }

        if (getProxy() != null) {
            buff.append(" Proxy:").append(getProxy()).append(":").
                append(getProxyPort());

        }

        buff.append("Max redirects:").append(getMaxRedirects());

        if (getImageWidth() != -1) {
            buff.append(" Width:").append(getImageWidth());
        }
        if (this.getMaxImageSize() != -1) {
            buff.append(" Max image size:").append(getMaxImageSize());
        }
        buff.append(" Scale mode:");
        switch (this.scaleMode) {
            case ImageConstants.SCALE_MODE_BILINEAR:
                buff.append("Bilinear");
                break;
            case ImageConstants.SCALE_MODE_BICUBIC:
                buff.append("Bicubic");
                break;
            case ImageConstants.SCALE_MODE_NEAREST:
                buff.append("Nearest");
                break;
            default:
                buff.append("UNKNOWN");
                break;
        }

        buff.append(" Scale Larger:").append(canScaleLarger());
        buff.append(" GIF Enabled:").append(isGifEnabled());
        buff.append(" JPEG [ Mode:");
        switch (getJpegMode()) {
            case ImageConstants.JPEG_BASELINE:
                buff.append("Baseline");
                break;
            case ImageConstants.JPEG_PROGRESSIVE:
                buff.append("Progressive");
                break;
            default:
                buff.append("UNKNOWN");
                break;
        }
        buff.append(" Min quality:").append(getMinimumJPEGQuality());
        buff.append(" ]");
        buff.append(" Min bit depth:").append(getMinimumBitDepth());

        return buff.toString();
    }

    public int getPreserveXLeft() {
        return preserveXLeft;
    }

    /**
     * This sets the X left bound of the image preserved area
     *
     * @param preserveXLeft X left bound of the image preserved area
     */
    public void setPreserveXLeft(int preserveXLeft) {
        this.preserveXLeft = preserveXLeft;
    }

    public int getPreserveXRight() {
        return preserveXRight;
    }

    /**
     * This sets the X right bound of the image preserved area
     *
     * @param preserveXRight X right bound of the image preserved area
     */
    public void setPreserveXRight(int preserveXRight) {
        this.preserveXRight = preserveXRight;
    }

    /**
     * This sets the image preserved area defined as a string of the
     * [<left-bound>[','<right-bound>]].
     *
     * @param preserveX string representaion of the image preserved area
     */
    public void setPreserveX(String preserveX) {
        try {
            int[] args = ConfigurationUtilities.listToIntArray(preserveX,
                                                     DEF_IMG_PRESERVE);
            if (args[1] != ImageConstants.NO_CLIP_RIGHT && args[1] < args[0]) {
                // preserved area cannot be empty
                throw new IllegalArgumentException(
                    "Illegal preserveX  - right bound less then left bound");
            }
            setPreserveXLeft(args[0]);
            setPreserveXRight(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Illegal preserveX");
        }
    }

    // javadoc inherited
    public String getImgBottomY() {
        return imgBottomY;
    }

    /**
     * set bottom y image coordinate
     *
     * @param imgBottomY
     */
    public void setImgBottomY(String imgBottomY) {
        this.imgBottomY = imgBottomY;
    }

    // javadoc inherited
    public String getImgLeftX() {
        return imgLeftX;
    }

    /**
     * set left x image coordinate
     *
     * @param imgLeftX
     */
    public void setImgLeftX(String imgLeftX) {
        this.imgLeftX = imgLeftX;
    }

    // javadoc inherited
    public String getImgRightX() {
        return imgRightX;
    }

    /**
     * set right x image coordinate
     *
     * @param imgRightX
     */
    public void setImgRightX(String imgRightX) {
        this.imgRightX = imgRightX;
    }

    // javadoc inherited
    public String getImgTopY() {
        return imgTopY;
    }

    /**
     * set top y image coordinate
     *
     * @param imgTopY
     */
    public void setImgTopY(String imgTopY) {
        this.imgTopY = imgTopY;
    }

    /**
     * Deserializer for the JpegMode attribute
     *
     * @param mode the JpegMode value
     * @return the corresponding integer value
     */
    public static int deserializeJpegMode(String mode) {
        int result = ImageConstants.JPEG_BASELINE;
        if (mode != null) {
            if (mode.trim().equalsIgnoreCase("progressive")) {
                result = ImageConstants.JPEG_PROGRESSIVE;
            } else {
                result = ImageConstants.JPEG_BASELINE;
            }
        }
        return result;
    }

    /**
     * Serializer for the JpegMode attribute
     *
     * @param value the integer value to serialize
     * @return the string representation
     */
    public static String serializeJpegMode(int value) {
        String result;
        if (value == ImageConstants.JPEG_BASELINE) {
            result = "baseline";
        } else {
            result = "progressive";
        }
        return result;
    }

    /**
     * Deserialize the scale model mode
     *
     * @param mode the mode
     * @return the int scale model mode
     */
    public static int deserializeScaleMode(String mode) {
        int result = ImageConstants.SCALE_MODE_BILINEAR;
        if (mode != null) {
            mode = mode.trim();
            if (mode.equalsIgnoreCase("bicubic")) {
                result = ImageConstants.SCALE_MODE_BICUBIC;
            } else if (mode.equalsIgnoreCase("nearest")) {
                result = ImageConstants.SCALE_MODE_NEAREST;
            }
        }
        return result;
    }

    /**
     * Serialize the scale model mode
     *
     * @param mode the int representation of the scale model
     * @return the string representation of the scale model
     */
    public static String serializeScaleMode(int mode) {
        String result = "bilinear";
        if (mode == ImageConstants.SCALE_MODE_NEAREST) {
            result = "nearest";
        } else if (mode == ImageConstants.SCALE_MODE_BICUBIC) {
            result = "bicubic";
        }
        return result;
    }

    /**
     * Serialize the String
     *
     * @param string
     * @return
     */
    public static String serializeString(String string) {
        String result = null;
        if (null != string) {
            string = string.trim();
            if (!"".equals(string)) {
                result = string;
            }
        }
        return result;
    }

    /**
     * deserialize a string name (treat empty string name as no string name)
     *
     * @param string the string name to deserialize
     * @return the deserialized string name or null of one was not specified
     */
    public static String deserializeString(String string) {
        String result = null;
        if (null != string) {
            string = string.trim();
            if (!"".equals(string)) {
                result = string;
            }
        }
        return result;
    }

    public boolean getIgnoreHostParameterInRequestUrl() {
        return ignoreHostParameterInRequestUrl;
    }

    public void setIgnoreHostParameterInRequestUrl(
            boolean ignoreHostParameterInRequestUrl) {
        this.ignoreHostParameterInRequestUrl = ignoreHostParameterInRequestUrl;
    }

    // javadoc inherited
    public boolean isPreserveOriginalImageFormat() {
        return preserveOriginalImageFormat;
    }

    /**
     * Sets information, if original image format should be preserved.
     *
     * @see com.volantis.ics.configuration.Configuration#isPreserveOriginalImageFormat()
     *
     * @param preserveOriginalImageFormat flag indicating if original image format
     *         should be preserved
     */
    public void setPreserveOriginalImageFormat(boolean preserveOriginalImageFormat) {
        this.preserveOriginalImageFormat = preserveOriginalImageFormat;
    }
}


