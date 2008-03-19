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

package com.volantis.mcs.protocols.css;

import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DefaultProtocolSupportFactory;
import com.volantis.mcs.protocols.Inserter;
import com.volantis.mcs.protocols.DefaultContentInserter;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public abstract class DOMTransformerTestAbstract
        extends TestCaseAbstract {

    protected DOMProtocol createDOMProtocol() {
        DefaultProtocolSupportFactory supportFactory =
                new DefaultProtocolSupportFactory();
        return new DOMProtocol(supportFactory, null) {
            public String defaultMimeType() {
                return "application/xhtml+xml";
            }

            public Inserter getInserter() {
                return new DefaultContentInserter(this, null, null);
            }
        };
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 ===========================================================================
*/
