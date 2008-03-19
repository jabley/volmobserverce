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
package com.volantis.mcs.runtime.configuration.project;

/**
 * Holds configuration information about project's generated resources.
 * <p>
 * Currently the only thing we generate per project are chart asset files, but
 * we will add more stuff over time.
 * <p>
 * This corresponds to the
 * mcs-config/projects/[default|project]/generated-resources elements.
 */
public class GeneratedResourcesConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The name of the base directory to generate resources into.
     */
    private String baseDir;

    /**
     * @see #baseDir
     */
    public String getBaseDir() {
        return baseDir;
    }

    /**
     * @see #baseDir
     */
    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Oct-04	5897/2	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 ===========================================================================
*/
