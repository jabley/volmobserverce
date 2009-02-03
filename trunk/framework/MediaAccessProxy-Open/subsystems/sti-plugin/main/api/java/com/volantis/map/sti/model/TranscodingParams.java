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
 * Transcoding parameters element model class.
 */
public class TranscodingParams {
    
    /**
     * Audio attribute.
     */
    protected Audio audio;

    /**
     * Image attribute.
     */
    protected Image image;

    /**
     * Video attribute.
     */
    protected Video video;

    /**
     * Text attribute.
     */
    protected Text text;

    /**
     * Multipart.
     */
    protected Multipart multipart;

    /**
     * Size limit attribute.
     */
    protected long sizeLimit;

    /**
     * Extension data properties.
     */
    protected Properties extensionData;

    /**
     * Getter for audio part.
     * 
     * @return audio.
     */
    public Audio getAudio() {
        return this.audio;
    }

    /**
     * Setter for audio part.
     * 
     * @param audio.
     */
    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    /**
     * Getter for image part.
     * 
     * @return image.
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Setter for image part.
     * 
     * @param image.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Getter for video part.
     * 
     * @return video.
     */
    public Video getVideo() {
        return this.video;
    }

    /**
     * Setter for video part.
     * 
     * @param video.
     */
    public void setVideo(Video video) {
        this.video = video;
    }

    /**
     * Getter for text part.
     * 
     * @return text.
     */
    public Text getText() {
        return this.text;
    }

    /**
     * Setter for text part.
     * 
     * @return text.
     */
    public void setText(Text text) {
        this.text = text;
    }

    /**
     * Getter for multipart.
     * 
     * @return multipart.
     */
    public Multipart getMultipart() {
        return this.multipart;
    }

    /**
     * Setter for multipart.
     * 
     * @param multipart.
     */
    public void setMultipart(Multipart multipart) {
        this.multipart = multipart;
    }

    /**
     * Getter for size limit attribute.
     * 
     * @return size limit.
     */
    public long getSizeLimit() {
        return this.sizeLimit;
    }

    /**
     * Setter for size limit attribute.
     * 
     * @param sizeLimit size limit.
     */
    public void setSizeLimit(long sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    /**
     * Getter for extension data properties. 
     * 
     * @return extension data properties.
     */
    public Properties getExtensionData() {
        return this.extensionData;
    }

    /**
     * Setter for extension data properties.
     * 
     * @param extensionData extension data properties.
     */
    public void setExtensionData(Properties extensionData) {
        this.extensionData = extensionData;
    }

}
