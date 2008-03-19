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
package com.volantis.xml.pipeline.sax.impl.operations.transform;

/**
 * This class is the key used when storing or retrieving cached templates
 */
public class TransformCacheKey {

    /**
     * Indicates whether the template is compiled
     */
    private boolean compilable;

    /**
     * The location of the XSLT or XSLTC of the template
     */
    private String templateLocation;

    /**
     * Construct a new TransformCacheKey.
     *
     * @param compilable True if the template has been compiled
     * @param location The fully qualified (absolute)  URI of the XSLT(C)
     */
    public TransformCacheKey(boolean compilable, String location) {
        this.compilable = compilable;
        templateLocation = location;
    }

    /**
     * Allows the compiled status of the template represented by this key
     * to be retrieved.
     *
     * @return True if the template is compiled, false otherwise
     */
    public boolean getCompilable() {
        return compilable;
    }

    /**
     * Allows the location of the template represented by this key to be
     * retrieved.
     *
     * @return The location of the template as an absolute URI string
     */
    public String getLocation() {
        return templateLocation;
    }

    // JavaDoc inherited
    public int hashCode() {
        return templateLocation.hashCode() + (compilable ? 1 : 0);
    }

    // JavaDoc inherited
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        TransformCacheKey key = (TransformCacheKey)obj;
        return templateLocation.equals(key.getLocation()) &&
                (compilable == key.getCompilable());
    }

    // JavaDoc inherited
    public String toString() {
        return templateLocation.toString() +
                (compilable ? " (compiled)" : "( not compiled)");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 26-Jan-04	551/5	claire	VBM:2004012204 Caching clean-up

 26-Jan-04	551/3	claire	VBM:2004012204 Fixed and optimised caching code

 26-Jan-04	551/1	claire	VBM:2004012204 Implementing caching for transforms

 ===========================================================================
*/
