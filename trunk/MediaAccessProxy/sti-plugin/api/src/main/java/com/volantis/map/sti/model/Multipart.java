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
 * Multipart element model class.
 */
public class Multipart extends Media {
    
    /**
     * Multipart presentation.
     */
    protected MultipartPresentation presentation;

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
     * Getter for multipart presentation attribute.
     * 
     * @return multipart presentation.
     */
    public MultipartPresentation getPresentation() {
        return this.presentation;
    }

    /**
     * Setter for multipart presentation.
     * 
     * @param presentation
     */
    public void setPresentation(MultipartPresentation presentation) {
        this.presentation = presentation;
    }

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
     * @param audio
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
     * @param image
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
     * @param video
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
     * Setter for Text part.
     * 
     * @param text
     */
    public void setText(Text text) {
        this.text = text;
    }

}
