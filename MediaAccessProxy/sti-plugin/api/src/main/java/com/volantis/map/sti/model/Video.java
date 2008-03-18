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
 * Video element model class.
 */
public class Video extends Media {
    
    /**
     * Video visual part.
     */
    protected VideoVideoVisual videoVisual;

    /**
     * Video audio part.
     */
    protected VideoVideoAudio videoAudio;

    /**
     * Getter for visual part of this video.
     * 
     * @return visual part.
     */
    public VideoVideoVisual getVideoVisual() {
        return this.videoVisual;
    }

    /**
     * Setter for visual part of this video.
     * 
     * @param videoVisual visual part.
     */
    public void setVideoVisual(VideoVideoVisual videoVisual) {
        this.videoVisual = videoVisual;
    }

    /**
     * Getter for audio part of this video.
     * 
     * @return audio part.
     */
    public VideoVideoAudio getVideoAudio() {
        return this.videoAudio;
    }

    /**
     * Setter for audio part of this video.
     * 
     * @param videoAudio audio part.
     */
    public void setVideoAudio(VideoVideoAudio videoAudio) {
        this.videoAudio = videoAudio;
    }

}
