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

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.protocols.widgets.attributes.AutocompleteAttributes;
import com.volantis.mcs.xdime.xforms.XFInputElementImpl;

import java.util.List;

/**
 * Test the widget:autocomplete element
 */
public class AutocompleteTestCase extends WidgetElementTestCaseAbstract {

    // Javadoc inherited
    protected void setUp() throws Exception {

        // Let the base class create basic mocks                
        super.setUp();

        addDefaultElementExpectations(AutocompleteAttributes.class);

        // Additional calls in callCloseOnProtocol
        protocolMock
            .expects.getMarinerPageContext()
            .returns(pageCtxMock).any();

        pageCtxMock
            .expects.generateUniqueFCID()
            .returns("FC_N_M");

        // create environment context mock
        final EnvironmentContextMock environmentCtxMock =
            new EnvironmentContextMock("environmentContextMock", expectations);
        environmentCtxMock.expects.getCachingDirectives().returns(null).any();
        final MarinerRequestContextMock requestCtxMock =
            ((MarinerRequestContextMock) xdimeContext.getInitialRequestContext());
        requestCtxMock.expects.getEnvironmentContext().returns(
            environmentCtxMock).any();
    }

    protected String getElementName() {
        return WidgetElements.AUTOCOMPLETE.getLocalName();
    }

    protected List /*of ElementType*/ getChildElements() {
        return super.getChildElements();
    }

    public void testWidget() throws Exception {
        XFInputElementImpl parent = new XFInputElementImpl(xdimeContext);
        pushElement(parent);

        executeTest();
    }
}
