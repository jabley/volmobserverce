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
 * $Header: /src/voyager/com/volantis/mcs/runtime/URLConstants.java,v 1.4 2002/03/18 12:41:19 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Feb-02    Paul            VBM:2002021203 - Created to make sure that
 *                              all code uses the same names for the different
 *                              url parameters we have.
 * 19-Feb-02    Paul            VBM:2001100102 - Added FORM_PARAMETER.
 * 21-Feb-02    Steve           VBM:2001101803 - Added from fragmentation parameters
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

/**
 * This interface contains URL related constants.
 */
public interface URLConstants {

    public static final String FRAGMENTATION_PARAMETER = "vfrag";

    public static final String FORM_FRAGMENTATION_PARAMETER = "vffrag";

    public static final String SEGMENTATION_PARAMETER = "defseg";

    public static final String FORM_PARAMETER = "vform";

    public static final String NEXT_FORM_FRAGMENT = "vnext";

    public static final String PREV_FORM_FRAGMENT = "vprevious";

    public static final String RESET_FORM_FRAGMENT = "vreset";

    public static final String ACTION_FORM_FRAGMENT = "vaction";
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
