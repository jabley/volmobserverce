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
package com.volantis.mcs.wbdom.io;


/**
 * An interface for clients of WBDOM to implement which controls the various
 * aspects of WBDOM serialisation behaviour, as defined in 
 * {@link EmptyElementConfiguration} and {@link URLAttributeConfiguration}.
 */ 
public interface SerialisationConfiguration 
        extends EmptyElementConfiguration, URLAttributeConfiguration {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
