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
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.dynamic;

import org.xml.sax.SAXException;

/**
 * Aborts the processing of the current SAX event.
 *
 * <p>This is used by the pipeline try operation to abort the processing of the
 * current SAX event. It is caught by the DynamicProcess and simply discarded
 * as it carries no useful information. The throwing of this exception avoids
 * the need for pipeline processes to check the error recovery state every time
 * that they dispatch an event down the pipeline. The use of this will affect
 * the behavior of any process that needed to do some work after dispatching the
 * event, even if, or especially if an error occurs. In those cases the process
 * should be rewritten to use a <code>try {...} finally {...}</code> block. It
 * can check the error recovery state as before using
 * {@link com.volantis.xml.pipeline.sax.XMLPipelineContext#inErrorRecoveryMode()}
 *
 * <p>This extends {@link RuntimeException} rather than
 * {@link SAXException} to avoid it being mistakenly caught by existing badly
 * written processes that insisted on rewrapping every exception that they
 * received.</p>
 */
public class ErrorRecoveryException
        extends RuntimeException {
}
