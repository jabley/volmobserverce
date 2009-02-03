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

package com.volantis.xml.pipeline.sax.impl.recorder;

import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.recorder.PipelinePlayer;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecording;
import com.volantis.xml.sax.recorder.SAXRecording;

/**
 * Implementation of {@link PipelineRecording} that ensures that the
 * {@link XMLPipelineContext} is correctly updated when played back.
 */
public class ContextPipelineRecording
        implements PipelineRecording {

    /**
     * The underlying recording.
     */
    private final SAXRecording recording;

    /**
     * Initialise.
     *
     * @param recording The underlying recording.
     */
    public ContextPipelineRecording(SAXRecording recording) {
        this.recording = recording;
    }

    // Javadoc inherited.
    public PipelinePlayer createPlayer() {
        return new ContextPipelinePlayer(recording);
    }

    // Javadoc inherited.
    public boolean isComplex() {
        return recording.isComplex();
    }
}
