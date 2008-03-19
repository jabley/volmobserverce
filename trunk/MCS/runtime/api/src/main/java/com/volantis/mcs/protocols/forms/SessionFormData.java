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
package com.volantis.mcs.protocols.forms;

import com.volantis.mcs.utilities.MarinerURL;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Encapsulates the information about a form that should be stored in the
 * session.
 * 
 * @mock.generate
 */
public class SessionFormData {

    /**
     * The {@link MarinerURL} representation of the fragmented form.
     * This member is lazily initialised when any of the associated
     * setters are called.  A null url value indicates that
     * a form has not yet been stored in the session.
     */
    private MarinerURL url;

    /**
     * String identifier which uniquely identifies this form in the session.
     */
    private final String formSpecifier;

    /**
     * The {@link FormDescriptor} representation of the entire form structure
     * - used on form submission to determine which of the query parameters
     * from {@link #url} should be propagated onto the output.
     */
    private final FormDescriptor formDescriptor;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param formSpecifier     uniquely identifies the form in the session
     * @param formDescriptor    describes the form structure
     */
    public SessionFormData(String formSpecifier,
                           FormDescriptor formDescriptor) {
        // Note that the mariner URL will be initilialised when
        // an associated setter method is called.
        this(null, formSpecifier, formDescriptor);
    }

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param url               url which represents the data stored in the
     *                          session for this form
     * @param formSpecifier     uniquely identifies the form in the session
     * @param formDescriptor    describes the form structure
     */
    public SessionFormData(MarinerURL url, String formSpecifier,
                           FormDescriptor formDescriptor) {
        this.url = url;
        this.formSpecifier = formSpecifier;
        this.formDescriptor = formDescriptor;
    }

    /**
     * Retrieve this fragmented form data as a {@link MarinerURL} where the
     * form fields appear as request parameters and their values.
     *
     * @return MarinerURL which represents the current state of this fragmented
     * form data or null if form data has not yet been stored in
     * the session.
     */
    public MarinerURL getAsMarinerURL() {
        return url;
    }

    /**
     * Get the names of all the fields in this form.
     *
     * @return Enumeration of all the field names in the form or null if this
     * form has no fields in this session
     */
    public Enumeration getFormFields() {
        Enumeration enumeration = null;
        if (url != null) {
            enumeration = url.getParameterNames();
        }
        return enumeration;
    }

    /**
     * Get the value of an field in this fragmented form.
     *
     * @param name      of the form field whose value is required
     * @return The value of the field or null if it does not exist.
     */
    public String getFieldValue(String name) {
        String fieldValue = null;
        if (url != null) {
            fieldValue = url.getParameterValue(name);
        }
        return fieldValue;
    }

    /**
     * Get the values of a multi valued form field.
     *
     * @param name      of the form field whose value is required
     * @return String[] The values of the field or null if it does not exist.
     */
    public String[] getFieldValues(String name) {
        String[] fieldValues = null;

        if (url != null) {
            fieldValues = url.getParameterValues(name);
        }
        return fieldValues;
    }

    /**
     * Remove a field value from the fragmented form.
     *
     * @param name      of the form field whose value to remove
     */
    public void removeFieldValue(String name) {

        if (url != null) {
            url.removeParameter(name);
        }

    }

    /**
     * Set the value of an attribute within a group. If the attribute does
     * not exist, it is created and added to the group. If the attribute
     * already exists, the value of the attribute is changed.
     *
     * @param name          of the attribute
     * @param value         of the attribute
     */
    public void setFieldValue(String name, String value) {
        initialiseFormURL();
        url.setParameterValue(name, value);
    }

    /**
     * Set the value of an attribute within a group. If the attribute does
     * not exist, it is created and added to the group. If the attribute
     * already exists, the value of the attribute is changed. This method
     * is needed so multi-valued form fields can be stored.
     *
     * @param name          of the attribute
     * @param value         an array of values for this attribute
     */
    public void setFieldValues(String name, String[] value) {
        initialiseFormURL();
        url.setParameterValues(name, value);
    }

    /**
     * Return the form specifier that is used to identify this form data in the
     * session.
     *
     * @return String form specifier which is used to identify this form data
     * in the session
     */
    public String getFormSpecifier() {
        return formSpecifier;
    }

    /**
     * Return the form descriptor that defines the structure of this form.
     *
     * @return FormDescriptor that defines the structure of this form
     */
    public FormDescriptor getFormDescriptor() {
        return formDescriptor;
    }

    /**
     * Add the new field descriptors to the form descriptor stored in the
     * session. Any fields that have already been added will not be duplicated,
     * but the new value will replace the existing one.
     *
     * @param fieldDescriptors      list of field descriptors to add to the
     *                              form descriptor stored in the session
     */
    public void addFieldDescriptors(List fieldDescriptors) {

        List oldFieldDescriptors = formDescriptor.getFields();
        int fieldCount = oldFieldDescriptors.size();

        for(Iterator i = fieldDescriptors.iterator(); i.hasNext(); ) {
            boolean found = false;
            FieldDescriptor fieldDescriptor = (FieldDescriptor) i.next();
            
            // if the field is already there, then replace it
            for (int j = 0; j < fieldCount && !found; j++) {
                if (fieldDescriptor.equals(oldFieldDescriptors.get(j))) {
                    oldFieldDescriptors.set(j, fieldDescriptor);
                    found = true;
                }
            }
            // if not, then add it.
            if (!found) {
                oldFieldDescriptors.add(fieldDescriptor);
            }
        }
    }

    /**
     * Sets {@link #url} to an 'empty' MarinerURL if null.
     */
    private void initialiseFormURL() {
        if (url == null) {
            url = new MarinerURL();
        }
    }
}
