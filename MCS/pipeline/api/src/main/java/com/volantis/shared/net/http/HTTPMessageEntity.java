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
package com.volantis.shared.net.http;

import java.io.Serializable;

/**
 * A HTTPMessageEntity is something that can form part of some protocol
 * message. An example of a protocol message would be an HTTP message such as
 * a request or a response. Examples of HTTPMessageEntity objects include
 * Cookie, Header and WebRequestParameter.
 *
 * <b>Warning:</b>  Serialized implementaions of this interface will not be
 * compatible with future releases. The current serialization support is only
 * appropriate for short term storage.
 * 
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface HTTPMessageEntity extends Serializable {
    /**
     * Get the name of this HTTPMessageEntity.
     * @return The name.
     */
    public String getName();

    /**
     * Get the value of this HTTPMessageEntity.
     * @return The value.
     */
    public String getValue();

    /**
     * Set the value of this HTTPMessageEntity.
     * @param value The value.
     */
    public void setValue(String value);

    /**
     * Get the identity of this HTTPMessageEntity.
     * @return The identity of this HTTPMessageEntity.
     */
    public HTTPMessageEntityIdentity getIdentity();

    /**
     * Override Object.equals(). Two HTTPMessageEntity objects are equal if
     * ALL their properties are equal. Use the identity to look for equal
     * identities.
     */
    // rest of javadoc inherited
    public boolean equals(Object o);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Oct-04	5990/1	matthew	VBM:2004102621 Mark HTTPMessageEntity as Serializable and modify its javadoc

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Aug-03	217/10	allan	VBM:2003071702 Rename and re-write HttpCookie

 31-Jul-03	217/8	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 28-Jul-03	217/6	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 24-Jul-03	217/1	allan	VBM:2003071702 WebDriver implementation. Intermediate commit

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 ===========================================================================
*/
