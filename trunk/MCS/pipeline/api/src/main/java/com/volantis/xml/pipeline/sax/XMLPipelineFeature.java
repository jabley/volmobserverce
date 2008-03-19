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
package com.volantis.xml.pipeline.sax;

import com.volantis.xml.pipeline.sax.XMLPipelineProcessImpl;

import java.util.HashMap;

/**
 * Typesafe enumeration that identifies the various featues that can be used
 * to configure a pipeline.
 */
public class XMLPipelineFeature {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Map to store the allowable instances of this type safe enumeration
     */
    private static HashMap entries = new HashMap();

    /**
     * Instance that indicates that the pipeline should not forward nested
     * startDocument() and endDocument() events to any process that has been
     * set as the "next" process of the pipeline.
     */
    public static final XMLPipelineFeature CONSUME_NESTED_DOUCUMENT_EVENTS =
            new XMLPipelineFeature("ConsumeNestedDocumentEvents");

    /**
     * Instance that indicates that a pipeline should be created that
     * can be nested inside another Pipeline.
     */
    public static final XMLPipelineFeature ALLOW_PIPELINE_TO_NEST =
            new XMLPipelineFeature("AllowPipelineToNest");

    /**
     * Instance that indicates that pipeline should be created that
     * has no special features set.
     */
    public static final XMLPipelineFeature STANDARD =
            new XMLPipelineFeature("Standard");

    /**
     * The internal name of the enumeration literal.
     */
    private String name;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param name the internal name of the enumeration literal
     */
    private XMLPipelineFeature(String name) {
        this.name = name;

        entries.put(name, this);
    }

    /**
     * Returns the internal name for the enumeration literal. This must not
     * be used for presentation purposes.
     *
     * @return internal name for the enumeration literal
     */
    public String toString() {
        return name;
    }

    /**
     * Retrieves the enumeration literal that is equivalent to the given
     * internal name, or null if the name is not recognized.
     *
     * @param name the internal name to be looked up
     * @return the equivalent enumeration literal or null if the name is
     *         not recognized
     */
    public static XMLPipelineFeature literal(String name) {
        return (XMLPipelineFeature)entries.get(name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 23-Jun-03	95/3	doug	VBM:2003061605 Fixed issure with literal method

 23-Jun-03	95/1	doug	VBM:2003061605 Document Event Filtering changes

 ===========================================================================
*/
