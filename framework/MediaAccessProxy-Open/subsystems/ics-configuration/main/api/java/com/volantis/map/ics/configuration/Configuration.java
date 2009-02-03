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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.configuration;

import javax.servlet.http.HttpServletRequest;

public interface Configuration {

    /**
     * Set the configuration to default values.
     */
    void reset();

    /**
     * Returns the prefered dither mode to use when quantizing an image to the
     * specified bit depth. If a mode has not been sepecified then a default
     * is automatically provided by the implmentations
     *
     * @param bitDepth the bit depth
     * @return a DitherMode
     */
    DitherMode getDitherMode(int bitDepth);

     /**
     * Returns the prefered dither mode to use when quantizing an image to the
     * specified bit depth.
     *
     * @param bitDepth the bit depth
     * @param defaultDither the DitherModed to use if one has not been
     * configured for the given bit depth
     * @return a DitherMode
     */
    DitherMode getDitherMode(int bitDepth, DitherMode defaultDither);

    /**
     * Returns whether or not we are allowed to read GIF format images.
     *
     * @return true if GIF reading is enabled, false otherwise.
     */
    boolean isGifEnabled();

    /**
     * Returns the name of the host holding the images.
     *
     * @return the image host name.
     */
    String getHost();

    /**
     * Returns device repository URL to use for image format preservation
     * feature.
     *
     * @return device repository URL.
     */
    String getRepositoryURL();

    /**
     * Returns the port to connect to the image host on.
     *
     * @return the image host connection port.
     */
    int getHostPort();

    /**
     * Return the optional protocol used to connect to the image host.
     *
     * @return the protocol used to connect to the image host. Defaults to
     * http
     */
    String getHostProtocol();

    /**
     * Returns the name of the squid proxy servlet.
     *
     * @return the squid proxy servlet host name.
     */
    String getProxy();

    /**
     * Returns the port to connect to the proxy servlet.
     *
     * @return the proxy servlet connection port.
     */
    int getProxyPort();

    /**
     * @return Returns the number of redirects between hosts to be followed.
     */
    int getMaxRedirects();

    /**
     * Returns whether BASELINE or PROGRESSIVE jpeg images should be rendered.
     *
     * @return the JPEG rendering mode.
     */
    int getJpegMode();

    /**
     * Returns the minimum bit depth that images can be rendered to before
     * scaling starts. This is only used if we are constrained by a memory
     * size.
     *
     * @return the minimum allowable bit depth.
     */
    int getMinimumBitDepth();

    /**
     * Returns the minimum quality that JPEG images can be rendered to before
     * scaling starts. The value is returned as a percentage from 0-100. This
     * value is only used if we are constrained by a memory size.
     *
     * @return the minimum allowable rendering quality.
     */
    int getMinimumJPEGQuality();

    /**
     * Returns whether an image is allowed to grow to the width specified by
     * the v.width parameter whether it is larger than the image or not.
     * Ordinarily images will only be scaled down.
     *
     * @return true if images can scale larger, false otherwise.
     */
    boolean canScaleLarger();

    /**
     * Returns the interpolation method to use when scaling. This will be
     * either nearest, bilinear or bicubic.
     *
     * @return the required interpolation method.
     */
    int getScaleMode();

    /**
     * Override the configuration values from request parameters
     */
    void requestOverride(HttpServletRequest request);

    /**
     * Return the width that the image should be scaled to or -1 if it has not
     * been set.
     *
     * @return the width to scale the image to in pixels or -1
     */
    int getImageWidth();

    /**
     * Return the maximum number of bytes that the image is allowed to occupy
     * or -1 if there is no limit.
     *
     * @return the maximum image size allowed.
     *
     *         {@link #getMaxImageSize} use {@link #exceedsMaxImageSize}.  It's
     *         too easy to forget that this method returns -1 if no maximum
     *         size is set.
     */
    int getMaxImageSize();

    /**
     * Returns true if the supplied imageSize exceeds {@link #getMaxImageSize};
     * otherwise returns false.  If no maximum image size has been set, this
     * method returns false.
     *
     * @param imageSize size to be compared against maximum image size set.
     * @return true if maximum image size has been exceeded; otherwise false.
     */
    boolean exceedsMaxImageSize(int imageSize);

    /**
     * Set the width that the image should be scaled to or -1 if it should be
     * ignored.
     *
     * @param i the width to scale the image to in pixels or -1
     * @throws IllegalArgumentException if the width is illegal
     */
    void setImageWidth(int i);

    /**
     * @return true if an in-memory io cache should be used, false if a file
     *         based image io cache should be used.
     */
    boolean getUseInMemoryIOCache();

    /**
     * Gets watermark URL. If watermarking isn't needed null is returned
     *
     * @return watermark URL
     */
    String getWatermarkURL();

    /**
     * This returns the X left bound of the image preserved area
     *
     * @return X left bound of the image preserved area
     */
    int getPreserveXLeft();

    /**
     * This returns the X right bound of the image preserved area
     *
     * @return X right bound of the image preserved area
     */
    int getPreserveXRight();

    /**
     * get bottom y image coordinate
     *
     * @return
     */
    String getImgBottomY();

    /**
     * get left x image coordinate
     *
     * @return
     */
    String getImgLeftX();

    /**
     * get right x image coordinate
     *
     * @return
     */
    String getImgRightX();

    /**
     * get top y image coordinate
     *
     * @return
     */
    String getImgTopY();

    /**
     * Create a deep copy of this Configuration
     *
     * @return a deep copy of this Configuration
     */
    Configuration createCopy();
}
