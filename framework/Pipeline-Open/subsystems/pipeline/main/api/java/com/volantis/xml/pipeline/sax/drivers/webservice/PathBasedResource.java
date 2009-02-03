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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.webservice;

public abstract class PathBasedResource implements WSDLResource {
    /**
     * The path to the resource in the classpath.
     */
    private String path;

    /**
     * Construct a new PathBasedResource with a given path.
     * @param path The path.
     */
    protected PathBasedResource(String path) {
        setPath(path);
    }

    /**
     * Get the path.
     * @return the path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the path.
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    // javadoc inherited
    public boolean equals(Object o) {
        boolean result = false;

        if (this == o) {
            result = true;
        } else {
            if (o instanceof PathBasedResource) {

                final PathBasedResource pathBasedResource = (PathBasedResource)o;

                if (path != null ? path.equals(pathBasedResource.path) :
                        pathBasedResource.path == null) {
                    result = true;
                }
            }

        }

        return result;
    }

    // javadoc inherited
    public int hashCode() {
        return (path != null ? path.hashCode() : 0);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 19-Jun-03	98/4	allan	VBM:2003022822 WS Connector renamed to WS Driver

 18-Jun-03	98/1	allan	VBM:2003022822 Promote prelimary classes so that other can access them

 ===========================================================================
*/
