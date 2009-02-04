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

import com.volantis.mcs.xdime.widgets.WidgetSchema;
import com.volantis.mcs.xdime.xhtml2.XHTML2Schema;
import com.volantis.mcs.xml.schema.model.AbstractSchema;
import com.volantis.mcs.xml.schema.model.CompositeModel;
import com.volantis.mcs.xml.schema.model.ElementSchema;

/**
 * Schema for the ticker namespace.
 */
public final class TickerSchema
        extends AbstractSchema {

    // Elements from Ticker namespace
    private final ElementSchema feed_poller = createElementSchema(TickerElements.FEED_POLLER);
    private final ElementSchema feed = createElementSchema(TickerElements.FEED);
    private final ElementSchema update_status = createElementSchema(TickerElements.UPDATE_STATUS);
    private final ElementSchema items_count = createElementSchema(TickerElements.ITEMS_COUNT);
    private final ElementSchema channels_count = createElementSchema(TickerElements.CHANNELS_COUNT);
    private final ElementSchema item_display = createElementSchema(TickerElements.ITEM_DISPLAY);
    private final ElementSchema item_title = createElementSchema(TickerElements.ITEM_TITLE);
    private final ElementSchema item_icon = createElementSchema(TickerElements.ITEM_ICON);
    private final ElementSchema item_description = createElementSchema(TickerElements.ITEM_DESCRIPTION);
    private final ElementSchema item_plain_description = createElementSchema(TickerElements.ITEM_PLAIN_DESCRIPTION);
    private final ElementSchema item_channel = createElementSchema(TickerElements.ITEM_CHANNEL);

    private final CompositeModel TICKER_TEXT = choice();
    private final CompositeModel TICKER_STRUCTURAL = choice();

    public TickerSchema(XHTML2Schema xhtml2, WidgetSchema widget) {
        
        TICKER_TEXT
            .add(update_status)
            .add(items_count)
            .add(channels_count)
            .add(item_title)
            .add(item_icon)
            .add(item_plain_description)
            .add(item_channel);

        TICKER_STRUCTURAL
            .add(feed_poller)
            .add(item_description)
            .add(item_display);

        // Add ticker elements to XHTML 2 STRUCTURE content set.
        xhtml2.STRUCTURAL.add(TICKER_STRUCTURAL);

        // Add ticker elements to XHTML 2 TEXT content set.
        xhtml2.TEXT.add(TICKER_TEXT);

        widget.TICKER_CAROUSEL_SOURCE.add(feed);

        // Ticker elements content model
        feed_poller.setContentModel(EMPTY);
        feed.setContentModel(EMPTY);
        update_status.setContentModel(EMPTY);
        items_count.setContentModel(EMPTY);
        channels_count.setContentModel(EMPTY);
        item_display.setContentModel(xhtml2.MIXED_FLOW);
        item_title.setContentModel(EMPTY);
        item_icon.setContentModel(EMPTY);
        item_description.setContentModel(EMPTY);
        item_plain_description.setContentModel(EMPTY);
        item_channel.setContentModel(EMPTY);

    }
}
