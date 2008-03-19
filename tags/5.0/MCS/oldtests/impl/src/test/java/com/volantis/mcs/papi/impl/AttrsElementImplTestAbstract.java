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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/AttrsElementImplTestAbstract.java,v 1.2 2003/04/22 10:59:03 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Apr-03    Allan           VBM:2003041506 - Abstract test class for 
 *                              AttrsElement elements. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.TestEnvironmentContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.papi.AttrsAttributes;
import com.volantis.mcs.papi.PAPIConstants;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.VolantisProtocolStub;
import com.volantis.mcs.runtime.RuntimeProjectMock;

import java.util.EmptyStackException;

/**
 * Test case for AttrsElement.
 */
public abstract class AttrsElementImplTestAbstract
        extends AbstractElementImplTestAbstract {

    /**
     * Properties used by the test methods.
     */
    protected AttrsAttributes attrsAttributes;
    protected TestMarinerRequestContext requestContext;
    protected TestMarinerPageContext pageContext;
    protected VolantisProtocolStub protocol;

    /**
     * Set up the various properties that are used by the test methods
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        attrsAttributes = createTestableAttrsAttributes();
        requestContext = new TestMarinerRequestContext();
        ContextInternals.setEnvironmentContext(requestContext,
                                               new TestEnvironmentContext());

        pageContext = new TestMarinerPageContext();
        requestContext.setMarinerPageContext(pageContext);
        protocol = new VolantisProtocolStub();
        pageContext.setProtocol(protocol);
        
        final RuntimeProjectMock projectMock =
                new RuntimeProjectMock("projectMock", expectations);

        requestContext.pushProject(projectMock);
        pageContext.pushRequestContext(requestContext);
    }

    /**
     * Tear down the properties set up in setUp().
     * @throws Exception
     */
    public void tearDown() throws Exception {
        attrsAttributes = null;
        requestContext = null;
        pageContext = null;
        protocol = null;
        super.tearDown();
    }

    /**
     * Test the elementStart method. This method calls the 2 param version
     * of elementStart that is in the specialization of AttrsElement and this
     * in turn calls the 3 param version that is in AttrElement. Thus both
     * are tested. In addition writeOpenMarkup is implicitly test here - see
     * design for more info.
     * @throws Exception
     */
    public void testElementStart() throws Exception {
        MCSAttributes protocolAttributes =
                createTestableProtocolAttributes();
        attrsAttributes.setId("id");
        attrsAttributes.setStyleClass("styleClass");
        attrsAttributes.setTitle("title");
        TestableAttrsElementImpl element =
                (TestableAttrsElementImpl) createTestablePAPIElement();

        element.setVolantisAttributes(protocolAttributes);

        AttrsElementImpl attrsElement = (AttrsElementImpl) element;
        int result = attrsElement.elementStart(requestContext,
                                               attrsAttributes);

        assertEquals("Comparing id.", attrsAttributes.getId(),
                     protocolAttributes.getId());
        assertEquals("Comparing title. ", attrsAttributes.getTitle(),
                     protocolAttributes.getTitle());

        // The writeOpenMarkup() method in the element should have been called.
        assertTrue("AttrsElementImpl.writeOpenMarkup() should have been called" +
                   " for element " + element + " but it was not.",
                   element.writeOpenMarkupHasBeenCalled());

        pageContext.popElement(element.getElement());// If this fails it throws

        assertEquals("Unexpected return result from elementStart",
                     PAPIConstants.PROCESS_ELEMENT_BODY,
                     result);
    }

    /**
     * Test the elementEnd method. This method calls the 2 param version
     * of elementEnd that is in the specialization of AttrsElementImpl and this
     * in turn calls the 3 param version that is in AttrElement. Thus both
     * are tested. In addition writeCloseMarkup is implicitly test here - see
     * design for more info.
     * @throws Exception
     */
    public void testElementEnd() throws Exception {
        TestableAttrsElementImpl element =
                (TestableAttrsElementImpl) createTestablePAPIElement();

        AttrsElementImpl attrsElement = (AttrsElementImpl) element;
        
        // First call elementStart so that the element in pushed onto the stack
        attrsElement.elementStart(requestContext, attrsAttributes);

        int result = attrsElement.elementEnd(requestContext,
                                             attrsAttributes);

        // The writeCloseMarkup() method in the element should have been called
        assertTrue("AttrsElement.writeCloseMarkup() should have been called" +
                   " for element " + element + " but it was not.",
                   element.writeCloseMarkupHasBeenCalled());

        try {
            pageContext.popElement(element.getElement());
            fail("Expected an EmptyStackException from popElement but got " +
                 "none.");
        } catch(EmptyStackException e) {
            // If we are here the test has passed.
        }

        assertEquals("Unexpected return result from elementEnd",
                     PAPIConstants.CONTINUE_PROCESSING,
                     result);
    }

    /**
     * Create some protocol attributes for use in test methods.
     * @return VolantisAttribute
     */
    protected abstract MCSAttributes createTestableProtocolAttributes();

    /**
     * Create some papi attributes for use in test methods.
     * @return PAPIAttributes
     */
    protected abstract AttrsAttributes createTestableAttrsAttributes();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 21-Feb-05	6986/2	emma	VBM:2005021411 Changes merged from MCS3.3

 15-Feb-05	6974/1	emma	VBM:2005021411 Allowing relative paths to devices.mdpr and xml repository

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	6183/2	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 18-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 08-Nov-04	6027/4	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/2	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 13-Feb-04	2966/1	ianw	VBM:2004011923 Added mcsi:policy function

 11-Feb-04	2761/1	mat	VBM:2004011910 Add Project repository

 ===========================================================================
*/
