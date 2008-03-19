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
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.Version;

import java.io.InputStream;

/**
 * Default implementation of {@link ContentIdentifier}.
 * <p>
 * This is implemented by delegating the actual recognition to a
 * {@link ContentRecogniser}.
 */
public class DefaultContentIdentifier
        implements ContentIdentifier {

    /**
     * The version that this object identifies.
     */
    private Version version;

    /**
     * Object which does the actual recognition.
     */
    private ContentRecogniser contentRecogniser;

    /**
     * Initialise.
     *
     * @param version the version that this object identifies.
     * @param contentRecogniser object which does the actual recognition.
     */
    public DefaultContentIdentifier(Version version,
            ContentRecogniser contentRecogniser) {

        this.version = version;
        this.contentRecogniser = contentRecogniser;
    }

    // Javadoc inherited.
    public Version getVersion() {

        return version;
    }

    // Javadoc inherited.
    public boolean identifyContent(InputStream input)
            throws ResourceMigrationException {

        return contentRecogniser.recogniseContent(input);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
