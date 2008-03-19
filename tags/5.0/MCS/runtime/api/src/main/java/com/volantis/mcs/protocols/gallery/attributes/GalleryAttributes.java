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

package com.volantis.mcs.protocols.gallery.attributes;

/**
 * ItemsCount element attributes.
 */
public class GalleryAttributes extends BaseGalleryAttributes {
    private String items;
    private String slideshow;
    private String slideshowPopup;
    /**
     * @return Returns the items.
     */
    public String getItems() {
        return items;
    }
    /**
     * @param items The items to set.
     */
    public void setItems(String items) {
        this.items = items;
    }
    /**
     * @return Returns the slideshow.
     */
    public String getSlideshow() {
        return slideshow;
    }
    /**
     * @param slideshow The slideshow to set.
     */
    public void setSlideshow(String slideshow) {
        this.slideshow = slideshow;
    }
    /**
     * @return Returns the slideshowPopup.
     */
    public String getSlideshowPopup() {
        return slideshowPopup;
    }
    /**
     * @param slideshowPopup The slideshowPopup to set.
     */
    public void setSlideshowPopup(String slideshowPopup) {
        this.slideshowPopup = slideshowPopup;
    }
}
