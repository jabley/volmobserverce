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
 * $Header: /src/voyager/com/volantis/mcs/protocols/TimerAttributes.java,v 1.2 2002/08/06 14:10:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06 Aug 02    sumit           VBM:2002080509 - Created for <timer>
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;


public class TimerAttributes
        extends MCSAttributes {

    /**
     * The duration attribute of the timer element
     */
    private String duration;

    /**
     * Create a new <code>TimerAttributes</code>.
     */
    public TimerAttributes() {
        initialise();
    }

    /**
     * Reinitialise all the data members. This is called from the constructor and
     * also from reset.
     */
    private void initialise() {
        duration = null;
    }

    /**
     * Set the value of the duration attribute.
     *
     * @param duration The new value of the duration attribute.
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * Get the value of the duration attribute.
     *
     * @return The value of the duration attribute.
     */
    public String getDuration() {
        return duration;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
