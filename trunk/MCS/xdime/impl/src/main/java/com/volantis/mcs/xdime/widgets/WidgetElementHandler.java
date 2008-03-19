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
package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElement;
import com.volantis.mcs.xdime.initialisation.ElementFactory;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapBuilder;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapPopulator;

/**
 * Define mapping between Widget elements and their factories.
 */
public class WidgetElementHandler extends ElementFactoryMapPopulator {

    // Javadoc inherited.
    public void populateMap(ElementFactoryMapBuilder builder) {

        builder.addMapping(WidgetElements.BUTTON, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ButtonElement(context);
            }
        });
        builder.addMapping(WidgetElements.DISPLAY, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new DisplayElement(context);
            }
        });
        builder.addMapping(WidgetElements.INPUT, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new InputElement(context);
            }
        });
        builder.addMapping(WidgetElements.NEXT, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new NextElement(context);
            }
        });
        builder.addMapping(WidgetElements.PREVIOUS, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new PreviousElement(context);
            }
        });
        builder.addMapping(WidgetElements.PLAY, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new PlayElement(context);
            }
        });
        builder.addMapping(WidgetElements.STOP, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new StopElement(context);
            }
        });
        builder.addMapping(WidgetElements.PAUSE, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new PauseElement(context);
            }
        });
        builder.addMapping(WidgetElements.DISMISS, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new DismissElement(context);
            }
        });
        builder.addMapping(WidgetElements.CAROUSEL, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new CarouselElement(context);
             }
        });
        builder.addMapping(WidgetElements.CLOCK_CONTENT, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ClockContentElement(context);
            }
        });
        builder.addMapping(WidgetElements.DIGITAL_CLOCK, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new DigitalClockElement(context);
            }
        });
        builder.addMapping(WidgetElements.STOPWATCH, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new StopwatchElement(context);
            }
        });
        builder.addMapping(WidgetElements.TIMER, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new TimerElement(context);
            }
        });
        builder.addMapping(WidgetElements.REFRESH, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new RefreshElement(context);
             }
        });
        builder.addMapping(WidgetElements.POPUP, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new PopupElement(context);
             }
        });
        builder.addMapping(WidgetElements.TICKER_TAPE, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new TickerTapeElement(context);
             }
        });
        builder.addMapping(WidgetElements.PROGRESS, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new ProgressElement(context);
             }
        });
        builder.addMapping(WidgetElements.FOLDING_ITEM, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new FoldingItemElement(context);
             }
        });
        builder.addMapping(WidgetElements.LOAD, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new LoadElement(context);
             }
        });
        builder.addMapping(WidgetElements.SUMMARY, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new SummaryElement(context);
             }
        });
        builder.addMapping(WidgetElements.DETAIL, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new DetailElement(context);
             }
        });
        builder.addMapping(WidgetElements.WIZARD, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new WizardElement(context);
             }
        });
        builder.addMapping(WidgetElements.LAUNCH, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new LaunchElement(context);
             }
        });
        builder.addMapping(WidgetElements.FIELD_EXPANDER, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new FieldExpanderElement(context);
             }
        });
        builder.addMapping(WidgetElements.VALIDATE, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new ValidateElement(context);
             }
        });
        builder.addMapping(WidgetElements.MESSAGE, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new MessageElement(context);
             }
        });
        builder.addMapping(WidgetElements.MULTIPLE_VALIDATOR, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new MultipleValidatorElement(context);
             }
        });
        builder.addMapping(WidgetElements.FIELD, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new FieldElement(context);
             }
        });
        builder.addMapping(WidgetElements.AUTOCOMPLETE, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new AutocompleteElement(context);
             }
        });
        builder.addMapping(WidgetElements.TABS, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new TabsElement(context);
            }
        });
        builder.addMapping(WidgetElements.TAB, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new TabElement(context);
            }
        });
        builder.addMapping(WidgetElements.MAP, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new MapElement(context);
            }
        });
        builder.addMapping(WidgetElements.MAP_VIEW, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new MapViewElement(context);
            }
        });
        builder.addMapping(WidgetElements.MAP_LOCATION_MARKER, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new MapLocationMarkerElement(context);
            }
        });
        builder.addMapping(WidgetElements.MAP_LOCATION_MARKERS, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new MapLocationMarkersElement(context);
            }
        });
        builder.addMapping(WidgetElements.LOG, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new LogElement(context);
            }
        });
        builder.addMapping(WidgetElements.DATE_PICKER, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new DatePickerElement(context);
            }
        });
        builder.addMapping(WidgetElements.MONTH_DISPLAY, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new MonthDisplayElement(context);
            }
        });
        builder.addMapping(WidgetElements.NEXT_MONTH, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new NextMonthElement(context);
            }
        });
        builder.addMapping(WidgetElements.PREVIOUS_MONTH, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new PreviousMonthElement(context);
            }
        });
        builder.addMapping(WidgetElements.YEAR_DISPLAY, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new YearDisplayElement(context);
            }
        });
        builder.addMapping(WidgetElements.NEXT_YEAR, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new NextYearElement(context);
            }
        });
        builder.addMapping(WidgetElements.PREVIOUS_YEAR, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new PreviousYearElement(context);
            }
        });
        builder.addMapping(WidgetElements.CALENDAR_DISPLAY, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new CalendarDisplayElement(context);
            }
        });
        builder.addMapping(WidgetElements.SET_TODAY, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new SetTodayElement(context);
            }
        });
        builder.addMapping(WidgetElements.HANDLER, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new HandlerElement(context);
            }
        });
        builder.addMapping(WidgetElements.SCRIPT, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ScriptElement(context);
            }
        });
        builder.addMapping(WidgetElements.BLOCK, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new BlockElement(context);
            }
        });
        builder.addMapping(WidgetElements.BLOCK_CONTENT, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new BlockContentElement(context);
            }
        });        
        builder.addMapping(WidgetElements.FETCH, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new FetchElement(context);
            }
        });
        builder.addMapping(WidgetElements.SELECT, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new SelectElement(context);
            }
        });                
        builder.addMapping(WidgetElements.OPTION, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new OptionElement(context);
            }
        });                
        builder.addMapping(WidgetElements.DECK, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new DeckElement(context);
            }
        });
        builder.addMapping(WidgetElements.DECK_PAGE, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new DeckPageElement(context);
            }
        });              
        builder.addMapping(WidgetElements.TABLE, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new TableElement(context);
            }
        });              
        builder.addMapping(WidgetElements.TBODY, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new TableBodyElement(context);
            }
        });              
    }
}
