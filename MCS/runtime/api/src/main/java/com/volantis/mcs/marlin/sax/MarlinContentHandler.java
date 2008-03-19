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
 * $Header: /src/voyager/com/volantis/mcs/marlin/sax/MarlinContentHandler.java,v 1.3 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Apr-2003  Chris W         VBM:2003030404 - MarlinContentHandler turned
 *                              into an abstract class and this interface
 *                              extracted.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.marlin.sax;

import org.xml.sax.ContentHandler;

/**
 * This class is responsible for processing the incoming SAX2 events and
 * invoking PAPI.
 * <p>
 * This class requires that the startElement and endElement methods are given
 * non-null values for the localName parameter. This can be ensured by using
 * a namespace aware parser and setting the feature identified by the URI
 * http://xml.org/sax/features/namespaces.
 * <p>
 * This class can be used anywhere that a ContentHandler can be used, e.g. with
 * an XMLReader, or XMLFilter.
 * <pre>
 *   MarlinContentHandler handler = MarlinSAXHelper.getContentHandler(marinerRequestContext);
 *   XMLReader reader = MarlinSAXHelper.getXMLReader ();
 *   reader.setContentHandler(handler);
 * </pre>
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface MarlinContentHandler extends ContentHandler {   
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jul-03	776/1	chrisw	VBM:2003071005 Remove MarlinContentHandler.setInitialRequestContext()

 ===========================================================================
*/
