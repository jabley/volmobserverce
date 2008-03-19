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
package com.volantis.mcs.ibm.websphere.mcsi;

/**
 * Attribute values for the generated-resource IAPI element.
 */
public class GeneratedResourcesAttributes extends MCSIAttributes {

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
    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * @see #baseDir
     */
    public String getBaseDir() {
        return baseDir;
    }

    // Javadoc inherited
    public void reset() {
        baseDir = null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/1	ianw	VBM:2004090605 New Build system

 28-Oct-04	5897/2	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 ===========================================================================
*/
