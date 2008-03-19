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
 * Image element model class.
 */
public class Image extends Media {
    
    /**
     * Codec attribute.
     */
    protected String codec;

    /**
     * Codec parameters properties.
     */
    protected Properties codecParams;

    /**
     * Image color scheme attribute.
     */
    protected ImageColorScheme colorScheme;

    /**
     * Width attribute.
     */
    protected int width;

    /**
     * Height attribute.
     */
    protected int height;

    /**
     * Resize directive attribute.
     */
    protected String resizeDirective;

    /**
     * Upsize allowed attribute.
     */
    protected boolean upsizeAllowed;

    /**
     * Getter for codec attribute. 
     * 
     * @return codec attribute.
     */
    public String getCodec() {
        return this.codec;
    }

    /**
     * Setter for codec attribute.
     * 
     * @param codec codec.
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
     * Getter for image color scheme. 
     * 
     * @return color scheme.
     */
    public ImageColorScheme getColorScheme() {
        return this.colorScheme;
    }

    /**
     * Setter for image color scheme.
     * 
     * @param colorScheme image color scheme.
     */
    public void setColorScheme(ImageColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    /**
     * Getter for width attribute. 
     * 
     * @return width attribute of image.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Setter for width attribute.
     * 
     * @param width width.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Getter for height attribute. 
     * 
     * @return height of image.
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
     * Getter for upsize allowed. 
     * 
     * @return upsize allowed attribute.
     */
    public boolean getUpsizeAllowed() {
        return this.upsizeAllowed;
    }

    /**
     * Setter for upsize allowed.
     * 
     * @param upsizeAllowed upsize allowed.
     */
    public void setUpsizeAllowed(boolean upsizeAllowed) {
        this.upsizeAllowed = upsizeAllowed;
    }

}
