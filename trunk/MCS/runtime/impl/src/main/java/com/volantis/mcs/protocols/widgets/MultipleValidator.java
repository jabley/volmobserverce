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

package com.volantis.mcs.protocols.widgets;

import java.util.ArrayList;
import java.util.Iterator;

import com.volantis.mcs.protocols.widgets.attributes.FieldAttributes;
import com.volantis.mcs.protocols.widgets.attributes.MultipleValidatorAttributes;

/**
 * Multiple validator.
 */
public class MultipleValidator {
    /**
     * Attributes.
     */
    private MultipleValidatorAttributes attributes;

    /**
     * URL to the validation page.
     */
    private String sourceURL;

    /**
     * List of validated fields.
     */
    private ArrayList fields;

    /**
     * ID of the message area element, to display validation messages.
     */
    private String messageArea;

    /**
     * ID of the popup widget, to display validation messages.
     */
    private String messagePopup;

    /**
     * Creates new instance, with the specified ID.
     * 
     * @param attributes The ID of the corresponding MultipleValidatorElement.
     */
    public MultipleValidator(MultipleValidatorAttributes attributes) {
        this.attributes = attributes;
        this.sourceURL = null;
        this.fields = new ArrayList(1);
        this.messageArea = null;
    }

    /**
     * Sets URL of the validator.
     * 
     * @param sourceURL The URL of the validator.
     */
    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    /**
     * Adds a field to validate.
     * 
     * @param attributes The attributes of the field.
     */
    public void addField(FieldAttributes attributes) {
        fields.add(attributes);
    }

    /**
     * Sets ID of the element, which acts as a container for validation
     * messages.
     * 
     * @param id The ID of the message area element
     */
    public void setMessageArea(String id) {
        this.messageArea = id;
    }

    /**
     * Sets ID of the popup widget, which will be used to display validation
     * messages.
     * 
     * @param id The ID of the popup widget
     */
    public void setMessagePopup(String id) {
        this.messagePopup = id;
    }

    /**
     * Returns URL of the validator, or null of it's unspecified.
     * 
     * @return The URL of the validator.
     */
    public String getSourceURL() {
        return this.sourceURL;
    }

    /**
     * Returns iterator over added field attributes.
     * 
     * @return The iterator over added field attributes.
     */
    public Iterator getFieldsIterator() {
        return this.fields.iterator();
    }

    /**
     * Returns the ID of the element, which acts as a placeholder for validation
     * messages, or null if it's unspecified.
     * 
     * @return The ID of the message area element.
     */
    public String getMessageArea() {
        return this.messageArea;
    }

    /**
     * Returns the ID of the popup widget, which will be used to show validation
     * messages, or null if it's unspecified.
     * 
     * @return The ID of the popup widget.
     */
    public String getMessagePopup() {
        return this.messagePopup;
    }

    /**
     * Returns attributes of this multiple validator.
     * 
     * @return The attributes of this multiple validator.
     */
    public MultipleValidatorAttributes getAttributes() {
        return this.attributes;
    }
}
