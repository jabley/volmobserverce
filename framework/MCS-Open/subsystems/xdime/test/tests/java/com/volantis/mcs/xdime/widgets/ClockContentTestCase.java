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

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.protocols.widgets.attributes.ClockContentAttributes;
import com.volantis.mcs.xdime.XDIMEContentHandler;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Test the widget:clock-content element
 */
public class ClockContentTestCase extends WidgetElementTestCaseAbstract {

    // Javadoc inherited
    protected void setUp() throws Exception {
        
        // Let the base class create basic mocks 
        super.setUp();  
        XDIMEContentHandler handler = getXDIMEContentHandler();
        MarinerRequestContextMock context = 
        	(MarinerRequestContextMock)handler.getCurrentRequestContext();
        ApplicationContext appContext = new ApplicationContext(context);
        
        context.expects.getApplicationContext().returns(appContext).fixed(2);
               
        addDefaultElementExpectations(ClockContentAttributes.class);
    }

    protected String getElementName() {
        return WidgetElements.CLOCK_CONTENT.getLocalName();
    }

    public void testWidget() throws Exception {
        DigitalClockElement parent = new DigitalClockElement(xdimeContext);        
        pushElement(parent);
    	AttributesImpl attr = new AttributesImpl();
        attr.addAttribute(
                "", /* uri */ 
                "id", /* localName */
                "", /* qName */
                "", /* type */
                "widgetId" /* value */
            );
        attr.addAttribute(
                "", /* uri */ 
                "type", /* localName */
                "", /* qName */
                "", /* type */
                "digit" /* value */
            );

        attr.addAttribute(
                "", /* uri */ 
                "value", /* localName */
                "", /* qName */
                "", /* type */
                "1" /* value */
            );           
        executeTest(attr);  	
    }


    
}
