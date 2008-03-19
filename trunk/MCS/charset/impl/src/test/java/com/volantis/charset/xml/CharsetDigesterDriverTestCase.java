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
 * $Header: /src/voyager/testsuite/unit/com/volantis/charset/xml/CharsetDigesterDriverTestCase.java,v 1.2 2003/04/28 15:27:39 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Apr-03    Mat             VBM:2003040701 - Initialise and run the 
 *                              Digester for the charset configuration.
 * 22-Apr-03    Mat             VBM:2003040701 - Changed to search for UTF8
 *                              in the config, rather than rely on it being
 *                              the first element.
 * ----------------------------------------------------------------------------
 */             

package com.volantis.charset.xml;

import com.volantis.charset.configuration.Alias;
import com.volantis.charset.configuration.Charset;
import com.volantis.charset.configuration.Charsets;
import com.volantis.charset.configuration.xml.CharsetDigesterDriver;
import com.volantis.xml.xerces.parsers.SAXParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import junit.framework.*;
import org.apache.log4j.Category;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import our.apache.commons.digester.*;

/**
 *
 * @author mat                       
 */                                
public class CharsetDigesterDriverTestCase extends TestCase {
    
    public CharsetDigesterDriverTestCase(java.lang.String testName) {
        super(testName);
    }        
    
   

    /** Test of digest method, of class com.volantis.charset.configuration.xml.DigesterDriver. */
    public void testDigest() {
        // This testcase assumes the following charset definition in the 
        // com.volantis.charset.configuration.xml.charset-config.xml
        // file.
        // <charset name="UTF-8" MIBenum="106" complete="true" preload="true">
        //      <alias name="UTF8"/>
        // </charset>
        CharsetDigesterDriver dd = new CharsetDigesterDriver();
        
        Charsets css = dd.digest();
        assertNotNull("No charsets digested", css);
        
        ArrayList charset = css.getCharsets();
        assertNotNull("No charset information", charset);
        
        // Construct a UTF8 charset so we can get the index in the ArrayList 
        Charset UTF8 = new Charset();
        UTF8.setName("utf-8");
        UTF8.setMIBenum(106);
        UTF8.setPreload(true);
        UTF8.setComplete(true);
        
        int utf8Index = charset.indexOf(UTF8);
        
        Charset cs = (Charset) charset.get(utf8Index);
        assertNotNull("No utf8 charset in map", cs);
        
        assertEquals("UTF-8 charset not correctly configured", cs.getName(), "utf-8");
        assertEquals("MIBenum not correct", cs.getMIBenum(), 106);
        assertTrue("Complete attribute is false", cs.isComplete());
        assertTrue("Preload attribute is false", cs.isPreload());
        
        ArrayList aliases = cs.getAlias();
        assertNotNull("No aliases found", aliases);
        
        Alias UTF8Alias = new Alias();
        UTF8Alias.setName("UTF8");
        
        int utf8AliasIndex = aliases.indexOf(UTF8Alias);
        
        Alias a = (Alias) aliases.get(utf8AliasIndex);
        assertNotNull("No aliases found", a);
        
        assertEquals("Incorrect alias in list", a.getName(), "utf8");
        
        
        
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
