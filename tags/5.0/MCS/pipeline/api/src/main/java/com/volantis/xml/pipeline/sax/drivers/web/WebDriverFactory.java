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

package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;

/**
 * A class for creating Web driver specific objects.
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
public abstract class WebDriverFactory {

    /**
     * A <code>WebDriverFactory</code> instance that will be used
     * as the default implementation of this abstract class.
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    protected static WebDriverFactory defaultFactory =
            new DefaultWebDriverFactory();

    /**
     * Get the default instance of this factory.
     * @return The default instance of this factory.
     */
    public static WebDriverFactory getDefaultInstance() {
        return defaultFactory;
    }

    /**
     * Return an encapsulation of all the rules for using the web driver in
     * a dynamic pipeline.
     * @return A DynamicRuleConfigurator that encapsulates all the rules for
     * the web driver.
     */
    public abstract DynamicRuleConfigurator getRuleConfigurator();

    /**
     * Create a WebDriverRequest.
     * @return A new WebDriverRequest
     */
    public abstract WebDriverRequest createRequest();

    /**
     * Create a WebDriverResponse.
     * @return A new WebDriverResponse
     */
    public abstract WebDriverResponse createResponse();

    /**
     * Create a <code>WebDriverConfiguration<code> instance
     * @return a <code>WebDriverConfiguration<code> instance
     */
    public abstract WebDriverConfiguration createConfiguration();

    /**
     * Create a <code>HTTPCacheConfiguration<code> instance
     * @return a <code>HTTPCacheConfiguration<code> instance
     */
    public abstract HTTPCacheConfiguration createHTTPCacheConfiguration();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 ===========================================================================
*/
