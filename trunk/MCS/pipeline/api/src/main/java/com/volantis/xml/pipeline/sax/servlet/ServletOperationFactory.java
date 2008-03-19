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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.servlet;

import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;

/**
 * A class for creating Servlet operation specific objects.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class ServletOperationFactory {

    /**
     * A <code>ServletOperationFactory</code> instance that will be used
     * as the default implementation of this abstract class.
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    protected static ServletOperationFactory defaultFactory =
            new DefaultServletOperationFactory();

    /**
     * Get the default instance of this factory.
     * @return The default instance of this factory.
     */
    public static ServletOperationFactory getDefaultInstance() {
        return defaultFactory;
    }

    /**
     * Return an encapsulation of all the rules for using the servlet specific
     * pipeline operations.
     * <p>The currently supported operations are listed below and although more
     * are likely to be added in future.</p>
     * <ul>
     * <li>includeServlet</li>
     * <li>includeJSP</li>
     * </ul>
     * @return A DynamicRuleConfigurator that encapsulates all the rules for
     * servlet specific pipeline operations.
     */
    public abstract DynamicRuleConfigurator getRuleConfigurator();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 ===========================================================================
*/
