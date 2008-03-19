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
package com.volantis.mcs.migrate.impl.framework.identification;

import com.volantis.mcs.migrate.api.framework.ContentRecogniser;
import com.volantis.mcs.migrate.api.framework.StreamMigrator;
import com.volantis.mcs.migrate.api.framework.Version;

import java.util.Iterator;

/**
 * Default implementation of {@link IdentificationFactory}.
 * <p>
 * This is implemented by simply creating the default versions of the objects
 * defined in the interface.
 */
public class DefaultIdentificationFactory implements IdentificationFactory {

    // Javadoc inherited.
    public ContentIdentifier createContentIdentifier(
            Version version, ContentRecogniser contentRecogniser) {

        return new DefaultContentIdentifier(version,
                contentRecogniser);
    }

    // Javadoc inherited.
    public Step createStep(Version input, Version output,
            StreamMigrator streamMigrator) {

        return new DefaultStep(input, output, streamMigrator);
    }

    // Javadoc inherited.
    public TypeIdentifierBuilder createTypeIdentifierBuilder() {

        return new DefaultTypeIdentifierBuilder(this);
    }

    // Javadoc inherited.
    public Match createMatch(String typeName, String versionName,
            Iterator sequence) {

        return new DefaultMatch(typeName, versionName, sequence);
    }

    // Javadoc inherited.
    public ResourceIdentifierBuilder createResourceIdentifierBuilder() {

        return new DefaultResourceIdentifierBuilder();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8036/7	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
