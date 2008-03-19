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
package com.volantis.map.sti.model;

/**
 * Video visual element model class.
 */
public class VideoVideoVisual {
    
    /**
     * Codec attribute.
     */
    protected String codec;

    /**
     * Codec parameters.
     */
    protected Properties codecParams;

    /**
     * Size limit for visual part.
     */
    protected long sizeLimit;

    /**
     * Bit rate attribute.
     */
    protected int bitRate;

    /**
     * Frame rate.
     */
    protected float frameRate;

    /**
     * Width attribute.
     */
    protected int width;

    /**
     * Height attribute.
     */
    protected int height;

    /**
     * Resize directive.
     */
    protected String resizeDirective;

    /**
     * Upsize allowed attribute.
     */
    protected boolean upsizeAllowed;

    /**
     * Transformations.
     */
    protected Transformations transformations;

    /**
     * Getter for codec attribute.
     * 
     * @return codec
     */
    public String getCodec() {
        return this.codec;
    }

    /**
     * Setter for codec attribute.
     * 
     * @param codec codec
     */
    public void setCodec(String codec) {
        this.codec = codec;
    }

    /**
     * Getter for codec parameters.
     * 
     * @return codec parameters.
     */
    public Properties getCodecParams() {
        return this.codecParams;
    }

    /**
     * Setter for codec parameters.
     * 
     * @param codecParams codec parameters.
     */
    public void setCodecParams(Properties codecParams) {
        this.codecParams = codecParams;
    }

    /**
     * Getter for size limit for this part.
     * 
     * @return size limit.
     */
    public long getSizeLimit() {
        return this.sizeLimit;
    }

    /**
     * Setter for size limit for this part.
     * 
     * @param sizeLimit size limit.
     */
    public void setSizeLimit(long sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    /**
     * Getter for bit rate attribute.
     * 
     * @return bit rate.
     */
    public int getBitRate() {
        return this.bitRate;
    }

    /**
     * Setter for bit rate attribute.
     * 
     * @param bitRate bit rate.
     */
    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    /**
     * Getter for frame rate attribute.
     * 
     * @return frame rate.
     */
    public float getFrameRate() {
        return this.frameRate;
    }

    /**
     * Setter for frame rate attribute.
     * 
     * @param frameRate frame rate.
     */
    public void setFrameRate(float frameRate) {
        this.frameRate = frameRate;
    }

    /**
     * Getter for width attribute.
     * 
     * @return width attribute.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Setter for width attribute.
     * 
     * @param width width attribute.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Getter for height attribute.
     * 
     * @return height attribute.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Setter for height attribute.
     * 
     * @param height height attribute.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Getter for resize directive.
     * 
     * @return resize directive.
     */
    public String getResizeDirective() {
        return this.resizeDirective;
    }

    /**
     * Setter for resize directive.
     * 
     * @param resizeDirective resize directive.
     */
    public void setResizeDirective(String resizeDirective) {
        this.resizeDirective = resizeDirective;
    }

    /**
     * Getter for upsize allowed attribute.
     * 
     * @return upsize allowed attribute.
     */
    public boolean getUpsizeAllowed() {
        return this.upsizeAllowed;
    }

    /**
     * Setter for upsize allowed attribute.
     * 
     * @param upsizeAllowed upsize allowed attribute.
     */
    public void setUpsizeAllowed(boolean upsizeAllowed) {
        this.upsizeAllowed = upsizeAllowed;
    }

    /**
     * Getter for transformations on this part.
     * 
     * @return transformations.
     */
    public Transformations getTransformations() {
        return this.transformations;
    }

    /**
     * Setter for transformations on this part.
     * 
     * @param transformations transformations.
     */
    public void setTransformations(Transformations transformations) {
        this.transformations = transformations;
    }

}
