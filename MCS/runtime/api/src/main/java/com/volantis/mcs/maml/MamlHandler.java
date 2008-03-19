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
 * $Header: /src/voyager/com/volantis/mcs/maml/MamlHandler.java,v 1.13 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12 Mar 2002  Steve           VBM:2002022203 - SAX handler to create MAML
 *                              elements when parsing an XML file.
 * 12 Mar 2002  Steve           VBM:2002022203 - Fixed bug where the element
 *                              was not looking at the state of it's parent
 *                              element to determine whether output has been
 *                              overridden by the parent having a SKIP BODY
 *                              state.
 * 14 Mar 2002  Steve           VBM:2002022203 - Improved java doc comments
 * 18 Mar 2002  Ian             VBM:2002031203 - Changed log4j Category from
 *                              class to string.
 * 19 Mar 2002  Steve           VBM:2002022203 - Fixed bug where elementEnd()
 *                              was not being called for the parent element
 *                              when it returns SKIP_ELEMENT_BODY from the
 *                              elementStart() of the PAPI element.
 *  9 Apr 2002  Ian             VBM:2002032002 Changed calls to 
 *                              MarinerServletRequestContext to reference the
 *                              new createNestedContext() method of 
 *                              MarinerRequestContext in startElement to remove
 *                              dependencies on servlet components.
 * 11 Jul 2002  Sumit           VBM:2002070810 Removed content.trim() in
 *                              characters(char[], int, int )
 * 20 Aug 2002  Steve           VBM:2002081505 If the characters being output
 *                              in characters() are all whitespace characters
 *                              then do not bother doing any output. This uses
 *                              a new isWhitespace() method to determine if the
 *                              output is all whitespace. If there is at least
 *                              one non-whitespace character then the string
 *                              is output in its entirity... whitespace and
 *                              all.
 * 30 Sep 2002  Steve           VBM:2002081505  Uses 
 *                              WhitespaceUtilities.isWhitespace to check if a
 *                              string only contains whitespace characters.
 * 06-Nov-2002  Mat             VBM:2002090207 - Added rulesMap
 * 22-Nov-02    Paul            VBM:2002112214 - Deprecated and modified to use
 *                              the new MarlinContentHandler, MarlinSAXHelper.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * 25-Apr-03    Chris W         VBM:2003030404 - Removed constructor that took
 *                              a RulesMap parameter. MarlinContentHandler
 *                              obtained from MarlinSAXHelper
 * 28-May-03    Steve           VBM:2003042206 - Patch 2003041501 from Metis
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.maml;

import com.volantis.mcs.context.MarinerRequestContext;

import com.volantis.mcs.marlin.sax.MarlinContentHandler;
import com.volantis.mcs.marlin.sax.MarlinSAXHelper;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.xml.sax.ExtendedSAXException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ContentHandler;

import org.xml.sax.helpers.DefaultHandler;

/**
 * @deprecated See {@link MarlinContentHandler} and {@link MarlinSAXHelper}.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class MamlHandler extends DefaultHandler
{
   /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MamlHandler.class);

    /**
     * The ContentHandler which does all the work.
     */
    private ContentHandler contentHandler;

    /** Creates a new instance of MamlHandler without a rulesMap */
    public MamlHandler()
    {
    }

    /**
     * Set the request context. The request context is pushed onto
     * the context stack and all subsequent tags will use it.
     * @param context The request context from either the Servlet or JSP
     * calling the parser.
     */
    public void setRequestContext( MarinerRequestContext context )
    {
        contentHandler = MarlinSAXHelper.getContentHandler(context);
    }

    /**
     * This method delegates to the matching method in the
     * MarlinContentHandler.
     */
    public void startElement( String uri, String local, String raw,
                              Attributes attrs ) throws SAXException
    {
        contentHandler.startElement (uri, local, raw, attrs);
    }

    /**
     * This method delegates to the matching method in the
     * MarlinContentHandler.
     */
    public void endElement( String uri, String local, String raw )
        throws SAXException
    {
        contentHandler.endElement (uri, local, raw);
    }

    /**
     * This method delegates to the matching method in the
     * MarlinContentHandler.
     */
    public void characters(char[] ch, int start, int length)
        throws SAXException
    {
        contentHandler.characters (ch, start, length);
    }

    /**
     * Called by the sax parser when a non-serious error is encountered in
     * the XML file.
     * @param exc  A SAXParseException describing the problem
     */
    public void warning( SAXParseException exc )
    {
        logger.warn("warning", exc );
    }

    /**
     * Called by the sax parser when a serious error is encountered in
     * the XML file.
     * @param exc  A SAXParseException describing the problem
     */
    public void error( SAXParseException exc )
    {
        logger.error("error", exc );
    }

    /**
     * Called by the sax parser when a fatal error is encountered in
     * the XML file.
     * @param exc  A SAXParseException describing the problem
     */
    public void fatalError( SAXParseException exc ) throws SAXException
    {
        logger.error("error", exc );
        throw new ExtendedSAXException( exc );
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * end:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9391/2	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 10-Jul-03	776/1	chrisw	VBM:2003071005 Remove MarlinContentHandler.setInitialRequestContext()

 ===========================================================================
*/
