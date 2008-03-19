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

import com.volantis.mcs.protocols.widgets.attributes.TableAttributes;
import com.volantis.mcs.xdime.xhtml2.XHTML2Elements;
import com.volantis.mcs.xml.schema.validation.DocumentValidator;

/**
 * Test the widget:table element
 */
public class TableTestCase extends WidgetElementTestCaseAbstract {

    // Javadoc inherited
    protected void setUp() throws Exception {
        
        // Let the base class create basic mocks 
        super.setUp();        

        addDefaultElementExpectations(TableAttributes.class);
    }

    protected String getElementName() {
        return WidgetElements.TABLE.getLocalName();
    }

    public void testWidget() throws Exception {

        startDocumentAndElement(getElementName());

        DocumentValidator validator = xdimeContext.getDocumentValidator();

        validator.open(XHTML2Elements.THEAD);
        validator.open(XHTML2Elements.TR);
        validator.open(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TR);
        validator.close(XHTML2Elements.THEAD);   
        
        validator.open(XHTML2Elements.TFOOT);
        validator.open(XHTML2Elements.TR);
        validator.open(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TR);
        validator.close(XHTML2Elements.TFOOT);   

        validator.open(XHTML2Elements.TBODY);
        validator.open(XHTML2Elements.TR);
        validator.open(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TR);
        validator.close(XHTML2Elements.TBODY);   

        validator.open(WidgetElements.TBODY);
        validator.open(XHTML2Elements.TR);
        validator.open(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TR);
        validator.close(WidgetElements.TBODY);   

        validator.open(WidgetElements.TBODY);
        validator.open(WidgetElements.LOAD);
        validator.close(WidgetElements.LOAD);
        validator.open(XHTML2Elements.TR);
        validator.open(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TR);
        validator.close(WidgetElements.TBODY);   

        endElementAndDocument(getElementName());
    }
}
