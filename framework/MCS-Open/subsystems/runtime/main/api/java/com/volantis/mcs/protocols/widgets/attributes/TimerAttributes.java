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
 *  * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.widgets.attributes;

/**
 * Holds properties specific to TimerElement
 */
public class TimerAttributes
        extends BaseClockAttributes {

    private Integer startTime = null;
    private Integer stopTime = null;

    /**
     * Sets start-time attribute
     *
     * @param start
     */
    public void setStartTime(Integer start) {
        this.startTime = start;
    }

    /**
     * Gets start-time attribute
     */
    public Integer getStartTime() {
        return this.startTime;
    }

    /**
     * Sets stop-time attribute
     *
     * @param stop
     */
    public void setStopTime(Integer stop) {
        this.stopTime = stop;
    }

    /**
     * Gets stop-time attribute
     */
    public Integer getStopTime() {
        return this.stopTime;
    }


    /**
     * Holds load attributes.
     */
    private LoadAttributes loadAttributes;

    /**
     * @return Returns the loadAttributes.
     */
    public LoadAttributes getLoadAttributes() {
        return loadAttributes;
    }

    /**
     * @param loadAttributes The loadAttributes to set.
     */
    public void setLoadAttributes(LoadAttributes loadAttributes) {
        this.loadAttributes = loadAttributes;
    }

}
