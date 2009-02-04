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

package com.volantis.mcs.protocols.css.reference;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.css.CssCandidate;

import java.io.IOException;

/**
 * Wraps a {@link CssReference} to make it behave like a {@link CssCandidate}.
 *
 * @todo This should be removed when the CssCandidate code is removed.
 */
public class CssCandidateAdapter
    implements CssCandidate {

    /**
     * The CSS reference.
     */
    private final CssReference reference;

    /**
     * Initialise.
     *
     * @param reference The object to adapt.
     */
    public CssCandidateAdapter(CssReference reference) {
        this.reference = reference;
    }

    // Javadoc inherited.
    public void writeCss(VolantisProtocol protocol, OutputBuffer buffer)
            throws IOException {

        reference.writePlaceHolderMarkup(buffer);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 ===========================================================================
*/
