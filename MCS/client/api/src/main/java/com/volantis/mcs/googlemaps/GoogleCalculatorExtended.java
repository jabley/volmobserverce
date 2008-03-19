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
package com.volantis.mcs.googlemaps;

/**
 * Google calculator extended is used because in extended mode GGeoString is represented
 * by additional pair of offset (offx,offy). Most methods are inherited from GoogleCalculator
 * but some needs to be overrride. 
 */
public class GoogleCalculatorExtended extends GoogleCalculator {

    private static GoogleCalculatorExtended instance = null;
    
    public static GoogleCalculator getInstance(){
        if(instance == null){
            instance = new GoogleCalculatorExtended();
        }
        return instance;
    }
    
    private GoogleCalculatorExtended(){
    	super();
    }
    
    /**
     * Convert pixel into extended image coordinages - one image is divided into
     * EXTENDED_BASExEXTENDED_BASE parts.
     */
    public GImage fromGPixelToGImage(GPoint pixelPoint) {
    	int pX = (int)pixelPoint.getX();
    	int pY = (int)pixelPoint.getY();
    	
    	int imageX = GMapConst.EXTENDED_BASE*(pX/BASE)+(GMapConst.EXTENDED_BASE*(pX % BASE))/BASE;
    	int imageY = GMapConst.EXTENDED_BASE*(pY/BASE)+(GMapConst.EXTENDED_BASE*(pY % BASE))/BASE;
    	
        return new GImage(imageX,imageY,0);
    }

	/**
     * use extended gGeoStrings for calculations
     * @param photoList
     * @return
     */
    public String gGeoStringListToString(GGeoString [] photoList){
        StringBuffer result = new StringBuffer("[");
        for(int i = 0;i<photoList.length;i++){
            if(i!=0){
                result.append(",");
            }
            result.append("{").append("t: '").append(photoList[i].toString());
            result.append("', offx: ").append(photoList[i].getOffsetX());
            result.append(", offy: ").append(photoList[i].getOffsetY());
            result.append("}");
        }
        result.append("]");
        return result.toString();    
    }

    /**
     * convert from extended geo string
     * @param geoString
     * @return
     */
	public GImage fromGeoStringToGImage(GGeoString geoString) {
        int x = 0,y = 0;
        int number;
        String withoutInitial = geoString.toString().substring(1);
        for(int i = 0;i<withoutInitial.length();i++){
            number = numberExtractor(withoutInitial.substring(i,i+1));
            x = (x << 1) + number / 2;
            y = (y << 1) + number % 2;
        }
        return new GImage(GMapConst.EXTENDED_BASE*x+geoString.getOffsetX()
        	,GMapConst.EXTENDED_BASE*y+geoString.getOffsetY(),
        	this.fromTxtZoomToCoordZoom(geoString.getZoom()));
    }

	
	/**
	 * Normalize image coordinates to values acceptable by standard converter
	 */
	public GGeoString fromGImageToGeoString(GImage imagePoint) {
		GImage normalCoordImage = new GImage(imagePoint.getImgX()/GMapConst.EXTENDED_BASE,
				imagePoint.getImgY()/GMapConst.EXTENDED_BASE,
				imagePoint.getZoom());
		int offsetX = imagePoint.getImgX() % GMapConst.EXTENDED_BASE;
		int offsetY = imagePoint.getImgY() % GMapConst.EXTENDED_BASE;
		
		GGeoString result = super.fromGImageToGeoString(normalCoordImage);
		result.setOffsetX(offsetX);
		result.setOffsetY(offsetY);
		return result;
	} 
    
    	
    
    
}
