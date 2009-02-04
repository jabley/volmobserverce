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
package com.volantis.mcs.xdime.ticker.response;

import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElement;
import com.volantis.mcs.xdime.initialisation.ElementFactory;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapBuilder;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapPopulator;
import com.volantis.mcs.xdime.ticker.TickerResponseElements;

/**
 * Define mapping between Widget elements and their factories.
 */
public class TickerResponseElementHandler extends ElementFactoryMapPopulator {

    // Javadoc inherited.
    public void populateMap(ElementFactoryMapBuilder builder) {

        builder.addMapping(TickerResponseElements.FEED_POLLER, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new FeedPollerElement(context);
             }
        });

        builder.addMapping(TickerResponseElements.ADD_ITEM, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new AddItemElement(context);
            }
        });

        builder.addMapping(TickerResponseElements.TITLE, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new TitleElement(context);
            }
        });

        builder.addMapping(TickerResponseElements.ICON, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new IconElement(context);
            }
        });

        builder.addMapping(TickerResponseElements.DESCRIPTION, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new DescriptionElement(context);
            }
        });

        builder.addMapping(TickerResponseElements.PLAIN_DESCRIPTION, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new PlainDescriptionElement(context);
            }
        });

        builder.addMapping(TickerResponseElements.REMOVE_ITEM, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new RemoveItemElement(context);
            }
        });

        builder.addMapping(TickerResponseElements.SET_SKIP_TIMES, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new SetSkipTimesElement(context);
            }
        });

        builder.addMapping(TickerResponseElements.SKIP_TIME, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new SkipTimeElement(context);
            }
        });

        builder.addMapping(TickerResponseElements.SET_URL, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new SetURLElement(context);
            }
        });

        builder.addMapping(TickerResponseElements.SET_POLLING_INTERVAL, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new SetPollingIntervalElement(context);
            }
        });

    }
}
