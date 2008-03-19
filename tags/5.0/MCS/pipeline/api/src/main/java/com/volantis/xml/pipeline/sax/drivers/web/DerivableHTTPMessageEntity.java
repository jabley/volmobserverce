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

import com.volantis.shared.net.http.HTTPMessageEntity;
import com.volantis.shared.net.http.HTTPMessageEntityIdentity;

/**
 * A HTTPMessageEntity that does not have to have a name upon creation.
 * Instead the name may be derived from a "from" property.
 * 
 * <b>Warning:</b>  Serialized implementaions of this interface will not be
 * compatible with future releases. The current serialization support is only
 * appropriate for short term storage.
 */
public interface DerivableHTTPMessageEntity extends HTTPMessageEntity {

    /**
     * Set the name of this DerivableHTTPMessageEntity
     * @param name The name.
     */
    public void setName(String name);

    /**
     * Get the from property from which to derive the name and possibly
     * other properties.
     * @return The from property.
     */
    public String getFrom();

    /**
     * Set the from property.
     * @param from The from.
     */
    public void setFrom(String from);

    /**
     * Get an identity for this DerivableHTTPMessageEntity based on the
     * <code>from</code> property rather than the <code>name</code>
     * property.
     */
    public HTTPMessageEntityIdentity getFromIdentity();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Oct-04	5990/1	matthew	VBM:2004102621 Mark HTTPMessageEntity as Serializable and modify its javadoc

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	217/7	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 28-Jul-03	217/5	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 24-Jul-03	217/1	allan	VBM:2003071702 WebDriver implementation. Intermediate commit

 ===========================================================================
*/
