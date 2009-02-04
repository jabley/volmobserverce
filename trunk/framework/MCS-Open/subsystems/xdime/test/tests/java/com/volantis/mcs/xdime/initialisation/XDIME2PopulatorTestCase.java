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

package com.volantis.mcs.xdime.initialisation;

import com.volantis.mcs.xdime.schema.SIElements;
import com.volantis.mcs.xdime.xforms.XFormElements;
import com.volantis.mcs.xdime.xhtml2.XHTML2Elements;
import com.volantis.mcs.xdime.widgets.WidgetElements;
import com.volantis.mcs.xdime.widgets.ResponseElements;
import com.volantis.mcs.xdime.schema.XDIMECPInterimSIElements;
import com.volantis.mcs.xdime.schema.XDIMECPSIElements;
import com.volantis.mcs.xdime.mcs.MCSElements;
import com.volantis.mcs.xdime.events.XMLEventsElements;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests for {@link XDIME2Populator}.
 */
public class XDIME2PopulatorTestCase
        extends TestCaseAbstract {

    private ElementFactoryMap factoryMap;

    protected void setUp() throws Exception {
        super.setUp();

        XDIME2Populator populator = new XDIME2Populator();
        ElementFactoryMapBuilder builder = new ElementFactoryMapBuilder();
        populator.populateMap(builder);
        factoryMap = builder.buildFactoryMap();
    }

    /**
     * Ensure that populator contains SI elements.
     */
    public void testContainsSIElements() {
        assertNotNull(factoryMap.getElementFactory(SIElements.INSTANCE));
    }

    /**
     * Ensure that populator contains XDIME CP SI elements.
     */
    public void testContainsXDIMECPSIElements() {
        assertNotNull(factoryMap.getElementFactory(XDIMECPSIElements.INSTANCE));
    }

    /**
     * Ensure that populator contains XDIME CP Interim SI elements.
     */
    public void testContainsXDIMECPInterimSIElements() {
        assertNotNull(factoryMap.getElementFactory(
                XDIMECPInterimSIElements.INSTANCE));
    }

    /**
     * Ensure that populator contains XForm elements.
     */
    public void testContainsXFormElements() {
        assertNotNull(factoryMap.getElementFactory(XFormElements.SELECT));
    }

    /**
     * Ensure that populator contains XHTML2 elements.
     */
    public void testContainsXHTML2Elements() {
        assertNotNull(factoryMap.getElementFactory(XHTML2Elements.DIV));
    }

    /**
     * Ensure that populator contains MCS elements.
     */
    public void testContainsMCSElements() {
        assertNotNull(factoryMap.getElementFactory(MCSElements.HANDLER));
    }

    /**
     * Ensure that populator contains Widget elements.
     */
    public void testContainsWidgetElements() {
        assertNotNull(factoryMap.getElementFactory(WidgetElements.CAROUSEL));
    }

    /**
     * Ensure that populator contains Response elements.
     */
    public void testContainsResponseElements() {
        assertNotNull(factoryMap.getElementFactory(ResponseElements.BODY));
    }

    /**
     * Ensure that populator contains XMLEvent elements.
     */
    public void testContainsXMLEventElements() {
        assertNotNull(factoryMap.getElementFactory(XMLEventsElements.LISTENER));
    }
}
