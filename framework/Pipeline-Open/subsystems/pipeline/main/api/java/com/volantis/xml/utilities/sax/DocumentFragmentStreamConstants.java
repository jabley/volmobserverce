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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 15-May-2003  Sumit       VBM:2003050606 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.utilities.sax;

/**
 * This is an interface that defines the names of the elements used by
 * the AddRootElement InputStream and Reader to use as a root element
 */
public interface DocumentFragmentStreamConstants {
    /**
     * The array of characters that opens the pseudo root element
     */
    public static final byte OPEN_ELEMENT[] = {'<', 'f', 'r', 'a', 'g', 'm', 'e', 'n', 't', '>'};

    /**
     * The array of characters that closes the pseudo root element
     */
    public static final byte CLOSE_ELEMENT[] = {'<', '/', 'f', 'r', 'a', 'g', 'm', 'e', 'n', 't', '>'};

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
