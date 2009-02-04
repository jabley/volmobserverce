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
 * $Header: /src/voyager/com/volantis/charset/configuration/Charsets.java,v 1.2 2003/04/28 15:36:22 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Apr-03    Mat             VBM:2003040701 - Holds information on charsets
 * 22-Apr-03    Mat             VBM:2003040701 - Updated javadoc
 * 09-May-03    Mat             VBM:2003040701 - Initialise charsets in 
 *                              constructor
 * ----------------------------------------------------------------------------
 */

package com.volantis.charset.configuration;

import java.util.ArrayList;

/**
 * Contains a map of Charset classes, as parsed from the charset-config file.
 * @author  mat
 */
public class Charsets {

    /**
     * Holds a list of the charset elements present in the config file.
     */
    private ArrayList charsets = null;
    
    /** Creates a new instance of CharsetConfiguration */
    public Charsets() {
	charsets = new ArrayList();
    }

    /** 
     * Add a charset
     *
     * @param charset The charset to add
     */
    public void addCharset(Charset charset) {
        charsets.add(charset);
    }

    /**
     * Get the list of charsets
     *
     * @return The list of charsets
     */
    public ArrayList getCharsets() {
        return charsets;
    }
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
