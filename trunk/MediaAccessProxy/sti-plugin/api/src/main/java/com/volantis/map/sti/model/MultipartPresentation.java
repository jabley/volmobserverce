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
 * Multipart presentation element model class.
 */
public class MultipartPresentation {
    
    /**
     * Content type attribute.
     */
    protected String contentType;

    /**
     * Content type parameters.
     */
    protected Properties contentTypeParams;

    /**
     * Template attribute.
     */
    protected String template;

    /**
     * Layout attribute.
     */
    protected String layout;

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
     * Getter for template attribute.
     * 
     * @return template.
     */
    public String getTemplate() {
        return this.template;
    }

    /**
     * Setter for template attribute.
     * 
     * @param template
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Getter for layout attribute.
     * 
     * @return layout.
     */
    public String getLayout() {
        return this.layout;
    }

    /**
     * Setter for layout attribute.
     * 
     * @param layout
     */
    public void setLayout(String layout) {
        this.layout = layout;
    }

}
