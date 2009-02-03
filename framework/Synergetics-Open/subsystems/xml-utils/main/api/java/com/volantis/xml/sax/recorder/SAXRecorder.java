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

import org.xml.sax.ContentHandler;

/**
 * Records events sent to its event handler for play back later.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface SAXRecorder {

    /**
     * Get the content handler whose events will be recorded.
     *
     * @return The content handler whose events will be recorded.
     */
    public ContentHandler getContentHandler();

    /**
     * Get the recording.
     *
     * <p>This should only be called after the
     * {@link ContentHandler#endDocument} event has been processed.
     * Calling it at any other time will result in an
     * {@link IllegalStateException} being thrown.</p>
     *
     * @return The recording.
     * @throws IllegalStateException If this is called before the recording
     *                               has finished.
     */
    public SAXRecording getRecording();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Apr-05	1262/1	pduffin	VBM:2005041105 Added support for preparsing the pipeline template

 ===========================================================================
*/
