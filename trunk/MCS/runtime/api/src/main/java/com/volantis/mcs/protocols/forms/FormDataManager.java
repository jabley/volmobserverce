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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.IndexedObjectCache;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Responsible for managing form data within a session.
 *
 * @mock.generate
 */
public class FormDataManager {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(FormDataManager.class);

    /**
     * Map of form specifiers against {@link SessionFormData}.
     */
    private Map forms;

    /**
     * Cache of form descriptor objects.
     */
    private IndexedObjectCache formDescriptorCache;

    /**
     * Get the form specifiers which identify all of the forms in the session.
     *
     * @return Iterator to iterate through the form specifier
     */
    public Iterator getFormSpecifiers() {
        return getForms().keySet().iterator();
    }

    /**
     * Retrieve the {@link SessionFormData} which encapsulates the state of
     * the fragmented form which is identified by the given form specifier.
     * If no form data exists for the given specifier in this session, an empty
     * set of form data is returned (to avoid null checks all over the place).
     *
     * @param formSpecifier     identifier of the form whose data to return
     * @return SessionFormData which encapsulates the state of
     * the fragmented form which is identified by the given form specifier
     */
    public SessionFormData getSessionFormData(String formSpecifier) {

        final Map forms = getForms();
        SessionFormData formData = (SessionFormData) forms.get(formSpecifier);

        // If the form data by this name has not yet been requested, then
        // create an empty one and add it to the list.
        if (formData == null) {
            // Make sure that the specifier is valid. It should have been
            // created by this class, and should therefore be "s" + the index
            // of a form descriptor in the form descriptor cache.
            FormDescriptor formDescriptor = getFormDescriptor(formSpecifier);
            if (formDescriptor == null) {
                throw new MissingFormDataException("The form specifier, " +
                        formSpecifier + ", is not valid");
            }
            formData = new SessionFormData(formSpecifier, formDescriptor);
            forms.put(formSpecifier, formData);
        }
        return formData;
    }

    /**
     * Retrieve the {@link SessionFormData} which encapsulates the state of
     * the fragmented form which is identified by the given form descriptor. If
     * no form data exists for the given specifier in this session, an empty
     * set of form data is returned (to avoid null checks all over the place).
     *
     * @param form  descriptor which identifies the form whose data to return
     * @return SessionFormData which encapsulates the state of the
     * fragmented form which is identified by the given form specifier
     */
    public SessionFormData getSessionFormData(FormDescriptor form) {
        String formSpecifier = getFormSpecifier(form);
        return getSessionFormData(formSpecifier);
    }


    /**
     * Retrieve the {@link SessionFormData} which encapsulates the state of
     * the fragmented form which is identified by the given form name. If no
     * form data exists for the given name in this session, null is returned.
     * This is less efficient than using either of the other two session form
     * data accessors.
     *
     * @param formName     name of the form whose data to return
     * @return SessionFormData which encapsulates the state of the fragmented
     * form which is identified by the given form name. May return null.
     */
    public SessionFormData getSessionFormDataByName(String formName) {
        final Map forms = getForms();
        SessionFormData requestedformData = null;
        Iterator i = forms.values().iterator();
        while (i.hasNext() && requestedformData == null) {
            SessionFormData formData = (SessionFormData) i.next();
            if (formData.getFormDescriptor().getName().equals(formName)) {
                requestedformData = formData;
            }
        }
        return requestedformData;
    }

    /**
     * Get the String specifier which uniquely identifies the form described by
     * the supplied {@link FormDescriptor} in this session context. Will add
     * the form descriptor to the cache and generate a new form specifier if
     * it is not present.
     *
     * @param form  for which to retrieve the form specifier
     * @return String form specifier which uniquely identifies the form
     * described by the supplied form descriptor in this session context
     */
    public String getFormSpecifier(FormDescriptor form) {
        // Get the index of the form descriptor within the session.
        int index = getFormDescriptorCache().getIndex(form);

        // Make the string to use to identify the form.
        String formSpecifier = "s" + index;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Form specifier for " + form + " is " + formSpecifier);
        }

        return formSpecifier;
    }

    /**
     * Process the specified string and return the canonical
     * <code>FormDescriptor</code> object based on the specified state and
     * possible changes to it. Creates a new form descriptor (and update the
     * cache) if none exists in the cache for the given specifier. The returned
     * object will have had its cache index set. May return null if the
     * specifier is invalid.
     *
     * @param specifier     The String which identifies a
     *                      <code>FormDescriptor</code> in the cache.
     * @return Retrieve the form descriptor which has been cached for the given
     * specifier, or create a new <code>FormDescriptor</code> and add it to the
     * cache if it is not already present. The returned object will have had
     * its cache index set. May return null if the specifier is invalid.
     */
    private FormDescriptor getFormDescriptor(String specifier) {
        // The specifier is of the following format.
        // s<state index>
        int stateIndex = -1;

        String value;

        // If the first character is a 's' then the text between it and the
        // first . is the current state index.
        if (specifier.charAt(0) == 's') {
            value = specifier.substring(1);

            try {
                stateIndex = Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                LOGGER.warn("state-index-info", new Object[]{value},
                        nfe);
                return null;
            }
        } else {
            // Otherwise the string is invalid.
            LOGGER.warn("unknown-type", new Object[]{specifier});
            return null;
        }

        return (FormDescriptor)getFormDescriptorCache().getObject(stateIndex);
    }

    protected Map getForms() {
        synchronized (this) {
            if (forms == null) {
                forms = new HashMap();
            }

            return forms;
        }
    }

    private IndexedObjectCache getFormDescriptorCache() {
        synchronized (this) {
            if (formDescriptorCache == null) {
                formDescriptorCache = new IndexedObjectCache();
            }

            return formDescriptorCache;
        }
    }
}
