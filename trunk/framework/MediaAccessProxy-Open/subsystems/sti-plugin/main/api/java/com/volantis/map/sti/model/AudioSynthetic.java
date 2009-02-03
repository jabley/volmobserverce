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
 * Audio synthetic element model.
 */
public class AudioSynthetic {
    
    /**
     * Number of channel to use attribute.
     */
    protected int channelToUse;

    /**
     * Channels priority attribute.
     */
    protected String channelsPriority;

    /**
     * Instrument attribute.
     */
    protected int instrument;

    /**
     * Getter for channel to use attribute.
     * 
     * @return channel to use.
     */
    public int getChannelToUse() {
        return this.channelToUse;
    }

    /**
     * Setter for channel to use attribute.
     * 
     * @param channelToUse channel to use.
     */
    public void setChannelToUse(int channelToUse) {
        this.channelToUse = channelToUse;
    }

    /**
     * Getter for channels priority attribute.
     * 
     * @return channels priority.
     */
    public String getChannelsPriority() {
        return this.channelsPriority;
    }

    /**
     * Setter for channels priority attribute.
     * 
     * @param channelsPriority channels priority.
     */
    public void setChannelsPriority(String channelsPriority) {
        this.channelsPriority = channelsPriority;
    }

    /**
     * Getter for instrument attribute.
     * 
     * @return instrument.
     */
    public int getInstrument() {
        return this.instrument;
    }

    /**
     * Setter for instrument attribute.
     * 
     * @param instrument instrument attribute.
     */
    public void setInstrument(int instrument) {
        this.instrument = instrument;
    }

}
