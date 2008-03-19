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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-03-03     Doug            VBM:2003030405 - Created. Interface that
 *                              allows a XMLPipeline to be treated as an
 *                              XMLFilter.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax;

import org.xml.sax.XMLFilter;

/**
 * A pipeline encapsulated inside an XMLFilter.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * <p>Apart from the additional requirements added by the implementation of
 * the XMLFilter interface and the fact that the XMLPipelineReader invokes
 * an XMLReader to do the parsing rather than delegating to the parent this has
 * identical functionality to the XMLPipelineReader.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface XMLPipelineFilter
        extends XMLFilter, XMLPipelineReader {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 20-Jan-04	529/1	adrian	VBM:2004011904 Pipeline API updates in preparation for fully integrating ContextUpdating/Annotating processes

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 ===========================================================================
*/
