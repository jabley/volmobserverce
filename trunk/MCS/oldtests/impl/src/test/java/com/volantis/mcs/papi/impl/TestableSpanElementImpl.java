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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/TestableSpanElementImpl.java,v 1.2 2003/04/22 10:59:03 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Apr-03    Allan           VBM:2003041506 - "Testable" AttrElement for the
 *                              SpanElement. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolStub;
import com.volantis.mcs.papi.impl.TestableAttrsElementImpl;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.PAPIAttributes;

/**
 * A "testable" AttrElement for the SpanElement (we can't extend SpanElement
 * because it is final so this is a wrapper instead.)
 */
public class TestableSpanElementImpl extends AttrsElementImpl
        implements TestableAttrsElementImpl {

    /**
     * The actual SpanElement that this class makes testable.
     */
    private SpanElementImpl element = new SpanElementImpl();

    /**
     * Flag to indicate whether or not writeOpenMarkup() has been called
     * since the flag was last checked.
     */
    private boolean writeOpenMarkupHasBeenCalled = false;

    /**
     * Flag to indicate whether or not writeCloseMarkup() has been called
     * since the flag was last checked.
     */
    private boolean writeCloseMarkupHasBeenCalled = false;

    // javadoc inherited
    public AttrsElementImpl getElement() {
        return element;
    }

    // Javadoc inherited.
    MCSAttributes getMCSAttributes() {
        return element.pattributes;
    }

    // javadoc inherited
    public void setVolantisAttributes(MCSAttributes attributes) {
        element.pattributes =
                (com.volantis.mcs.protocols.SpanAttributes) attributes;
    }

    // javadoc inherited - this method must reset writeOpenMarkupHasBeenCalled.
    public boolean writeOpenMarkupHasBeenCalled() {
        boolean result = writeOpenMarkupHasBeenCalled;
        writeOpenMarkupHasBeenCalled = false;
        return result;
    }

    // javadoc inherited - this method must reset writeCloseMarkupHasBeenCalled
    public boolean writeCloseMarkupHasBeenCalled() {
        boolean result = writeCloseMarkupHasBeenCalled;
        writeCloseMarkupHasBeenCalled = false;
        return result;
    }

    /**
     * Override parent method to set the writeOpenMarkupHasBeenCalled flag.
     */
    void writeOpenMarkup(VolantisProtocol protocol)
            throws PAPIException {
        element.writeOpenMarkup(protocol);
        writeOpenMarkupHasBeenCalled = true;
    }

    /**
     * Override parent method to set the writeCloseMarkupHasBeenCalled flag.
     */
    void writeCloseMarkup(VolantisProtocol protocol)
            throws PAPIException {
        element.writeCloseMarkup(protocol);
        writeCloseMarkupHasBeenCalled = true;
    }

    /**
     * Call styleElementStart on the wrapped SpanElement. This method also uses
     * some jiggery-pockery to set the writeOpenMarkupHasBeenCalled flag
     * if this is true.
     * @param context The MarinerRequestContext.
     * @param papiAttributes The PAPIAttributes.
     * @return the result of SpanElement.elementStart().
     * @throws PAPIException If SpanElement.elementStart() throws one.
     */
    public int styleElementStart(final MarinerRequestContext context,
                            final PAPIAttributes papiAttributes)
            throws PAPIException {

        ReturnsIntInvoker invoker = new ReturnsIntInvoker() {
            public int invoke() throws PAPIException {
                return element.styleElementStart(context, papiAttributes);
            }
        };


        return new ElementStartEndInvoker().invokeElementStartEnd(context,
                                                                  invoker);
    }

    /**
     * Call elementEnd on the wrapped SpanElement. This method also uses
     * some jiggery-pockery to set the writeCloseMarkupHasBeenCalled flag
     * if this is true.
     * @param context The MarinerRequestContext.
     * @param papiAttributes The PAPIAttributes.
     * @return the result of SpanElement.elementEnd().
     * @throws PAPIException If SpanElement.elementEnd() throws one.
     */
    public int styleElementEnd(final MarinerRequestContext context,
                          final PAPIAttributes papiAttributes)
            throws PAPIException {

        ReturnsIntInvoker invoker = new ReturnsIntInvoker() {
            public int invoke() throws PAPIException {
                return element.styleElementEnd(context, papiAttributes);
            }
        };


        return new ElementStartEndInvoker().invokeElementStartEnd(context,
                                                                  invoker);
    }

    /** 
     * The only way to find out if elementStart  or elementEnd is calling
     * writeOpen/CloseMarkup is to find out if writeOpen/CloseSpan on the
     * protocol is being called (since that is what writeOpen/CloseMarkup does).
     * In order to test this, we need to use a protocol that can give us
     * this information. This means creating a new protocol here that 
     * will do this; setting this protocol inside the pageContext that is
     * inside the requestContext; then setting it back to the original
     * protocol when we're done so that the caller of this method is
     * not affected.
     * 
     * Since both the writeOpenMarkup and writeCloseMarkup tests require the
     * same setup - the only difference is the call to elementStart() or
     * elementEnd() - this class is created to make use of a command pattern
     * that facilitates both tests without duplication.
     **/
    private class ElementStartEndInvoker {
        public int invokeElementStartEnd(MarinerRequestContext requestContext,
                                         ReturnsIntInvoker invoker)
                throws PAPIException {
            TestMarinerPageContext pageContext = (TestMarinerPageContext)
                    ((TestMarinerRequestContext) requestContext).getMarinerPageContext();
            VolantisProtocol newProtocol = new VolantisProtocolStub() {
                public void writeOpenSpan(com.volantis.mcs.protocols.SpanAttributes attributes) {
                    writeOpenMarkupHasBeenCalled = true;
                }

                public void writeCloseSpan(com.volantis.mcs.protocols.SpanAttributes attributes) {
                    writeCloseMarkupHasBeenCalled = true;
                }
            };
            VolantisProtocol origProtocol = pageContext.getProtocol();
            pageContext.setProtocol(newProtocol);

            int result = invoker.invoke();

            pageContext.setProtocol(origProtocol);

            return result;

        }
    }

    /**
     * A method invoker for use with the command pattern used by the
     * ElementStartEndInvoker class and methods that use it.
     */ 
    private interface ReturnsIntInvoker {
        public int invoke() throws PAPIException;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 14-Aug-03	958/3	chrisw	VBM:2003070704 Fixed SpanElementTestCase

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 ===========================================================================
*/
