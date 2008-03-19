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

package com.volantis.xml.pipeline.sax.recorder;

import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLProcess;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Records stream of SAX events within the pipeline to be replayed at a later
 * date, possibly many times.
 *
 * <p>The recorder is used as follows:</p>
 * <pre>
 *     ...XMLPipeline pipeline...
 *     XMLPipelineContext context = pipeline.getPipelineContext();
 *     XMLPipelineFactory factory = context.getPipelineFactory();
 *     PipelineRecorder recorder = factory.createPipelineRecorder();
 *     recorder.startRecording(pipeline);
 *
 * either
 *     ContentHandler handler = recorder.getRecordingHandler();
 *     handler.startElement(...);
 *        :
 *        :   Pump events to be recorded to the handler.
 *        :
 *
 * or
 *     XMLProcess process = recorder.getRecordingProcess();
 *     ....addProcess(process);
 *        :
 *        :   Pump events to be recorded down the pipeline.
 *        :
 *
 *     PipelineRecording recording = recorder.stopRecording();
 *     ...store recording...
 *
 * some time later
 *
 *     ....XMLProcess process....
 *     PipelinePlayer player = recording.createPlayer();
 *     player.play(process);
 *
 * </pre>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @volantis-api-exclude-from PublicAPI
 * @volantis-api-exclude-from ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface PipelineRecorder {
    /**
     * Start the event recording.
     *
     * <p>This method will cause the recorded SAX event stream to be annotated
     * with information from the {@link XMLPipelineContext} associated with the
     * specified pipeline. See
     * {@link XMLPipelineFactory#createContextAnnotatingProcess()}.</p>
     *
     * @throws SAXException          if there was a problem starting the
     *                               recorder
     * @throws IllegalStateException if the recorder had previously been
     *                               started or stopped.
     */
    void startRecording(XMLPipeline pipeline) throws SAXException;

    /**
     * Start the event recording.
     *
     * <p>This method will not annotate the recorded SAX event stream with
     * information from the associated {@link XMLPipelineContext}.</p>
     *
     * @throws SAXException          if there was a problem starting the
     *                               recorder
     * @throws IllegalStateException if the recorder had previously been
     *                               started or stopped.
     */
    void startRecording() throws SAXException;

    /**
     * Get the {@link ContentHandler} that will record events directed to it.
     *
     * @return The recording {@link ContentHandler}.
     */
    ContentHandler getRecordingHandler();

    /**
     * Get a process that can be added into the pipeline and that will record
     * the events directed to it.
     *
     * @return The recording process to add to the pipeline.
     */
    XMLProcess getRecordingProcess();

    /**
     * Stop the event recording.
     *
     * <p>This stops recording and creates and returns the
     * {@link PipelineRecording}.</p>
     *
     * @return The newly created {@link PipelineRecording}.
     * @throws SAXException          if there was a problem stopping the
     *                               recorder
     * @throws IllegalStateException if the recorder had previously been
     *                               stopped or has not been started.
     */
    PipelineRecording stopRecording() throws SAXException;
}
