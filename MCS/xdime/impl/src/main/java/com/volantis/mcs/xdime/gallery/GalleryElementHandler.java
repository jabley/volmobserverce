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
package com.volantis.mcs.xdime.gallery;

import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElement;
import com.volantis.mcs.xdime.initialisation.ElementFactory;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapBuilder;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapPopulator;

/**
 * Define mapping between Gallery widget elements and their factories.
 */
public class GalleryElementHandler extends ElementFactoryMapPopulator {

    // Javadoc inherited.
    public void populateMap(ElementFactoryMapBuilder builder) {

        builder.addMapping(GalleryElements.START_ITEM_NUMBER, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new StartItemNumberElement(context);
             }
        });

        builder.addMapping(GalleryElements.GALLERY, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new GalleryElement(context);
            }
        });

        builder.addMapping(GalleryElements.ITEM_DISPLAY, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ItemDisplayElement(context);
            }
        });

        builder.addMapping(GalleryElements.ITEM, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ItemElement(context);
            }
        });

        builder.addMapping(GalleryElements.ITEM_NUMBER, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ItemNumberElement(context);
            }
        });

        builder.addMapping(GalleryElements.ITEMS_COUNT, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ItemsCountElement(context);
            }
        });
        builder.addMapping(GalleryElements.PAGE_NUMBER, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new PageNumberElement(context);
            }
        });

        builder.addMapping(GalleryElements.PAGES_COUNT, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new PagesCountElement(context);
            }
        });
        builder.addMapping(GalleryElements.ITEMS, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ItemsElement(context);
            }
        });

        builder.addMapping(GalleryElements.SLIDESHOW, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new SlideshowElement(context);
            }
        });

        builder.addMapping(GalleryElements.END_ITEM_NUMBER, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new EndItemNumberElement(context);
            }
        });
    }
}
