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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import java.io.IOException;

import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.runtime.VolantisMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base class for integration tests that need to check markup generated 
 * by different protocols.
 *
 * This class provides only the basic plumbing necessary to create a protocol.
 * It was created by refactoring RootTestCaseAbstract into two classes.
 */
public abstract class MarkupTestCaseAbstract extends TestCaseAbstract {
    
    // Mocks created by this class
    protected MarinerPageContextMock marinerPageContextMock;
    protected MarinerRequestContextMock requestContextMock;
    protected EnvironmentContextMock environmentContextMock;
    protected VolantisMock volantisMock;
    protected DOMProtocol protocol;

    /** 
     * Used internally for converting DOM to String
     */ 
    private final DOMFactory domFactory = DOMFactory.getDefaultInstance();

    // Javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        
        // Create basic mocks
        marinerPageContextMock =  new MarinerPageContextMock("marinerPageContextMock", expectations);
        requestContextMock =  new MarinerRequestContextMock("requestContextMock", expectations);
        volantisMock = new VolantisMock("volantisMock", expectations);
        environmentContextMock = new EnvironmentContextMock("environmentContextMock", expectations);
        
        // Interconnect mocks
        marinerPageContextMock
            .expects.getRequestContext()
            .returns(requestContextMock).any();
    
        marinerPageContextMock
            .expects.getVolantisBean()
            .returns(volantisMock).any();
        
        // Let the test class create the actual protocol 
        protocol = createProtocol();
        
        // Make the protocol use our mock structure
        protocol.setMarinerPageContext(marinerPageContextMock);        
    }

    /**
     * Return an instance of the Protocol class under test. This method needs
     * to be implemented by test class.
     *
     * @return Protocol class instance
     */
    protected abstract DOMProtocol createProtocol();
    
    /**
     * Convenience method for converting DOMOutputBuffer to string
     */
    protected String domToString(DOMOutputBuffer dom) throws IOException {
        Document doc = domFactory.createDocument();
        doc.addNode(dom.getRoot());
        return DOMUtilities.toString(doc, protocol.getCharacterEncoder());
    }
}
