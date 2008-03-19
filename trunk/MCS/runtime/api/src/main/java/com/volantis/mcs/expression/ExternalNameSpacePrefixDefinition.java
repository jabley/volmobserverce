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
package com.volantis.mcs.expression;


/**
 * This class is a container for namespace prefixes created outside of
 * the expressions package. It is used by the registration process to avoid 
 * the use of Map implementations.
 */
public class ExternalNameSpacePrefixDefinition {

    /**
     *  Volantis copyright mark.
     */
    private static String mark =
        "(c) Volantis Systems Ltd 2004. ";
        
    /**
     * The prefix.
     */
    private String prefix;

    /**
     * The URI.
     */
    private String uri;
    
    /**
    * Construct a new ExternalNameSpacePrefixDefinition.
    * @param prefix The prefix for the namespace.
    * @param uri The URI for the namespace.
    */
    public ExternalNameSpacePrefixDefinition(String prefix, String uri) {

        this.prefix = prefix;
        this.uri = uri;
    }

    /**
     * Get the prefix for this NameSpace.
     * @return the prefix for this Namespace.
     */
    public String getPrefix() {
        return this.prefix;
    }
    
    /**
     * Get the URI for the NameSpace.
     * @return the URI for the NameSpace.
     */
    public String getUri() {
        return this.uri;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Feb-04	2966/1	ianw	VBM:2004011923 Added mcsi:policy function

 ===========================================================================
*/
