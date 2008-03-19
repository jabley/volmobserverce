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
 * $Header: /src/voyager/com/volantis/mcs/layouts/LayoutException.java,v 1.5 2002/03/18 12:41:16 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Jun-01    Paul            VBM:2001062704 - Added this change history and
 *                              sorted out the copyright.
 * 29-Oct-01    Paul            VBM:2001102901 - Update javadoc.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.repository.RepositoryException;

/**
 * The class for exceptions caused by problems processing layouts.
 */
public class LayoutException
        extends RepositoryException {

    /**
     * Copyright mark.
     */
    private static String mark = "(c) Volantis Systems Ltd 2000. ";

    // javadoc inherited
    public LayoutException(String message) {
        super(message);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-May-05	8432/3	matthew	VBM:2005042912 remove ExceptionLocalizer from LayoutException

 23-May-05	8432/1	matthew	VBM:2005042912 remove ExceptionLocalizer from LayoutException

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
