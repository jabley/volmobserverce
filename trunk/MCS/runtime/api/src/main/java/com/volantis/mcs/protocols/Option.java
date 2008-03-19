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
 * $Header: /src/voyager/com/volantis/mcs/protocols/Option.java,v 1.3 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Jan-03    Doug            VBM:2002120213 - Created. New interface that
 *                              allows OptionVisitor instatances to visit 
 *                              Option implementations.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import java.util.List;

/**
 * Interface for Select control Options
 */
public interface Option {

    /**
     * Method that allows an OptionVisitor clients to "visit" an Option.
     * @param optionVisitor The OptionVisitor instance
     * @param object Allows an arbitary parameter to be passed to the
     * OptionVisitor instance.
     */
    public void visit(OptionVisitor optionVisitor, Object object)
            throws ProtocolException;

    /**
     * Tell the option what has been selected. The option may then
     * select/deselect itself.
     *
     * @param values selected
     */
    public void selectedValues(List values);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
