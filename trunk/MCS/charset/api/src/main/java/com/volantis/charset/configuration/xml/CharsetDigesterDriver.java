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
 * $Header: /src/voyager/com/volantis/charset/configuration/xml/CharsetDigesterDriver.java,v 1.2 2003/04/28 15:36:22 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Apr-03    Mat             VBM:2003040701 - Initialise and run the 
 *                              Digester for the charset configuration.
 * 22-Apr-03    Mat             VBM:2003040701 - Made some javadoc changes and
 *                              changed the CONFIG filename generation to be
 *                              based on the class package to make it portable.
 * ----------------------------------------------------------------------------
 */

package com.volantis.charset.configuration.xml;

import com.volantis.charset.configuration.Charsets;
import com.volantis.xml.xerces.parsers.SAXParser;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import our.apache.commons.digester.*;

/**
 * Initialise and run the Digester for the charset configuration.
 * @author  mat
 */
public class CharsetDigesterDriver {

    /** The copyright statement. */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(CharsetDigesterDriver.class);
    
    private static String CHARSET_CONFIG_FILENAME;
    
    static {
    String classPath = CharsetDigesterDriver.class.getName().replace('.', '/');
    CHARSET_CONFIG_FILENAME = classPath.substring(0,
                              classPath.lastIndexOf("/") + 1) +
                              "charset-config.xml";
    }
    
    /** Creates a new instance of CharsetDigesterDriver */
    public CharsetDigesterDriver() {
    }

    /**
     * Perform the digestion of the XML
     *
     * @return The charsets root element.
     */
    public Charsets digest() {
        
        Charsets cs = null;
        Digester digester = new Digester(new SAXParser());

        ClassLoader loader = this.getClass().getClassLoader();
        InputStream stream = 
            loader.getResourceAsStream(CHARSET_CONFIG_FILENAME);
        InputSource is = new InputSource(stream);

        digester.setValidating(false);
        digester.addRuleSet(new CharsetRuleSet());
        
        try {
            cs = (Charsets) digester.parse(is);
        } catch (IOException ioe) {
            logger.error("ioexception-parsing", new Object[]{CHARSET_CONFIG_FILENAME}, ioe);
        } catch (SAXException se) {
            logger.error("sax-parsing-error", new Object[]{CHARSET_CONFIG_FILENAME}, se);
        }
        
        return cs;
    }
         
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/1	tony	VBM:2004012601 localisation services update

 ===========================================================================
*/
