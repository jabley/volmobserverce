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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/WMLVersion1_1.java,v 1.2 2002/03/22 18:24:28 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Jul-01    Paul            VBM:2001062810 - Added this header, fixed
 *                              the code so that it only gets values out of
 *                              attributes once and uses append for each
 *                              separate part of the output string instead of
 *                              inline string concatenation. Also made use of
 *                              the new getStyle method in VolantisProtocol to
 *                              simplify the code, implemented the extended
 *                              function form methods and sorted out the
 *                              nested paragraph problem.
 * 16-Jul-01    Paul            VBM:2001070508 - Fixed javadoc comments.
 * 23-Jul-01    Paul            VBM:2001070507 - Stopped creating StringBuffers
 *                              when returning a fixed string.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 22-Feb-02    Paul            VBM:2002021802 - Moved from protocols package
 *                              and modified to make it generate a DOM.
 * 23-May-03    Mat             VBM:2003042907 - Added constructor that 
 *                              gets the protocol configuration.
 * 30-May-03    Mat             VBM:2003042911 - Removed doProtocolString()
 *                              Tidied imports.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;


/**
 * This is a sub-class of the WMLRoot protocol class to provide the precise
 * definition of the WML Version 1.1 protocol. Very little here is different
 * from the HTMLroot class definition, so most things are referenced to the
 * superclass.
 */
public class WMLVersion1_1
        extends WMLRoot {

    /**
     * Initialise this object.
     *
     * @param supportFactory The factory used by the protocol to obtain support
     *                       objects.
     * @param configuration  The protocol specific configuration.
     */
    public WMLVersion1_1(
            ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {

        super(supportFactory, configuration);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 23-Jun-05	8833/3	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 05-Jun-03	285/4	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
