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
package com.volantis.xml.sax;

import com.volantis.shared.throwable.ExtendedThrowableTestAbstract;

/**
 * Unit test for the ExtendSAXParseException class
 */ 
public class ExtendedSAXParseExceptionTestCase 
        extends ExtendedThrowableTestAbstract {

    public ExtendedSAXParseExceptionTestCase() {
        super(ExtendedSAXParseException.class);
    }

    protected Throwable createException() {
        return new ExtendedSAXParseException(null, null, null, -1, -1);
    }

    protected Throwable createException(String s) {
        return new ExtendedSAXParseException(s, null, null, -1, -1);
    }

    protected Throwable createCausedException(String s) {
        return new ExtendedSAXParseException(s, null, null, -1, -1,
                createExceptionCause());
    }

    protected Throwable createCausedException(String s, Throwable t) {
        return new ExtendedSAXParseException(s, null, null, -1, -1, t);
    }

    protected Throwable createCausedException() {
        return new ExtendedSAXParseException(null, null, null, -1, -1,
                createExceptionCause());
    }

    /**
     * Tests the getException() method.
     * @throws Exception if an error occurs
     */ 
    public void testGetException() throws Exception {
        // construct an ExtendedSAXParseException with a root cause
        Exception rootCause = new Exception();
        
        ExtendedSAXParseException e = 
                new ExtendedSAXParseException("message", null, rootCause);
        
        assertEquals("Root cause exception should be the Exception passed " +
                     "in on construction ", rootCause, e.getException());
    }
    
    /**
     * Tests the getException() method.
     * @throws Exception if an error occurs
     */ 
    public void testGetExceptionWithThrowableCause() throws Exception {
        // construct an ExtendedSAXParseException with a root cause
        Throwable rootCause = new Throwable();
        
        ExtendedSAXParseException e = 
                new ExtendedSAXParseException("message", null, rootCause);
        
        assertNull("Root exception should be null", e.getException());
    }
    
    /**
     * Tests the getException() method.
     * @throws Exception if an error occurs
     */ 
    public void testGetExceptionWithNoCause() throws Exception {
        // construct an ExtendedSAXParseException without a root cause        
        ExtendedSAXParseException e = 
                new ExtendedSAXParseException("message", null);
        
        assertNull("Root exception should be null", e.getException());
    }
                }

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 15-Jul-03	191/1	doug	VBM:2003071403 Extended version of the SAXParseException class

 ===========================================================================
*/
