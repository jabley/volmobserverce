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

package com.volantis.mcs.xdime.response;

import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElement;
import com.volantis.mcs.xdime.widgets.ResponseElements;
import com.volantis.mcs.xdime.initialisation.ElementFactory;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapBuilder;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapPopulator;

/**
 * Define mapping between Widget Response elements and their factories.
 */
public class ResponseElementHandler extends ElementFactoryMapPopulator {

    // Javadoc inherited.
    public void populateMap(ElementFactoryMapBuilder builder) {

        builder.addMapping(ResponseElements.HEAD, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new ResponseHeadElement(context);
             }
        });
        builder.addMapping(ResponseElements.RESPONSE, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new ResponseResponseElement(context);
             }
        });
        builder.addMapping(ResponseElements.BODY, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new ResponseBodyElement(context);
             }
        });
        builder.addMapping(ResponseElements.LINK, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new ResponseLinkElement(context);
             }
        });
        builder.addMapping(ResponseElements.CAROUSEL, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new ResponseCarouselElement(context);
             }
        });
        builder.addMapping(ResponseElements.CLOCK, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ResponseClockElement(context);
            }
       });
        builder.addMapping(ResponseElements.TIMER, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ResponseTimerElement(context);
            }
        });
        builder.addMapping(ResponseElements.TICKER_TAPE, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new ResponseTickerTapeElement(context);
             }
        });
        builder.addMapping(ResponseElements.PROGRESS, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new ResponseProgressElement(context);
             }
        });
        builder.addMapping(ResponseElements.FOLDING_ITEM, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new ResponseFoldingitemElement(context);
             }
        });
        builder.addMapping(ResponseElements.VALIDATION, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new ResponseValidationElement(context);
             }
        });
        builder.addMapping(ResponseElements.FIELD, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new ResponseFieldElement(context);
             }
        });
        builder.addMapping(ResponseElements.MESSAGE, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new ResponseMessageElement(context);
             }
        });
        builder.addMapping(ResponseElements.AUTOCOMPLETE, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new ResponseAutocompleteElement(context);
             }
        });
        builder.addMapping(ResponseElements.TAB, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ResponseTabElement(context);
            }
       });
       builder.addMapping(ResponseElements.DATE_PICKER, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ResponseDatePickerElement(context);
            }
       });
       builder.addMapping(ResponseElements.MAP, new ElementFactory() {
           public XDIMEElement createElement(XDIMEContextInternal context) {
		        return new ResponseMapElement(context);
           }
       });
       builder.addMapping(ResponseElements.DECK, new ElementFactory() {
           public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ResponseDeckElement(context);
           }
       });
       builder.addMapping(ResponseElements.TABLE_BODY, new ElementFactory() {
           public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ResponseTableBodyElement(context);
           }
       });
       builder.addMapping(ResponseElements.ERROR, new ElementFactory() {
           public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ResponseErrorElement(context);
           }
       });
    }
}
