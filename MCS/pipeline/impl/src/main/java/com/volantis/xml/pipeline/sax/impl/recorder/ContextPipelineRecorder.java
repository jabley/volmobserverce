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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.impl.recorder;

import com.volantis.xml.pipeline.sax.XMLHandlerAdapter;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextAnnotatingProcess;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecorder;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecording;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextAnnotatingProcess;
import com.volantis.xml.sax.recorder.SAXRecorder;
import com.volantis.xml.sax.recorder.SAXRecorderConfiguration;
import com.volantis.xml.sax.recorder.SAXRecorderFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * This event recorder is annotated from the context of a specified pipeline
 * on recording.
 *
 * <p>The recording it creates will ensure that at playback time it updates the
 * context of a specified playback pipeline.</p>
 */
public class ContextPipelineRecorder implements PipelineRecorder {

    /**
     * The underlying SAX event recorder.
     */
    private SAXRecorder recorder;

    /**
     * The process that annotates the stream with contextual information.
     *
     * <p>This must not be made visible outside this class, at least not as
     * an {@link XMLProcess} as it could be added to a pipeline which would
     * stops it sending its events to the contained recorder.</p>
     */
    private XMLProcess streamAnnotatingProcess;

    /**
     * The handler to which events that are to be recorded are directed.
     */
    private ContentHandler recordingHandler;

    /**
     * A wrapper around the {@link #streamAnnotatingProcess}.
     */
    private XMLProcess process;

    // Javadoc inherited.
    public void startRecording(XMLPipeline pipeline) throws SAXException {
        if (streamAnnotatingProcess != null) {
            throw new IllegalStateException(
                    "Cannot restart a recorder that has already been started.");
        }

        SAXRecorderFactory factory = SAXRecorderFactory.getDefaultInstance();
        SAXRecorderConfiguration configuration =
                factory.createSAXRecorderConfiguration();
        configuration.setRecordPerEventLocation(true);
        recorder = factory.createSAXRecorder(configuration);

        recordingHandler = recorder.getContentHandler();
        if (pipeline != null) {
            XMLProcess cap = new ContextAnnotatingProcess(true);
            cap.setPipeline(pipeline);
            XMLHandlerAdapter adapter = new XMLHandlerAdapter();
            adapter.setContentHandler(recordingHandler);
            cap.setNextProcess(adapter);
            cap.startProcess();

            streamAnnotatingProcess = cap;
            recordingHandler = streamAnnotatingProcess;
        }
    }

    // Javadoc inherited.
    public void startRecording() throws SAXException {
        startRecording(null);
    }

    // Javadoc inherited.
    public PipelineRecording stopRecording() throws SAXException {
        if (recordingHandler == null) {
            throw new IllegalStateException("Cannot stop a recorder that " +
                                            "has not already been started.");
        }

        if (streamAnnotatingProcess != null) {
            streamAnnotatingProcess.stopProcess();
            streamAnnotatingProcess = null;
        }

        recordingHandler = null;

        return new ContextPipelineRecording(recorder.getRecording());
    }

    // Javadoc inherited.
    public ContentHandler getRecordingHandler() {
        return recordingHandler;
    }

    // Javadoc inherited.
    public XMLProcess getRecordingProcess() {
        if (recordingHandler == null) {
            throw new IllegalStateException(
                    "Cannot access the process until after the recorder " +
                    "has been started");
        }
        if (streamAnnotatingProcess == null) {
            throw new IllegalStateException(
                    "Cannot access the process unless within a pipeline");
        }

        if (process == null) {
            XMLHandlerAdapter adapter = new XMLHandlerAdapter();
            adapter.setContentHandler(streamAnnotatingProcess);
            adapter.setErrorHandler(streamAnnotatingProcess);
            process = adapter;
        }
        return process;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Feb-04	525/3	adrian	VBM:2004011902 fixed bug setting the baseuri on included content within template bindings

 05-Feb-04	525/1	adrian	VBM:2004011902 fixed rework issues for baseuri support work

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 ===========================================================================
*/
