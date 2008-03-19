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

package com.volantis.mcs.protocols.widgets.attributes;

/**
 * Holds properties specific to MapLocationMarkerElement
 */
public class MapLocationMarkerAttributes extends WidgetAttributes {

    private double latitude = Double.NaN;
    private double longitude = Double.NaN;
    private String src;
    private int minZoom;
    private int maxZoom;
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    } 
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setSrc(String src) {
        this.src = src;
    } 
    
    public String getSrc() {
        return src;
    }
    
    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    } 
    
    public int getMinZoom() {
        return minZoom;
    }
    
    public void setMaxZoom(int maxZoom) {
        this.maxZoom = maxZoom;
    } 
    
    public int getMaxZoom() {
        return maxZoom;
    }
}
