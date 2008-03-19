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
 * $Header: /src/voyager/com/volantis/mcs/papi/XFMultipleSelectElement.java,v 1.4 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Nov-01    Paul            VBM:2001112909 - Created.
 * 04-Mar-02    Paul            VBM:2001101803 - Replaced the elementStartImpl
 *                              method which specified whether the select
 *                              element is multiple or not with a call to the
 *                              constructor of the super class which does the
 *                              same thing.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

/**
 * The xfmuselect element.
 */
public class XFMultipleSelectElementImpl
        extends XFSelectElementImpl {

    /**
     * Create a new <code>XFMultipleSelectElement</code>.
     */
    public XFMultipleSelectElementImpl() {
        super(true);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/3	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
