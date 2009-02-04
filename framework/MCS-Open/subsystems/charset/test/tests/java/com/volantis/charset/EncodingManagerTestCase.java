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
 * $Header: /src/voyager/testsuite/unit/com/volantis/charset/EncodingManagerTestCase.java,v 1.2 2003/04/28 15:25:03 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Mar-03    Mat             VBM:2003033107 - Encoding manager test case
 * 22-Apr-03    Mat             VBM:2003033107 - Changed Encoding.getEncoding()
 *                              method calls renamed to getCharsetName()
 * ----------------------------------------------------------------------------
 */               

package com.volantis.charset;

import com.volantis.charset.configuration.Charset;
import java.util.HashMap;

import junit.framework.*;

/**
 *
 * @author mat                       
 */                                
public class EncodingManagerTestCase extends TestCase {
    
    public EncodingManagerTestCase(java.lang.String testName) {
        super(testName);
    }        
    
    /** Test of initialiseManager method, of class com.volantis.charset.EncodingManager. */
    public void testInitialiseManager() {
        EncodingManager em = new EncodingManager();
        // This testcase assumes the following charset definition in the 
        // com.volantis.charset.configuration.xml.charset-config.xml
        // file.
        // <charset name="UTF-8" MIBenum="106" complete="true" preload="true">
        //      <alias name="UTF8"/>
        // </charset>
        HashMap aliasMap = em.getCharsetNameAliasMap();
        HashMap preloadMap = em.getPreloadedEncodingMap();
        HashMap charsetMap = em.getCharsetMap();
        
        assertTrue("aliasesMap not correct", 
                aliasMap.get("utf8").equals("utf-8"));
        Encoding enc = (Encoding) preloadMap.get("utf-8");
        assertNotNull("No encoding in preloadMap", enc);
        if(enc != null) {
            assertEquals("preloadMap not correct", enc.getMIBEnum(), 106);
        }
        Charset cs = (Charset) charsetMap.get("utf-8");
        assertNotNull("No character set in charsetMap", cs);
        if(cs != null) {
            String name = cs.getName();
            assertEquals("Wrong charset in charsetMap", name, "utf-8");
        }
    }

    /** Test of getEncoding method, of class com.volantis.charset.EncodingManager. */
    public void testGetEncoding() {
        Encoding enc;
        EncodingManager em = new EncodingManager();
        
        enc = em.getEncoding("UTF-8");
        if(!(enc instanceof NoEncoding)) {
            fail("Did not get a NoEncoding class when expecting one");
        }
        assertEquals("encoding not UTF-8", enc.getMIBEnum(), 106);
        
        // UTF8 is an alias for UTF-8, so should get a UTF-8 encoding back.
        enc = em.getEncoding("UTF8");
        if(!(enc instanceof NoEncoding)) {
            fail("Did not get a NoEncoding class when expecting one");
        }
        assertEquals("encoding not utf-8", enc.getMIBEnum(), 106);
        
        enc = em.getEncoding("iso-8859-1");
        if(!(enc instanceof BitSetEncoding)) {
            fail("Did not get a BitSetEncoding class when expecting one");
        }
        assertEquals("encoding not iso-8859-1", enc.getMIBEnum(), 4);
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/3	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
