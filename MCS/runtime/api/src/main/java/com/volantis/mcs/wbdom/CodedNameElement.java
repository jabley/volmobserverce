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

import com.volantis.mcs.wbsax.ElementNameCode;
import com.volantis.mcs.wbsax.SingleByteInteger;

/**
 * A {@link WBDOMElement} with a coded name.
 * <p>
 * This corresponds to a WBSAX element beginning with an 
 * {@link ElementNameCode}.
 */ 
public class CodedNameElement extends WBDOMElement
        implements CodedNameProvider {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The name of this element.
     */
    private ElementNameCode nameCode;

    /**
     * Create a new instance of this class, with the coded name provided.
     */
    public CodedNameElement(ElementNameCode nameCode) {
        this.nameCode = nameCode;
    }

    // Javadoc inherited.
    public String getName() {
        return nameCode.getName();
    }

    /**
     * Get the name code of this element.
     * 
     * @return the name code.
     */ 
    public ElementNameCode getNameCode() {
        return nameCode;
    }

    // Javadoc inherited.
    public SingleByteInteger getCodedName() {
        return nameCode;
    }

    // Javadoc inherited.
    public void accept(NameVisitor visitor) throws WBDOMException {
        visitor.visitCodeProvider(this);
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
