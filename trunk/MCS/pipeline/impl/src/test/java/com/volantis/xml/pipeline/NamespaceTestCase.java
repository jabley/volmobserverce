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
package com.volantis.xml.pipeline;

import junit.framework.TestCase;

/**
 * @todo javadoc
 */
public class NamespaceTestCase extends TestCase {
    public NamespaceTestCase(String name) {
        super(name);
    }

    /**
     * Used for testing the extension of the literals set
     */
    public static class MyNamespace extends Namespace {
        public MyNamespace(String name) {
            super(name);
        }
    }

    public void testDefaultPrefix() throws Exception {
        assertEquals("http://www.volantis.com/xmlns/marlin-pipeline",
                     Namespace.PIPELINE.getURI());
        assertEquals("http://www.volantis.com/xmlns/marlin-template",
                     Namespace.TEMPLATE.getURI());                          
    }

    public void testSetURI() throws Exception {
        Namespace.TEMPLATE.setURI("my://uri");

        assertEquals("http://www.volantis.com/xmlns/marlin-pipeline",
                     Namespace.PIPELINE.getURI());
        assertEquals("my://uri",
                     Namespace.TEMPLATE.getURI());

        Namespace.TEMPLATE.setURI(null);
    }

    public void testAddLiteral() throws Exception {
        MyNamespace MINE = new MyNamespace("mine");

        assertEquals("http://www.volantis.com/xmlns/marlin-mine",
                     MINE.getURI());
    }

    public void testAddDuplicateLiteral() throws Exception {
        try {
            MyNamespace TEMPLATE = new MyNamespace("template");

            fail("Should have caused an exception");
        } catch (IllegalStateException e) {
            // This is expected
        }
    }

    public void testAddNullLiteral() throws Exception {
        try {
            MyNamespace NULL = new MyNamespace(null);

            fail("Should have caused an exception");
        } catch (IllegalArgumentException e) {
            // This is expected
        }
    }

    public void testLiteral() throws Exception {
        assertSame(Namespace.PIPELINE, Namespace.literal("pipeline"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 05-Nov-03	438/1	doug	VBM:2003091803 Fixed test case to catch correct exception

 11-Jun-03	66/1	philws	VBM:2003061103 Added class for handling Namespace URIs

 ===========================================================================
*/
