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

package com.volantis.mcs.xdime.schema;

import com.volantis.mcs.xdime.events.XMLEventsElements;
import com.volantis.mcs.xdime.mcs.MCSElements;
import com.volantis.mcs.xdime.xforms.XFormElements;
import com.volantis.mcs.xdime.xhtml2.XHTML2Elements;
import com.volantis.mcs.xdime.widgets.WidgetElements;
import com.volantis.mcs.xdime.widgets.ResponseElements;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.model.SchemaNamespaces;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests for {@link XDIME2Elements}.
 */
public class XDIME2ElementsTestCase
        extends TestCaseAbstract {

    private SchemaNamespaces elements;

    protected void setUp() throws Exception {
        super.setUp();

        elements = XDIME2Elements.getDefaultInstance();
    }

    public void checkElementType(ElementType type) {
        ElementType languageType = elements.getElementType(
                type.getNamespaceURI(), type.getLocalName());
        assertSame(type, languageType);
    }

    /**
     * Ensure that populator contains DISelect elements.
     */
    public void testContainsDISelectElements() {
        checkElementType(DISelectElements.SELECT);
    }

    /**
     * Ensure that populator contains SI elements.
     */
    public void testContainsSIElements() {
        checkElementType(SIElements.INSTANCE);
    }

    /**
     * Ensure that populator contains XDIME CP SI elements.
     */
    public void testContainsXDIMECPSIElements() {
        checkElementType(XDIMECPSIElements.INSTANCE);
    }

    /**
     * Ensure that populator contains XDIME CP Interim SI elements.
     */
    public void testContainsXDIMECPInterimSIElements() {
        checkElementType(XDIMECPInterimSIElements.INSTANCE);
    }

    /**
     * Ensure that populator contains XForm elements.
     */
    public void testContainsXFormElements() {
        checkElementType(XFormElements.SELECT);
    }

    /**
     * Ensure that populator contains XHTML2 elements.
     */
    public void testContainsXHTML2Elements() {
        checkElementType(XHTML2Elements.DIV);
    }

    /**
     * Ensure that populator contains MCS elements.
     */
    public void testContainsMCSElements() {
        checkElementType(MCSElements.HANDLER);
    }

    /**
     * Ensure that populator contains Widget elements.
     */
    public void testContainsWidgetElements() {
        checkElementType(WidgetElements.CAROUSEL);
    }

    /**
     * Ensure that populator contains Response elements.
     */
    public void testContainsResponseElements() {
        checkElementType(ResponseElements.BODY);
    }

    /**
     * Ensure that populator contains XMLEvent elements.
     */
    public void testContainsXMLEventElements() {
        checkElementType(XMLEventsElements.LISTENER);
    }
}
