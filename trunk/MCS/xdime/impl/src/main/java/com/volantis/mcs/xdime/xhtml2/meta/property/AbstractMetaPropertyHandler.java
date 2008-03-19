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
package com.volantis.mcs.xdime.xhtml2.meta.property;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xhtml2.meta.datatype.DataType;
import com.volantis.mcs.context.MetaData;
import com.volantis.synergetics.localization.MessageLocalizer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Abstract base class for property handlers to handle the (probably common)
 * checks:
 * <ul>
 * <li>the availability of the target id,</li>
 * <li>multiple definition of a property for the same element</li>
 * </ul>
 *
 * Stores the meta values in a map inside the metaDataMap. The keys of the inner
 * map are the property names and the values are the stored meta values. If the
 * property allows multiple definitions for the same element the values are
 * store in a list, otherwise the content returned from the content processor is
 * stored.
 *
 * Assumes that the only acceptable type is the default type.
 */
public abstract class AbstractMetaPropertyHandler implements MetaPropertyHandler {

    /**
     * Used to obtain localized messages
     */
    private static final MessageLocalizer LOCALIZER =
        LocalizationFactory.createMessageLocalizer(
            AbstractMetaPropertyHandler.class);

    /**
     * Returns true if the property can be applied to individual elements.
     *
     * @return true if the property can have element scope
     */
    protected boolean hasElementScope() {
        return false;
    }

    /**
     * Returns true if the property can be applied to the whole page.
     *
     * @return true if the property can have page scope
     */
    protected boolean hasPageScope() {
        return false;
    }

    /**
     * Returns true if multiple definition of the property is allowed for the
     * same element or document.
     *
     * @return true if multiple values allowed for the same element
     */
    protected boolean multipleInstancesAllowed() {
        return false;
    }

    /**
     * To perform other checks on the content. Implementations should throw
     * XDIMEException if one of the additional conditions failed.
     *
     * @param content the content to check
     * @param context the xdime context
     * @throws XDIMEException when the content is invalid
     */
    protected abstract void checkContent(
            final Object content, final XDIMEContextInternal context)
        throws XDIMEException;

    /**
     * Checks if the content has the expected type.
     *
     * @param content the content to check
     * @param expected the expected class
     * @throws XDIMEException if the content is null or doesn't have the right
     * type
     */
    protected void checkContentType(final Object content, final Class expected)
            throws XDIMEException {

        if (content == null || !expected.isAssignableFrom(content.getClass())) {
            throw new XDIMEException(LOCALIZER.format(
                "invalid-meta-content-type", new Object[] {
                    expected.getName(), content.getClass().getName()}));
        }
    }

    // javadoc inherited
    public boolean isAcceptableType(final DataType type) {

        return getDefaultDataType().equals(type);
    }

    // javadoc inherited
    public void process(final Object content, final XDIMEContextInternal context,
                        final String id, final String propertyName)
            throws XDIMEException {

        // check the id
        if (id == null) {
            if (!hasPageScope()) {
                throw new XDIMEException(LOCALIZER.format(
                    "invalid-meta-property-scope-page", propertyName));
            }
        } else if (!hasElementScope()) {
            throw new XDIMEException(LOCALIZER.format(
                "invalid-meta-property-scope-element", propertyName));
        }

        checkContent(content, context);

        // store the content
        final MetaData metaData;
        if (id == null) {
            metaData = context.getPageMetaData();
        } else {
            metaData = context.getElementMetaData(id);
        }
        if (multipleInstancesAllowed()) {
            metaData.addPropertyValue(propertyName, content);
        } else {
            if (metaData.setPropertyValue(propertyName, content) != null) {
                throw new XDIMEException(LOCALIZER.format(
                    "meta-property-occurrence-error", propertyName));
            }
        }
    }
}
