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

package com.volantis.xml.pipeline.sax;

import com.volantis.xml.pipeline.sax.flow.FlowControlManager;

/**
 * An extended {@link XMLPipelineFactory} for use within MCS only.
 */
public abstract class InternalXMLPipelineFactory
        extends XMLPipelineFactory {

    /**
     * Get the internal instance.
     *
     * @return The internal instance.
     */
    public static InternalXMLPipelineFactory getInternalInstance() {
        return (InternalXMLPipelineFactory) getDefaultInstance();
    }

    /**
     * Create an XMLPipelineFactory that can be wrapped by another factory.
     *
     * @param wrapper The wrapping factory.
     * @return The wrappable factory.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public abstract InternalXMLPipelineFactory createWrappableFactory(
            InternalXMLPipelineFactory wrapper);

    /**
     * Create a {@link FlowControlManager} instance.
     *
     * @return The manager.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public abstract FlowControlManager createFlowControlManager();
}
