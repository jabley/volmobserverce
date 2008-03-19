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

import com.volantis.shared.net.http.HTTPMessageEntities;

/**
 * This interface provides access to the request within which the web driver
 * is currently running.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and source
 * levels.</strong></p>
 *
 * <p>It is used by the web driver to retrieve information that it needs to
 * modify and pass on to the server it is retrieving the resources from.</p>
 * <p>Instances of this class (and all objects referenced from it) will be
 * marked as immutable before it is added into the pipeline context.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface WebDriverRequest {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Set the cookies that contains the cookies that the web driver
     * can pass on to the server.
     * @param cookies The cookies that the web driver should use.
     */
    public void setCookies(HTTPMessageEntities cookies);

    /**
     * Get the cookies jar that contains the cookies that the web driver
     * can pass on to the server.
     * @return The CookieJar that the web driver should use.
     */
    public HTTPMessageEntities getCookies();

    /**
     * Set the headers that the web driver can pass on to the server.
     * @param headers The headers that the web driver should use.
     */
    public void setHeaders(HTTPMessageEntities headers);

    /**
     * Get the headers that the web driver can pass onto the server.
     * @return The Headers associated with this object.
     */
    public HTTPMessageEntities getHeaders();

    /**
     * Get the request parameters that the web driver can pass onto the server.
     * @param parameters The request parameters that the web driver can use.
     */
    public void setRequestParameters(HTTPMessageEntities parameters);

    /**
     * Get the request parameters that the web driver can pass onto the server.
     * @return The request parameters that the web driver can use.
     */
    public HTTPMessageEntities getRequestParameters();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	217/9	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/7	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 31-Jul-03	217/5	allan	VBM:2003071702 Ensure correct array types created. Add our-commons-logging to build

 28-Jul-03	217/3	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 17-Jul-03	215/3	steve	VBM:2003070806 Removed throws ImmutableObjectException declarations

 17-Jul-03	215/1	steve	VBM:2003070806 Implement we driver

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
