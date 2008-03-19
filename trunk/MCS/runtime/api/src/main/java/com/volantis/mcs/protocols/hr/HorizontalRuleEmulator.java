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

package com.volantis.mcs.protocols.hr;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ProtocolException;

/**
 * Interface for emulating <code>HR</code> element in non-supported protocols.
 */
public interface HorizontalRuleEmulator {

    /**
     * Emulate an <code>HR</code> element
     *
     * @param domOutputBuffer DOM buffer holidng the resultant output
     * @param hrAttrs         <code>HR</code> attributes
     *
     * @return A reference to the element used to emulate the <code>HR</code>
     *         element, or an <code>HR</code> element if emulation is not
     *         required. Calling code can check to see which element has been
     *         returned to decide upon further processing
     *
     * @throws ProtocolException
     */
    public Element emulateHorizontalRule(DOMOutputBuffer domOutputBuffer,
                                         HorizontalRuleAttributes hrAttrs)
            throws ProtocolException;

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9565/1	ibush	VBM:2005081219 HR Rule Emulation

 ===========================================================================
*/
