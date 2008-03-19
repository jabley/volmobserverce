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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom;

/**
 * A marker interface to identify classes which annotate a document in a DOM.
 * <p>
 * Currently this is in use for WBDOM and the accurate dissector, but it is
 * intended that this be used by all DOMs which can be accurately dissected.
 * <p>
 * NOTE: I have put the document and node annotation classes in the dom 
 * package with the expectation that the remaining MCS DOM classes will be 
 * eventually be refactored away to a mcsdom package which is a peer of the 
 * wbdom package. Thus the dom package will become the repository for classes 
 * which are shared betweeen the different DOM implementations.
 */ 
public interface DocumentAnnotation {
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/
