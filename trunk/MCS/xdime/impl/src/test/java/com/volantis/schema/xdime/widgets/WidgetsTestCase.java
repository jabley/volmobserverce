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

package com.volantis.schema.xdime.widgets;

import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.schema.NoDeclarationFor;
import com.volantis.schema.SchemaTestBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junit.framework.Test;

/**
 * Test case for the Widgets schemata.
 */
public class WidgetsTestCase extends TestCaseAbstract {

    /**
     * Build a test suite.
     * 
     * @return The test suite.
     */
    public static Test suite() {

        SchemaTestBuilder builder 
            = new SchemaTestBuilder(WidgetsTestCase.class, XDIMESchemata.ALL_XDIME2_SCHEMATA);

        builder.addInvalidDocument("xml/no-such-widget.xdime", 
                new NoDeclarationFor("widget:no-such-widget"));

        // Uncomment all tests when the schema is fixed
        // builder.addValidDocument("xml/autocomplete.xdime");
        builder.addValidDocument("xml/carousel.xdime");
        builder.addValidDocument("xml/date-picker.xdime");
        //builder.addValidDocument("xml/dynamicmenu.xdime");
        //builder.addValidDocument("xml/field-expander.xdime");
        builder.addValidDocument("xml/folding-item.xdime");
        //builder.addValidDocument("xml/item-gallery.xdime");
        //builder.addValidDocument("xml/map.xdime");
        //builder.addValidDocument("xml/multiple-validator.xdime");
        //builder.addValidDocument("xml/popup.xdime");
        builder.addValidDocument("xml/progressbar.xdime");
        //builder.addValidDocument("xml/simple-validator.xdime");
        //builder.addValidDocument("xml/tabs.xdime");
        builder.addValidDocument("xml/ticker-tape.xdime");
        //builder.addValidDocument("xml/wizard.xdime");
        builder.addValidDocument("xml/map-location-markers.xdime");        
        builder.addValidDocument("xml/date-picker.xdime");
        builder.addValidDocument("xml/digital-clock.xdime");
        builder.addValidDocument("xml/stopwatch.xdime");
        builder.addValidDocument("xml/timer.xdime");
        builder.addValidDocument("xml/deck.xdime");
        builder.addValidDocument("xml/ajax-deck.xdime");
        builder.addValidDocument("xml/table.xdime");
        builder.addValidDocument("xml/ajax-table.xdime");
        
        return builder.getSuite();
    }
}
