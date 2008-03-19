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
 * Image color scheme element model class.
 */
public class ImageColorScheme {
    
    /**
     * Scheme attribute. 
     */
    protected String scheme;

    /**
     * Depth attribute.
     */
    protected int depth;

    /**
     * Getter for scheme. 
     * 
     * @return scheme.
     */
    public String getScheme() {
        return this.scheme;
    }

    /**
     * Setter for scheme attribute.
     * 
     * @param scheme scheme attribute.
     */
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * Getter for depth attribute. 
     * 
     * @return depth.
     */
    public int getDepth() {
        return this.depth;
    }

    /**
     * Setter for depth attribute.
     * 
     * @param depth depth attribute.
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

}
