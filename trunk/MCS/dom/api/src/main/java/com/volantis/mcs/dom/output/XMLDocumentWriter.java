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
 * $Header: /src/voyager/com/volantis/mcs/dom/output/XMLOutputter.java,v 1.2 2002/11/12 09:58:37 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * 20-May-02    Paul            VBM:2002042202 - Added a space between the
 *                              name of an empty element and the /> so that
 *                              empty tags work properly on older devices.
 * 23-May-02    Paul            VBM:2002042202 - Moved from
 *                              com.volantis.mcs.dom.DOMOutputter, added
 *                              extra parameters to output methods for
 *                              a CharacterEncoding.
 * 30-Oct-02    Sumit           VBM:2002091801 - Added processAttribute(..)
 *                              method to allow subclasses to handle attrs
 * 20-May-03    Mat             VBM:2003042907 - Changed to implement
 *                              DocumentOutputter and to accept the
 *                              Writer and CharacterEncoder on the constructor
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom.output;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.xml.XMLDTD;
import com.volantis.mcs.dom.xml.XMLDTDBuilder;

import java.io.IOException;
import java.io.Writer;

/**
 * Output DOM Elements and Text nodes in XML format.
 */
public class XMLDocumentWriter
        extends AbstractDocumentWriter {

    private static final XMLDTD DEFAULT_DTD;
    static {
        XMLDTDBuilder builder = new XMLDTDBuilder();
        DEFAULT_DTD = (XMLDTD) builder.buildDTD();
    }

    /**
     * Constructor.
     *
     * @param writer the writer
     */
    public XMLDocumentWriter(Writer writer) {
        this(writer, DEFAULT_DTD);
    }

    /**
     * Constructor.
     *
     * @param writer the writer
     * @param dtd    The DTD that this document must write.
     */
    public XMLDocumentWriter(Writer writer, XMLDTD dtd) {
        super(writer, dtd);
    }


    protected boolean outputOpenTagImpl(
            Element element, CharacterEncoder encoder)
            throws IOException {
        
        return outputOpenTag(element, element.isEmpty(), true, encoder);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 08-Aug-05	9205/1	rgreenall	VBM:2005062107 Forward port of VBM:2005062107

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 28-Apr-04	4048/1	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 25-Feb-04	2974/4	steve	VBM:2004020608 Merging Hell

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 17-Feb-04	2974/2	steve	VBM:2004020608 SGML Quote handling

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 05-Feb-04	2794/3	steve	VBM:2004012613 HTML Quote handling

 ===========================================================================
*/
