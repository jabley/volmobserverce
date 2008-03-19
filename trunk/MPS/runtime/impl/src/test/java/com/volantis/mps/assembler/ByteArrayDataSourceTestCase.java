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
 * $Header: /src/mps/testsuite/unit/com/volantis/mps/assembler/ByteArrayDataSourceTestCase.java,v 1.1 2002/11/22 14:22:46 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Nov-02    Steve           VBM:2002111814 - Created.
 * ----------------------------------------------------------------------------
 */


package com.volantis.mps.assembler;

import java.io.*;
import java.io.ByteArrayInputStream;
import junit.framework.*;

public class ByteArrayDataSourceTestCase extends TestCase
{
    
    public ByteArrayDataSourceTestCase(java.lang.String testName)
    {
        super(testName);
    }
    
    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite(ByteArrayDataSourceTestCase.class);
        
        return suite;
    }
    
    /** Test of getInputStream method, of class com.volantis.mps.assembler.ByteArrayDataSource. */
    public void testGetInputStreamFromInputStream() throws IOException {
        System.out.println("testGetInputStreamFromInputStream");

        ByteArrayDataSource source =
                new ByteArrayDataSource(getInputStream(), "text/plain");
        InputStream data = source.getInputStream();
        InputStream compare = getInputStream();

        int dch, cch;
        while ((dch = data.read()) != -1) {
            cch = compare.read();
            if (cch == -1) {
                fail("Too much data returned from input stream.");
            }
            assertEquals(dch, cch);
        }
        cch = compare.read();
        if (cch != -1) {
            fail("Not enough data returned from input stream.");
        }
    }
    
    /** Test of getInputStream method, of class com.volantis.mps.assembler.ByteArrayDataSource. */
    public void testGetInputStreamFromBytes() throws IOException {
        System.out.println("testGetInputStreamFromBytes");

        ByteArrayDataSource source =
                new ByteArrayDataSource(getBytes(), "text/plain");
        InputStream data = source.getInputStream();
        InputStream compare = getInputStream();

        int dch, cch;
        while ((dch = data.read()) != -1) {
            cch = compare.read();
            if (cch == -1) {
                fail("Too much data returned from input stream.");
            }
            assertEquals(dch, cch);
        }
        cch = compare.read();
        if (cch != -1) {
            fail("Not enough data returned from input stream.");
        }
    }

    /** Test of getInputStream method, of class com.volantis.mps.assembler.ByteArrayDataSource. */
    public void testGetInputStreamFromString() throws IOException {
        System.out.println("testGetInputStreamFromString");

        ByteArrayDataSource source =
                new ByteArrayDataSource(getString(), "text/plain");
        InputStream data = source.getInputStream();
        InputStream compare = getInputStream();

        int dch, cch;
        while ((dch = data.read()) != -1) {
            cch = compare.read();
            if (cch == -1) {
                fail("Too much data returned from input stream.");
            }
            assertEquals(dch, cch);
        }
        cch = compare.read();
        if (cch != -1) {
            fail("Not enough data returned from input stream.");
        }
    }
    
    /** Test of getOutputStream method, of class com.volantis.mps.assembler.ByteArrayDataSource. */
    public void testGetOutputStream() throws IOException {
        System.out.println("testGetOutputStream");

        ByteArrayDataSource source = null;
            source = new ByteArrayDataSource( getInputStream(), "text/plain" );
        
        try {
            OutputStream out = source.getOutputStream();
            fail( "Expected IOException" );
        }
        catch( IOException ioe ) {
            // success.
        }
    }
    
    /** Test of getContentType method, of class com.volantis.mps.assembler.ByteArrayDataSource. */
    public void testGetContentType() throws IOException {
        System.out.println("testGetContentType");
        
        ByteArrayDataSource source = 
                 new ByteArrayDataSource( getInputStream(), "text/plain" );
        assertEquals( "text/plain", source.getContentType() );
    }
    
    
    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    
    /** Get a data input stream */
    private InputStream getInputStream() throws IOException
    {
        ClassLoader loader = this.getClass().getClassLoader();
        String resourceName = this.getClass().getName().replace( '.', '/' );
        resourceName = resourceName + ".txt";
                
        InputStream in = loader.getResourceAsStream( resourceName );
        if( in == null ) {
            throw new IOException( "Cannot find resource " + resourceName );
        }
        return in;
    }
    
    /** Get data as a byte array */
    private byte[] getBytes() throws IOException
    {
        InputStream in = getInputStream();    
        byte[] buffer = new byte[ in.available() ];
        in.read( buffer );
        return buffer;       
    }
    
    /** Get the data as a string */
    private String getString() throws IOException
    {
        return new String( getBytes() );
    }
            
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Dec-03	75/1	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements

 ===========================================================================
*/
