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

package com.volantis.xml.pipeline.sax.convert;

/**
 * <code>AbsoluteToRelativeURLConfiguration</code> implementation.
 */
public class AbsoluteToRelativeURLConfiguration
        extends ConverterConfiguration {

    /**
     *  Volantis copyright mark.
     */
    private static String mark
            = "(c) Volantis Systems Ltd 2003. ";

    /**
     * The baseURL to match against absolute url to attempt to convert
     * it to a relative url.
     */
    private String baseURL;

    /**
     * After removing the baseURL from an absolute url this substitute path may
     * be prefixed on the relative url
     */
    private String substitutionPath;

    /**
     * Default constructor simply calls parent constructor.
     *
     * @param tuples the tuples.
     */
    public AbsoluteToRelativeURLConfiguration(ConverterTuple[] tuples) {
        super(tuples);
    }

    /**
     * Set the base url to match against absolute url.
     * @param url the base url
     */
    public void setBaseURL(String url) {
        baseURL = url;
    }

    /**
     * Get the base url to be matched against abolute url.  This value may be
     * dynamically updated and as such should not be stored in by the operation
     * process.
     * @return the base url
     */
    public String getBaseURL() {
        return baseURL;
    }

    /**
     * Set the substitute url path.  The substitute url is prefixed to the
     * urls that have been successfully resolved from absolute to relative
     * paths.
     * @param path the url to prefix to relative url
     */
    public void setSubstitutionPath(String path) {
        substitutionPath = path;
    }

    /**
     * the substitute url path.  The substitute url is prefixed to the
     * urls that have been successfully resolved from absolute to relative
     * paths.  This value may be dynamically updated and as such should not
     * be stored in by the operation process.
     * @return the url to prefix to relative url
     */
    public String getSubstitutionPath() {
        return substitutionPath;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Mar-04	656/1	adrian	VBM:2004031602 Updated pipeline with DSB enhancement requests

 23-Mar-04	624/1	adrian	VBM:2004031904 Updated AbsoluteToRelativeURL process

 08-Aug-03	308/3	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process - create external Tuple classes

 08-Aug-03	308/1	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process

 ===========================================================================
*/
