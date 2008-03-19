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
package com.volantis.mcs.protocols.html;

/**
 * This interface defines the extra methods that we need to add to 
 * {@link XHTMLBasic} in order to make it testable.
 * <p>
 * Once this code is generally understood we should probably attempt to 
 * refactor the design of the Protocols so that these methods migrate30 slowly
 * into the real Protocol interface, as the need to have these methods here
 * is a design smell, and means that we need to cut & paste code into each
 * TestProtocol subclass.
 * 
 * @see HTMLVersion4_0 
 */
public interface HTMLVersion4_0Testable extends XHTMLBasicTestable {

    /**
     * Setup css emulation
     * @param emulate Set this to true to enable css emulation.
     */
    void setUpCssEmulation(boolean emulate);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Aug-03	1240/1	chrisw	VBM:2003070811 implemented rework

 21-Aug-03	1219/1	chrisw	VBM:2003070811 implemented rework

 21-Aug-03	1152/1	chrisw	VBM:2003070811 implemented rework

 ===========================================================================
*/
