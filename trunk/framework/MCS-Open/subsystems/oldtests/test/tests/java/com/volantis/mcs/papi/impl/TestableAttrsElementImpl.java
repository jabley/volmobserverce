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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/TestableAttrsElementImpl.java,v 1.2 2003/04/22 10:59:03 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Apr-03    Allan           VBM:2003041506 - "Testable" interface for 
 *                              AttrsElements. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.protocols.MCSAttributes;

/**
 * A "testable" interface for AttrsElements.
 */ 
public interface TestableAttrsElementImpl {
    /**
     * Get the real element associated with this TestableAttrsElementImpl.
     * @return the AttrsElement that this TestableAttrsElementImpl is or that it
     * wraps.
     */ 
    public AttrsElementImpl getElement();
    
    /**
     * Set the Volantis attributes assocated with this TestableAttrsElementImpl.
     * @param attributes
     */ 
    public void setVolantisAttributes(MCSAttributes attributes);

    /**
     * @return true if the writeOpenMarkup() method in this 
     * TestableAttrsElementImpl has been called since the last call to this
     * method; otherwise false.
     */ 
    public boolean writeOpenMarkupHasBeenCalled();
    
    /**
     * @return true if the writeCloseMarkup() method in this 
     * TestableAttrsElementImpl has been called since the last call to this
     * method; otherwise false.
     */ 
    public boolean writeCloseMarkupHasBeenCalled();
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
