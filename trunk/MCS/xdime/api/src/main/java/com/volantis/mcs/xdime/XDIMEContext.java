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
/*
----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. 
 *
----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime;

/**
 * The context for the the current XDIME Content Handler
 */
public interface XDIMEContext {

    /**
     * This method returns the current ContentWriter for the request.
     * <p/>
     * The Writer that is returned should only be considered valid between
     * calls to the #startElement method of the content handler that uses this
     * context. This is because each call to #startElement may cause a new
     * Writer to be pushed onto the stack.
     */
    public FastWriter getContentWriter ();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Sep-05	9415/1	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 28-Jul-05	9129/1	emma	VBM:2005071304 Modifications after review

 27-Jul-05	9060/4	tom	VBM:2005071304 Added Sel Select

 18-Jul-05    9021/1    ianw    VBM:2005071114 interim commit of XDIME
API for DISelect integration

 ===========================================================================
*/
