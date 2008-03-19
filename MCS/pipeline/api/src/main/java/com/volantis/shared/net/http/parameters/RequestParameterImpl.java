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
package com.volantis.shared.net.http.parameters;

import com.volantis.shared.net.http.HTTPMessageEntityImpl;
import com.volantis.shared.net.http.HTTPMessageEntityIdentity;
import com.volantis.shared.net.http.SimpleHTTPMessageEntityIdentity;

/**
 * An implementation of WebRequestParameter.
 */
public class RequestParameterImpl extends HTTPMessageEntityImpl
        implements RequestParameter {
    /**
     * A constructor that relies on specializations to call setName().
     */
    protected RequestParameterImpl() {
    }

    /**
     * Create a new RequestParameterImpl with the specified name and value.
     * @param name The request parameter name.
     */
    public RequestParameterImpl(String name) {
        super(name);
    }

    // javadoc inherited
    public HTTPMessageEntityIdentity getIdentity() {
        return new SimpleHTTPMessageEntityIdentity(getName(),
                                                   RequestParameter.class);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	217/5	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 28-Jul-03	217/3	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 24-Jul-03	217/1	allan	VBM:2003071702 WebDriver implementation. Intermediate commit

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 ===========================================================================
*/
