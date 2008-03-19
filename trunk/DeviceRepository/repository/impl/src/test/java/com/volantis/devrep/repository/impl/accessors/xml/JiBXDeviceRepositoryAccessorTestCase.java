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
package com.volantis.devrep.repository.impl.accessors.xml;

import com.volantis.devrep.repository.impl.accessors.AbstractDeviceRepositoryAccessor;
import com.volantis.devrep.repository.impl.DeviceRepositoryLocationImpl;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.xml.XMLRepositoryConfiguration;
import com.volantis.mcs.repository.xml.XMLRepositoryFactory;

import java.io.File;
import java.lang.reflect.UndeclaredThrowableException;

public class JiBXDeviceRepositoryAccessorTestCase
        extends XMLDeviceRepositoryAccessorTestAbstract {

    private static final XMLRepositoryFactory REPOSITORY_FACTORY =
        XMLRepositoryFactory.getDefaultInstance();

    /**
     * Factory method used to create the accessor under test. The only
     * requirement is that the accessor returned must use the specified
     * file as its device repository source.
     *
     * @return the accessor to be tested
     */
    protected AbstractDeviceRepositoryAccessor createAccessor(
            final File file) {
        final XMLRepositoryConfiguration configuration =
            REPOSITORY_FACTORY.createXMLRepositoryConfiguration();
        try {
            final LocalRepository xmlRepository =
                REPOSITORY_FACTORY.createXMLRepository(configuration);
            return new JiBXDeviceRepositoryAccessor(xmlRepository,
                    new DeviceRepositoryLocationImpl(file.getPath()));
        } catch (RepositoryException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Nov-05	10404/1	geoff	VBM:2005112301 Implement meta data for JiBX device repository accessor

 13-Nov-05	9896/3	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 ===========================================================================
*/
