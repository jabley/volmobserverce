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
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolStub;
import com.volantis.mcs.papi.impl.BlockElementTestAbstract;
import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.papi.AnchorAttributes;
import com.volantis.mcs.papi.PAPIException;

/**
 * Test case for the anchor element.
 * <p>
 * For now it just tests that we deal with id and name properly.
 */ 
public class AnchorElementImplTestCase extends BlockElementTestAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    // Inherit javadoc.
    protected BlockAttributes createTestableBlockAttributes() {
        return new AnchorAttributes();
    }

    /**
     * Convenience method to return the result from 
     * {@link #createTestableBlockAttributes} as an 
     * {@link AnchorAttributes}.
     */ 
    protected AnchorAttributes createTestableAnchorAttributes() {
        return (AnchorAttributes) createTestableBlockAttributes();
    }

    /**
     * Ensure that a PAPI id attribute generates a Protocol id attribute.
     * 
     * @throws com.volantis.mcs.papi.PAPIException
     */ 
    public void testIdGeneratesId() throws PAPIException {
        AnchorAttributes papiAttrs = createTestableAnchorAttributes();
        String value = "value";
        papiAttrs.setId(value);
        checkIdIsGenerated(papiAttrs, value);
    }

    /**
     * Ensure that a PAPI name attribute generates a Protocol id attribute.
     * 
     * @throws PAPIException
     */ 
    public void testNameGeneratesId() throws PAPIException {
        AnchorAttributes papiAttrs = createTestableAnchorAttributes();
        String value = "value";
        papiAttrs.setName(value);
        checkIdIsGenerated(papiAttrs, value);
    }

    /**
     * Ensure that identical PAPI id and name attributes generates a Protocol 
     * id attribute.
     * 
     * @throws PAPIException
     */ 
    public void testIdAndNameSameGeneratesId() throws PAPIException {
        AnchorAttributes papiAttrs = createTestableAnchorAttributes();
        String value = "value";
        papiAttrs.setId(value);
        papiAttrs.setName(value);
        checkIdIsGenerated(papiAttrs, value);
    }

    /**
     * Ensure that different PAPI id and name attribute generates a Protocol 
     * id attribute with the value supplied to id - that is, that id overrides
     * name.
     * 
     * @throws PAPIException
     */ 
    public void testIdAndNameDifferentGeneratesId() throws PAPIException {
        AnchorAttributes papiAttrs = createTestableAnchorAttributes();
        String value = "value";
        papiAttrs.setId(value);
        papiAttrs.setName("ignored");
        // NOTE: this should generate a warning as well, but a bit hard to
        // test for without some infrastructure...
        checkIdIsGenerated(papiAttrs, value);
    }

    /**
     * Ensure that the {@link AnchorElementImpl#elementStartImpl}, given the PAPI
     * attributes specified, generates a Protocol id attribute of the value 
     * specified.
     *  
     * @param papiAttrs the PAPI attributes to use.
     * @param value the Protocol id attribute value to be generated.
     * @throws PAPIException
     */ 
    private void checkIdIsGenerated(AnchorAttributes papiAttrs, 
            final String value) throws PAPIException {
        // enable to informally test the claim in the comment above. 
        // enableLog4jDebug();
        
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        VolantisProtocol protocol = new VolantisProtocolStub() {
            public void writeOpenAnchor (
                    com.volantis.mcs.protocols.AnchorAttributes attributes) {
                assertEquals( "", value, attributes.getId());
            }
        };
        pageContext.pushRequestContext(requestContext);
        pageContext.setProtocol(protocol);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        protocol.setMarinerPageContext(pageContext);
        
        AnchorElementImpl element = new AnchorElementImpl();
        element.elementStartImpl(requestContext, papiAttrs);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 15-Mar-04	3403/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 ===========================================================================
*/
