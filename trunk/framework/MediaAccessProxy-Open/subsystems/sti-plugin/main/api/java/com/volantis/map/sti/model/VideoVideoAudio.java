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
 * Video audio element model class.
 */
public class VideoVideoAudio {
    
    /**
     * Codec attribute.
     */
    protected String codec;

    /**
     * Codec parameters.
     */
    protected Properties codecParams;

    /**
     * Size limit of audio part.
     */
    protected long sizeLimit;

    /**
     * Bit rate.
     */
    protected int bitRate;

    /**
     * Sampling rate.
     */
    protected int samplingRate;

    /**
     * Sampling resolution.
     */
    protected int samplingResolution;

    /**
     * Channels attribute.
     */
    protected String channels;

    /**
     * Transformations for this part.
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
     * Getter for sampling rate attribute.
     * 
     * @return sampling rate.
     */
    public int getSamplingRate() {
        return this.samplingRate;
    }

    /**
     * Setter for sampling rate attribute.
     * 
     * @param samplingRate sampling rate.
     */
    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }

    /**
     * Getter for sampling resolution attribute.
     * 
     * @return sampling resolution.
     */
    public int getSamplingResolution() {
        return this.samplingResolution;
    }

    /**
     * Setter for sampling resolution attribute.
     * 
     * @param samplingResolution sampling resolution.
     */
    public void setSamplingResolution(int samplingResolution) {
        this.samplingResolution = samplingResolution;
    }

    /**
     * Getter for channels attribute.
     * 
     * @return channels.
     */
    public String getChannels() {
        return this.channels;
    }

    /**
     * Setter for channels attribute.
     * 
     * @param channels channels.
     */
    public void setChannels(String channels) {
        this.channels = channels;
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
