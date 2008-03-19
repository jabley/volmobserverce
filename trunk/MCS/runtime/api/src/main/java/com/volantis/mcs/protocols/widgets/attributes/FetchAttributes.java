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

package com.volantis.mcs.protocols.widgets.attributes;

/**
 * Holds properties specific to FetchElement.
 */
public class FetchAttributes extends WidgetAttributes {
    
    private String src;    
    private String when;
    private String transformation;
    private String service;
    private String transformCache;
    private String transformCompile; 
    
    public String getWhen() {
        return when;
    }
    
    public void setWhen(String when) {
        this.when = when;
    }
    
    public String getSrc() {
        return src;
    }
    
    public void setSrc(String src) {
        this.src = src;
    }

    /**
     * @return Returns the transformation.
     */
    public String getTransformation() {
        return transformation;
    }

    /**
     * @param transformation The transformation to set.
     */
    public void setTransformation(String transformation) {
        this.transformation = transformation;
    }

    /**
     * @return Returns the transformCache.
     */
    public String getTransformCache() {
        return transformCache;
    }

    /**
     * @param transformCache The transformCache to set.
     */
    public void setTransformCache(String transformCache) {
        this.transformCache = transformCache;
    }

    /**
     * @return Returns the transformCompile.
     */
    public String getTransformCompile() {
        return transformCompile;
    }

    /**
     * @param transformCompile The transformCompile to set.
     */
    public void setTransformCompile(String transformCompile) {
        this.transformCompile = transformCompile;
    }

    /**
     * @return Returns the service.
     */
    public String getService() {
        return service;
    }

    /**
     * @param service The service to set.
     */
    public void setService(String service) {
        this.service = service;
    }    
}
