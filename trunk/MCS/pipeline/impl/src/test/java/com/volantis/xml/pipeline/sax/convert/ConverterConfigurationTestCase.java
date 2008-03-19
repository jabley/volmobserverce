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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.convert;

import com.volantis.xml.pipeline.sax.convert.ConverterConfiguration;

import junit.framework.TestCase;

public class ConverterConfigurationTestCase extends TestCase {


    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private ConverterTuple [] prepareTuples(){
        ConverterTuple [] result  = null;
        result =  new ConverterTuple[]{
            new ConverterTuple(null, "a", "href"),
            new ConverterTuple(null, "form", "action"),
            new ConverterTuple(null, "frame", "src"),
            new ConverterTuple(null, "link", "href"),
            new ConverterTuple(null, "xfform", "action"),
            new ConverterTuple(null, "submission", "action"),
        };
        return result;
    }

    private boolean areTuplesEquals(ConverterTuple expected, ConverterTuple received){
        return expected.getAttribute().equals(received.getAttribute()) &&
                expected.getElement().equals(received.getElement()) &&
                ((expected.getNamespaceURI() == null && received.getNamespaceURI() == null) ||
                    (expected.getNamespaceURI() != null
                        && expected.getNamespaceURI().equals(received.getNamespaceURI())
                    )
                );
    }

    private boolean areTuplesArrayEquals(ConverterTuple [] expectedTuples, ConverterTuple [] receivedTuples){
        boolean result = false;
        if(expectedTuples.length == receivedTuples.length){
            for(int i = 0;i<expectedTuples.length;i++){
                if(!areTuplesEquals(expectedTuples[i],receivedTuples[i])){
                    return result;
                }
            }
            result = true;
        }
        return result;
    }

    public void testGetTuples(){
        ConverterTuple [] initialTuples = prepareTuples();
        ConverterTuple [] expectedTuples = new ConverterTuple[] {
                new ConverterTuple(null, "link","href")
        };

        ConverterConfiguration configuration =
                new ConverterConfiguration(initialTuples);
        ConverterTuple [] received = configuration.
                getTuples(null,"link");
        assertTrue(areTuplesArrayEquals(expectedTuples,received));
    }


    public void testSetTuples(){
        ConverterTuple [] initialTuples = prepareTuples();
        ConverterTuple [] expectedTuples = new ConverterTuple[] {
                new ConverterTuple(null, "link","href")
        };

        ConverterConfiguration configuration =
                new ConverterConfiguration();
        configuration.setTuples(initialTuples);
        ConverterTuple [] received = configuration.
                getTuples(null,"link");
        assertTrue(areTuplesArrayEquals(expectedTuples,received));

    }


    public void testPutTuples(){
        ConverterTuple [] initialTuples = prepareTuples();
        ConverterTuple [] expectedTuples = new ConverterTuple[] {
                new ConverterTuple(null, "link","href")
        };

        ConverterConfiguration configuration =
                new ConverterConfiguration();
        configuration.putTuplesByMode("html",initialTuples);
        ConverterTuple [] received = configuration.
                getTuplesByMode(null,"link","html");
        assertNotNull(received);
        assertTrue(areTuplesArrayEquals(expectedTuples,received));
        ConverterTuple [] receivedBad = configuration.
                getTuplesByMode(null,"link","xdime");
        assertNull(receivedBad);

    }

}
