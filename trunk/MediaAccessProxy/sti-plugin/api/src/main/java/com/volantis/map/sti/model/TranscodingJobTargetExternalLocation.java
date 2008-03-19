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
package com.volantis.map.sti.model;

/**
 * Transcoding job target external location element model class.
 */
public class TranscodingJobTargetExternalLocation {
    
    /**
     * Path to external location.
     */
    protected String path;

    /**
     * Name attribute.
     */
    protected String name;

    /**
     * Getter for path to external location attribute.
     * 
     * @return path.
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Setter for path to external location attribute.
     * 
     * @param path.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Getter for name attribute.
     * 
     * @return name.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Setter for name attribute.
     * 
     * @param name.
     */
    public void setName(String name) {
        this.name = name;
    }

}
