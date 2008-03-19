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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.migrate.impl.framework;

import com.volantis.mcs.migrate.api.framework.ResourceMigrator;
import com.volantis.mcs.migrate.impl.framework.identification.ResourceIdentifier;
import com.volantis.mcs.migrate.impl.framework.io.StreamBufferFactory;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;

/**
 * Default implementation of {@link CoreFactory}.
 */
public class DefaultCoreFactory implements CoreFactory {

    // Javadoc inherited.
    public ResourceMigrator createResourceMigrator(StreamBufferFactory factory,
            ResourceIdentifier recogniser, NotificationReporter reporter) {

        return new DefaultResourceMigrator(factory, recogniser, reporter);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-May-05	8036/10	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/7	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
