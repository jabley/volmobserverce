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

import com.volantis.mcs.protocols.widgets.attributes.TimerAttributes;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Test the widget:timer element
 */
public class TimerTestCase extends WidgetElementTestCaseAbstract {

    // Javadoc inherited
    protected void setUp() throws Exception {
        
        // Let the base class create basic mocks 
        super.setUp();        
        
        addDefaultElementExpectations(TimerAttributes.class);
    }

    protected String getElementName() {
        return WidgetElements.TIMER.getLocalName();
    }

    public void testWidget() throws Exception {
        TimerElement parent = new TimerElement(xdimeContext);        
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
                "start-time", /* localName */
                "", /* qName */
                "", /* type */
                "60000" /* value */
            );

        attr.addAttribute(
                "", /* uri */ 
                "stop-time", /* localName */
                "", /* qName */
                "", /* type */
                "0" /* value */
            );         
        executeTest(attr);
    }
}
