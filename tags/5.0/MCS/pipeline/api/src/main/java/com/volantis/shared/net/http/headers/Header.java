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
package com.volantis.shared.net.http.headers;

import com.volantis.shared.net.http.HTTPMessageEntity;

/**
 * Interface representing a header that may form part of a request to or a
 * response from a server.
 *
 * <b>Warning:</b>  Serialized implementaions of this interface will not be
 * compatible with future releases. The current serialization support is only
 * appropriate for short term storage.
 * 
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface Header extends HTTPMessageEntity {
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Oct-04	5990/1	matthew	VBM:2004102621 Mark HTTPMessageEntity as Serializable and modify its javadoc

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 28-Jul-03	217/1	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 ===========================================================================
*/
