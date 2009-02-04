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

import com.volantis.mcs.xdime.xforms.XFormsSchema;
import com.volantis.mcs.xdime.xhtml2.XHTML2Schema;
import com.volantis.mcs.xml.schema.model.AbstractSchema;
import com.volantis.mcs.xml.schema.model.CompositeModel;
import com.volantis.mcs.xml.schema.model.ElementSchema;
import com.volantis.mcs.xml.schema.model.ContentModel;

/**
 * Schema for the widget namespace.
 */
public final class WidgetSchema
        extends AbstractSchema {

    // Elements from widgets namespace
    private final ElementSchema carousel = createElementSchema(WidgetElements.CAROUSEL);
    private final ElementSchema ticker_tape = createElementSchema(WidgetElements.TICKER_TAPE);
    private final ElementSchema refresh = createElementSchema(WidgetElements.REFRESH);
    private final ElementSchema progress = createElementSchema(WidgetElements.PROGRESS);
    private final ElementSchema popup = createElementSchema(WidgetElements.POPUP);
    private final ElementSchema dismiss = createElementSchema(WidgetElements.DISMISS);
    private final ElementSchema folding_item = createElementSchema(WidgetElements.FOLDING_ITEM);
    public final ElementSchema summary = createElementSchema(WidgetElements.SUMMARY);
    public final ElementSchema detail = createElementSchema(WidgetElements.DETAIL);
    public final ElementSchema load = createElementSchema(WidgetElements.LOAD);
    private final ElementSchema fetch = createElementSchema(WidgetElements.FETCH);
    private final ElementSchema wizard = createElementSchema(WidgetElements.WIZARD);
    private final ElementSchema launch = createElementSchema(WidgetElements.LAUNCH);
    private final ElementSchema field_expander = createElementSchema(WidgetElements.FIELD_EXPANDER);
    private final ElementSchema validate = createElementSchema(WidgetElements.VALIDATE);
    private final ElementSchema message = createElementSchema(WidgetElements.MESSAGE);
    private final ElementSchema multiple_validator = createElementSchema(WidgetElements.MULTIPLE_VALIDATOR);
    private final ElementSchema field = createElementSchema(WidgetElements.FIELD);
    private final ElementSchema autocomplete = createElementSchema(WidgetElements.AUTOCOMPLETE);
    private final ElementSchema log = createElementSchema(WidgetElements.LOG);
    private final ElementSchema tabs = createElementSchema(WidgetElements.TABS);
    private final ElementSchema tab = createElementSchema(WidgetElements.TAB);
    private final ElementSchema digital_clock = createElementSchema(WidgetElements.DIGITAL_CLOCK);
    private final ElementSchema clock_content = createElementSchema(WidgetElements.CLOCK_CONTENT);
    private final ElementSchema stopwatch = createElementSchema(WidgetElements.STOPWATCH);
    private final ElementSchema timer = createElementSchema(WidgetElements.TIMER);
    private final ElementSchema next = createElementSchema(WidgetElements.NEXT);
    private final ElementSchema previous = createElementSchema(WidgetElements.PREVIOUS);
    private final ElementSchema play = createElementSchema(WidgetElements.PLAY);
    private final ElementSchema stop = createElementSchema(WidgetElements.STOP);
    private final ElementSchema pause = createElementSchema(WidgetElements.PAUSE);
    private final ElementSchema button = createElementSchema(WidgetElements.BUTTON);
    private final ElementSchema display = createElementSchema(WidgetElements.DISPLAY);
    private final ElementSchema input = createElementSchema(WidgetElements.INPUT);
    private final ElementSchema map = createElementSchema(WidgetElements.MAP);
    private final ElementSchema map_view = createElementSchema(WidgetElements.MAP_VIEW);
    private final ElementSchema map_location_marker = createElementSchema(WidgetElements.MAP_LOCATION_MARKER);
    private final ElementSchema map_location_markers = createElementSchema(WidgetElements.MAP_LOCATION_MARKERS);
    private final ElementSchema script = createElementSchema(WidgetElements.SCRIPT);
    private final ElementSchema handler = createElementSchema(WidgetElements.HANDLER);
    private final ElementSchema block = createElementSchema(WidgetElements.BLOCK);
    final ElementSchema block_content = createElementSchema(WidgetElements.BLOCK_CONTENT);
    private final ElementSchema select = createElementSchema(WidgetElements.SELECT);
    private final ElementSchema option = createElementSchema(WidgetElements.OPTION);
    private final ElementSchema deck = createElementSchema(WidgetElements.DECK);
    final ElementSchema deck_page = createElementSchema(WidgetElements.DECK_PAGE);
    private final ElementSchema table = createElementSchema(WidgetElements.TABLE);
    private final ElementSchema tbody = createElementSchema(WidgetElements.TBODY);

    private final ElementSchema date_picker = createElementSchema(WidgetElements.DATE_PICKER);
    private final ElementSchema month_display = createElementSchema(WidgetElements.MONTH_DISPLAY);
    private final ElementSchema next_month = createElementSchema(WidgetElements.NEXT_MONTH);
    private final ElementSchema previous_month = createElementSchema(WidgetElements.PREVIOUS_MONTH);
    private final ElementSchema year_display = createElementSchema(WidgetElements.YEAR_DISPLAY);
    private final ElementSchema next_year = createElementSchema(WidgetElements.NEXT_YEAR);
    private final ElementSchema previous_year = createElementSchema(WidgetElements.PREVIOUS_YEAR);
    private final ElementSchema calendar_display = createElementSchema(WidgetElements.CALENDAR_DISPLAY);
    private final ElementSchema set_today = createElementSchema(WidgetElements.SET_TODAY);

    // Controls are inline elements which cannot be freely nessted inside other
    // inline elements. In particular controls cannot be nested inside other control.
    private final CompositeModel WIDGET_CONTROLS = choice();
    private final CompositeModel WIDGET = choice();
    public final CompositeModel TICKER_CAROUSEL_SOURCE = choice();

    public WidgetSchema(XHTML2Schema xhtml2, XFormsSchema xforms) {
        WIDGET_CONTROLS
                .add(button)
                .add(input)
                .add(select)
                .add(display)
                .add(dismiss)
                .add(next)
                .add(previous)
                .add(play)
                .add(stop)
                .add(pause);

        // Initialise the widget content set.
        WIDGET.add(carousel)
              .add(ticker_tape)
              .add(popup)
              .add(progress)
              .add(folding_item)
              .add(wizard)
              .add(field_expander)
              .add(multiple_validator)
              .add(tabs)
              .add(map)
              .add(map_view)
              .add(map_location_marker)
              .add(map_location_markers)
              .add(log)
              .add(date_picker)
              .add(block)
              .add(block_content)
              .add(digital_clock)
              .add(stopwatch)
              .add(timer)
              .add(deck)
              .add(deck_page)
              .add(table);

        xhtml2.HEAD_CONTENT
                .add(handler)
                .add(script);

        // Add widget elements to XHTML 2 STRUCTURAL content set.
        xhtml2.STRUCTURAL
                .add(WIDGET);

        // Add widget elements to XHTML 2 TEXT content set.
        xhtml2.TEXT
            .add(WIDGET_CONTROLS)
            .add(dismiss)
            .add(next)
            .add(previous)
            .add(play)
            .add(stop)
            .add(pause)
            .add(next_month)
            .add(next_year)
            .add(previous_month)
            .add(previous_year)
            .add(set_today)
            .add(month_display)
            .add(year_display)
            .add(calendar_display);

        // Some of the elements can contain arbitary text but not widget
        // controls, possibly because they are nested within controls
        // themselves. 
        ContentModel MIXED_TEXT_EXCLUDING_CONTROLS =
                wrapper(xhtml2.MIXED_TEXT).exclude(WIDGET_CONTROLS);

        button.setContentModel(MIXED_TEXT_EXCLUDING_CONTROLS);
        display.setContentModel(EMPTY);
        dismiss.setContentModel(MIXED_TEXT_EXCLUDING_CONTROLS);
        next.setContentModel(MIXED_TEXT_EXCLUDING_CONTROLS);
        previous.setContentModel(MIXED_TEXT_EXCLUDING_CONTROLS);
        play.setContentModel(MIXED_TEXT_EXCLUDING_CONTROLS);
        stop.setContentModel(MIXED_TEXT_EXCLUDING_CONTROLS);
        pause.setContentModel(MIXED_TEXT_EXCLUDING_CONTROLS);
        launch.setContentModel(MIXED_TEXT_EXCLUDING_CONTROLS);
        input.setContentModel(bounded(choice().add(PCDATA)));

        select.setContentModel(sequence()
                .add(bounded(option)
                .atLeastOne()));

        option.setContentModel(PCDATA);

        refresh.setContentModel(EMPTY);

        TICKER_CAROUSEL_SOURCE.add(refresh);

        carousel.setContentModel(sequence()
                .add(bounded(TICKER_CAROUSEL_SOURCE).optional())
                .add(bounded(xhtml2.li)));

        progress.setContentModel(sequence().add(
                bounded(refresh).optional()));

        next_month.setContentModel(MIXED_TEXT_EXCLUDING_CONTROLS);
        previous_month.setContentModel(MIXED_TEXT_EXCLUDING_CONTROLS);
        next_year.setContentModel(MIXED_TEXT_EXCLUDING_CONTROLS);
        previous_year.setContentModel(MIXED_TEXT_EXCLUDING_CONTROLS);
        set_today.setContentModel(MIXED_TEXT_EXCLUDING_CONTROLS);

        year_display.setContentModel(EMPTY);
        month_display.setContentModel(EMPTY);
        calendar_display.setContentModel(EMPTY);

        popup.setContentModel(bounded(choice()
                .add(xhtml2.FLOW)
                .add(PCDATA)));

        ticker_tape.setContentModel(sequence()
                .add(bounded(TICKER_CAROUSEL_SOURCE).optional())
                .add(xhtml2.MIXED_TEXT));

        load.setContentModel(EMPTY);
        fetch.setContentModel(EMPTY);
        refresh.setContentModel(EMPTY);

        folding_item.setContentModel(sequence()
                .add(bounded(summary).atLeastOne())
                .add(bounded(detail).atLeastOne()));

        summary.setContentModel(xhtml2.MIXED_FLOW);

        // The commented-out content model for "detail" element is valid 
        // (compliant with functional specifivation), but
        // for unknown reason (propably a bug in validation engine), following
        // element does not pass validation: <widget:detail>foo</widget:detail>.
        // That's why another validation rule is used, which passes all
        // valid markup, but does not fail in some cases of invalid markup.
        // Until validation engine gets fixed, it's better to have a
        // validation which allows some invalid markup rather than fail on valid
        // one.
        
        //detail.setContentModel(choice()
        //        .add(xhtml2.MIXEL_FLOW)
        //        .add(load));
        detail.setContentModel(sequence()
                .add(bounded(load).optional())
                .add(xhtml2.MIXED_FLOW));

        wizard.setContentModel(bounded(choice()
                .add(xhtml2.FLOW)
                .add(PCDATA)
                .add(launch)));

        field_expander.setContentModel(xhtml2.MIXED_FLOW);

        // Add multiple-validator to the set of XForms controls.
        xforms.FORM_CONTROLS.add(multiple_validator);

        multiple_validator.setContentModel(sequence()
                .add(bounded(field))
                .add(validate));

        field.setContentModel(EMPTY);

        // Add validate to the body of xf:input element.
        xforms.INPUT_CONTENT.add(validate);
        validate.setContentModel(bounded(message));

        message.setContentModel(PCDATA);

        // Add autocomplete to the content model for the xf:input element.
        xforms.INPUT_CONTENT.add(autocomplete);
        autocomplete.setContentModel(EMPTY);

        tabs.setContentModel(sequence()
                .add(bounded(tab).min(1)));

        tab.setContentModel(bounded(choice()
                .add(load)
                .add(xhtml2.MIXED_FLOW)).optional());

        log.setContentModel(EMPTY);

        map.setContentModel(xhtml2.MIXED_FLOW);
        map_view.setContentModel(EMPTY);
        map_location_marker.setContentModel(EMPTY);
        map_location_markers.setContentModel(sequence()
                .add(bounded(map_location_marker)));

        date_picker.setContentModel(sequence()
                .add(bounded(load).optional())
                .add(xhtml2.MIXED_FLOW));

        clock_content.setContentModel(xhtml2.MIXED_FLOW);

        digital_clock.setContentModel(sequence()
                .add(bounded(refresh).optional())
                .add(bounded(clock_content)));

        stopwatch.setContentModel(sequence()
                .add(bounded(clock_content)));

        timer.setContentModel(sequence()
                .add(bounded(load).optional())
                .add(bounded(clock_content)));


        script.setContentModel(PCDATA);
        handler.setContentModel(EMPTY);

        block.setContentModel(sequence()
                .add(bounded(load).optional())
                .add(bounded(fetch).optional())
                .add(bounded(refresh).optional())
                .add(bounded(block_content).optional()));
        block_content.setContentModel(xhtml2.MIXED_FLOW);

        deck.setContentModel(choice()
                .add(bounded(load).optional())
                .add(bounded(deck_page)));

        deck_page.setContentModel(xhtml2.MIXED_FLOW);

        table.setContentModel(sequence()
                .add(bounded(xhtml2.caption).optional())
                .add(choice()
                        .add(sequence()
                                .add(bounded(xhtml2.thead).optional())
                                .add(bounded(xhtml2.tfoot).optional())
                                .add(bounded(choice()
                                        .add(xhtml2.tbody)
                                        .add(tbody)).atLeastOne()))
                        .add(bounded(xhtml2.tr).atLeastOne())));

        tbody.setContentModel(sequence()
                .add(bounded(load).optional())
                .add(bounded(xhtml2.tr)));
    }
}
