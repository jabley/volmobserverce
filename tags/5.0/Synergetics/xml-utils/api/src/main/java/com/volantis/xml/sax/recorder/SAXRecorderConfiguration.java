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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.sax.recorder;

/**
 * The configuration for a {@link SAXRecorder}.
 *
 * <p>This is a place holder for future configuration options that will
 * control the behaviour of the recorded, e.g. the ability to ignore various
 * events.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface SAXRecorderConfiguration {

    /**
     * Controls what the recorder does with ignorable white space.
     *
     * <p>If true then the recorder will not include it in the recording that
     * it is creating, otherwise it will.</p>
     *
     * @param discardIgnorableWhitespace The new value of the
     * discardIgnorableWhitespace property.
     */
    void setDiscardIgnorableWhitespace(boolean discardIgnorableWhitespace);

    /**
     * Get the value of the discardIgnorableWhitespace property.
     *
     * @return The value of the discardIgnorableWhitespace property.
     */
    boolean getDiscardIgnorableWhitespace();

    /**
     * Controls whether the recorder records per event location information
     * or not.
     *
     * <p>If true then the recorder will record per event location information,
     * i.e. line and column number, system and public id, otherwise it will
     * not.</p>
     *
     * @param recordPerEventLocation The new value of the
     * recordPerEventLocation property.
     */
    void setRecordPerEventLocation(boolean recordPerEventLocation);

    /**
     * Get the value of the recordPerEventLocation property.
     *
     * @return The value of the recordPerEventLocation property.
     */
    boolean getRecordPerEventLocation();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Apr-05	1262/1	pduffin	VBM:2005041105 Added support for preparsing the pipeline template

 ===========================================================================
*/
