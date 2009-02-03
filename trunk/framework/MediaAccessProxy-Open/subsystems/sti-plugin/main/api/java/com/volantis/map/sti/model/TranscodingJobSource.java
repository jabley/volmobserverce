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
 * Transcoding job source element model class.
 */
public class TranscodingJobSource {
    
    /**
     * Content type attribute.
     */
    protected String contentType;

    /**
     * Content type parameters.
     */
    protected Properties contentTypeParams;

    /**
     * Location of source.
     */
    protected String location;

    /**
     * Extension data properties.
     */
    protected Properties extensionData;

    /**
     * Getter for content type attribute.
     * 
     * @return content type.
     */
    public String getContentType() {
        return this.contentType;
    }

    /**
     * Setter for content type attribute.
     * 
     * @param contentType content type attribute.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Getter for content type parameters.
     * 
     * @return content type parameters.
     */
    public Properties getContentTypeParams() {
        return this.contentTypeParams;
    }

    /**
     * Setter for content type parameters.
     * 
     * @param contentTypeParams content type parameters.
     */
    public void setContentTypeParams(Properties contentTypeParams) {
        this.contentTypeParams = contentTypeParams;
    }

    /**
     * Getter for location attribute.
     * 
     * @return location.
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Setter for location attribute.
     * 
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for extension data properties. 
     * 
     * @return extension data properties.
     */
    public Properties getExtensionData() {
        return this.extensionData;
    }

    /**
     * Setter for extension data properties.
     * 
     * @param extensionData extension data properties.
     */
    public void setExtensionData(Properties extensionData) {
        this.extensionData = extensionData;
    }

}
