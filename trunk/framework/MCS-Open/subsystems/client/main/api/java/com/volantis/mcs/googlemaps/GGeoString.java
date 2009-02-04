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
 * GeoString is textual representation google map photo image. For example one of
 * Warsaw photo has 'trtqtrsrqrs' representation used for get it from photo server.
 * GeoString always starts from t and contains characters 'q','r','t','s'. 
 * Each of character has special meaning, shortly speaking can be splited into four
 * images where
 * q - upper left part
 * r - upper right part
 * t - bottom left part
 * s - bottom right part
 * characters from left part of string are more significant (older) than from right.
 * With this notation 'tqrts' means part of map than can be found in following steps
 * t - first character meant whole world
 * q - upper left part of whole world
 * r - upper right part of previous images
 * t - bottom left part of previous (after 'r')
 * s - bottom right part of previous - which meant 1/256 part of original image. 
 * 
 * Each representation belong to certain zoom level  - for geostring zoom level is string length - 1
 * (-1 because of initial 't')
 */
public class GGeoString {
    private String geoString;
    
    /**
     *  Offset is used for better quality improvement
     *  Eeach image is splitted into 9 parts 3x3 and each pair offsets 0,1,2 
     *  specyfying position of each part of image inside original image. 
     */    
    /**
     * Default values are zero because when no splitting
     * no offset is necessary. Used mainly in equals method. 
     */
	private int offsetX = 0;
	
	private int offsetY = 0;
    
    public GGeoString(String string){
        this.geoString = string;
    }

	public GGeoString(String string,int offx, int offy){
        this.geoString = string;
        this.offsetX = offx;
        this.offsetY = offy;
    }
        
    public String toString(){
        return this.geoString;        
    }
    
    public int getZoom(){
        return this.geoString.length()-1;
    }
    
	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	// inherited
	// implemented mainly for geoString compare in unit tests
	public boolean equals(Object obj) {
		boolean result = false;
		if(obj instanceof GGeoString){
			GGeoString newObj = (GGeoString)obj;
			if((newObj.offsetX == this.offsetX) &&
			   (newObj.offsetY == this.offsetY) &&
			   (newObj.geoString.equals(this.geoString))){
				result = true;
			}
		} 
		return result;
	}

	// inherited
	// implemented mainly for geoString compare in unit tests
	public int hashCode() {
		return this.geoString.hashCode();
	}   
	
	
	
	
}
