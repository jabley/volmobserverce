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
 * The configuration required to allow the URLToURLC process to perform
 * the required URL transcodings.
 */
public class URLToURLCConfiguration extends ConverterConfiguration {

    /**
     * The object that is used to actually do the generation of the URLC.
     */
    private URLConverter urlConverter;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param urlConverter the object used to actually do the generation of
     *                      the URLC
     * @param tuples        the rules defining where to look for URLs requiring
     *                      conversion
     */
    public URLToURLCConfiguration(URLConverter urlConverter,
                                  ConverterTuple[] tuples) {

        super(tuples);
        if (urlConverter == null) {
            throw new IllegalArgumentException(
                    "A URLConverter must be specified");
        }
        this.urlConverter = urlConverter;
    }

    /**
     * Returns the object that can be used to generate the URLC.
     *
     * @return the object used to generate the URLC
     */
    public URLConverter getURLConverter() {
        return urlConverter;
    }

    /**
     * Return a URLtoURLConfiguration ConverterTuple 'template'.
     *
     * @return      a URLtoURLConfiguration ConverterTuple 'template'.
     */
    protected ConverterTuple[] getTemplate() {
        return new URLToURLCTuple[0];
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 08-Aug-03	308/4	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process - create external Tuple classes

 08-Aug-03	308/2	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process

 06-Aug-03	299/1	philws	VBM:2003080504 Pipeline work for the DSB convertImageURLToDMS process

 ===========================================================================
*/
