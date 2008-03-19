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
 * $Header: /src/voyager/com/volantis/mcs/pickle/Attic/PickleNativeElementImpl.java,v 1.1.2.1 2003/04/16 15:56:46 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Apr-03    Steve           VBM:2003041501 Pickle element that writes to
 *                              the nativeWriter of the protocol.
 * 19-May-03    Chris W         VBM:2003051902 AbstractElement.getNativeWriter
 *                              inlined into elementContent
 * 28-May-03    Steve           VBM:2003042206 Patch 2003041501 from Metis
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.pickle.impl;

import java.io.IOException;
import java.io.Writer;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.pickle.impl.AbstractPickleElementImpl;
import com.volantis.mcs.pickle.PickleInlineAttributes;


/**
 * Instances of this class should be used when the pickle output is to be
 * written to the current pane using the nativeWriter writer.
 */
public final class PickleNativeElementImpl extends AbstractPickleElementImpl {
    /**
     * The copyright statement.
     */
    private static String mark="(c) Volantis Systems Ltd 2002.";


    // Javadoc inherited from super class.
    public int elementEnd(MarinerRequestContext context,
        PAPIAttributes papiAttributes)
        throws PAPIException {
        // Make sure that the attributes are of the correct type.
        PickleInlineAttributes attributes=(PickleInlineAttributes)papiAttributes;

        // The super class will call the protocol.
        return super.elementEnd(context, papiAttributes);
    }

    // Javadoc inherited from super class.
    public int elementStart(MarinerRequestContext context,
        PAPIAttributes papiAttributes)
        throws PAPIException {
        // Make sure that the attributes are of the correct type.
        PickleInlineAttributes attributes=(PickleInlineAttributes)papiAttributes;

        // The super class will handle initialising the protocol attributes and
        // calling the protocol.
        return super.elementStart(context, papiAttributes);
    }
    
    // Javadoc inherited from super class.
  public Writer getContentWriter (MarinerRequestContext context,
                                  PAPIAttributes papiAttributes)
    throws PAPIException {

    MarinerPageContext pageContext
        = ContextInternals.getMarinerPageContext (context);
    VolantisProtocol protocol = pageContext.getProtocol ();            
    Writer writer = protocol.getNativeWriter();

    return writer;
  }
}


/*
 * Local variables:
 * c-basic-offset: 2
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/1	ianw	VBM:2005051203 Final chunk of resolving accurev hell

 18-May-05	8196/4	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Jun-03	459/1	mat	VBM:2003061910 Change getContentWriter() to return correct nativeWriter for Native markup elements

 ===========================================================================
*/
