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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.Replica;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;

/**
 * A format renderer that is used to render replicas.
 *
 * @todo later is this used, or is the Replica resolved on load of the layout?
 */
public class ReplicaRenderer
        extends AbstractFormatRenderer {
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ReplicaRenderer.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                        ReplicaRenderer.class);

    // javadoc inherited
    public void render(final FormatRendererContext context, final FormatInstance instance)
            throws RendererException {
        try {
            if (!instance.isEmpty()) {
                Replica replica = (Replica)instance.getFormat();
                Format replicaFormat = replica.getFormat();

                if (logger.isDebugEnabled()) {
                    logger.debug("Rendering replicant format " +
                            replicaFormat.getFormatType());
                }

                if (replicaFormat == null) {
                    throw new IOException(exceptionLocalizer.format(
                            "render-replica-no-replicant", replica));
                } else {
                    FormatInstance replicaFormatInstance =
                            context.getFormatInstance(replicaFormat,
                                    instance.getIndex());
                    context.renderFormat(replicaFormatInstance);
                }
            }
        } catch (IOException e) {
            throw new RendererException(
                    exceptionLocalizer.format("renderer-error",
                            instance.getFormat()), e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/5	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 21-Dec-04	6402/1	philws	VBM:2004120208 Added aligning spatial format iterator renderer

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/
