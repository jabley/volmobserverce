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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.net.http.HTTPMessageEntity;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * This is a HTTPMessageEntityMerger for simple HTTPMessageEntities
 * i.e. HTTPMessageEntity objects that only have name and value
 * properties like headers and request parameters.
 */
public  class SimpleHTTPMessageEntityMerger
        implements HTTPMessageEntityMerger {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    SimpleHTTPMessageEntityMerger.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    SimpleHTTPMessageEntityMerger.class);

    /**
     * The type of HTTPMessageEntity that this
     * SimpleHTTPMessageEntity merges entities into.
     */
    private Class type;

    /**
     * Construct a new SimpleHTTPMessageEntityMerger that merges
     * HTTPMessageEntity objects into a particular class of
     * HTTPMessageEntity.
     */
    public SimpleHTTPMessageEntityMerger(Class type) {
        this.type = type;
    }

    /**
     * Merge the name and value of master and alternative into a new
     * SimpleHTTPMessageEntity.
     * @param master
     * @param alternative
     * @return
     */
    public HTTPMessageEntity
            mergeHTTPMessageEntities(HTTPMessageEntity master,
                                     HTTPMessageEntity alternative)
            throws HTTPException {

        DerivableHTTPMessageEntity entity = null;
        try {
            entity = (DerivableHTTPMessageEntity)
                    type.newInstance();
            entity.setName(master.getName()); // master must have a name

            if (master.getValue() != null) {
                entity.setValue(master.getValue());
            } else {
                entity.setValue(alternative.getValue());
            }
        } catch (InstantiationException e) {
            // Calling fatalError would necessitate that this class either
            // be a singleton or have the interface change so that it
            // takes the XMLProcess or make instances of this class non-
            // static. Since this class is private it should never be
            // the case that this exception or the IllegalAccessException
            // is thrown so instead a SAXException is merely thrown and
            // someone else can catch it and log a fatal.
            logger.error("unexpected-instantiation-exception", e);
            throw new HTTPException(exceptionLocalizer.format(
                    "unexpected-instantiation-exception"), e);
        } catch (IllegalAccessException e) {
            logger.error("unexpected-illegal-access-exception", e);
            throw new HTTPException(exceptionLocalizer.format(
                    "unexpected-illegal-access-exception"), e);
        }

        return entity;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Mar-05	7443/1	matthew	VBM:2005031708 refactor AbstractPluggableHTTPManager

 ===========================================================================
*/
