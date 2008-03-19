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
package com.volantis.mcs.clientapp;

import java.util.ArrayList;
import java.util.List;

public class GalleryFeed implements ItemGalleryFeed {

    private static int GALLERY_SIZE = 10;

    private static String [] SUMMARY_LINKS;
    static {
        SUMMARY_LINKS = new String[GALLERY_SIZE];
        for(int i = 0 ;i< GALLERY_SIZE; i++){
            SUMMARY_LINKS[i] = "images/gallery/thumbs/img" + i + "_small.mimg";
        }
    };

    private static String [] DETAILS_LINKS;
    static {
        DETAILS_LINKS = new String[GALLERY_SIZE];
        for(int i = 0 ;i< GALLERY_SIZE; i++){
            DETAILS_LINKS[i] = "images/gallery/photos/img" + i + ".mimg";
        }
    };

    private static String [] SUMMARY_TEXT;
    static {
        SUMMARY_TEXT = new String[GALLERY_SIZE];
        for(int i = 0 ;i< GALLERY_SIZE; i++){
            SUMMARY_TEXT[i] = "Summary "+i;
        }
    };

    private static String [] DETAILS_TEXT;
    static {
        DETAILS_TEXT = new String[GALLERY_SIZE];
        for(int i = 0 ;i< GALLERY_SIZE; i++){
            DETAILS_TEXT[i] = "Details "+i;
        }
    };


    private static String [] DETAILS_DESC;
    static {
        DETAILS_DESC = new String[GALLERY_SIZE];
        for(int i = 0 ;i< GALLERY_SIZE; i++){
            DETAILS_DESC[i] = "Description "+i;
        }
    };

    private List galleryList;

    public GalleryFeed(){
        galleryList = new ArrayList();
        fillGalleryList();
    }

    private GalleryItem createGalleryItem(int i){
        GalleryItem item = new GalleryItem();
        item.itemDetailsDescription = DETAILS_DESC[i];
        item.itemDetailsLink = DETAILS_LINKS[i];
        item.itemDetailsText = DETAILS_TEXT[i];
        item.itemSummaryLink = SUMMARY_LINKS[i];
        item.itemSummaryText = SUMMARY_TEXT[i];
        return item;
    }

    private void fillGalleryList(){
        for(int i=0;i<GALLERY_SIZE;i++){
            galleryList.add(createGalleryItem(i));
        }
    }

    public List getGalleryItems() {
        return galleryList;
    }

}
