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
package com.volantis.devrep.repository.impl.testtools.device;

import com.volantis.synergetics.testtools.io.ResourceTemporaryFileCreator;

/**
 * A {@link com.volantis.synergetics.testtools.io.TemporaryFileCreator} implementation
 * which creates the default device repository we use for testing.
 * <p>
 * The file that this class manages is a cut down and customised version of the
 * full repository file which is suitable for testing. The full file (1000+
 * devices) is far large (and thus too slow to process) to use in unit tests.
 * <p>
 * NOTE: This class and accompanying device repository file has been created to
 * try and centralise the usage of device repository files in tests. Previously
 * we had many device repository files and managing them was a nightmare. Also,
 * they were mostly full size ones and were therefore slowing down the
 * testsuite considerable.
 */
public class TestDeviceRepositoryCreator extends ResourceTemporaryFileCreator {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    public TestDeviceRepositoryCreator() {

        super(TestDeviceRepositoryCreator.class, "test-device-repository.mdpr");
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-04	5315/1	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 ===========================================================================
*/
