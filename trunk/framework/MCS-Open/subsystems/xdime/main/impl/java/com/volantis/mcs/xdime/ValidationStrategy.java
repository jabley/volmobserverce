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
package com.volantis.mcs.xdime;

import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.validation.DocumentValidator;
import com.volantis.mcs.xml.schema.validation.ValidationException;

/**
 * A strategy for element validation.
 */
public abstract class ValidationStrategy {

    /**
     * Validate the opening of an element.
     *
     * @param context
     * @param elementType
     * @throws XDIMEException
     */
    public abstract void open(
            XDIMEContextInternal context, ElementType elementType)
            throws XDIMEException;

    /**
     * Validate the closing of an element.
     *
     * @param context
     * @param elementType
     * @throws XDIMEException
     */
    public abstract void close(
            XDIMEContextInternal context, ElementType elementType)
            throws XDIMEException;

    /**
     * A validation strategy for elements which can appear anywhere.
     * <p>
     * In this case it is easier to just no validate than it is to try and add
     * the element to every part of the schema.
     */
    public static final ValidationStrategy ANYWHERE = new ValidationStrategy() {

        // Javadoc inherited
        public void open(XDIMEContextInternal context, ElementType elementType)
                throws XDIMEException {
        }

        // Javadoc inherited
        public void close(XDIMEContextInternal context, ElementType elementType)
                throws XDIMEException {
        }
    };

    /**
     * A validation strategy which validates the element as normal.
     * <p>
     * All elements apart from elements which can appear anywhere should use this
     * strategy.
     */
    public static final ValidationStrategy VALIDATE = new ValidationStrategy() {


        // Javadoc inherited
        public void open(XDIMEContextInternal context, ElementType elementType)
                throws XDIMEException {

            // Get the document validator.
            DocumentValidator validator = context.getDocumentValidator();
            try {
                validator.open(elementType);
            } catch (ValidationException e) {
                throw new XDIMEException(e);
            }
        }

        // Javadoc inherited
        public void close(XDIMEContextInternal context, ElementType elementType)
                throws XDIMEException {

            DocumentValidator validator = context.getDocumentValidator();
            try {
                validator.close(elementType);
            } catch (ValidationException e) {
                throw new XDIMEException(e);
            }
        }
    };
}
