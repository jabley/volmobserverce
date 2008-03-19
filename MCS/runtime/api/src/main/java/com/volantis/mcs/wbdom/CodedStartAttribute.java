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

import com.volantis.mcs.wbsax.AttributeStartCode;
import com.volantis.mcs.wbsax.SingleByteInteger;

/**
 * A {@link WBDOMAttribute} with a coded "start", which includes a name and 
 * an optional value prefix.
 * <p>
 * This corresponds to a WBSAX attribute beginning with an 
 * {@link AttributeStartCode}.
 */ 
public class CodedStartAttribute extends WBDOMAttribute 
        implements CodedNameProvider {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The "start" of the attribute, which includes the name and an 
     * optional value prefix in a single code.
     */
    private AttributeStartCode startCode;

    /**
     * Create a new instance of this class, with the coded "start" provided.
     */
    public CodedStartAttribute(AttributeStartCode startCode) {
        this.startCode = startCode;
    }

    // Javadoc inherited.
    public String getName() {
        return startCode.getName();
    }

    // Javadoc inherited.
    public String getValuePrefix() {
        return startCode.getValuePrefix();
    }

    // Javadoc inherited.
    public SingleByteInteger getCodedName() {
        return startCode;
    }

    // Javadoc inherited.
    public void accept(NameVisitor visitor) throws WBDOMException {
        visitor.visitCodeProvider(this);
    }
    
    // Javadoc inherited.
    public String toString() {
        return getClass().getName()
                + "@" + Integer.toHexString(System.identityHashCode(this))
                + " [" + getName() + "=" + getValueBuffer() + "]";
    }

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
