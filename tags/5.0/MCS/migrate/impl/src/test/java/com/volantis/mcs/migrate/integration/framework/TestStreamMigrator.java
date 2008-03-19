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
package com.volantis.mcs.migrate.integration.framework;

import com.volantis.mcs.migrate.api.framework.StreamMigrator;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.StepType;
import com.volantis.mcs.migrate.impl.framework.io.InputStreamByteArray;
import com.volantis.mcs.migrate.impl.framework.io.OutputStreamByteArray;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import junit.framework.Assert;

/**
 * A test implementation of {@link StreamMigrator} for use with integration
 * tests where a mock would not be suitable.
 */
class TestStreamMigrator implements StreamMigrator {

    /**
     * The input data we expect.
     */
    private final String inputData;

    /**
     * The output data we generate.
     */
    private final String outputData;

    /**
     * Initialise.
     *
     * @param inputData the input data we expect.
     * @param outputData the output data we generate.
     */
    public TestStreamMigrator(String inputData,
        String outputData) {

        this.inputData = inputData;
        this.outputData = outputData;
    }

    // Javadoc inherited.
    public void migrate(InputStream input, OutputStream output, 
        StepType type)
            throws ResourceMigrationException {

        try {

            // Check the input.
            InputStreamByteArray isba = new InputStreamByteArray(input);
            Assert.assertTrue(new String(isba.getByteArray()).equals(inputData));

            // Generate output.
            OutputStreamByteArray osba = new OutputStreamByteArray(
                    outputData.getBytes());
            osba.writeTo(output);

        } catch (IOException e) {
            throw new ResourceMigrationException("unlikely", e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
