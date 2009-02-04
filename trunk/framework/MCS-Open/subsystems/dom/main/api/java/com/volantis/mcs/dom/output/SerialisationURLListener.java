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
package com.volantis.mcs.dom.output;

/**
 * A listener interface for URLs found during DOM serialisation.
 * <p>
 * Implementations of this interface may be provided at DOM serialisation time,
 * and any URLs which are found whilst performing the serialisation will be
 * passed back through the {@link #foundURL} method.
 * <p>
 * This is particularly useful for clients which need to know which URLs are
 * referenced by a page which is being generated. For now, this means the 
 * packaging implementation.
 * <p>
 * NOTE: I have put this class in the dom.output (and others in .dom package) 
 * package with the expectation that the remaining MCS DOM classes will be 
 * eventually be refactored away to a mcsdom package which is a peer of the 
 * wbdom package. Thus the dom package will become the repository for classes 
 * which are shared betweeen the different DOM implementations.
 */ 
public interface SerialisationURLListener {

    /**
     * This method will be called at the point where a URL is serialised
     * to the output.
     *  
     * @param url the url which was found.
     */ 
    void foundURL(String url);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/5	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
