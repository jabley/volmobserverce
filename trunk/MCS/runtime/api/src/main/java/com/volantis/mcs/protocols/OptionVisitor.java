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
 * $Header: /src/voyager/com/volantis/mcs/protocols/OptionVisitor.java,v 1.3 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Jan-03    Doug            VBM:2002120213 - Created. New interface that
 *                              allows implentations to visit Options and
 *                              Option Groups.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

/**
 *  New interface that allows implentations to visit Options and Option Groups.
 */
public interface OptionVisitor {

    /**
     * Method to allow implementations to "visit" a SelectOption
     * @param selectOption the SelectOption to be visited
     * @param object param to allow an Object to be passed in
     */
    public void visit(SelectOption selectOption,
                      Object object) throws ProtocolException;

    /**
     * Method to allow implementations to "visit" a SelectOptionGroup
     * @param selectOptionGroup the SelectOptionGroup to be visited
     * @param object param to allow an Object to be passed in
     */
    public void visit(SelectOptionGroup selectOptionGroup,
                      Object object) throws ProtocolException;
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
