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
package com.volantis.xml.pipeline.sax.impl.operations.transform;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import javax.xml.transform.TransformerException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Test class for CollatingErrorListener.
 */
public class CollatingErrorListenerTestCase extends TestCaseAbstract{
    
    private final static String EXC_1 = "exception_content_1";
    private final static String EXC_2 = "exception_content_2";
    private final static String EXC_3 = "exception_content_3";

    Set exceptionsList = new LinkedHashSet();
    
    // javadoc inherited
    public void setUp(){
        exceptionsList.clear();
        exceptionsList.add(new TransformerException(EXC_1));
        exceptionsList.add(new TransformerException(EXC_2));
        exceptionsList.add(new TransformerException(EXC_3));
        
    }
    
    // javadoc inherited
    public void testFatalError() throws Exception {
        CollatingErrorListener collatingErrorListener = new CollatingErrorListener();
        Iterator iterator = exceptionsList.iterator();
        StringBuffer expectedResult = new StringBuffer();
        while(iterator.hasNext()){
            TransformerException te = (TransformerException)iterator.next();
            collatingErrorListener.fatalError(te);
            expectedResult.append(te.getMessageAndLocation()).append("\n");            
        }
        assertEquals("expected: "+expectedResult+" but was: "+
                collatingErrorListener.getErrorBuffer(),
                expectedResult.toString(),collatingErrorListener.getErrorBuffer().toString());
        collatingErrorListener.resetErrorBuffer();
        assertEquals("buffer should be empty but it was not",
                new StringBuffer().toString(),  
                collatingErrorListener.getErrorBuffer().toString());
    }

    // javadoc inherited
    public void testError() throws Exception {
        CollatingErrorListener collatingErrorListener = new CollatingErrorListener();
        Iterator iterator = exceptionsList.iterator();
        StringBuffer expectedResult = new StringBuffer();
        while(iterator.hasNext()){
            TransformerException te = (TransformerException)iterator.next();
            collatingErrorListener.error(te);
            expectedResult.append(te.getMessageAndLocation()).append("\n");
        }
        assertEquals("expected: "+expectedResult+" but was: "+
                collatingErrorListener.getErrorBuffer(),
                expectedResult.toString(),collatingErrorListener.getErrorBuffer().toString());
        collatingErrorListener.resetErrorBuffer();
        assertEquals("buffer should be empty but it was not",
                new StringBuffer().toString(),  
                collatingErrorListener.getErrorBuffer().toString());
    }

    // javadoc inherited
    public void testWarning() throws Exception {
        CollatingErrorListener collatingErrorListener = new CollatingErrorListener();
        Iterator iterator = exceptionsList.iterator();
        StringBuffer expectedResult = new StringBuffer();
        while(iterator.hasNext()){
            TransformerException te = (TransformerException)iterator.next();
            collatingErrorListener.warning(te);
            expectedResult.append(te.getMessageAndLocation()).append("\n");
        }
        assertEquals("expected: "+expectedResult+" but was: "+
                collatingErrorListener.getErrorBuffer(),
                expectedResult.toString(),collatingErrorListener.getErrorBuffer().toString());
        collatingErrorListener.resetErrorBuffer();
        assertEquals("buffer should be empty but it was not",
                new StringBuffer().toString(),  
                collatingErrorListener.getErrorBuffer().toString());
    }   
}
