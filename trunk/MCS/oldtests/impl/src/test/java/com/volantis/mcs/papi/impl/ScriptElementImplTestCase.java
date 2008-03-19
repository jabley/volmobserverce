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
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.ScriptAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DefaultProtocolSupportFactory;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.html.XHTMLFull;
import com.volantis.mcs.protocols.html.XHTMLFullConfiguration;
import com.volantis.mcs.protocols.html.XHTMLTransitional;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverMock;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

/**
 * This class tests ScriptElement papi element
 */
public class ScriptElementImplTestCase extends MockTestCaseAbstract {

    /**
     * The ScriptElement being tested
     */
    private ScriptElementImpl element;
    
    /**
     * The attributes associated with the PaneElement being tested
     */
    private ScriptAttributes attributes;
    
    /**
     * The MarinerRequestContext
     */
    private MarinerRequestContext requestContext;
    
    /**
     * The MarinerPageContext
     */
    private TestMarinerPageContext pageContext;
    
    /**
     * The protocol generating the output
     */
    private VolantisProtocol protocol;
    
    /**
     * Default JUnit constructor
     * @param name
     */
    public ScriptElementImplTestCase(String name) {
        super(name);
    }

    /**
     * Set up the test cases
     */
    private void privateSetUp( VolantisProtocol p, 
                               TestMarinerPageContext context ) {            
        protocol = p;

        requestContext = new TestMarinerRequestContext();

        pageContext = context;
        pageContext.pushRequestContext(requestContext);
        pageContext.setProtocol(protocol);

        final PolicyReferenceResolverMock referenceResolverMock =
                new PolicyReferenceResolverMock("referenceResolverMock",
                        expectations);
        pageContext.setPolicyReferenceResolver(referenceResolverMock);

        referenceResolverMock.expects.resolveQuotedScriptExpression(null)
                .returns(null).any();

        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        protocol.setMarinerPageContext(pageContext);

        element = new ScriptElementImpl();
        attributes = new ScriptAttributes();        

    }

    /**
     * Test the script element with the XHTMLBasic protocol
     */ 
    public void testXHTMLBasic() throws Exception {

        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestXHTMLBasicFactory(),
                InternalDeviceTestHelper.createTestDevice());

        privateSetUp(protocol, new TestMarinerPageContext());

        // Element start should return SKIP ELEMENT BODY as XHTMLBasic
        // does not support script.
        int i = element.elementStart(requestContext,attributes );        
        assertEquals("Wrong code returned by elementStart",
                     PAPIElement.SKIP_ELEMENT_BODY, i );
        
        i = element.exprElementEnd(requestContext,attributes);
        assertEquals("Wrong code returned by elementEnd",
                     PAPIElement.CONTINUE_PROCESSING, i);
    }

    /**
     * Test the script element with the XHTMLTransitional protocol
     */ 
    public void testXHTMLTransitional() throws Exception {

        MyXHTMLTransitional protocol = new MyXHTMLTransitional();
        privateSetUp( protocol, new MyPageContext() );

        attributes.setLanguage("Language");
        attributes.setType("text/javascript");
        attributes.setCharSet("Ascii");
        attributes.setDefer("defer");
        attributes.setStyleClass("scripto");
        
        // Element start should return SKIP ELEMENT BODY as XHTMLBasic
        // does not support script.
        int i = element.elementStart(requestContext,attributes );        
        assertEquals("Wrong code returned by elementStart",
                PAPIElement.PROCESS_ELEMENT_BODY, i );
        
        i = element.exprElementEnd(requestContext,attributes);
        assertEquals("Wrong code returned by elementEnd",
                PAPIElement.CONTINUE_PROCESSING, i);

        DOMOutputBuffer dob = protocol.getCurrentBuffer();
        String str = DOMUtilities.toString(dob.getRoot());
        assertEquals("Incorrect output ", 
            "<script charset=\"Ascii\" defer=\"defer\" language=\"Language\"" +
            " type=\"text/javascript\"/>", str );
    }

    /**
     * Test the script element with the XHTMLFull protocol
     */ 
    public void testXHTMLFull() throws Exception {
        
        MyXHTMLFull protocol = new MyXHTMLFull();
        privateSetUp( protocol, new MyPageContext() );

        attributes.setLanguage("Language");
        attributes.setType("text/javascript");
        attributes.setCharSet("Ascii");
        attributes.setDefer("defer");
        attributes.setStyleClass("scripto");
        
        // Element start should return SKIP ELEMENT BODY as XHTMLBasic
        // does not support script.
        int i = element.elementStart(requestContext,attributes );        
        assertEquals("Wrong code returned by elementStart",
                PAPIElement.PROCESS_ELEMENT_BODY, i );
        
        i = element.exprElementEnd(requestContext,attributes);
        assertEquals("Wrong code returned by elementEnd",
                PAPIElement.CONTINUE_PROCESSING, i);

        // Language is not handled by XHTMLFull
        DOMOutputBuffer dob = protocol.getCurrentBuffer();
        String str = DOMUtilities.toString(dob.getRoot());
        assertEquals("Incorrect output ", 
                "<script charset=\"Ascii\" defer=\"defer\""+
                " type=\"text/javascript\"/>", str );
    }

    /**
     * A TestMarinerPageContext that can handle javascript
     */
    public class MyPageContext extends TestMarinerPageContext {
        // Javadoc Inherited
        public boolean getBooleanDevicePolicyValue(String policyName) {
            // volantis-api-include-in Auto-generated method stub
            // todo: this should come from a device as well
            if(DevicePolicyConstants.SUPPORTS_JAVASCRIPT.equals(policyName)) {
                return true;
            } else {
                return super.getBooleanDevicePolicyValue(policyName);
            }
        }
    }
        
    /**
     * XHTMLInternal that handles javascript
     * todo: replace this with normal test instance created by builder.
     */
    public class MyXHTMLTransitional extends XHTMLTransitional {
        
        DOMOutputBuffer outputbuffer;

        MyXHTMLTransitional() {
            super(new DefaultProtocolSupportFactory(),
                    new XHTMLFullConfiguration());
            outputbuffer = new DOMOutputBuffer();
            outputbuffer.initialise();
        }
        
        // Javadoc Inherited
        public boolean supportsJavaScript() {
            // todo: this should come from a device!
            return true;
        }
                
        // Javadoc Inherited
        protected DOMOutputBuffer getCurrentBuffer() {
            return outputbuffer;
        }
    }

    /**
     * XHTMLFull that handles javascript
     * todo: replace this with normal test instance created by builder.
     */
    public class MyXHTMLFull extends XHTMLFull {
        
        DOMOutputBuffer outputbuffer;

        MyXHTMLFull() {
            super(new DefaultProtocolSupportFactory(),
                new XHTMLFullConfiguration());
            outputbuffer = new DOMOutputBuffer();
            outputbuffer.initialise();
        }
        
        // Javadoc Inherited
        public boolean supportsJavaScript() {
            // todo: this should come from a device!
            return true;
        }
        
        // Javadoc Inherited
        protected DOMOutputBuffer getCurrentBuffer() {
            return outputbuffer;
        }
    }
    
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Oct-04	5907/1	claire	VBM:2004100108 mergevbm: Handling suitable encoding of scripts depending on protocol

 21-Oct-04	5887/1	claire	VBM:2004100108 Handling suitable encoding of scripts depending on protocol

 25-Mar-04	3576/1	steve	VBM:2003032711 type attribute on Script element

 ===========================================================================
*/
