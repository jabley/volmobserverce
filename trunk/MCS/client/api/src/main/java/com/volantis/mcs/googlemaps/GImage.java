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
package com.volantis.mcs.googlemaps;

/**
 * Class taht holds image coordinates. Image coordinates points image
 * from wolrd map stored on server and are used to retrieve it.
 * 
 *  Each image has three importand coordinates:
 *  x,y - points image
 *  zoom - points zoom level for which image should be retrieved, this is non-intuitive
 *  but the lower zoom the more details are on map and separate images. 
 *  zoom = 0 is the most detailed map.
 *  zoom = 17 is whole world in one image.
 *  
 *  x,y are dependant on zoom because for some levels there are no images
 *  under certain coordinates. This is calculated in GoogleCalculator and not described
 *  here. 
 *  
 *  Rough approximation could be:
 *  x,y = 2^(17-zoom) where zom belongs to [0,17]
 *  
 *  Moreover it is not documetned by google as not part of public API, so can be changed
 *  without noticing us about that fact. 
 *  
 *  As google maps quality improvement functionality each google original image was splitted
 *  into 9 parts (3x3) but it doesn't affect GImage implementation because it still operate 
 *  on the same coordinates. 
 *  
 */
public class GImage {
    
    private int imgX;
    private int imgY;
    
    private int zoom;
        
    public GImage(int imgX, int imgY,int zoom){
        this.imgX = imgX;
        this.imgY = imgY;
        this.zoom = zoom;
    }
    
    public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public int getImgX() {
        return imgX;
    }

    public void setImgX(int imgX) {
        this.imgX = imgX;
    }

    public int getImgY() {
        return imgY;
    }

    public void setImgY(int imgY) {
        this.imgY = imgY;
    }
    
    
    

}
