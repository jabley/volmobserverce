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

package com.volantis.mcs.xdime.gallery;

import com.volantis.mcs.xdime.widgets.WidgetResponseSchema;
import com.volantis.mcs.xdime.widgets.WidgetSchema;
import com.volantis.mcs.xdime.xhtml2.XHTML2Schema;
import com.volantis.mcs.xml.schema.model.AbstractSchema;
import com.volantis.mcs.xml.schema.model.CompositeModel;
import com.volantis.mcs.xml.schema.model.ElementSchema;

/**
 * Schema for gallery namespace.
 */
public final class GallerySchema
        extends AbstractSchema {

    //  Elements from Gallery widget
    private final ElementSchema item_display =
            createElementSchema(GalleryElements.ITEM_DISPLAY);
    private final ElementSchema item_number =
            createElementSchema(GalleryElements.ITEM_NUMBER);
    private final ElementSchema items_count =
            createElementSchema(GalleryElements.ITEMS_COUNT);
    private final ElementSchema page_number =
            createElementSchema(GalleryElements.PAGE_NUMBER);
    private final ElementSchema pages_count =
            createElementSchema(GalleryElements.PAGES_COUNT);
    private final ElementSchema start_item_number =
            createElementSchema(GalleryElements.START_ITEM_NUMBER);
    private final ElementSchema end_item_number =
            createElementSchema(GalleryElements.END_ITEM_NUMBER);
    private final ElementSchema gallery = createElementSchema(GalleryElements.GALLERY);
    private final ElementSchema slideshow = createElementSchema(GalleryElements.SLIDESHOW);
    private final ElementSchema items = createElementSchema(GalleryElements.ITEMS);
    private final ElementSchema item = createElementSchema(GalleryElements.ITEM);

    private final CompositeModel GALLERY_TEXT = choice();
    private final CompositeModel GALLERY_STRUCTURAL = choice();

    public GallerySchema(
            XHTML2Schema xhtml2, WidgetSchema widget,
            WidgetResponseSchema response) {

        GALLERY_STRUCTURAL
                .add(gallery)
                .add(slideshow)
                .add(items);

        GALLERY_TEXT
                .add(item_display)
                .add(item_number)
                .add(items_count)
                .add(page_number)
                .add(pages_count)
                .add(start_item_number)
                .add(end_item_number);

        xhtml2.STRUCTURAL.add(GALLERY_STRUCTURAL);

        xhtml2.TEXT.add(GALLERY_TEXT);

        response.RESPONSE_CONTENT.add(items);

        item_display.setContentModel(EMPTY);
        item_number.setContentModel(EMPTY);
        items_count.setContentModel(EMPTY);
        page_number.setContentModel(EMPTY);
        pages_count.setContentModel(EMPTY);
        start_item_number.setContentModel(EMPTY);
        end_item_number.setContentModel(EMPTY);
        gallery.setContentModel(xhtml2.MIXED_FLOW);
        slideshow.setContentModel(xhtml2.MIXED_FLOW);
        items.setContentModel(choice()
                .add(widget.load)
                .add(bounded(item)));
        item.setContentModel(sequence()
                .add(widget.summary)
                .add(widget.detail));
    }

}
