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
* (c) Volantis Systems Ltd 2005. 
* ----------------------------------------------------------------------------
*/


package com.volantis.mcs.papi;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.context.MarinerRequestContext;

import java.io.Writer;

/**
 * This class provides the common code for delegating to a real PAPI implemntion class.
 * <p>This class must not be part of the public API</p>
 */
public class AbstractPAPIDelegatingElement implements DeprecatedPAPIElement {


    /**
     * The delegated PAPI element implementation
     */
    protected PAPIElement delegate;

    public AbstractPAPIDelegatingElement(PAPIElementFactory papiElementFactory) {
        delegate = papiElementFactory.createPAPIElement();
    }

    // Javadoc inherited from super class.
    public int elementStart (MarinerRequestContext context,
                             PAPIAttributes papiAttributes)
            throws PAPIException {

        return delegate.elementStart(context,papiAttributes);
    }

    // Javadoc inherited from super class.
    public void elementContent (MarinerRequestContext context,
                                PAPIAttributes papiAttributes,
                                String content)
            throws PAPIException {

        delegate.elementContent(context,papiAttributes, content);
    }

    // Javadoc inherited from super class.
    public Writer getContentWriter (MarinerRequestContext context,
                                    PAPIAttributes papiAttributes)
            throws PAPIException {

        return delegate.getContentWriter(context,papiAttributes);
    }

    // Javadoc inherited from super class.
    public int elementEnd (MarinerRequestContext context,
                           PAPIAttributes papiAttributes)
            throws PAPIException {

        return delegate.elementEnd(context,papiAttributes);
    }

    // Javadoc inherited from super class.
    public void elementReset (MarinerRequestContext context) {

        delegate.elementReset(context);
    }

    // Javadoc inherited from super class.
    public void elementDirect(MarinerRequestContext context,
                              String content) throws PAPIException {
        ((DeprecatedPAPIElement)delegate).elementDirect(
                context, content);
    }

    // Javadoc inherited from super class.
    public Writer getDirectWriter(MarinerRequestContext context)
            throws PAPIException {
        return ((DeprecatedPAPIElement)delegate).getDirectWriter(
                context);

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 ===========================================================================
*/
