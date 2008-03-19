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
package com.volantis.xml.pipeline.sax;

import com.volantis.xml.pipeline.sax.adapter.AdapterProcess;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import junit.framework.Assert;

/**
 * Mock {@link AdapterProcess} implementation that allows client to 
 * perform various asserts to ensure that a given method was invoked.
 */
public class MockAdapterProcess extends XMLProcessTestable
        implements AdapterProcess {

    /**
     * Flag used to record whether the setElementDetails method was invoked
     */
    boolean wasSetElementDetailsInvoked = false;

    /**
     * Flag used to record whether the processAttributes method was invoked
     */
    boolean wasProcessAttributesInvoked = false;

    /**
     * Creates a new <code>MockAdapterProcess</code> instance
     */
    public MockAdapterProcess() {
    }

    /**
     * Creates a new <code>MockAdapterProcess</code> instance 
     * @param processIdentifier string that identifies this process
     */
    public MockAdapterProcess(String processIdentifier) {
        super(processIdentifier);
    }

    // javadoc inherited
    protected void assertState() {
        super.assertState();
        String msg = "MockAdapterProcess in illegal state for performing test";
        Assert.assertTrue(msg, !wasSetElementDetailsInvoked);
        Assert.assertTrue(msg, !wasProcessAttributesInvoked);
    }

    // javadoc inherited
    public void reset() {
        super.reset();
        this.wasSetElementDetailsInvoked = false;
        this.wasProcessAttributesInvoked = false;
    }

    // javadoc inherited
    public void setElementDetails(String namespaceURI, String localName,
                                  String qName) {
        this.wasSetElementDetailsInvoked = true;
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.qName = qName;
    }

    /**
     * Performs an assert to ensure that the setElementDetails method 
     * was invoked
     */
    public void assertSetElementDetailsInvoked(String expectedNamespaceURI,
                                               String expectedLocalName,
                                               String expectedQName) {
        Assert.assertTrue("setElementDetails() was not invoked",
                          wasSetElementDetailsInvoked);

        Assert.assertEquals("Unexpected namespaceURI parameter passed to " +
                            "setElementDetails()",
                            expectedNamespaceURI,
                            namespaceURI);

        Assert.assertEquals("Unexpected localName parameter passed to " +
                            "setElementDetails()",
                            expectedLocalName,
                            localName);

        Assert.assertEquals("Unexpected qName parameter passed to " +
                            "setElementDetails()",
                            expectedQName,
                            qName);
    }

    /**
     * Performs an assert to ensure that the setElementDetails method 
     * was NOT invoked
     */
    public void assertSetElementDetailsNotInvoked() {
        Assert.assertTrue("setElementDetails() was invoked",
                          !wasSetElementDetailsInvoked);
    }

    // javadoc inherited
    public void processAttributes(Attributes attributes) throws SAXException {
        this.wasProcessAttributesInvoked = true;
        this.atts = attributes;
    }

    /**
     * Performs an assert to ensure that the setProcessAttributes method 
     * was invoked
     */
    public void assertProcessAttributesInvoked(Attributes expectedAtts) {
        Assert.assertTrue("processAttributes() was not invoked",
                          wasProcessAttributesInvoked);

        Assert.assertEquals("Unexpected attributes parameter passed to " +
                            "processAttributes()",
                            expectedAtts,
                            atts);
    }

    /**
     * Performs an assert to ensure that the setProcessAttributes method 
     * was NOT invoked
     */
    public void assertProcessAttributesNotInvoked() {
        Assert.assertTrue("processAttributes() was invoked",
                          !wasProcessAttributesInvoked);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 05-Aug-03	290/1	doug	VBM:2003080412 Provided DynamicElementRule implementation for adding Adapters to a pipeline

 ===========================================================================
*/
