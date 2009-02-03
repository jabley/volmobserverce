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

import com.volantis.shared.net.http.parameters.RequestParameterImpl;
import com.volantis.shared.net.http.parameters.RequestParameter;
import com.volantis.shared.net.http.HTTPMessageEntityIdentity;
import com.volantis.shared.net.http.SimpleHTTPMessageEntityIdentity;

public class WebRequestParameter extends RequestParameterImpl
        implements DerivableHTTPMessageEntity {
    /**
     * The [derivable] from property.
     */
    private String from;
    
    /**
     * The target property of this WebRequestParameter.
     */
    private String target;

    /**
     * Set the name of this WebRequestParameter.
     * @param name The name.
     */
    public void setName(String name) {
        super.setName(name);
    }

    /**
     * Get the from property.
     * @return The from.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Set the from property
     * @param from The from.
     */
    public void setFrom(String from) {
        this.from = from;
    }
    
    /**
     * Get the target property.
     * @return The target
     */
    public String getTarget() {
        return target;
    }

    /**
     * Set the target property
     * @param target The target
     */
    public void setTarget(String target) {
        this.target = target;
    }

    // javadoc inherited
    public HTTPMessageEntityIdentity getFromIdentity() {
        return new SimpleHTTPMessageEntityIdentity(getFrom(),
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

 31-Jul-03	217/6	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 28-Jul-03	217/4	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 24-Jul-03	217/1	allan	VBM:2003071702 WebDriver implementation. Intermediate commit

 ===========================================================================
*/
