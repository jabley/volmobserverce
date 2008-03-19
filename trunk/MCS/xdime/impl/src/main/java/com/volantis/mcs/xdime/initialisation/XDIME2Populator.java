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

import com.volantis.mcs.xdime.diselect.DISelectElementHandler;
import com.volantis.mcs.xdime.events.XMLEventsElementHandler;
import com.volantis.mcs.xdime.gallery.GalleryElementHandler;
import com.volantis.mcs.xdime.mcs.MCSElementHandler;
import com.volantis.mcs.xdime.response.ResponseElementHandler;
import com.volantis.mcs.xdime.ticker.TickerElementHandler;
import com.volantis.mcs.xdime.ticker.response.TickerResponseElementHandler;
import com.volantis.mcs.xdime.widgets.WidgetElementHandler;
import com.volantis.mcs.xdime.xforms.SIElementHandler;
import com.volantis.mcs.xdime.xforms.XFormsElementHandler;
import com.volantis.mcs.xdime.xhtml2.XHTML2ElementHandler;

/**
 * Populates a {@link ElementFactoryMapBuilder} with mappings for all the
 * element types in XDIME 2.
 */
public class XDIME2Populator
        extends ElementFactoryMapPopulator {

    /**
     * The populators for the individual namespaces.
     */
    private static final ElementFactoryMapPopulator[] POPULATORS =
            new ElementFactoryMapPopulator[]{
                new DISelectElementHandler(),
                new MCSElementHandler(),
                new ResponseElementHandler(),
                new SIElementHandler(),
                new WidgetElementHandler(),
                new XFormsElementHandler(),
                new XHTML2ElementHandler(),
                new XMLEventsElementHandler(),
                new TickerElementHandler(),
                new TickerResponseElementHandler(),
                new GalleryElementHandler()
            };

    // Javadoc inherited.
    public void populateMap(ElementFactoryMapBuilder builder) {
        for (int i = 0; i < POPULATORS.length; i++) {
            ElementFactoryMapPopulator populator = POPULATORS[i];
            populator.populateMap(builder);
        }
    }
}
