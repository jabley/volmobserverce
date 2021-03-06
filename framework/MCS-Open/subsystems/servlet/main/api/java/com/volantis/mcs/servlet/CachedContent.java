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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet;

import com.volantis.shared.content.ContentStyle;

import java.io.IOException;

public interface CachedContent {

    ContentStyle getContentStyle();

    byte[] getAsByteArray() throws IOException;

    char[] getAsCharArray() throws IOException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-05	10756/9	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 08-Dec-05	10677/1	geoff	VBM:2005120708 Orange pages have literal non-breaking spaces in them.

 ===========================================================================
*/
