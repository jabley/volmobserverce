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
 * Audio element model class.
 */
public class Audio extends Media {
    
    /**
     * Codec attribute. 
     */
    protected String codec;

    /**
     * Parameters of codec.
     */
    protected Properties codecParams;

    /**
     * Bit rate attribute.
     */
    protected int bitRate;

    /**
     * Sampling rate attribute.
     */
    protected int samplingRate;

    /**
     * Sampling resolution attribute.
     */
    protected int samplingResolution;

    /**
     * Channels attribute.
     */
    protected String channels;

    /**
     * Audio syntheric attribute.
     */
    protected AudioSynthetic synthetic;

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
     * @param codec codec attribute.
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
     * Getter for bit rate.
     * 
     * @return bit rate.
     */
    public int getBitRate() {
        return this.bitRate;
    }

    /**
     * Setter for bit rate.
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
     * Getter for sampling resolution.
     * 
     * @return sampling resolution.
     */
    public int getSamplingResolution() {
        return this.samplingResolution;
    }

    /**
     * Setter for sampling resolution.
     * 
     * @param samplingResolution sampling resolution.
     */
    public void setSamplingResolution(int samplingResolution) {
        this.samplingResolution = samplingResolution;
    }

    /**
     * Getter for channels.
     * 
     * @return channels attribute.
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
     * Getter for audio synthetic attribute.
     * 
     * @return audio synthetic attribute.
     */
    public AudioSynthetic getSynthetic() {
        return this.synthetic;
    }

    /**
     * Setter for audio synthetic attribute.
     * 
     * @param synthetic audio synthetic.
     */
    public void setSynthetic(AudioSynthetic synthetic) {
        this.synthetic = synthetic;
    }

}
