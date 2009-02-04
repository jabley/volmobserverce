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
package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.mcs.xdime.DataHandlingStrategy;
import com.volantis.mcs.xdime.IgnoreDataStrategy;
import com.volantis.mcs.xdime.StylableXDIMEElement;
import com.volantis.mcs.xdime.StylingStrategy;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xml.schema.model.ElementType;

/**
 * Abstract superclass for all XForms element classes.
 */
public class XFormsElement extends StylableXDIMEElement {

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param strategy              determines how the element should be styled
     * @param type                  type of the element
     * @param dataHandlingStrategy  determines how any data (character data or
     *                              markup) encountered while processing this
     * @param context
     */
    protected XFormsElement(
            StylingStrategy strategy,
            ElementType type,
            DataHandlingStrategy dataHandlingStrategy,
            XDIMEContextInternal context) {

        super(type, strategy,
                dataHandlingStrategy, context);

        final EnvironmentContext environmentContext =
            ContextInternals.getEnvironmentContext(
                context.getInitialRequestContext());
        final ResponseCachingDirectives cachingDirectives =
            environmentContext.getCachingDirectives();
        if (cachingDirectives != null) {
            cachingDirectives.disable();
        }
    }

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param strategy      determines if and how the element should be styled
     * @param type          type of the element
     * @param context
     */
    protected XFormsElement(
            StylingStrategy strategy, ElementType type,
            XDIMEContextInternal context) {
        this (strategy, type, IgnoreDataStrategy.getDefaultInstance(), context);
    }

    // Javadoc inherited.
    protected String getNamespace() {
        return XDIMESchemata.XFORMS_NAMESPACE;
    }
}
