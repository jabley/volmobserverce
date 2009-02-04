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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom;

import com.volantis.mcs.wbsax.SingleByteInteger;

/**
 * An interface for WBDOM objects which provide coded name information.
 * <p>
 * Instances of {@link NameVisitor} use this interface to extract coded name 
 * information from instances of {@link CodedNameElement} and 
 * {@link CodedStartAttribute}.
 * <p>
 * Name codes in WBSAX are different types and this just provides the name 
 * code as the parent type - {@link SingleByteInteger}. Thus, it is up the 
 * caller to cast it to the appropriate subtype if necessary.
 */ 
public interface CodedNameProvider {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";
    
    /**
     * Get the code containing name information.
     * 
     * @return the code.
     */ 
    SingleByteInteger getCodedName();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/8	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/
