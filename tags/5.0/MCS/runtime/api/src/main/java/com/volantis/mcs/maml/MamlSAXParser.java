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
 * $Header: /src/voyager/com/volantis/mcs/maml/MamlSAXParser.java,v 1.12 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12 Mar 2002  Steve           VBM:2002022203 A SAX parser to parse a maml XML
 *                              file.
 * 14 Mar 2002  Steve           VBM:2002022203 Log invalid URL Exceptions
 * 18 Mar 2002  Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 *  9 Apr 2002  Ian             VBM:2002032002 Changed comments to reflect the
 *                              use of the createNestedContext() method of
 *                              MarinerRequestContext.
 * 06-Nov-2002  Mat             VBM:2002090207 - Added rulesMap
 * 15-Nov-02    Doug            VBM:2002071507 - Changed all 
 *                              com.volantis.mcs.xmlparser.parsers.SAXParser
 *                              references to
 *                              com.volantis.xml.xerces.parsers.SAXParser.
 * 22-Nov-02    Paul            VBM:2002112214 - Deprecated and modified to use
 *                              the new MarlinContentHandler, MarlinSAXHelper.
 * 11-Feb-03    Ian             VBM:2003020607 - Ported method from Metis to 
 *                              parse string of XML.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * 25-Apr-03    Chris W         VBM:2003030404 - Now uses MarlinContentHandler
 *                              and MarlinSAXHelper
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.maml;

import com.volantis.mcs.context.MarinerRequestContext;

import com.volantis.mcs.marlin.sax.AbstractPAPIContentHandler;
import com.volantis.mcs.marlin.sax.MarlinContentHandler;
import com.volantis.mcs.marlin.sax.MarlinSAXHelper;
import com.volantis.mcs.marlin.sax.AbstractMarlinContentHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.util.Map;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.xml.sax.ExtendedSAXException;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ContentHandler;

/**
 * @deprecated See {@link MarlinContentHandler} and {@link MarlinSAXHelper}.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class MamlSAXParser
{
   /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(MamlSAXParser.class);

    /**
     * The handler for maml files
     */
    private ContentHandler handler;

    /**
     * The SAX parser
     */
    private XMLReader parser;

    /**
     * Set the request context for the parser. The appropriate context should
     * be obtained from MarinerRequestContext.createNestedContext(), either a
     * MarinerJspRequestContext for JSP's or a MarinerServletRequestContext for
     * Servlets.
     * @param context The request context to use for the parser.
     */
    public void setRequestContext( MarinerRequestContext context )
    {
        handler = MarlinSAXHelper.getContentHandler(context);
        parser = MarlinSAXHelper.getXMLReader ();
        parser.setContentHandler(handler);
        parser.setErrorHandler (new InternalErrorHandler ());
    }

    /**
     * Parse an XML file containing MAML XML source.
     * @param uri The uri of the XML file to parse.
     */
    public void parse( String uri ) throws IOException, SAXException
    {
        parser.parse( uri );
    }
    
    /**
     * Parse an InputStream containing MAML XML source.
     * @param is The InputStream containing the XML to parse.
     */
    public void parse( InputStream is ) throws IOException, SAXException
    {
        parser.parse( new InputSource(is) );
    }

    /**
     * Parse an InputSource containing MAML XML source.
     * @param is The InputSource containing the XML to parse.
     */
    public void parse( InputSource is ) throws IOException, SAXException
    {
        parser.parse( is );
    }
    
    /**
     * Parse the input directly from a string which contains MAML XML source.
     *
     * @param xml The MAML XML document as a string
     * @throws IOException if there is a problem reading from the document
     * @throws SAXException if there is a problem with the XML in the document
     * @deprecated
     */
    public void parseString(String xml) throws IOException, SAXException {
        parser.parse(new InputSource(new StringReader(xml)));

    }
    /**
     * Parse the input from a URL which returns MAML XML source.
     * @param spec  The URL of the input source
     */
    public void parseURL( String spec ) throws IOException, SAXException
    {
        URL url;

        try {
            url = new URL( spec );
        }
        catch( MalformedURLException mue )
        {
            logger.error("invalid-url", mue );
            throw new ExtendedSAXException( mue );
        }

        URLConnection connection = url.openConnection();
        connection.setUseCaches( false );
        parser.parse( new InputSource( connection.getInputStream() ) );
    }

    private class InternalErrorHandler
        implements ErrorHandler {

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
}

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

 28-Aug-03	1253/4	doug	VBM:2003082202 fixed duplicate history problem

 13-Aug-03	776/4	chrisw	VBM:2003071005 Fix supermerge conflicts

 22-Jul-03	833/1	adrian	VBM:2003071902 added marlin support for invocation elements

 ===========================================================================
*/
