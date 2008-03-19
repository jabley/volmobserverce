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

package com.volantis.mcs.xdime.schema;

import com.volantis.mcs.xdime.events.XMLEventsElements;
import com.volantis.mcs.xdime.gallery.GalleryElements;
import com.volantis.mcs.xdime.mcs.MCSElements;
import com.volantis.mcs.xdime.ticker.TickerElements;
import com.volantis.mcs.xdime.ticker.TickerResponseElements;
import com.volantis.mcs.xdime.validation.XDIME2CompiledSchema;
import com.volantis.mcs.xdime.widgets.ResponseElements;
import com.volantis.mcs.xdime.widgets.WidgetElements;
import com.volantis.mcs.xdime.xhtml2.XHTML2Elements;
import com.volantis.mcs.xml.schema.compiler.CompiledSchema;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class XDIME2CompiledSchemaTestCase
        extends TestCaseAbstract {

    public void testElements() {
        CompiledSchema schema = XDIME2CompiledSchema.getCompiledSchema();

        checkContainsElement(schema, XHTML2Elements.A);
        checkContainsElement(schema, WidgetElements.AUTOCOMPLETE);
        checkContainsElement(schema, ResponseElements.AUTOCOMPLETE);
        checkContainsElement(schema, TickerElements.CHANNELS_COUNT);
        checkContainsElement(schema, TickerResponseElements.ADD_ITEM);
        checkContainsElement(schema, GalleryElements.END_ITEM_NUMBER);
        checkContainsElement(schema, MCSElements.SCRIPT);
        checkContainsElement(schema, DISelectElements.IF);
        checkContainsElement(schema, XMLEventsElements.LISTENER);
    }

    private void checkContainsElement(CompiledSchema schema, ElementType element) {
        assertTrue("Does not contain " + element,
                schema.containsElementType(element));
    }
}
