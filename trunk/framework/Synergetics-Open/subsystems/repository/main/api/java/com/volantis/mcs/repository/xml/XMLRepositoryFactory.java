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

package com.volantis.mcs.repository.xml;

import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Factory for creating XML Repository related objects.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User specializations of this class are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @since 3.5.1
 */
public abstract class XMLRepositoryFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
                new MetaDefaultFactory(
                        "com.volantis.mcs.repository.impl.xml.XMLRepositoryFactoryImpl",
                        XMLRepositoryFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static XMLRepositoryFactory getDefaultInstance() {
        return (XMLRepositoryFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    protected XMLRepositoryFactory() {
    }

    /**
     * Create an object to encapsulate the XML repository configuration.
     *
     * @return A new {@link XMLRepositoryConfiguration} instance.
     */
    public abstract XMLRepositoryConfiguration createXMLRepositoryConfiguration();

    /**
     * Create a local repository.
     *
     * @param configuration The configuration, may be null.
     * @return The newly instantiated {@link LocalRepository}.
     */
    public abstract LocalRepository createXMLRepository(
            XMLRepositoryConfiguration configuration)
            throws RepositoryException;
}
