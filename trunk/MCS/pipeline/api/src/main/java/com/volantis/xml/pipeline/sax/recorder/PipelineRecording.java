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

/**
 * A recording of a stream of SAX events.
 *
 * <p>This is thread safe (although the created {@link PipelinePlayer}s are
 * not) so can be safely cached and used by multiple threads.</p>
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
public interface PipelineRecording {

    /**
     * Create a {@link PipelinePlayer} to play the recording.
     *
     * <p>The returned player is not thread safe and so a new one needs
     * creating for every thread that wishes to play a recording. However, it
     * is reusable, i.e. can play the same recording as many times as
     * necessary.</p>
     *
     * @return The {@link PipelinePlayer} to play the recording.
     */
    PipelinePlayer createPlayer();

    /**
     * Indicates whether the content is complex, i.e. contains elements.
     *
     * @return True if the content is complex, false otherwise.
     */
    boolean isComplex();
}
