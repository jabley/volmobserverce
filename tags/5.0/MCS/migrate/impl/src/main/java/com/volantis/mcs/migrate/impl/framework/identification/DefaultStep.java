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

import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.StreamMigrator;
import com.volantis.mcs.migrate.api.framework.Version;
import com.volantis.mcs.migrate.api.framework.StepType;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Default implementation of {@link Step}.
 * <p>
 * This is implemented to delegate to a {@link StreamMigrator} to do the
 * actual migration.
 */
public class DefaultStep implements Step {

    /**
     * The version that this step accepts as input.
     */
    protected Version input;

    /**
     * The version that this step generates as output.
     */
    protected Version output;

    /**
     * The object that does the actual migration of content.
     */
    protected StreamMigrator streamMigrator;

    /**
     * Initialise.
     *
     * @param inputVersion the version accepted as input.
     * @param outputVersion the version generated as output.
     * @param streamMigrator does the actual migration of content.
     */
    public DefaultStep(Version inputVersion, Version outputVersion,
            StreamMigrator streamMigrator) {

        this.input = inputVersion;
        this.output = outputVersion;
        this.streamMigrator = streamMigrator;
    }

    // Javadoc inherited
    public Version getInput() {

        return input;
    }

    // Javadoc inherited
    public Version getOutput() {

        return output;
    }

    // Javadoc inherited.
    public void migrate(InputStream input, OutputStream output,
        StepType typeOfValidation)
            throws ResourceMigrationException {

        streamMigrator.migrate(input,  output, typeOfValidation);
    }

    public String toString() {
        return "Step: " + input + ":" + output + ":" + streamMigrator;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-May-05	8036/10	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/8	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/6	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
