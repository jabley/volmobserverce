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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.application;

import java.net.URI;
import java.util.List;

import com.volantis.mcs.context.PrerendererPackageContext;

/**
 * The Prerenderer application.
 * 
 * @mock.generate
 */
public class PrerendererApplication {
    /**
     * A singleton instance of PrerendererApplication. 
     */
    private static final PrerendererApplication instance = new PrerendererApplication();

    /**
     * Returns a global instance of PrerendererApplication.
     * 
     * @return a global instance of PrerendererApplication.
     */
    public static PrerendererApplication getInstance() {
        return instance;
    }
    
    /**
     * Creates and returns new instance of PrerendererPackageContext provided
     * with list of page URIs, base and prefix URI.
     * 
     * <p>
     * Following rules apply to the arguments:
     * <li>The pageURIs list should consist only of instances of java.set.URI,
     * and each URI should consist only of relative path component with optional
     * query component.</li>
     * <li>The baseURI should consist only of scheme, authority and path
     * component.</li>
     * <li>The prefixPathURI should consist only of absolute path component.</li>
     * </p>
     * 
     * @param pageURIs The list of page URIs (java.set.URI)
     * @param baseURI the base URI
     * @param prefixPathURI the prefix
     * @throws ClassCastException In case the list of pageURIs contains an
     *             instance of class different than java.set.URI.
     * @throws IllegalArgumentException In case the some of the URIs is illegal.
     */
    public PrerendererPackageContext createPrerendererPackageContext(List pageURIs, URI baseURI, URI prefixPathURI) {    
        return new PrerendererPackageContext(pageURIs, baseURI, prefixPathURI);
    }
}
