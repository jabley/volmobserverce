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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.sti.impl;

import com.volantis.map.operation.Operation;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.operation.Result;
import com.volantis.map.sti.transcoder.Transcoder;
import com.volantis.map.sti.transcoder.TranscoderException;
import com.volantis.map.sti.transcoder.TranscoderFactory;
import com.volantis.map.sti.transcoder.TranscoderFactoryException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The transcoding operation performing request to the STI service.
 * <p>
 * Every request, a new instance of Transcoder is obtained from specified
 * TranscodingFactory. Thus, this implementation is thread-safe.
 */
public final class DefaultOperation implements Operation {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DefaultOperation.class);

    /**
     * The Transcoder factory.
     */
    private final TranscoderFactory transcoderFactory;

    /**
     * Mutex used for thread synchronization.
     */
    private final Object mutex = new Object();

    /**
     * Initializes this Operation with TranscoderFactory, which will be used to
     * create instances of Transcoder performing actual transcoding.
     * 
     * @param transcoderFactory The TranscoderFactory to use.
     */
    public DefaultOperation(TranscoderFactory transcoderFactory) {
        this.transcoderFactory = transcoderFactory;

    }

    // Javadoc inherited
    public Result execute(ResourceDescriptor descriptor,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Result result = Result.SUCCESS;

        try {
            // Create new instance of transcoder in synchronized block, since
            // the TranscoderFactory is not guaranteed to be thread-safe.
            Transcoder transcoder;

            synchronized (mutex) {
                transcoder = transcoderFactory.createTranscoder();            }

            // Perform transcoding on local instance of transcoder,
            // thus in thread-safe way.
            transcoder.transcode(descriptor, request, response);

        } catch (TranscoderFactoryException e) {
            // thrown when the TranscoderFactory instantiation fails.
            LOGGER.error(e);
            result = Result.UNSUPPORTED;

        } catch (TranscoderException e) {
            // thrown when the transcoding process fails.
            LOGGER.error(e);
            result = Result.UNSUPPORTED;
        }

        return result;
    }
}
