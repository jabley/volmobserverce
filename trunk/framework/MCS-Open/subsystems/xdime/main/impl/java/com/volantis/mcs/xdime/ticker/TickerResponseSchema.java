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

package com.volantis.mcs.xdime.ticker;

import com.volantis.mcs.xdime.widgets.WidgetResponseSchema;
import com.volantis.mcs.xdime.xhtml2.XHTML2Schema;
import com.volantis.mcs.xml.schema.model.AbstractSchema;
import com.volantis.mcs.xml.schema.model.CompositeModel;
import com.volantis.mcs.xml.schema.model.ElementSchema;

/**
 * Schema for the ticker response namespace.
 */
public final class TickerResponseSchema
        extends AbstractSchema {

    // Elements from Ticker response namespace
    private final ElementSchema feed_poller =
            createElementSchema(TickerResponseElements.FEED_POLLER);
    private final ElementSchema add_item =
            createElementSchema(TickerResponseElements.ADD_ITEM);
    private final ElementSchema title = createElementSchema(TickerResponseElements.TITLE);
    private final ElementSchema icon = createElementSchema(TickerResponseElements.ICON);
    private final ElementSchema description =
            createElementSchema(TickerResponseElements.DESCRIPTION);
    private final ElementSchema plain_description =
            createElementSchema(TickerResponseElements.PLAIN_DESCRIPTION);
    private final ElementSchema remove_item =
            createElementSchema(TickerResponseElements.REMOVE_ITEM);
    private final ElementSchema set_skip_times =
            createElementSchema(TickerResponseElements.SET_SKIP_TIMES);
    private final ElementSchema skip_time =
            createElementSchema(TickerResponseElements.SKIP_TIME);
    private final ElementSchema set_url = createElementSchema(TickerResponseElements.SET_URL);
    private final ElementSchema set_polling_interval =
            createElementSchema(TickerResponseElements.SET_POLLING_INTERVAL);

    private final CompositeModel TICKER_RESPONSE = choice();


    public TickerResponseSchema(
            XHTML2Schema xhtml2, WidgetResponseSchema response) {
        
        TICKER_RESPONSE.add(feed_poller);

        response.RESPONSE_CONTENT.add(TICKER_RESPONSE);

        feed_poller.setContentModel(bounded(choice()
                .add(add_item)
                .add(remove_item)
                .add(set_skip_times)
                .add(set_url)
                .add(set_polling_interval)));

        add_item.setContentModel(sequence()
                .add(title)
                .add(bounded(icon).optional())
                .add(description)
                .add(bounded(plain_description).optional()));

        title.setContentModel(xhtml2.MIXED_TEXT);
        icon.setContentModel(xhtml2.MIXED_TEXT);
        description.setContentModel(xhtml2.MIXED_FLOW);
        plain_description.setContentModel(xhtml2.MIXED_TEXT);

        remove_item.setContentModel(EMPTY);

        set_skip_times.setContentModel(bounded(skip_time).min(0).max(2));
        skip_time.setContentModel(EMPTY);
        set_url.setContentModel(PCDATA);
        set_polling_interval.setContentModel(PCDATA);
    }
}
