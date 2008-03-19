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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.descriptorstore;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;

/**
 * Localized exception used to indicate a problem in the Configuration Store
 */
public class ResourceDescriptorStoreException extends Exception {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
            ResourceDescriptorStoreException.class);

    /**
     * Takes a Key and a single substitution parameter
     *
     * @param key the message key for localization
     * @param obj a single substitution parameter
     */
    public ResourceDescriptorStoreException(String key, Object obj) {
        super(EXCEPTION_LOCALIZER.format(key, obj));
    }

    /**
     * Takes a Key and a array of substitution parameters
     *
     * @param key the message key for localization
     * @param obj an array of substitution parameters
     */
    public ResourceDescriptorStoreException(String key, Object[] obj) {
        super(EXCEPTION_LOCALIZER.format(key, obj));
    }

}
