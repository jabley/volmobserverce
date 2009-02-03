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

package com.volantis.mcs.repository.impl.xml;

import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.xml.XMLRepositoryConfiguration;
import com.volantis.mcs.repository.xml.XMLRepositoryFactory;

public class XMLRepositoryFactoryImpl
        extends XMLRepositoryFactory {

    public XMLRepositoryConfiguration createXMLRepositoryConfiguration() {
        return new XMLRepositoryConfigurationImpl();
    }

    public LocalRepository createXMLRepository(
            XMLRepositoryConfiguration configuration)
            throws RepositoryException {

        // Instantiate a configuration if one is not provided as that makes
        // the following code easier to use.
        if (configuration == null) {
            configuration = new XMLRepositoryConfigurationImpl();
        }

        return new XMLRepositoryImpl();
    }
}
