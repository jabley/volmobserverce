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
 * Model class for Adaptation class node.
 */
public class AdaptationClass {
    
    /**
     * Class name attribute.
     */
    protected String className;

    /**
     * Informing if adaptation class is allowed.
     */
    protected boolean allowed;

    /**
     * Class ref attribute.
     */
    protected String classRef;


    /**
     * Getter for class name attribute.
     * 
     * @return class name attribute.
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * Setter for class name attribute.
     * 
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Gets allowed attribute.
     * 
     * @return allowed attribute.
     */
    public boolean getAllowed() {
        return this.allowed;
    }

    /**
     * Setter for allowed attribute.
     * 
     * @param allowed information if class is allowed.
     */
    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    /**
     * Getter for class ref attribute.
     * 
     * @return class ref attribute.
     */
    public String getClassRef() {
        return this.classRef;
    }

    /**
     * Setter for class ref attribute.
     * 
     * @param classRef class ref.
     */
    public void setClassRef(String classRef) {
        this.classRef = classRef;
    }

}
