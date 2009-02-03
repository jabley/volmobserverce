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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.flow;

/**
 * A pipeline object that can control the flow of events through the pipeline.
 * <p>The flow control mechanism is intended to allow the flow of events to
 * be influenced at the earliest possible point in the pipeline. The ideal of
 * course is for the events never to even be introduced into the pipeline at
 * all but that is dependent on the source of the events being able to handle
 * flow control.</p>
 * <p>Just because a flow controller receives an event indicating that flow
 * control has started does not mean that it will actually be required to
 * perform any flow control. It all depends on whether it is responsible for
 * processing or generating events during the period in which the pipeline is in
 * flow control mode.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface FlowController {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Called when the pipeline enters flow control mode.
     */
    public void beginFlowControl();

    /**
     * Called when the pipeline exits flow control mode.
     */
    public void endFlowControl();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
