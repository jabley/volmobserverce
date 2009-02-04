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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.output;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;

import java.io.IOException;

/**
 * Interface defining an Outputter capable of writing DOM Documents and
 * DOM Element hierarchies.
 */
public interface DocumentOutputter {
    
    /**
     * Output a DOM Document.
     * @param document the Document to output
     * @throws IOException if an error occurs during output
     */
    public void output(Document document) throws IOException;

    /**
     * Output a DOM Element Hierarchy.
     * @param element the Element to output
     * @throws IOException if an error occurs during output
     */
    public void output(Element element) throws IOException;

    /**
     * Closes the underlying writer after calling flush.
     *
     * @throws IOException if an error occurs during the close.
     */
    public void flush() throws IOException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling

 05-Feb-04	2794/2	steve	VBM:2004012613 HTML Quote handling

 ===========================================================================
*/
