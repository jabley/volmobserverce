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

import com.volantis.mcs.xdime.xhtml2.XHTML2Schema;
import com.volantis.mcs.xml.schema.model.AbstractSchema;
import com.volantis.mcs.xml.schema.model.CompositeModel;
import com.volantis.mcs.xml.schema.model.ElementSchema;

/**
 * Schema for the widget response namespace.
 */
public final class WidgetResponseSchema
        extends AbstractSchema {

    private final ElementSchema response = createElementSchema(ResponseElements.RESPONSE);
    private final ElementSchema head = createElementSchema(ResponseElements.HEAD);
    private final ElementSchema body = createElementSchema(ResponseElements.BODY);
    private final ElementSchema link = createElementSchema(ResponseElements.LINK);
    private final ElementSchema carousel = createElementSchema(ResponseElements.CAROUSEL);
    private final ElementSchema clock = createElementSchema(ResponseElements.CLOCK);
    private final ElementSchema timer = createElementSchema(ResponseElements.TIMER);
    private final ElementSchema ticker_tape = createElementSchema(ResponseElements.TICKER_TAPE);
    private final ElementSchema progress = createElementSchema(ResponseElements.PROGRESS);
    private final ElementSchema folding_item = createElementSchema(ResponseElements.FOLDING_ITEM);
    private final ElementSchema validation = createElementSchema(ResponseElements.VALIDATION);
    private final ElementSchema field = createElementSchema(ResponseElements.FIELD);
    private final ElementSchema message = createElementSchema(ResponseElements.MESSAGE);
    private final ElementSchema autocomplete = createElementSchema(ResponseElements.AUTOCOMPLETE);
    private final ElementSchema tab = createElementSchema(ResponseElements.TAB);
    private final ElementSchema date_picker = createElementSchema(ResponseElements.DATE_PICKER);
    private final ElementSchema map = createElementSchema(ResponseElements.MAP);
    private final ElementSchema deck = createElementSchema(ResponseElements.DECK);
    private final ElementSchema tbody = createElementSchema(ResponseElements.TABLE_BODY);
    private final ElementSchema error = createElementSchema(ResponseElements.ERROR);

    public final CompositeModel RESPONSE_CONTENT = choice();

    public WidgetResponseSchema(XHTML2Schema xhtml2, WidgetSchema widget) {

        RESPONSE_CONTENT
                .add(carousel)
                .add(clock)
                .add(timer)
                .add(ticker_tape)
                .add(progress)
                .add(folding_item)
                .add(autocomplete)
                .add(validation)
                .add(tab)
                .add(date_picker)
                .add(map)
                .add(deck)
                .add(tbody)
                .add(error)
                .add(widget.block_content);

        response.setContentModel(sequence()
                .add(bounded(head).optional())
                .add(body));

        body.setContentModel(RESPONSE_CONTENT);

        head.setContentModel(bounded(link));

        link.setContentModel(EMPTY);

        carousel.setContentModel(bounded(xhtml2.li));

        clock.setContentModel(PCDATA);

        timer.setContentModel(PCDATA);

        ticker_tape.setContentModel(xhtml2.MIXED_TEXT);

        // should be norrowed to 0..100 integer
        progress.setContentModel(PCDATA);

        folding_item.setContentModel(bounded(choice()
                .add(xhtml2.FLOW)));

        validation.setContentModel(sequence()
                .add(bounded(message).optional())
                .add(bounded(field)));

        field.setContentModel(sequence().add(message));

        message.setContentModel(xhtml2.MIXED_FLOW);

        autocomplete.setContentModel(bounded(xhtml2.li).optional());

        tab.setContentModel(xhtml2.MIXED_FLOW);

        date_picker.setContentModel(EMPTY);
        map.setContentModel(PCDATA);
        deck.setContentModel(bounded(widget.deck_page));
        tbody.setContentModel(bounded(xhtml2.tr));
        error.setContentModel(PCDATA);

    }
}
