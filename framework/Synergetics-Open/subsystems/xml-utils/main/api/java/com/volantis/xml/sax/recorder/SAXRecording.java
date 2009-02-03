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
 * Contains recorded SAX events that can be repeatedly played back.
 *
 * <p>Playback is thread safe, so multiple threads can use the same recording
 * simultaneously but they each need their own {@link SAXPlayer}.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface SAXRecording {

    /**
     * Create a {@link SAXPlayer} that will play this recording.
     *
     * @return The newly created {@link SAXPlayer}.
     */
    public SAXPlayer createPlayer();

    /**
     * Indicates whether the context is complex.
     *
     * <p>Complex content contains both elements and optionally character data,
     * simple content on the other hand only contains character data.</p>
     *
     * @return True if the recording contains complex data, false otherwise.
     */
    public boolean isComplex();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Apr-05	1262/1	pduffin	VBM:2005041105 Added support for preparsing the pipeline template

 ===========================================================================
*/
