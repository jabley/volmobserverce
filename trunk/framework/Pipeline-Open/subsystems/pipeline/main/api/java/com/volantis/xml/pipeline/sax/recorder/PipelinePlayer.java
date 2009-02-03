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

import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLProcess;
import org.xml.sax.SAXException;

/**
 * Plays previously recorded SAX events to the specified process.
 *
 * <p>Instances of this are not thread safe and therefore must only be used by
 * one thread at a time.</p>
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
public interface PipelinePlayer {

    /**
     * Play the previous recorded SAX events to the process.
     *
     * <p>It will update the {@link XMLPipelineContext} associated with the
     * specified process with information from the event stream. See
     * {@link XMLPipelineFactory#createContextUpdatingProcess()} </p>
     *
     * <p>This method can be called multiple times to replay the same stream
     * of events over and over again.</p> 
     *
     * @param process The process to which the events will be played.
     * @throws SAXException If there was a problem playing, or processing the
     *                      events.
     */
    void play(XMLProcess process)
            throws SAXException;
}
